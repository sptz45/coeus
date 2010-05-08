/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.view.scalate

import java.io.File
import javax.servlet.ServletContext
import scala.collection.mutable.ArrayBuffer
import scala.tools.nsc.Global
import org.fusesource.scalate.{Binding, TemplateEngine}
import org.fusesource.scalate.layout.DefaultLayoutStrategy
import org.fusesource.scalate.servlet.ServletResourceLoader
import org.fusesource.scalate.util.ClassPathBuilder
import com.coeusweb.view.helper.ScopeAccessor


class CustomTemplateEngine(servletContext: ServletContext) extends TemplateEngine {
  
  resourceLoader = new ServletResourceLoader(servletContext)
  
  bindings = List(Binding.of[CustomRenderContext](name="context", importMembers=true, isImplicit=false),
                  Binding.of[ScopeAccessor](name="scopes", importMembers=true, isImplicit=true))

  layoutStrategy = new DefaultLayoutStrategy(this, "WEB-INF/layouts/default.ssp")
  
  classpath =
    (new ClassPathBuilder).addPathFrom(getClass)
                          .addPathFrom(classOf[ServletContext])
                          .addPathFrom(classOf[Product])
                          .addPathFrom(classOf[Global])
                          .addClassesDir(servletContext.getRealPath("/WEB-INF/classes"))
                          .addLibDir(servletContext.getRealPath("/WEB-INF/lib"))
                          .classPath
  
  var bindingAttributes: Iterable[(String, Any)] = _
  
  def configure(config: ScalateConfig) {
    for (dir <- config.workingDirectory) {
      workingDirectory = new File(servletContext.getRealPath(dir))
      workingDirectory.mkdirs
    }
  
    bindings = bindings ++ config.bindings
    bindingAttributes = config.bindingAttributes
    
    allowCaching = config.allowCaching
    allowReload = config.allowReload
  }
}
