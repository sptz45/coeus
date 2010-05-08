/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.view

import com.coeusweb.{ WebRequest, WebResponse }
import com.coeusweb.http.HttpStatus

/**
 * A <code>View</code> that when rendered will redirect the user to the 
 * specified <code>location</code> using the specified HTTP status code.
 * 
 * <p>The <code>status</code> is usually one of:</p>
 * <ul>
 * <li>301 - MOVED_PERMANENTLY</li>
 * <li>302 - MOVED_TEMPORARILY</li>
 * <li>303 - SEE_OTHER (used in redirect-after-post)</li>
 * <li>307 - TEMPORARY_REDIRECT</li>
 * </ul> 
 * 
 * @param location the location to redirect to
 * @param status the HTTP status code of the response (must be a redirection, 300 series)
 * @param parameters any request parameters to include in the redirect URL
 * 
 * @see HttpStatus
 */
class RedirectView(location: String, status: Int, parameters: (String, Any)*) extends View {

  /** The content-type of this <code>View</code> is "text/html". */
  def contentType: String = "text/html"
  
  def render(request: WebRequest, response: WebResponse) {
    require(HttpStatus.isRedirection(status), "Cannot redirect using status code: " + status)
    response.status = status
    response.contentType = contentType
    response.header("Location", encodeUrl(request, response))
  }
  
  private def encodeUrl(request: WebRequest, response: WebResponse): String = {
    
    val contextPath = request.contextPath 
    if (location.startsWith("http") || (contextPath == "/" && parameters.length == 0))
      return location
      
    val builder = new StringBuilder(contextPath.length + location.length)
      
    if (request.contextPath != "/") {
      builder.append(contextPath)
    }
    if (location.charAt(0) != '/') {
      builder.append('/')
    }
    builder.append(location)
    appendParameters(builder)
    
    response.servletResponse.encodeRedirectURL(builder.toString)
  }
  
  private def appendParameters(builder: StringBuilder) {
    if (parameters.isEmpty) return
    
    if (! builder.contains('?')) builder.append("?")
    else builder.append("&")
    
    for ((name, value) <- parameters) {
      builder.append(name).append("=").append(value).append("&")
    }
    
    if (builder.endsWith("&"))
      builder.deleteCharAt(builder.length -1)
  }
}
