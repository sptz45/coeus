/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.test

import com.coeusweb.core.RequestContext
import com.coeusweb.core.interception.Interceptor
import com.coeusweb.view.{View, ViewResolver}


class MockViewResolver(views: Map[String, View]) extends ViewResolver {
  def resolve(name: String) = views.getOrElse(name, null)
}


class MockInterceptor extends Interceptor {
  
  private var pre, post, after = false
  
  def wasCalled = pre && post && after
  
  def reset() {
    pre = false
    post = false
    after = false
  }

  def preHandle(context: RequestContext) = {
    pre = true
    true
  }
  def postHandle(context: RequestContext) {
    post = true
  }  
  def afterRender(context: RequestContext) {
    after = true
  }
}