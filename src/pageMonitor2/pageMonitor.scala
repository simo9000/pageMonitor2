package pageMonitor2

import java.util.Observer;

import scala.collection.concurrent.Map;
import scala.collection.concurrent.TrieMap;

class pageMonitor extends Thread{

  var pageMaster = new TrieMap[Integer,webPage]();
  
  private var alive = false;
  
  def startMonitoring(){
    alive = true;
    start()
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