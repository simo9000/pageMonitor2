package pageMonitor2.com.NotificationEngine

import java.sql.SQLException
import pageMonitor2.com.monitor.pageMonitor

import pageMonitor2.webPage
import concurrent._
import java.sql.ResultSet;
import ExecutionContext.Implicits.global

class NotificationEngine(monitor: pageMonitor) extends Thread {

  private var alive = false;
  private var pollingFrequency = 30000
  
  def initialize(){
    val activePages = dbInterface.getActivePages()
    while(activePages.next()){
       val pageID = activePages.getInt("pk_id")
       val URL = activePages.getString("fdURL")
       monitor.addPage(pageID,new webPage(pageID,URL))
     }
    
    val activeNotifications = dbInterface.getActiveNotifications()
    while(activeNotifications.next()){
        val pageID = activeNotifications.getInt("PageID")
        val UserID = activeNotifications.getInt("UserID")
        val emailAddress = activeNotifications.getString("emailAddress")
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
      // handle new pages and notifications concurrently yet in logical sequence
      val newPages: Future[ResultSet] = Future {
        dbInterface.getNewPages()
      }
      newPages onSuccess {
        case response => {while(response.next()){
              val pageID = response.getInt("pk_id")
              val URL = response.getString("fdURL")
              monitor.addPage(pageID,new webPage(pageID,URL))
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
                monitor.addWatcher(pageID, UserID, new EmailNotification(emailAddress))
              }
              dbInterface.setNewNotificationsToActive()
            }
          }
        dbInterface.setNewPagesToActive()
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
            monitor.removeWatcher(PageID, UserID)
          }
          
          // look for pages to delete having removed dead notifications
          val deletedPages: Future[ResultSet] = Future{
            dbInterface.getPagesToDelete()
          }
          deletedPages onSuccess {
            case response => while(response.next()){
              monitor.removePage(response.getInt("pk_id"))
            }
            dbInterface.clearDeletedPages()
          }
        }   
       dbInterface.clearDeletedNotifications()
      }  
      
      Thread.sleep(pollingFrequency)
    }
  }
}