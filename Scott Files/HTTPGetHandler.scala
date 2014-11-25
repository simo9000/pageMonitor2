
package pageMonitor2

import concurrent._
import ExecutionContext.Implicits.global

abstract class HTTPGetHandler {

  var URL : String = "empty"

  def setURL(url : String){
    URL = url
  }

  def retrieveURL(): String = {
    return URL;
  }

  protected def refresh(){
    val f: Future[String] = Future {
      HTTP_GET.getHTML(URL);
    }
    f onSuccess {
      case response => {
        //println("SUCCESS")
        handleResponce(response)
      }
    }
  }

  protected def handleResponce(response : String)

}