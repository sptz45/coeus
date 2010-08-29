package com.coeusweb.config

import com.google.inject.Injector
import com.coeusweb.core.factory.GuiceControllerFactory

trait GuiceSupport {

  this: WebModule =>
  
  def injector: Injector
  
  GuiceRegistrar.registerControllers(this, injector)
  controllerFactory = new GuiceControllerFactory(injector)
}