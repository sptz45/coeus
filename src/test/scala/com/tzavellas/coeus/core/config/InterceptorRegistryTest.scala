/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.tzavellas.coeus.core.config

import org.junit.Test
import org.junit.Assert._
import com.tzavellas.coeus.core.interception._

class InterceptorRegistryTest {

  val registry = new InterceptorRegistry { }
  
  @Test
  def by_default_interceptors_contains_the_flash_scope_and_thead_local_interceptors() {
    val interceptors = registry.interceptors.result
    assertTrue(interceptors.exists(_.isInstanceOf[FlashScopeInterceptor]))
    assertTrue(interceptors.exists(_.isInstanceOf[ThreadLocalInterceptor]))
  }
}