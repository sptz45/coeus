/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.scope

import java.util.Enumeration
import javax.servlet.http.HttpSession
import scala.collection.JavaConversions.asIterator

/**
 * A wrapper for <code>HttpSession</code> that provides convenient methods
 * for common operations.
 */
class WebSession(val servletSession: HttpSession) extends ScopedContainer with CheckAndActOperations {
  
  /**
   * Return the best object available to be used as a mutex for doing concurrent
   * check-and-act operations on the HttpSession.
   * 
   * <p>This method returns the mutex that was setup by <code>HttpSessionMutexListener</code>
   * if available, else returns the specified <code>HttpSession</code>. If you do not want to
   * use the <code>HttpSessionMutexListener</code> check if your container always returns the
   * same object instance for the same <code>HttpSession</code>.</p> 
   * 
   * @see {@link com.coeusweb.scope.support.HttpSessionMutexListener HttpSessionMutexListener}
   */
  protected def mutex: AnyRef = WebSession.getMutex(servletSession)
  
  def getAttribute[T](attribute: String): T =
    servletSession.getAttribute(attribute).asInstanceOf[T]
  
  def attributeNames = servletSession.getAttributeNames.asInstanceOf[Enumeration[String]]
  
  def update(attribute: String, value: Any) {
    servletSession.setAttribute(attribute, value)
  }
  
  def -=(attribute: String) {
    servletSession.removeAttribute(attribute)
  }
  
  /** Invalidates the session. */
  def invalidate() {
    servletSession.invalidate()
  }
  
  /**
   * Tests whether the wrapped <code>HttpSession</code> is new.
   * 
   * @return <code>true</code> if the server has created the session but the client
   *          has not yet joined.
   *
   * @throws IllegalStateException if the HttpSession is invalidated.
   */
  def isNew = servletSession.isNew
}

/**
 * Helper methods for <code>HttpSession</code> and <code>WebSession</code>
 * objects.
 */
object WebSession {
  
  private[this] val MUTEX_NAME = classOf[WebSession] + ".MUTEX"

  /**
   * Put an object, suitable to be used as a mutex, in the specified
   * <code>HttpSession</code>.
   */
  def setupMutex(session: HttpSession) {
    session.setAttribute(MUTEX_NAME, CheckAndActOperations.newMutex)
  }
  
  private def getMutex(session: HttpSession): AnyRef = {
    val mutex = session.getAttribute(MUTEX_NAME)
    if (mutex eq null) session else mutex
  }
}
