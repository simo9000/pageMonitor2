
package pageMonitor3.monitor

import concurrent._
//import ExecutionContext.Implicits.global
import java.util.Calendar
//import pageMonitor3.monitor.HTTP_GET

abstract class HTTPGetHandler {
  
  private var URL : String = "empty"
  private var start : Calendar = null
  private var end : Calendar = null
  private var requestTime : Long = 0
  private var context : ExecutionContext = null
  
  def setURL(url : String){
    URL = url
  }
  
  protected def getURL() : String = {
    return URL
  }
  
  def setContext(ex: ExecutionContext){
    context = ex
  }
  
  def getRequest_Time() : Double = {
    return requestTime
  }
  
  protected def refresh(){
    refresh(context)
  }
  
  protected def refresh(implicit ex : ExecutionContext){
    val f: Future[String] = Future {
      start = Calendar.getInstance()
      HTTP_GET.getElementText(URL,"content")
    }
    f onSuccess {
      case response => 
      if (response != null)
        handleResponce(response)
      end = Calendar.getInstance()
      requestTime = end.getTimeInMillis() - start.getTimeInMillis()
      //println("execution time = " + diff + " milliseconds")
      considerRefresh(requestTime)
    }
    f onFailure {
      case t => end = Calendar.getInstance()
      requestTime = end.getTimeInMillis() - start.getTimeInMillis()
      considerRefresh(requestTime)
    }
  }

  protected def handleResponce(response : String)

  protected def considerRefresh(requestTime: Double)
  
}