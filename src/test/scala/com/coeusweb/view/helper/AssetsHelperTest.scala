/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.view.helper

import javax.servlet.ServletContext
import org.junit.Test
import org.junit.Assert._
import org.mockito.Mockito._
import com.coeusweb.test.TestHelpers

class AssetsHelperTest {
  import AssetsHelperTest._
  
  @Test
  def without_asset_hosts() {
    assertEquals("""<script type="text/javascript" src="/test/assets/jquery.js?0"></script>""", helper1.script("jquery").toString)
    assertEquals("""<link href="/test/assets/yui.css?0" type="text/css" rel="stylesheet" media="print"></link>""", helper1.stylesheet("yui", "print").toString)
    assertEquals("""<img src="/test/assets/logo.png?0" alt="The logo"></img>""", helper1.image("logo.png", "The logo").toString)
    assertEquals("""<img width="10" height="20" src="/test/assets/logo.png?0" alt="The logo"></img>""", helper1.image("logo.png", "The logo", 10, 20).toString)
  }
  
  @Test
  def with_asset_host() {
    assertEquals("""<script type="text/javascript" src="http://assets.example.com/assets/jquery.js?0"></script>""", helper2.script("jquery").toString)
    assertEquals("""<link href="http://assets.example.com/assets/yui.css?0" type="text/css" rel="stylesheet" media="print"></link>""", helper2.stylesheet("yui", "print").toString)
    assertEquals("""<img src="http://assets.example.com/assets/logo.png?0" alt="The logo"></img>""", helper2.image("logo.png", "The logo").toString)
    assertEquals("""<img width="10" height="20" src="http://assets.example.com/assets/logo.png?0" alt="The logo"></img>""", helper2.image("logo.png", "The logo", 10, 20).toString)
  }
  
  @Test
  def custom_version() {
    assertEquals("""<script type="text/javascript" src="/test/assets/jquery.js?42"></script>""", helper3.script("jquery").toString)
  }
}

object AssetsHelperTest extends TestHelpers {
  
  val host = "http://assets.example.com"
  
  val context = mock[ServletContext]
  when(context.getContextPath).thenReturn("/test")
  
  object helper1 extends AssetsHelper {
    val servletContext = context
  }
  
  object helper2 extends AssetsHelper {
    val servletContext = context
    override val assetHosts = List(host)
  }
  
  object helper3 extends AssetsHelper {
    val servletContext = context
    override val version = Some("42")
  }
}