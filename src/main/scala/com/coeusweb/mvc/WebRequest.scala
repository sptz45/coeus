/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb
package mvc

import java.util.{ Date, Enumeration, Locale }
import javax.servlet.ServletContext
import javax.servlet.http.{ HttpServletRequest, Cookie }
import scala.collection.Map
import scala.collection.JavaConversions.asIterator
import bind.ConverterRegistry
import i18n.locale.LocaleResolver
import i18n.msg.MessageBundle
import http.HttpRequestHeaders
import http.multipart.{ FormFile, MultipartHttpServletRequest }
import scope._
import param._

/**
 * A class that represents a web request.
 * 
 * @param servletRequest the actual Servlet request
 * @param pathContext the path variables
 * @param localeResolver the configured <code>LocaleResolver</code>
 * @param converters the default converters
 */
class WebRequest(
  val servletContext: ServletContext,
  val servletRequest: HttpServletRequest,
  pathContext: Map[String, String],
  localeResolver: LocaleResolver,
  val converters: ConverterRegistry,
  val messages: MessageBundle) extends ScopedContainer with HttpRequestHeaders {
  
  /**
   * Returns a <code>Reader</code> with the request's body
   * 
   * <p>Either this method or {@link #inputStream} may be called to read the request's
   * body, not both.</p>
   */
  def reader = servletRequest.getReader
  
  /**
   * Returns an <code>InputStream</code> with the request'a body.
   * 
   * <p>Either this method or {@link #reader} bay be called to read the request's body,
   * not both.</p>
   */
  def inputStream = servletRequest.getInputStream
  
  def getAttribute[T](attribute: String): T =
    servletRequest.getAttribute(attribute).asInstanceOf[T]
  
  def attributeNames =
    asIterator(servletRequest.getAttributeNames.asInstanceOf[Enumeration[String]])
  
  def update(attribute: String, value: Any) {
    servletRequest.setAttribute(attribute, value)
  }
  
  def -=(attribute: String) {
    servletRequest.removeAttribute(attribute)
  }
  
  private[this] var _session: WebSession = _
  
  /** Return the WebSession and optionally creates one if it doesn't exist. */
  def session = {
    if (_session eq null)
      _session = new WebSession(servletRequest.getSession)
    _session
  }
  
  /** Returns an existing session on None if a session does't already exist. */
  def existingSession =
    if (servletRequest.getSession(false) eq null) None else Some(session)
  
  /**
   * Invalidates the current {@code WebSession} and creates a new one.
   * 
   * <p>This is needed in many cases (such as when performing a user login)
   * to defend from <a href="http://en.wikipedia.org/wiki/Session_fixation">session-fixation</a>
   * attacks.</p>
   * 
   * @return the newly created session.
   */
  def resetSession(): WebSession = {
    for (existing <- existingSession) {
      existing.invalidate()
      _session = null
    }
    session
  }
  
  /** Returns the flash scope. */
  lazy val flash = new FlashScope(session)
  
  /** Returns the application scope. */
  lazy val application = new ApplicationScope(servletContext)
  
  /** Returns the request parameters. */
  lazy val params = new RequestParameters(servletRequest, locale, converters)
  
  /** Returns the path variables. */
  lazy val path = new PathParameters(pathContext, locale, converters)
  
  /** The user's locale as returned by the configured <code>LocaleResolver</code>. */
  def locale: Locale = localeResolver.resolve(servletRequest)
  
  /** The HTTP method of the request. */
  def method = servletRequest.getMethod
  
  /**
   * The request URI of the request.
   * 
   * @see HttpServletRequest#getRequestURI
   */
  def requestUri = servletRequest.getRequestURI
  
  /**
   * The context path of the web application.
   * 
   * @see HttpServletRequest#getContextPath
   */
  def contextPath = servletRequest.getContextPath
  
  /**
   * Return an array with all the {@code Cookie}s the client sent with his request.
   * 
   * @see HttpServletRequest#getCookies
   */
  def cookies = servletRequest.getCookies
  
  /** Return the {@code Cookie} with the specified name. */
  def cookies(name: String): Option[Cookie] = cookies find { _.getName == name }
  
  /**
   * Tests whether this request originated from a {@code XMLHttpRequest} Javascript object.
   * 
   * <p>This implementation is not 100% accurate. It simply checks that the client has sent
   * the HTTP header <em>X-Requested-With</em> with the value <em>XMLHttpRequest</em>. If the
   * client doesn't send this header then this method will return {@code false} even if the
   * request originated from a {@code XMLHttpRequest} Javascript object. This is OK because
   * all major Javascript libraries send this header when performing AJAX requests.</p>
   */
  def isAjax = header("X-Requested-With").getOrElse(null) == "XMLHttpRequest"
  
  
  /**
   * Tests whether the request contains a "If-Modified-Since" HTTP header with a
   * date that is before the specified modification time.
   */
  def isModifiedSince(mtime: Long): Boolean = {
    for (lastModified <- headerDate("If-Modified-Since"))
      return mtime > lastModified
    true
  }
  
  /**
   * Tests whether the request contains a "If-None-Match" HTTP header with a value
   * that matches the specified etag.
   */
  def etagMatches(etag: String): Boolean = {
    for (re <- header("If-None-Match")) return re == etag
    false
  }
  
  /**
   * Tests whether this request is a multipart request.
   * 
   * <p>A request is multipart when it has a POST HTTP method and a
   * <a href="http://en.wikipedia.org/wiki/MIME">mutipart</a>
   * content-type.</p>
   */
  def isMultipart = WebRequest.isMultipart(servletRequest)
  
  def files(name: String): Option[FormFile] = servletRequest match {
    case multipart: MultipartHttpServletRequest => Option(multipart.getFile(name))
    case _ => None
  }
}

object WebRequest {
  
  private[this] val request = new ThreadLocal[WebRequest]
  
  /** Get the current web request. */
  def currentRequest = request.get
  
  /** Set the current web request. */
  def currentRequest_=(r: WebRequest) {
    if (r eq null)
      request.remove()
    else
      request.set(r)
  }
  
  def isMultipart(req: HttpServletRequest): Boolean = {
    if ("post" != req.getMethod.toLowerCase) return false
    val contentType = req.getContentType
    if (contentType eq null) return false
    // Can be "multipart/form-data" or "multipart/mixed"
    if (contentType.toLowerCase.startsWith("multipart/")) return true
    false
  }
}
