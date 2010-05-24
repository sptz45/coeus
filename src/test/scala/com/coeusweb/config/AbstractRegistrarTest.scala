package com.coeusweb.config

import org.junit.Assert._
import com.coeusweb.core._


abstract class AbstractRegistrarTest {

  val config: DispatcherConfig
  
  def assertViewName(path: String, view: String) {
    config.requestResolver.resolve(path, Symbol("GET")) match {
      case SuccessfulResolution(hander, vars) => assertEquals(view, hander.handle(config.controllerFactory, null, null)) 
      case _ => fail("No hander found")
    }
  }
}