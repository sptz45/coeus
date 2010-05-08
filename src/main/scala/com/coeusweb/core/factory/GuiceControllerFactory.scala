/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.core.factory

import com.google.inject.{ AbstractModule, Guice }
import com.coeusweb.Controller
import com.coeusweb.config.DispatcherConfig

class GuiceControllerFactory extends ControllerFactory {
  
  private val controllerModule = new PublicBindModule
  
  private lazy val injector = Guice.createInjector(controllerModule, applicationModule)

  def init(config: DispatcherConfig) { }
  
  final def registerClass[C <: Controller](klass: Class[C]) {
    controllerModule.bind(klass)
  }

  final def createController[C <: Controller](klass: Class[C]): C = injector.getInstance(klass)

  def applicationModule: AbstractModule = new PublicBindModule
}

private class PublicBindModule extends AbstractModule {
  def configure() { }
    
  override def bind[C](c: Class[C]) = super.bind(c)
}