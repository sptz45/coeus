package com.coeusweb.config

import com.coeusweb.interceptor._
import com.coeusweb.scope.support.FlashScopeInterceptor

trait InterceptorRegistry {
  
  private val _interceptors = Interceptors.newBuilder
  _interceptors += new ThreadLocalRequestInterceptor
  _interceptors += new FlashScopeInterceptor
  _interceptors

  def register(interceptor: RequestInterceptor) {
    _interceptors += interceptor
  }
  
  def interceptors = new Interceptors(_interceptors)
}