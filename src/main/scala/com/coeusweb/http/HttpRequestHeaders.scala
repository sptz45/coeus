/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.http

import java.util.{Enumeration, Date}
import javax.servlet.http.HttpServletRequest
import scala.collection.JavaConversions._
import com.coeusweb.WebRequest

/**
 * Methods to access HTTP headers from a WebRequest. 
 */
trait HttpRequestHeaders {

  this: WebRequest =>
  
  def headerParse[T](name: String)(implicit m: Manifest[T]): Option[T] = {
    val resultClass = m.erasure
    val result = 
      if (resultClass == classOf[String]) {
        servletRequest.getHeader(name)
      } else if (resultClass == classOf[Int]) {
        val num = servletRequest.getIntHeader(name)
        if (num == -1) null else num
      } else if (resultClass == classOf[Date]) {
        val millis = servletRequest.getDateHeader(name)
        if (millis == -1) null else new Date(millis)
      } else {
        parseHeader(servletRequest.getHeader(name), m)
      }
    Option(result.asInstanceOf[T])
  }
  
  def header(name: String) = Option(servletRequest.getHeader(name))
  
  def headerInt(name: String): Option[Int] = {
    val num = servletRequest.getIntHeader(name)
    if (num == -1) None else Some(num)
  }
  
  def headerDate(name: String): Option[Long] = {
    val millis = servletRequest.getDateHeader(name)
    if (millis == -1) None else Some(millis)
  }
  
  def headerValues(name: String): Iterator[String] = {
    val headers = servletRequest.getHeaders(name)
    if (headers eq null)
      throw new SecurityException("The servlet container that you've deployed you application does not allow servlets to call the HttpServletRequest.getHeaders method.")
    headers.asInstanceOf[Enumeration[String]]
  }
  
  def headerNames: Iterator[String] = {
    val names = servletRequest.getHeaderNames
    if (names eq null)
      throw new SecurityException("The servlet container that you've deployed you application does not allow servlets to call the HttpServletRequest.getHeaderNames method.")
    names.asInstanceOf[Enumeration[String]]
  }
  
  private def parseHeader[T](value: String, m: Manifest[T]): T = {
    if(value eq null) return null.asInstanceOf[T]
    val parser = converters.converter(m.erasure)
    parser.parse(value, locale).asInstanceOf[T]
  }
}

