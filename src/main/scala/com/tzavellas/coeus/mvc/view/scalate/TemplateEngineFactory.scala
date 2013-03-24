package com.tzavellas.coeus.mvc.view.scalate

import scala.tools.nsc.Global
import javax.servlet.ServletContext
import org.fusesource.scalate.util.ClassPathBuilder
import org.fusesource.scalate.layout.DefaultLayoutStrategy
import org.fusesource.scalate.servlet.ServletResourceLoader
import org.fusesource.scalate.{ Binding, TemplateEngine }
import com.tzavellas.coeus.Stage
import com.tzavellas.coeus.mvc.scope.ScopeAccessor

private object TemplateEngineFactory {
  
  def newEngine(config: ScalateConfigurator) = {
    val engine = new TemplateEngine
    
    applyDefaults(engine, config.servletContext)
    
    engine.bindings = engine.bindings ++ config.bindings.values

    config.configure(engine)
    
    engine
  }
  
  
  private def applyDefaults(engine: TemplateEngine, servletContext: ServletContext) {
    engine.mode = Stage.of(servletContext).name
    
    engine.resourceLoader = new ServletResourceLoader(servletContext)
    engine.layoutStrategy = new DefaultLayoutStrategy(engine, "WEB-INF/layouts/default.ssp")
    
    engine.bindings = List(
      Binding.of[CustomRenderContext](name="context", importMembers=true, isImplicit=false),
      Binding.of[ScopeAccessor](name="scopes", importMembers=true, isImplicit=true))
    
    engine.classpath =
      (new ClassPathBuilder).addPathFrom(getClass)
                            .addPathFrom(classOf[ServletContext])
                            .addPathFrom(classOf[Product])
                            .addPathFrom(classOf[Global])
                            .addClassesDir(servletContext.getRealPath("/WEB-INF/classes"))
                            .addLibDir(servletContext.getRealPath("/WEB-INF/lib"))
                            .classPath
  }

}