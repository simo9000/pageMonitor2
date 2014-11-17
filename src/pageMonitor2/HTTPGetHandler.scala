
package pageMonitor2

import concurrent._
import ExecutionContext.Implicits.global

abstract class HTTPGetHandler {
  
  var URL = "empty"
  
  def setURL(url : String){
    URL = url
  }
  
  protected def refresh(){
      val f: Future[String] = Future {
        HTTP_GET.getHTML(URL);
      }
      f onSuccess {
        case response => handleResponce(response)
      }
    }
  
  protected def handleResponce(response : String)
  
}