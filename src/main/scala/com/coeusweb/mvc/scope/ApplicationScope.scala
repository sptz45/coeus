/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.mvc.scope

import java.util.Enumeration
import javax.servlet.ServletContext
import scala.collection.JavaConversions.asIterator

/**
 * A <code>ScopedContainer</code> implementation for <code>ServletContext</code>.
 */
class ApplicationScope(val servletContext: ServletContext) extends ScopedContainer with CheckAndActOperations {
  
  /**
   * Return a mutex for synchronizing access to <code>ServletContext</code>.
   *
   * @see CheckAndActOperations
   * @see ApplicationScope#setupMutex(ServletContext)
   */
  protected def mutex = ApplicationScope.getExistingMutex(servletContext)
  
  def getAttribute[T](attribute: String): T =
    servletContext.getAttribute(attribute).asInstanceOf[T]
  
  def update(attribute: String, value: Any) {
    servletContext.setAttribute(attribute, value)
  }
  
  def -=(attribute: String) {
    servletContext.removeAttribute(attribute)
  }
  
  def attributeNames = servletContext.getAttributeNames.asInstanceOf[Enumeration[String]]
}

/**
 * Helper methods for <code>ServletContext</code> and <code>ApplicationScope</code>
 * objects.
 */
object ApplicationScope {
  
  private[this] val MUTEX_NAME = classOf[ApplicationScope] + ".MUTEX"
  
  /**
   * Put an object, suitable to be used as a mutex, in the given
   * <code>ServletContext</code>.
   */
  def setupMutex(application: ServletContext) {
    application.setAttribute(MUTEX_NAME, CheckAndActOperations.newMutex)
  }
  
  private def getExistingMutex(sc: ServletContext): AnyRef = {
    def mutex = sc.getAttribute(MUTEX_NAME)
    assert(mutex ne null, "No mutex found for ApplicationScope. Have you called ApplicationScope.setupMutex() before attempting to synchronize on this scope?")
    mutex
  }
}