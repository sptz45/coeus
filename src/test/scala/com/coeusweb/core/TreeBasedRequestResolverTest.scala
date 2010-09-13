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
  
  val handler = new Handler(null, null)
  val resolver = new TreeBasedRequestResolver
  
  @Test
  def adding_the_same_path_produces_one_node() {
    registerGet("/books");  assertNumberOfNodes(1)
    registerGet("/books");  assertNumberOfNodes(1)
  }
  
  @Test
  def test_tree_creation_by_adding_paths() {
    registerGet("/books");        assertNumberOfNodes(1)
    registerGet("/books/search"); assertNumberOfNodes(2)
    registerGet("/book-search");  assertNumberOfNodes(4)
    registerGet("/bank");         assertNumberOfNodes(6)
    registerGet("/author");       assertNumberOfNodes(7)
  }
  
  @Test
  def adding_shorter_uri() {
    registerGet("/books");  assertNumberOfNodes(1)
    registerGet("/book");   assertNumberOfNodes(2)
  }
  
  @Test
  def adding_paths_with_wildcards() {
    registerGet("/books");               assertNumberOfNodes(1)
    registerGet("/books/*");             assertNumberOfNodes(3)
    registerGet("/books/author");        assertNumberOfNodes(4)
    registerGet("/books/author/*.html"); assertNumberOfNodes(7)
    registerGet("/books/author/*.xtml"); assertNumberOfNodes(9)
    registerGet("/books/author/*.css");  assertNumberOfNodes(10)
  }
  
  @Test
  def do_not_replace_existing_node() {
    registerGet("/static/*.html"); assertNumberOfNodes(3)
    registerGet("/static/*.css");  assertNumberOfNodes(5)
  }
  
  @Test
  def replace_handler() {
    registerGet("/books", "/books-search.html")
    assertHandlerFound("/books")
    assertNumberOfNodes(2)

    val newHandler = new Handler(null, null)
    resolver.register("/books", 'GET, newHandler)
    assertEquals(newHandler, resolveSuccessfully("/books", 'GET).handler)
    assertNumberOfNodes(2)
  }
  
  @Test
  def adding_the_same_wildcard() {
    registerGet("/index/*");  assertNumberOfNodes(2)
    registerGet("/index/*");  assertNumberOfNodes(2)
    
    registerGet("/*");  assertNumberOfNodes(3)
    registerGet("/*");  assertNumberOfNodes(3)
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
    assertNoHandlerFound("/index")
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
    val handleB = new Handler(null, null)
    resolver.register("/b", 'GET, handleB)
    resolver.register("/books", 'GET, handler)
    
    val resolution = resolveSuccessfully("/books", 'GET)
    assertSame(handler, resolution.handler)
    assertNotSame(handleB, resolution.handler)
  }
  
  @Test
  def wildcard_has_lower_priority_in_matches() {
    val catchAll = new Handler(null, null)
    resolver.register("/*", 'GET, catchAll)
    resolver.register("/books", 'GET, handler)
    
    val resolution = resolveSuccessfully("/books", 'GET)
    assertSame(handler, resolution.handler)
    assertNotSame(catchAll, resolution.handler)
  }
  
  @Test
  def longest_match_when_two_wildcards_present_at_the_same_posistion() {
    val catchAll = new Handler(null, null)
    resolver.register("/books/*/edit", 'GET, handler)
    resolver.register("/books/*", 'GET, catchAll)
    
    val resolution = resolveSuccessfully("/books/12/edit", 'GET)
    assertSame(handler, resolution.handler)
    assertNotSame(catchAll, resolution.handler)
  }
  
  @Test
  def capturing_wildcard_matches_even_if_other_wildcard_present() {
    val catchAll = new Handler(null, null)
    resolver.register("/books/*", 'GET, catchAll)
    resolver.register("/books/{bookId}/edit", 'GET, handler)
    
    val resolution = resolveSuccessfully("/books/12/edit", 'GET)
    assertSame(handler, resolution.handler)
    assertNotSame(catchAll, resolution.handler)
  }
  
  def assertNumberOfNodes(expected: Int) {
    assertEquals(expected, resolver.nodes)
  }
  
  def assertHandlerFound(path: String, m: Symbol = 'GET) {
    resolver.resolve(path, m) match {
      case HandlerNotFound  =>
        fail("No handler found for path %s".format(path))
      
      case MethodNotAllowed =>
        fail("Method %s not allowed for path %s".format(m.toString, path))
      
      case _ => ()
    }
  }
  
  def assertNoHandlerFound(path: String, method: Symbol = 'GET) {
    resolver.resolve(path, method) match {
      case HandlerNotFound => ()
      case resolution      =>
        fail("Found handler for path %s and method %s".format(path, method.name))
    }
  }
  
  def resolveSuccessfully(path: String, method: Symbol): SuccessfulResolution = {
    resolver.resolve(path, method) match {
      case success: SuccessfulResolution => success
      case _ => Predef.error("expected sucessful request resolution")
    }
  }
  
  def registerGet(paths: String*) {
    for (path <- paths) resolver.register(path, 'GET, handler)
  }
}
