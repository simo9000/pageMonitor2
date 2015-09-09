package pageMonitor3.monitor

import java.util.Observer
import java.util.concurrent.Executors
import scala.collection.concurrent.TrieMap
import scala.concurrent.ExecutionContext
import pageMonitor3.webPage
import pageMonitor3.pageState
import java.util.Calendar


class pageMonitor extends Thread{

  private var pageMaster = new TrieMap[Integer,webPage]();
  val contextService = ExecutionContext fromExecutorService Executors.newCachedThreadPool()
  val context = contextService.prepare();

  
  private var alive = false;
  
  def startMonitoring(){
    alive = true;
    //start()
    pageMaster.foreach(f => f._2.enableMonitoring())
    pageMaster.foreach(f => f._2.considerRefresh())
  }
  
  def stopMonitoring(){
    this.synchronized{
      alive = false;
      pageMaster.foreach(f => f._2.killMonitoring())
    }
  }
  
  def addPage(id : Integer, page : webPage){
    pageMaster.put(id,page)
    page.setContext(context)
    page.considerRefresh()
    
  }
  
  def removePage(id : Integer){
    val page: Option[webPage] = pageMaster.remove(id)
    page map {_.killMonitoring()}
  }
  
  def addWatcher(pageID : Integer, userID : Integer, state : pageState, watcher: Observer){
    pageMaster.lookup(pageID).addWatcher(userID, watcher, state)
  }
  
  def addWatcher(pageID : Integer, userID : Integer, state : pageState, watcher: Observer, element : String){
    pageMaster.lookup(pageID).addWatcher(userID, watcher, state, element)
  }
  
  def removeWatcher(pageID : Integer, userID : Integer, state : pageState){
    pageMaster.lookup(pageID).removeWatcher(userID, state)
  }
  
  def removeWatcher(pageID : Integer, userID : Integer, state : pageState, element : String){
    pageMaster.lookup(pageID).removeWatcher(userID, state, element);
  }
  
  def removeAllWatchers(pageID : Integer, userID : Integer){
    pageMaster.lookup(pageID).removeAllWatchers(userID);
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