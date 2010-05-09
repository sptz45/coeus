/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.core.factory

import scala.collection.mutable.ArrayBuffer
import com.google.inject.{ AbstractModule, Guice }
import com.coeusweb.Controller
import com.coeusweb.config.DispatcherConfig

class GuiceControllerFactory(modules: AbstractModule*) extends ControllerFactory {
  
  private val controllers = new ControllersModule
  
  private lazy val injector = Guice.createInjector((controllers :: modules.toList): _*)

  def init(config: DispatcherConfig) { }
  
  final def registerClass[C <: Controller](klass: Class[C]) {
    controllers += klass
  }

  final def createController[C <: Controller](klass: Class[C]): C = injector.getInstance(klass)
}

private class ControllersModule extends AbstractModule {
  
  private val controllers = new ArrayBuffer[Class[_]]

  def +=(c: Class[_]) {
    controllers += c
  }
  
  def configure() {
    controllers foreach { bind(_) }
  }
}

private object NullGuiceModule extends AbstractModule {
  def configure() { /* do nothing */ }
}