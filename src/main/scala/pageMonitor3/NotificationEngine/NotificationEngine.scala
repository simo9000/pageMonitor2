package pageMonitor3.NotificationEngine

import java.sql.SQLException
import pageMonitor3.monitor.pageMonitor

import pageMonitor3.webPage
import concurrent._
import java.sql.ResultSet;
import ExecutionContext.Implicits.global

class NotificationEngine(monitor: pageMonitor) extends Thread {

  private var alive = false;
  private var pollingFrequency = 5000
  private val debug = 1; // 1 is debug 0 is not debug
  
  def initialize(){
    val activePages = dbInterface.getActivePages()
    while(activePages.next()){
       val pageID = activePages.getInt("pk_id")
       val URL = activePages.getString("fdURL")
       if (debug == 1) println("active page " + URL + " found")
       monitor.addPage(pageID,new webPage(pageID,URL))
     }
    
    val activeNotifications = dbInterface.getActiveNotifications()
    while(activeNotifications.next()){
        val pageID = activeNotifications.getInt("PageID")
        val UserID = activeNotifications.getInt("UserID")
        val emailAddress = activeNotifications.getString("emailAddress")
        if (debug == 1) println("active notification pageID=" + pageID + " found for " + emailAddress)
        monitor.addWatcher(pageID, UserID, new EmailNotification(emailAddress))
    }
  }
  
  
  def startMonitoring(){
    alive = true;
    start();
  }
  
  def stopMonitoring(){
    this.synchronized{
      alive = false;
    }
  }
  
  override def run(){
    while(alive){
      if (debug == 1) println("checking for changes...")
      // handle new pages and notifications concurrently yet in logical sequence
      val newPages: Future[ResultSet] = Future {
        dbInterface.getNewPages()
      }
      newPages onSuccess {
        case response => {while(response.next()){
          
              val pageID = response.getInt("pk_id")
              val URL = response.getString("fdURL")
              if (debug == 1) println ("new page " + URL + " found")
              monitor.addPage(pageID,new webPage(pageID,URL))
              dbInterface.setNewPageToActive(pageID)
            }
          // look for new notifications having adding new pages
          val newNotifications: Future[ResultSet] = Future{
            dbInterface.getNewNotifications()
          }
          
          newNotifications onSuccess{
              case response => while(response.next()){
                val pageID = response.getInt("PageID")
                val UserID = response.getInt("UserID")
                val emailAddress = response.getString("emailAddress")
                if (debug == 1) println ("new notification for pageID=" + pageID + " for " + emailAddress)
                monitor.addWatcher(pageID, UserID, new EmailNotification(emailAddress))
                dbInterface.setNewNotificationToActive(pageID, UserID)
              }
              
            }
          }
      }
      
      val deletedWatchers: Future[ResultSet] = Future{
        dbInterface.getNotificationsToDelete()
      }
      deletedWatchers onSuccess {
        case response => {
          
          // dispatch each notification deletion
          while(response.next()){
            val UserID = response.getInt("FK_USER_ID")
            val PageID = response.getInt("FK_PAGE_ID")
            if (debug == 1) println("Notification for UserID=" + UserID + " and pageID=" + PageID + " to be deleted")
            monitor.removeWatcher(PageID, UserID)
            dbInterface.clearDeletedNotification(PageID, UserID)
          }
          
          // look for pages to delete having removed dead notifications
          val deletedPages: Future[ResultSet] = Future{
            dbInterface.getPagesToDelete()
          }
          deletedPages onSuccess {
            case response => while(response.next()){
              val PageID = response.getInt("pk_id")
              if (debug == 1) println ("PageID=" + PageID + "to be deleted")
              monitor.removePage(PageID)
              dbInterface.clearDeletedPage(PageID)
            }
          }
        }   
      }  
      
      Thread.sleep(pollingFrequency)
    }
  }
}