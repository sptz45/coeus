/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.config

import scala.collection.mutable.ListBuffer
import com.coeusweb.Controller

/**
 * A trait to register <code>Controller</code> classes for a
 * {@code DispatcherServlet}.
 */
trait ControllerRegistry {
  
  private val _controllers = new ListBuffer[Class[Controller]]
  
  /** Register the specified controller class. */
  def register[C <: Controller](implicit cm: ClassManifest[C]) {
    register(cm.erasure.asInstanceOf[Class[Controller]])
  }
  
  /**
   * Register the specified controller class.
   * 
   * @param controller the controller class to register
   */
  def register[C <: Controller](controller: Class[C]) {
    _controllers += controller.asInstanceOf[Class[Controller]]
  }

  /** A collection with the registered controller classes. */
  def controllers: Seq[Class[Controller]] = _controllers
}