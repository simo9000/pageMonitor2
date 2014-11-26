
package pageMonitor2.com.monitor

import concurrent._
import ExecutionContext.Implicits.global
import java.util.Calendar;

abstract class HTTPGetHandler {
  
  var URL = "empty"
  var start : Calendar = null
  var end : Calendar = null
  var requestTime : Long = 0
  
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