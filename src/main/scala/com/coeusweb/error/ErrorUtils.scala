/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.error

import com.coeusweb.{ WebRequest, WebResponse }
import com.coeusweb.core.HttpException

/**
 * Helper methods for constructing exception handlers.
 */
object ErrorUtils {
  
  /**
   * Sets various error related request attributes.
   */
  def setupErrorPageAttributes(request: WebRequest, error: Throwable, servletName: String) {
    
    request("javax.servlet.error.exception") = error
    request("javax.servlet.error.exception_type") = error.getClass
    request("javax.servlet.error.message") = error.getMessage
    request("javax.servlet.error.request_uri") =  request.requestUri
    request("javax.servlet.error.servlet_name") = servletName
    
    request("javax.servlet.error.status_code") = error match {
      case e: HttpException => e.httpStatus
      case _                => 500
    }
  }
  
  /**
   * Sets the response's HTTP status based on the given Throwable.
   * 
   * <p>If the exception that occurred is an instance of {@code HttpException}
   * this instance also sets the HTTP status code of the response using the
   * exception's status.</p>
   * 
   * @param response the current web response
   * @param error the exception that occured during the request execution
   */
  def setResposeStatus(response: WebResponse, error: Throwable) {
    response.status = error match {
      case e: HttpException => e.httpStatus
      case _                => 500
    }
  }
}