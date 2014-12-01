
package pageMonitor3.monitor

import concurrent._
import ExecutionContext.Implicits.global
import java.util.Calendar
import pageMonitor3.monitor.HTTP_GET

abstract class HTTPGetHandler {
  
  private var URL = "empty"
  private var start : Calendar = null
  private var end : Calendar = null
  private var requestTime : Long = 0
  
  def setURL(url : String){
    URL = url
  }
  
  def getRequest_Time() : Double = {
    return requestTime
  }
  
  protected def refresh(){
      val f: Future[String] = Future {
        start = Calendar.getInstance()
        HTTP_GET.getHTML(URL)
      }
      f onSuccess {
        case response => handleResponce(response)
        end = Calendar.getInstance()
        requestTime = end.getTimeInMillis() - start.getTimeInMillis()
        //println("execution time = " + diff + " milliseconds")
      }
    }
  
  protected def handleResponce(response : String)
  
}