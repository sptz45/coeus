/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.core.factory

import scala.collection.mutable.ArrayBuffer
import com.google.inject.{ AbstractModule, Guice, Injector }
import com.coeusweb.Controller
import com.coeusweb.config.DispatcherConfig

class GuiceControllerFactory(parent: Injector, modules: AbstractModule*) extends ControllerFactory {
  
  def this(modules: AbstractModule*) = this(null.asInstanceOf[Injector], modules: _*)
  
  private val controllers = new ControllersModule
  
  private lazy val injector = parent match {
    case null => Guice.createInjector((controllers :: modules.toList): _*)
    case _    => parent.createChildInjector((controllers :: modules.toList): _*)
  }
  
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