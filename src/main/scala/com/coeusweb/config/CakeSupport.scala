package com.coeusweb.config

import com.coeusweb.core.factory.CakeControllerFactory

trait CakeSupport {

  this: WebModule =>

  def components: Seq[AnyRef]
  
  for (cc <- components.map(_.getClass))
    CakeRegistrar.registerControllers(this, cc)
  
  controllerFactory = new CakeControllerFactory(components:_*)  
}