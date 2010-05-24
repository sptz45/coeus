/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.config

import scala.collection.mutable.ListBuffer
import com.coeusweb.Controller


trait ControllerRegistry {
  
  private val _controllers = new ListBuffer[Class[Controller]]
  
  def register[C <: Controller](implicit c: ClassManifest[C]) {
    register(c.erasure.asInstanceOf[Class[Controller]])
  }
  
  def register[C <: Controller](klass: Class[C]) {
    _controllers += klass.asInstanceOf[Class[Controller]]
  }

  def controllers: Seq[Class[Controller]] = _controllers
}