/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.tzavellas.coeus

package object mvc {
  
  type Controller = controller.Controller
  
  type Path = annotation.Path
  type Get = annotation.Get
  type Post = annotation.Post
  type Put = annotation.Put
  type Delete = annotation.Delete
  type HttpMethod = annotation.HttpMethod
  
  type Binder = bind.Binder
  val  Binder = bind.Binder
  
  type AbstractController = controller.AbstractController
  type ResponseHelpers = controller.ResponseHelpers
  
  type BeforeFilter = controller.BeforeFilter
  type AfterFilter = controller.AfterFilter
  
  type View = view.View
}