/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.i18n.msg

import java.util.{ Locale, ResourceBundle, PropertyResourceBundle }

/**
 * A <code>PropertyResourceBundle</code> that loads the property files from class-path.
 * 
 * <p>This implementations will try load the property files using the specified ClassLoader.
 * If a ClassLoader is not specified then it will try to use the context ClassLoader of the
 * current thread and if it is not set it will use the ClassLoader that loaded this class.</p>
 * 
 * @param timeToLive the milliseconds before an entry gets expired from the cache
 * @param loader the classLoader to use for loading the property files
 * @param baseName the base name of the property files (see the documentation of
 *        {@link java.util.ResourceBundle#getBundle(String, Locale, ClassLoader) ResourceBundle}
 *        for the details of the naming conventions.
 */
class ClasspathMessageBundle(
  timeToLive: Long,
  loader: Option[ClassLoader] = None,
  baseName: String = "messages")
    extends PropertiesMessageBundle {
  
  private object control extends ResourceBundle.Control {
    override def getTimeToLive(base: String, locale: Locale) = timeToLive
    override def getFormats(base: String) = ResourceBundle.Control.FORMAT_PROPERTIES
  }
  
  override protected def getBundle(locale: Locale) = {
    ResourceBundle.getBundle(baseName, locale, classLoader, control).asInstanceOf[PropertyResourceBundle]
  }
  
  override def clearCache() {
    ResourceBundle.clearCache(classLoader)
  }
  
  private def classLoader: ClassLoader = {
    for (cl <- loader) return cl
    val contextLoader = Thread.currentThread.getContextClassLoader
    if (contextLoader != null) contextLoader else this.getClass.getClassLoader
  }
}
