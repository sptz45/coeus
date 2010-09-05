/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.mvc.view.scalate

import scala.collection.mutable.HashMap
import org.fusesource.scalate.Binding

class ScalateConfig(
  val workingDirectory: Option[String] = None,
  val allowCaching: Boolean = true,
  val allowReload: Boolean = true,
  val templatePrefix: String = "/WEB-INF/templates/",
  val templateSuffix: String = ".ssp") {
  
  val bindingMap = new HashMap[Binding, Any]
  
  def bindings = bindingMap.keySet.toList
  
  def bindingAttributes = bindingMap.map{ e => (e._1.name, e._2) }
  
  def bind[T](implicit m: Manifest[T]) = new BindingClass(m.erasure)
  
  /** Helper for the binding internal DSL */
  class BindingClass(klass: Class[_]) {
    def to(value: Any) = new BindingPair(klass, value)
  }
  
  /** Helper for the binding internal DSL */
  class BindingPair(klass: Class[_], value: Any) {
    def using(name: String, importMembers: Boolean, isImplicit: Boolean) {
      val binding = Binding(name, klass.getName, importMembers=importMembers, isImplicit=isImplicit)
      bindingMap += binding -> value
    }
  }
}
