/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.mvc.controller

import org.junit.Test
import org.junit.Assert._
import org.springframework.mock.web.MockHttpServletRequest
import com.coeusweb.bind.Binder
import com.coeusweb.mvc.WebRequest
import com.coeusweb.mvc.scope.RequiredAttributeException
import com.coeusweb.test.Assertions.assertThrows

class ModelAttributesTest {
  
  val request = new WebRequest(null, new MockHttpServletRequest, null, null, null, null)
  val model = new ModelAttributes(new Binder(null), request, storeInSession=true)
  
  @Test
  def add_a_model_attribute() {
    model("model") = spiros
    assertEquals(spiros, model("model"))
    assertEquals(spiros, request("model"))
    assertEquals("model", ModelAttributes.getModelAttributeName(request))
    assertEquals(spiros, ModelAttributes.getBindingResult(request).target)
  }

  @Test
  def add_a_model_attribute_using_generated_name() {
    model += spiros
    assertEquals(spiros, model("user"))
    assertEquals(spiros, request("user"))
    assertEquals("user", ModelAttributes.getModelAttributeName(request))
    assertEquals(spiros, ModelAttributes.getBindingResult(request).target)
  }
  
  @Test
  def adding_a_session_model_attribute() {
    model += spiros
    assertEquals(spiros, model("user"))
    assertEquals(spiros, request("user"))
    assertEquals(spiros, request.session("user"))
    assertEquals("user", ModelAttributes.getModelAttributeName(request))
    assertEquals(spiros, ModelAttributes.getBindingResult(request).target)
  }
  
  @Test
  def read_model_attribute_from_session_if_not_in_request() {
    model += spiros
    request -= "user"
    assertEquals(spiros, model("user"))
  }
  
  @Test
  def remove_a_model_attribute() {
    model += spiros
    model -= spiros 
    assertThrows[RequiredAttributeException] { model[Any]("user") }
    assertThrows[RequiredAttributeException] { request[Any]("user") }
    assertThrows[RequiredAttributeException] { request.session[Any]("user") }
    assertThrows[RequiredAttributeException] { ModelAttributes.getBindingResult(request) }
  }
  
  @Test
  def do_not_store_attribute_in_session() {
  	val notInSession = new ModelAttributes(new Binder(null), request, storeInSession=false)
  	notInSession += spiros
  	assertThrows[RequiredAttributeException] { request.session[User]("user") }
  }

  
  class User(var name: String)
  val spiros = new User("spiros")
}