package pageMonitor2.com.monitor

import java.util.Observer
import scala.collection.concurrent.TrieMap;
import pageMonitor2.webPage
import java.util.Calendar;


class pageMonitor extends Thread{

  private var pageMaster = new TrieMap[Integer,webPage]();
  
  private var alive = false;
  
  def startMonitoring(){
    alive = true;
    start()
  }
  
  def stopMonitoring(){
    this.synchronized{
      alive = false;
    }
  }
  
  def addPage(id : Integer, page : webPage){
    pageMaster.put(id,page)
  }
  
  def removePage(id : Integer){
    pageMaster.remove(id)
  }
  
  def addWatcher(pageID : Integer, userID : Integer, watcher: Observer){
    pageMaster.lookup(pageID).addWatcher(userID, watcher)
  }
  
  def removeWatcher(pageID : Integer, userID : Integer){
    pageMaster.lookup(pageID).removeWatcher(userID)
  }
  

  override def run(){
    while(alive){
      val start = Calendar.getInstance()
      pageMaster.foreach(f => f._2.considerRefresh())
      val end = Calendar.getInstance()
      val diff = end.getTimeInMillis() - start.getTimeInMillis()
      val sleepTime = (getAverageRequestTime - diff).asInstanceOf[Long]
      if (sleepTime > 20) Thread.sleep(sleepTime) else Thread.sleep(2000)
    }
  }
  
  private def getAverageRequestTime(): Long = {
    if (pageMaster.size == 0)
      return 0
    val it = pageMaster.iterator
    var total : Double = 0
    while (it.hasNext){
      total += it.next()._2.getRequest_Time()
    }
    return (total / pageMaster.size).toLong  
  }
  
    

  
}