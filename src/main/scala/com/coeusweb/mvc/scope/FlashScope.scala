/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.mvc.scope

import java.util.concurrent.ConcurrentHashMap
import scala.collection.mutable.HashSet
import scala.collection.JavaConversions.{ JConcurrentMapWrapper => MapWrapper, _ }
import com.coeusweb.mvc.WebRequest

/**
 * A <code>ScopedContainer</code> for storing attributes that get read
 * after a redirect.
 *
 * <p>This scope is useful for implementing the redirect-after-post workflow.
 * Any attribute that is stored in this scope will be automatically removed
 * at the end of the request if it has been read during the request or if it
 * has been stored in this scope for longer than one minute.</p>
 * 
 * @param session the user's WebSession
 */
class FlashScope(val session: WebSession) extends ScopedContainer {
  import FlashScope._
  
  /* Contains the names of the attributes that are marked for removal. */
  private lazy val marked = new HashSet[String]
  
  def getAttribute[T](attribute: String): T = {
    val entry = getFlashMap.get(attribute)
    (if (entry eq null) null else { marked += attribute; entry.value }).asInstanceOf[T]
  }
  
  def update(attribute: String, value: Any) {
    getFlashMap.put(attribute, new FlashScopeEntry(value))
  }
  
  def -=(attribute: String) {
    getFlashMap.remove(attribute)
  }
  
  def attributeNames = getFlashMap.keys
  
  def notice = apply("flash-notice")
  def notice_=(message: String) = update("flash-notice", message)
  
  def warning = apply("flash-warning")
  def warning_=(message: String) = update("flash-warning", message)
  
  def error = apply("flash-error")
  def error_=(message: String) = update("flash-error", message)
  
  /**
   * Removes from the scope any attribute that has been read or that has been
   * put into the scope more than one minute ago.
   */
  def sweep() {
    val flashMap = new MapWrapper(getFlashMap)
    for ((attr, entry) <- flashMap) if (marked.contains(attr) || hasExpired(entry)) {
      flashMap -= attr
    }
    if (flashMap.isEmpty) {
      session -= FLASH_SCOPE_NAME
    }
  }
  
  /* Tests whether the given entry has expired (used in unit tests). */
  private[scope] def hasExpired(entry: FlashScopeEntry) = entry.hasExpired

  /* Get the map that stores the attributes. */
  private def getFlashMap = getOrCreateFlashMap(session)
}

/**
 * Support methods for working with <code>FlashScope</code>.
 */
object FlashScope {
  private val FLASH_SCOPE_NAME = "flashScope"
  
  /**
   * Remove any stale objects from the <code>FlashScope</code>.
   * 
   * @param request the current request
   */
  def sweep(request: WebRequest) {
    for (session <- request.existingSession) {
      if (session.getAttribute[AnyRef](FLASH_SCOPE_NAME) ne null)
        request.flash.sweep()
    }
  }

  /* Gets the underlying map for storing the scope's attributes */
  private[scope] def getOrCreateFlashMap(session: WebSession) = {
    session.putIfAbsent(FLASH_SCOPE_NAME, new ConcurrentHashMap[String, FlashScopeEntry])
  }
}

private class FlashScopeEntry(val value: Any) {
  val timestamp = System.currentTimeMillis
  /* Entries expire after 1 minute. */
  def hasExpired = System.currentTimeMillis - timestamp >= 60000
}
