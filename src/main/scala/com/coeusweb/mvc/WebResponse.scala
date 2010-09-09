/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb
package mvc

import javax.servlet.http.{ HttpServletResponse, Cookie }
import http.{ HttpStatus, HttpResponseHeaders }

/**
 * The web response.
 * 
 * @param servletResponse the current {@link HttpServletResponse}
 */
class WebResponse(val servletResponse: HttpServletResponse) extends HttpResponseHeaders {
  
  private[this] var _status: Int = HttpStatus.OK

  /**
   * Returns a <code>PrintWriter</code> that can send character text to the client.
   * 
   * @see {@link javax.servlet.ServletResponse#getWriter ServletResponse.getWriter()}
   */
  def writer = servletResponse.getWriter
  
  /**
   * Returns an output stream suitable for writing binary data in the response.
   * 
   * @see {@link javax.servlet.ServletResponse#getOutputStream ServletResponse.getOutputStream()}
   */
  def outputStream = servletResponse.getOutputStream
  
  /**
   * Returns whether the response has already the status code and headers written. 
   */
  def isCommited = servletResponse.isCommitted
  
  /**
   * Get the response's content type.
   * 
   * @return the content type or <code>null</code> if a content type has not been set.
   * 
   * @see {@link HttpServletResponse#getContentType}
   */
  def contentType = servletResponse.getContentType
  
  /**
   * Sets the reponse's content type.
   * 
   * @see {@link HttpServletResponse#setContentType}
   */
  def contentType_=(ct: String) { servletResponse.setContentType(ct) }


  /**
   * Get the status code of this response.
   */
  def status: Int = _status
  
  /**
   * Set the status code of this response.
   * 
   * @see {@link HttpServletResponse#setStatus}
   */
  def status_=(status: Int) {
    _status = status
    servletResponse.setStatus(status)
  }
  
  /**
   * Sends an error response back to the client with the specified status.
   * 
   * @throws IllegalStateException if the response has been committed before this method call
   * @see {@link HttpServletResponse#sendError(Int)}
   */
  def sendError(status: Int) {
    require(HttpStatus.isError(status))
    _status = status
    servletResponse.sendError(status)
  }
  
  /**
   * Sends an error response back to the client with the specified status and message.
   * 
   * @throws IllegalStateException if the response has been committed before this method call
   * @see {@link HttpServletResponse#sendError(Int, String)}
   */
  def sendError(status: Int, message: String) {
    require(HttpStatus.isError(status))
    _status = status
    servletResponse.sendError(status, message)
  }
  
  /**
   * Adds the specified cookie to the response.
   */
  def addCookie(cookie: Cookie) {
    servletResponse.addCookie(cookie)
  }
  
  /**
   * Sets the cookie's <code>maxAge</code> to zero to remove it from the next request.
   */
  def removeCookie(toRemove: Cookie) {
    val cookie = new Cookie(toRemove.getName, toRemove.getValue)
    cookie.setDomain(toRemove.getDomain)
    cookie.setPath(toRemove.getPath)
    cookie.setMaxAge(0)
    servletResponse.addCookie(cookie)
  }
  
  /**
   * Sets a "Cache-Control" HTTP header to specify the for how long the response
   * is considered fresh.
   * 
   * <p>Frequently used cache control directives:
   * <ul>
   * <li><strong>public</strong>...</li>
   * <li><strong>private</strong>...</li>
   * <li><strong>must-revalidate</strong>...</li>
   * </ul>
   * </p>
   * 
   * @param maxAge the maximum age in seconds the response is considered fresh
   * @param directives any cache control directives
   */
  def expires(maxAge: Int, directives: String*) {
    val cc = new StringBuilder
    cc.append("max-age=").append(maxAge)
    for (directive <- directives) {
      cc.append(", ").append(directive)
    } 
    header("Cache-Control", cc.toString)
  }
  
  /**
   * Prevent the response from being cached in the browser and in any intermediaries.
   * 
   * <p>This method sets the "Cache-Control" HTTP header to "no-cache, no-store".</p>  
   */
  def preventCaching() {
    header("Cache-Control", "no-cache, no-store")
  }
}

object WebResponse {
  
  private[this] val response = new ThreadLocal[WebResponse]
  
  /** Get the current web response. */
  def currentResponse = response.get
  
  /** Set the current web response. */
  def currentResponse_=(r: WebResponse) {
    if (r eq null)
      response.remove()
    else
      response.set(r)
  }
}
