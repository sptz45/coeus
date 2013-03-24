/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.tzavellas.coeus.i18n.msg

import java.util.{ Locale, ResourceBundle, PropertyResourceBundle }
import com.tzavellas.coeus.util.internal.Interpolator 

/**
 * An abstract <code>MessageBundle</code> that provides common logic for loading
 * i18n messages from property files using <code>PropertyResourceBundle</code>.
 * 
 * @see ClasspathMessageBundle
 * @see ServletMessageBundle
 * @see PropertyResourceBundle
 * @see ResourceBundle
 */
abstract class PropertiesMessageBundle extends MessageBundle {

  final def apply(locale: Locale, code: String, args: Any*): String = {
    try {
     val message = getBundle(locale).getString(code)
     Interpolator.interpolateNumericVars(message, args)
    } catch {
      case cause: RuntimeException => throw new MessageNotFoundException(code, locale, cause)
    }
  }
  
  final def get(locale: Locale, code: String, args: Any*): Option[String] = {
    val bundle = getBundle(locale)
    if (bundle.containsKey(code)) {
      val message = bundle.getString(code)
      Some(Interpolator.interpolateNumericVars(message, args))
    } else {
      None
    }
  }
  
  /**
   * Load the bundle that contains the messages for the given locale.
   */
  protected def getBundle(locale: Locale): PropertyResourceBundle
  
  /** Clears any messages form the internal cache */
  def clearCache() {
    ResourceBundle.clearCache()
  }
}

/**
 * Useful constants for configuring subclasses of <code>PropertiesMessageBundle</code> 
 */
object PropertiesMessageBundle {

  /** Do not cache the loaded messages. */
  def DONT_CACHE = ResourceBundle.Control.TTL_DONT_CACHE
  
  /** Loaded messages never expire from cache. */
  def NO_EXPIRATION = ResourceBundle.Control.TTL_NO_EXPIRATION_CONTROL
}
