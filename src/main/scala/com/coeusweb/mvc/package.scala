/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb

package object mvc {
  
  type Path = com.coeusweb.annotation.Path
  type Get = com.coeusweb.annotation.Get
  type Post = com.coeusweb.annotation.Post
  type Put = com.coeusweb.annotation.Put
  type Delete = com.coeusweb.annotation.Delete
  type HttpMethod = com.coeusweb.annotation.HttpMethod
  
  //TODO uncomment when Scala bug #3384 gets fixed (it will be fixed in RC2)
  //type Binder = com.coeusweb.bind.Binder
  
  type Controller = com.coeusweb.Controller
  
  type AbstractController = com.coeusweb.controller.AbstractController
  
  type ResponseHelpers = com.coeusweb.controller.ResponseHelpers
  
  type BeforeFilter = com.coeusweb.controller.BeforeFilter
  
  type AfterFilter = com.coeusweb.controller.AfterFilter
  
  type View = com.coeusweb.view.View
}