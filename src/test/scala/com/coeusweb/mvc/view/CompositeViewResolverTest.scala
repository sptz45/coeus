/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.mvc.view

import org.junit.Test
import org.junit.Assert._
import org.mockito.Mockito._
import com.coeusweb.test.TestHelpers

class CompositeViewResolverTest extends TestHelpers {
  
  val first = mock[ViewResolver]
  val second = mock[ViewResolver]
  
  val resolver = new CompositeViewResolver(first, second)

  @Test
  def null_if_no_resolver_can_resolve_the_view_name() {
    assertNull(resolver.resolve("does-not-exist"))
  }
  
  @Test
  def return_the_view_from_the_first_resolver_that_resolves_the_view_name() {
    when(second.resolve("null-view")).thenReturn(NullView)
    assertEquals(NullView, resolver.resolve("null-view"))
  }
}
