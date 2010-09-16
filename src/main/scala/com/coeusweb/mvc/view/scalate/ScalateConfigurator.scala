/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.mvc.view.scalate

import javax.servlet.ServletContext
import scala.collection.mutable.Map
import org.fusesource.scalate.{ Binding, TemplateEngine }
import com.coeusweb.mvc.view.helper.DefaultViewHelpers

class ScalateConfigurator(val servletContext: ServletContext) {

  val bindings   = Map[String, Binding]()
  val attributes = Map[String, Any]()

  var templatePrefix = "/WEB-INF/templates/"
  var templateSuffix = ".ssp"

  bind("c" -> new DefaultViewHelpers(servletContext))


  def configure(engine: TemplateEngine) { }


  def bind[T: Manifest](binding: (String, T),
                        importMembers: Boolean = false,
                        isImplicit: Boolean = false) = {
    attributes += binding

    val name = binding._1
    val className = implicitly[Manifest[T]].erasure.getName

    bindings += (name -> new Binding(
      name, className, importMembers=importMembers, isImplicit=isImplicit))
  }
}