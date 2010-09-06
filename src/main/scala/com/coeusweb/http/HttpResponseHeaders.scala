/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.http

import java.util.Date
import javax.servlet.http.HttpServletResponse

/**
 * Methods to add HTTP headers to a WebResponse. 
 */
trait HttpResponseHeaders {
  
  val servletResponse: HttpServletResponse
  
  def header(name: String, value: String) {
    servletResponse.setHeader(name, value)
  }

  def header(name: String, value: Int) {
    servletResponse.setIntHeader(name, value)
  }
  
  def header(name: String, value: Date) {
    servletResponse.setDateHeader(name, value.getTime)
  }
  
  def addHeader(name: String, value: String) {
    servletResponse.addHeader(name, value)
  }

  def addHeader(name: String, value: Int) {
    servletResponse.addIntHeader(name, value)
  }
  
  def addHeader(name: String, value: Long) {
    servletResponse.addDateHeader(name, value)
  }
  
  /**
   * Tests whether the response contains an HTTP header with the given name.
   */
  def containtsHeader(headerName: String) = servletResponse.containsHeader(headerName)
}
