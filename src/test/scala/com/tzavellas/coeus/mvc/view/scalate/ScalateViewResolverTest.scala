/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.tzavellas.coeus.mvc.view.scalate

import javax.servlet.ServletContext
import org.junit.{ Before, Test, Ignore }
import org.junit.Assert._
import org.mockito.Mockito._
import org.mockito.Matchers._
import com.tzavellas.coeus.test.TestHelpers


class ScalateViewResolverTest extends TestHelpers {

  val servlets = mock[ServletContext]
  
  @Before
  def realPaths() {
    stubPath("/")
    stubPath("/WEB-INF/classes")
    stubPath("/WEB-INF/lib")
  }
  
  @Test
  def resolver_returns_null_for_nonexisting_template() {
    val resolver = new ScalateViewResolver(servlets)
    assertNull(resolver.resolve("does-not-exist"))
  }
  
  @Test
  def resolve_a_template() {
    val resolver = new ScalateViewResolver(servlets)
    stubPath("/WEB-INF/templates/text-only.ssp")
    assertNotNull(resolver.resolve("text-only"))
  }
  
  @Test
  def change_prefix_and_suffix_and_resolve_a_template() {
    val config = new ScalateConfigurator(servlets)
    config.templatePrefix="/WEB-INF/other-templates/"
    config.templateSuffix=".scaml"
    val resolver = new ScalateViewResolver(config)
    stubPath("/WEB-INF/other-templates/text-only.scaml")
    assertNotNull(resolver.resolve("text-only"))
  }
  
  def stubPath(path: String) {
    when(servlets.getRealPath(path)).thenReturn(realPath(path))
  }
  
  def realPath(path: String) = {
    val testClassPathRoot = new java.io.File(getClass.getClassLoader.getResource("WEB-INF").toURI).getParent
    testClassPathRoot.toString + (if (path.startsWith("/")) "" else "/") + path
  }
}
