package com.coeusweb.config

import org.junit.Test
import org.junit.Assert._
import com.google.inject.Guice
import com.coeusweb.Controller
import com.coeusweb.annotation.Get
import com.coeusweb.core._
import com.coeusweb.core.factory.GuiceControllerFactory

class GuiceRegistrarTest extends AbstractRegistrarTest {
  
  import GuiceRegistrarTest._
  
  val injector = Guice.createInjector(new WebModule)
  
  val config = new DispatcherConfig(null) {
    override lazy val controllerFactory = new GuiceControllerFactory(injector)
  }
  val registry = new ControllerRegistry(config)

  @Test
  def registers_controllers_from_guice_injector() {
    GuiceRegistrar.registerControllers(registry, injector)
    assertViewName("/guice/index", "guice")
  }
}

object GuiceRegistrarTest {
  import com.google.inject.AbstractModule
  import com.google.inject.Inject
  
  class WebModule extends AbstractModule {
    def configure() {
      bind(classOf[Dependency]).to(classOf[Implementation])
      bind(classOf[GuiceController])
    }
  }
  
  class GuiceController @Inject() (val injected: Dependency) extends Controller {
    @Get def index = injected.view
  }
  trait Dependency { def view: String }
  class Implementation extends Dependency { def view = "guice" }
}
