package pageMonitor2

import java.util.Observer;

import scala.collection.JavaConverters._
import scala.collection.concurrent.Map;
import scala.collection.concurrent.TrieMap;

import java.sql._;

class pageMonitor extends Thread{

  var pageMaster = new TrieMap[Integer,webPage]();

  private var alive = false;

  def startMonitoring(){
    alive = true;
    start()
  }

  def addPage(id : Integer, page : webPage){
    pageMaster.put(id,page)
    val createQuery: String = String.format("INSERT INTO tblMonitoredPages (pk_id,fdURL,fdProcessFlag)" + "values (%s,'%s',0);", id, page.getURL())
    DatabaseConnection.updateQuery(createQuery)
  }

  def removePage(id : Integer){
    pageMaster.remove(id)
  }

  def addWatcher(pageID : Integer, userID : Integer, watcher: Observer) {
    var requestQuery : String = "INSERT INTO tblNotificationRequests (FK_USER_ID,FK_PAGE_ID,fdActive) values (%s,%s,%s);".format(userID,pageID,0);
    DatabaseConnection.updateQuery(requestQuery);
    pageMaster.lookup(pageID).addWatcher(userID, watcher)
  }

  def removeWatcher(pageID : Integer, userID : Integer){
    pageMaster.lookup(pageID).removeWatcher(userID)
  }


  override def run(){
    while(alive){
      var test = null
      pageMaster.foreach(f => f._2.considerRefresh())
      Thread.sleep(100)
    }
  }

  def stopMonitoring(){
    this.synchronized{
      alive = false;
    }
  }

}