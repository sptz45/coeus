package com.coeusweb.config

import org.junit.Test
import org.junit.Assert._
import com.coeusweb.interceptor.ThreadLocalRequestInterceptor
import com.coeusweb.scope.support.FlashScopeInterceptor

class InterceptorRegistryTest {

  val registry = new InterceptorRegistry { }
  
  @Test
  def by_default_interceptors_contains_the_flash_scope_and_thead_local_interceptors() {
    val interceptors = registry.interceptors
    assertTrue(interceptors.exists(_.isInstanceOf[FlashScopeInterceptor]))
    assertTrue(interceptors.exists(_.isInstanceOf[ThreadLocalRequestInterceptor]))
  }
}