/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.core

import org.junit.Test
import org.junit.Assert._
import com.coeusweb.mvc.controller.Controller

class TreeBasedRequestResolverParserTest {
  import TreeBasedRequestResolver._
  
  val handler = new Handler(null, null)
  
  @Test
  def no_template_to_parse() {
    assertEquals(None, UriTemplateParser.parse("", 'GET, handler))
  }
  
  @Test
  def template_without_wildcards() {
    val node = parsePath("/this/is/a/uri/without/wildcards")
    assertEquals(1, node.numberOfNodes)
    assertEquals(handler, node.handlers('GET))
  }
  
  @Test
  def template_with_one_wildcard() {
    val node = parsePath("/search-*.html")
    assertEquals(3, node.numberOfNodes)
    assertEquals(handler, node.children.head.children.head.handlers('GET))
  }

  @Test
  def template_with_many_wildcards() {
    val node = parsePath("/books/*/authors/*/profile/*")
    assertEquals(6, node.numberOfNodes)
  }
  
  @Test
  def template_with_capturing_wildcard() {
    val node = parsePath("/books/{bookId}/edit")
    assertEquals(3, node.numberOfNodes)
    assertEquals("bookId", node.children.head.asInstanceOf[CapturingWildcardNode].variable)
    assertEquals(handler, node.children.head.children.head.handlers('GET))
  }
  
  @Test
  def template_with_many_capturing_wildcards() {
    val node = parsePath("/books/{bookId}/author/{authorId}")
    assertEquals(4, node.numberOfNodes)
    assertEquals("bookId",   node.children.head.asInstanceOf[CapturingWildcardNode].variable)
    assertEquals("authorId", node.children.head.children.head.children.head.asInstanceOf[CapturingWildcardNode].variable)
  }
  
  private def parsePath(path: String) = UriTemplateParser.parse(path, 'GET, handler).get
}
