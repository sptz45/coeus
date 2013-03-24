/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.tzavellas.coeus.mvc.controller

import org.junit.{Test, Before, After }
import org.junit.Assert._
import org.springframework.mock.web.MockHttpServletRequest
import com.tzavellas.coeus.bind.Binder
import com.tzavellas.coeus.mvc.WebRequest
import com.tzavellas.coeus.mvc.scope.RequiredAttributeException
import com.tzavellas.coeus.test.Assertions.assertThrows

class ModelAttributesTest {
  
  val request = new WebRequest(null, new MockHttpServletRequest, null, null, null, null)
  val model = new ModelAttributes(new Binder(null), storeInSession=true)
  
  @Before
  def setupWebRequest() { WebRequest.currentRequest = request }
  
  @After
  def cleanupWebRequest() { WebRequest.currentRequest = null }
  
  
  @Test
  def add_a_model_attribute() {
    model("model") = spiros
    assertEquals(spiros, model("model"))
    assertEquals(spiros, request("model"))
    assertEquals("model", ModelAttributes.getCurrentModelName(request))
    assertEquals(spiros, ModelAttributes.getBindingResult(request).target)
  }

  @Test
  def add_a_model_attribute_using_generated_name() {
    model += spiros
    assertEquals(spiros, model("user"))
    assertEquals(spiros, request("user"))
    assertEquals("user", ModelAttributes.getCurrentModelName(request))
    assertEquals(spiros, ModelAttributes.getBindingResult(request).target)
  }
  
  @Test
  def adding_a_session_model_attribute() {
    model += spiros
    assertEquals(spiros, model("user"))
    assertEquals(spiros, request("user"))
    assertEquals(spiros, request.session("user"))
    assertEquals("user", ModelAttributes.getCurrentModelName(request))
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
  	val notInSession = new ModelAttributes(new Binder(null), storeInSession=false)
  	notInSession += spiros
  	assertThrows[RequiredAttributeException] { request.session[User]("user") }
  }
  
  @Test
  def current_binding_result() {
    assertEquals(None, model.currentBindingResult)
    model += spiros
    val result = model.currentBindingResult[User].get
    assertSame(spiros, result.target)
  }
  
  @Test
  def no_errors_if_binding_result_not_present() {
    assertFalse(model.hasErrors)
  }
  
  @Test
  def the_model_has_errors_when_the_binding_result_has() {
    model += spiros
    assertFalse(model.hasErrors)
    model.currentBindingResult.get.addError("name", "name.error")
    assertTrue(model.hasErrors)
  }

  
  class User(var name: String)
  val spiros = new User("spiros")
}