/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.tzavellas.coeus.i18n.msg

import java.io.{ File, FileInputStream }
import java.util.{ Locale, ResourceBundle, PropertyResourceBundle }
import javax.servlet.ServletContext

/**
 * A <code>PropertyResourceBundle</code> that loads the property files from the specified
 * <code>ServletContext</code>.
 * 
 * <p>This implementations will try load the property files using the an absolute path obtained
 * by calling the {@link ServletContext#getRealPath(path)} method.</p>
 * 
 * @param servletContext the <code>ServletContext</code of the web application 
 * @param timeToLive the milliseconds before an entry gets expired from the cache
 * @param baseName the base name of the property files (see the documentation of
 *        {@link java.util.ResourceBundle#getBundle(String, Locale, ClassLoader) ResourceBundle}
 *        for the details of the naming conventions.
 */
class ServletMessageBundle(
  servletContext: ServletContext,
  timeToLive: Long,
  baseName: String = "WEB-INF/messages")
    extends PropertiesMessageBundle {
  
  private object control extends ResourceBundle.Control {
    override def getTimeToLive(base: String, locale: Locale) = timeToLive
    override def getFormats(base: String) = ResourceBundle.Control.FORMAT_PROPERTIES
    
    override def needsReload(base: String, locale: Locale, Format: String , loader: ClassLoader, bundle: ResourceBundle, loadTime: Long): Boolean = {
      if (bundle eq null) throw new NullPointerException
      val fileName = toResourceName(toBundleName(base, locale), "properties")
      val lastModified = new File(servletContext.getRealPath(fileName)).lastModified
      lastModified >= loadTime
    }
    
    override def newBundle(base: String, locale: Locale, format: String, loader: ClassLoader, reload: Boolean): ResourceBundle = {
      val fileName = toResourceName(toBundleName(base, locale), "properties")
      new PropertyResourceBundle(new FileInputStream(servletContext.getRealPath(fileName)))
    }
  }
  
  private val classLoader = this.getClass.getClassLoader

  override protected def getBundle(locale: Locale) = {
    ResourceBundle.getBundle(baseName, locale, classLoader, control).asInstanceOf[PropertyResourceBundle]
  }
}
