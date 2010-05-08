/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 * 
 * Ported from Apache Jakarta Commons Validator,
 * http://commons.apache.org/validator/
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.validation.vspec.constraint

import org.junit.Test
import org.junit.Assert._


class UriConstraintTest {

  import UriConstraintTest._
  
  val constraint = new UrlConstraint("http", "https")
  
  @Test
  def null_is_valid_url() {
    assertTrue(constraint.isValid(null))
  }
  
  @Test
  def isValid() {
    urls.foreach(url => assertEquals(url._1, url._2, constraint.isValid(url._1)))
  }
  
  @Test
  def validator202() {
    assertTrue(constraint.isValid("http://www.logoworks.comwww.logoworks.comwww.logoworks.comwww.logoworks.comwww.logoworks.comwww.logoworks.comwww.logoworks.comwww.logoworks.comwww.logoworks.comwww.logoworks.comwww.logoworks.comwww.logoworks.comwww.logoworks.comwww.logoworks.comwww.logoworks.comwww.logoworks.comwww.logoworks.comwww.logoworks.comwww.logoworks.comwww.logoworks.comwww.logoworks.comwww.logoworks.comwww.logoworks.comwww.logoworks.comwww.logoworks.comwww.logoworks.comwww.logoworks.comwww.logoworks.comwww.logoworks.comwww.log"))
  }
  
  @Test
  def validator204() {
    assertTrue(constraint.isValid("http://tech.yahoo.com/rc/desktops/102;_ylt=Ao8yevQHlZ4On0O3ZJGXLEQFLZA5"))
  }
  
  @Test
  def custom_url_schemes() {
    val ftpOnly = new UrlConstraint("ftp")
    assertFalse(ftpOnly.isValid("http://tech.yahoo.com/"))
    assertTrue(ftpOnly.isValid("ftp://ftp.ntua.gr/"))
  }
  
  @Test
  def all_url_schemes() {
    val allSchemes = new UrlConstraint()
    assertTrue(allSchemes.isValid("http://tech.yahoo.com/"))
    assertTrue(allSchemes.isValid("ftp://ftp.ntua.gr/"))
  }
}

/**
 * The data given below approximates the 4 parts of a URL
 * <scheme>://<authority><path>?<query> except that the port number
 * is broken out of authority to increase the number of permutations.
 * A complete URL is composed of a scheme+authority+port+path+query,
 * all of which must be individually valid for the entire URL to be considered
 * valid.
 */
private object UriConstraintTest {
  
  val schemes = List(("http://", true),
                     ("ftp://", false),
                     ("h3t://", false),
                     ("3ht://", false),
                     ("http:/", false),
                     ("http:", false),
                     ("http/", false),
                     ("://", false),
                     ("", false))
  
  val authorities = List(("www.google.com", true),
                         ("go.com", true),
                         ("go.au", true),
                         ("0.0.0.0", true),
                         ("255.255.255.255", true),
                         ("256.256.256.256", false),
                         ("255.com", true),
                         ("1.2.3.4.5", false),
                         ("1.2.3.4.", false),
                         ("1.2.3", false),
                         (".1.2.3.4", false),
                         ("go.a", false),
                         ("go.a1a", true),
                         ("go.1aa", false),
                         ("aaa.", false),
                         (".aaa", false),
                         ("aaa", true),
                         ("", false))
  
  val ports = List((":80", true),
                   (":65535", true),
                   (":0", true),
                   ("", true),
                   (":-1", false),
                   (":65636", false),
                   (":65a", false))
  
  val paths = List(("/test1", true),
                   ("/t123", true),
                   ("/$23", true),
                   ("/..", false),
                   ("/../", false),
                   ("/test1/", true),
                   ("/#", false),
                   ("", true),
                   ("/test1/file", true),
                   ("/t123/file", true),
                   ("/$23/file", true),
                   ("/../file", false),
                   ("/..//file", false),
                   ("/test1//file", true),
                   ("/#/file", false))
  
  val queries = List(("?action=view", true),
                     ("?action=edit&mode=up", true),
                     ("", true))
  
  val urls: Array[(String, Boolean)] = {
    val input = new Array[(String, Boolean)](schemes.size * authorities.size * ports.size * queries.size)
    var i = 0
    for (scheme <- schemes; auth <- authorities; port <- ports; query <- queries) {
      input(i) = (new StringBuilder().append(scheme._1).append(auth._1).append(port._1).append(query._1).toString, 
                  scheme._2 && auth._2 && port._2 && query._2)
      i += 1
    }
    input
  }
}