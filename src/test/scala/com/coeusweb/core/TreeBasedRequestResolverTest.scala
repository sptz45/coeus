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

class TreeBasedRequestResolverTest {
  
  val handler = new Handler[Controller](null, null)
  val resolver = new TreeBasedRequestResolver
  
  @Test
  def adding_the_same_path_produces_one_node() {
    registerGet("/books");  assertEquals(1, resolver.nodes)
    registerGet("/books");  assertEquals(1, resolver.nodes)
  }
  
  @Test
  def test_tree_creation_by_adding_paths() {
    registerGet("/books");        assertEquals(1, resolver.nodes)
    registerGet("/books/search"); assertEquals(2, resolver.nodes)
    registerGet("/book-search");  assertEquals(4, resolver.nodes)
    registerGet("/bank");         assertEquals(6, resolver.nodes)
    registerGet("/author");       assertEquals(7, resolver.nodes)
  }
  
  @Test
  def adding_shorter_uri() {
    registerGet("/books");  assertEquals(1, resolver.nodes)
    registerGet("/book");   assertEquals(2, resolver.nodes)
  }
  
  @Test
  def adding_paths_with_wildcards() {
    registerGet("/books");               assertEquals(1, resolver.nodes)
    registerGet("/books/*");             assertEquals(3, resolver.nodes)
    registerGet("/books/author");        assertEquals(4, resolver.nodes)
    registerGet("/books/author/*.html"); assertEquals(7, resolver.nodes)
    registerGet("/books/author/*.xtml"); assertEquals(9, resolver.nodes)
    registerGet("/books/author/*.css");  assertEquals(10, resolver.nodes)
  }
  
  @Test
  def do_not_replace_existing_node() {
    registerGet("/static/*.html"); assertEquals(3, resolver.nodes)
    registerGet("/static/*.css");  assertEquals(5, resolver.nodes)
  }
  
  @Test
  def replace_handler() {
    registerGet("/books", "/books-search.html")
    assertHandlerFound("/books")
    assertEquals(2, resolver.nodes)

    val newHandler = new Handler[Controller](null, null)
    resolver.register("/books", 'GET, newHandler)
    assertEquals(newHandler, resolveSuccessfully("/books", 'GET).handler)
    assertEquals(2, resolver.nodes)
  }
  
  @Test
  def adding_the_same_wildcard() {
    registerGet("/index/*");  assertEquals(2, resolver.nodes)
    registerGet("/index/*");  assertEquals(2, resolver.nodes)
    
    registerGet("/*");  assertEquals(3, resolver.nodes)
    registerGet("/*");  assertEquals(3, resolver.nodes)
  }
  
  @Test
  def adding_a_shorter_uri_adds_the_handler_to_the_common_parent() {
    registerGet("/book/author", "/book")
    assertHandlerFound("/book/author")
    assertHandlerFound("/book")
  }
  
  @Test
  def match_the_complete_path_when_no_wildcards() {
    registerGet("/book")
    assertHandlerFound("/book")
    assertNoHandlerFound("/book/author")
  }
  
  @Test
  def add_handler_to_root() {
    registerGet("/")
    assertHandlerFound("/")
  }
  
  @Test
  def add_handler_to_root_via_wildcard() {
    registerGet("/*")
    assertHandlerFound("/")
    assertHandlerFound("/index")
  }
  
  @Test
  def ending_slashes_get_removed_when_registered() {
    registerGet("/book////")
    assertHandlerFound("/book")
    assertHandlerFound("/book/")
  }
  
  
  @Test
  def ending_slash_also_matches() {
    registerGet("/book/author", "/book")
    assertHandlerFound("/book/author")
    assertHandlerFound("/book")
    assertHandlerFound("/book/")
    assertHandlerFound("/book////")
  }
  
  @Test
  def match_without_wildcards() {
    registerGet("/book", "/book/author")
    assertNoHandlerFound("does not exist")
    assertNoHandlerFound("/author")
    assertNoHandlerFound("/")
    assertNoHandlerFound("")
    assertHandlerFound("/book")
    assertNoHandlerFound("/book/price")
    assertHandlerFound("/book/author")
  }
  
  @Test
  def match_with_wildcards() {
    registerGet("/books/*/edit", "/books/*/authors/*/profile", "/search-*.html", "/search/*", "/bookmarks/{userId}/list")
    assertNoHandlerFound("/book")
    assertNoHandlerFound("/books/price")
    assertHandlerFound("/books/25/edit")
    assertNoHandlerFound("/books/25/delete")
    assertHandlerFound("/books/25/authors/12/profile")
    assertHandlerFound("/search-books.html")
    assertNoHandlerFound("/search-books.xml")
    assertHandlerFound("/search/all")
    assertHandlerFound("/bookmarks/sptz45/list")
  }
  
  @Test
  def extract_variables() {
    registerGet("/books/{bookId}/edit", "/books/{bookId}/authors/{authorId}/profile", "/bookmarks/{userId}")
    assertEquals("250", resolveSuccessfully("/books/250/edit", 'GET).pathVariables("bookId"))
    var resolution = resolveSuccessfully("/books/15/authors/12/profile", 'GET) 
    assertEquals(handler, resolution.handler)
    assertEquals("15", resolution.pathVariables("bookId"))
    assertEquals("12", resolution.pathVariables("authorId"))
    assertNoHandlerFound("/books/25/delete")
    assertEquals("sptz45", resolveSuccessfully("/bookmarks/sptz45", 'GET).pathVariables("userId"))
  }
  
  @Test
  def method_not_allowed_path() {
    registerGet("/books")
    assertEquals(MethodNotAllowed, resolver.resolve("/books", 'DELETE))
  }
  
  @Test
  def multiple_mehtods_for_path() {
    registerGet("/books")
    resolver.register("/books", 'POST, new Handler(null, null))
    assertHandlerFound("/books", 'GET)
    assertHandlerFound("/books", 'POST)
  }
  
  @Test
  def resolve_to_handler_with_the_longerst_path2() {
    val handleB = new Handler[Controller](null, null)
    resolver.register("/b", 'GET, handleB)
    resolver.register("/books", 'GET, handler)
    
    val resolution = resolveSuccessfully("/books", 'GET)
    assertSame(handler, resolution.handler)
    assertNotSame(handleB, resolution.handler)
  }
  
  @Test
  def wildcard_has_lower_priority_in_matches() {
    val catchAll = new Handler[Controller](null, null)
    resolver.register("/*", 'GET, catchAll)
    resolver.register("/books", 'GET, handler)
    
    val resolution = resolveSuccessfully("/books", 'GET)
    assertSame(handler, resolution.handler)
    assertNotSame(catchAll, resolution.handler)
  }
  
  @Test
  def longest_match_when_two_wildcards_present_at_the_same_posistion() {
    val catchAll = new Handler[Controller](null, null)
    resolver.register("/books/*/edit", 'GET, handler)
    resolver.register("/books/*", 'GET, catchAll)
    
    val resolution = resolveSuccessfully("/books/*/edit", 'GET)
    assertSame(handler, resolution.handler)
    assertNotSame(catchAll, resolution.handler)
  }
  
  @Test
  def capturing_wildcard_matches_even_if_other_wildcard_present() {
    val catchAll = new Handler[Controller](null, null)
    resolver.register("/books/*", 'GET, catchAll)
    resolver.register("/books/{bookId}/edit", 'GET, handler)
    
    val resolution = resolveSuccessfully("/books/12/edit", 'GET)
    assertSame(handler, resolution.handler)
    assertNotSame(catchAll, resolution.handler)
  }
  
  
  def assertHandlerFound(path: String, m: Symbol = 'GET) {
    resolver.resolve(path, m) match {
      case HandlerNotFound => fail("No handler found for path %s".format(path))
      case MethodNotAllowed => fail("Method %s not allowed for path %s".format(m.toString, path))
      case _ => ()
    }
  }
  
  def assertNoHandlerFound(path: String, m: Symbol = 'GET) {
    resolver.resolve(path, m) match {
      case HandlerNotFound => ()
      case _ => fail("Found handler for path %s".format(path))
    }
  }
  
  def resolveSuccessfully(path: String, method: Symbol): SuccessfulResolution = {
    resolver.resolve(path, method) match {
      case success: SuccessfulResolution => success
      case _ => Predef.error("excpected sucessful request resolution")
    }
  }
  
  def registerGet(paths: String*) {
    for (path <- paths) resolver.register(path, 'GET, handler)
  }
}
