/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.mvc.controller

import java.util.Locale
import org.junit.{ Before, Test }
import org.junit.Assert._
import org.springframework.mock.web._
import com.coeusweb._
import bind.ConverterRegistry.{defaultConverters => converters} 
import i18n.msg.MessageBundle
import mvc.view.{ViewName, NullView}


class AbstractControllerTest {
  import AbstractControllerTest._

  val controller = new PostController
  
  val messages: MessageBundle = null
  val servletContext = new MockServletContext
  

  @Test
  def validate_with_errors() {
    controller.request = request()
    assertErrors(controller.handleUsingValidate())
    assertBinding(controller.request)
  }
  
  @Test
  def validate_successful() {
    controller.request = request("title" -> "title", "content" -> "content")
    assertSuccess(controller.handleUsingValidate())
    assertBinding(controller.request)
  }
  
  @Test
  def ifValid_with_errors() {
    controller.request = request()
    assertErrors(controller.handleUsingIfValid())
    assertBinding(controller.request)
  }
  
  @Test
  def ifValid_successful() {
    controller.request = request("title" -> "title", "content" -> "content")
    assertSuccess(controller.handleUsingIfValid())
    assertBinding(controller.request)
  }
  
  @Test
  def ifValid_session_successful() {
    // Simulates two web requests by reusing the same WebRequest and Controller
    // objects. Dirty but OK for this test.
    controller.request = request("title" -> "title", "content" -> "content")
    controller.showSessionForm()
    assertNotNull(controller.request.session.getAttribute("post"))
    
    assertSuccess(controller.handleUsingIfValidSession())
    assertBinding(controller.request)
    assertNull(controller.request.session.getAttribute("post"))
  }

  @Test
  def not_modified_sets_the_status_code() {
    controller.request = request()
    controller.response = new WebResponse(new MockHttpServletResponse)
    assertEquals(NullView, controller.notModified)
    assertEquals(304, controller.response.status)
  }

  // -- Test Helper methods -----------------------------------------------------
  
  def request(params: (String, String)*) = {
    val mock = new MockHttpServletRequest("GET", "/post")
    for ((name, value) <- params) mock.setParameter(name, value)
    val resolver = new i18n.locale.FixedLocaleResolver(Locale.US)
    new WebRequest(servletContext, mock, null, resolver, converters, messages)
  }
  
  def assertSuccess(view: Any) {
    def doAssert(name: String) = assertEquals("Should have returned the success view", SUCCESS_VIEW, name)
    view match {
      case ViewName(name) => doAssert(name)
      case name: String => doAssert(name)
    }
  }
  
  def assertErrors(view: Any) {
    def doAssert(name: String) = assertEquals("Should have returned the error view", ERROR_VIEW, name)
    view match {
      case ViewName(name) => doAssert(name)
      case name: String => doAssert(name)
    }
  }
  
  def assertBinding(request: WebRequest) {
    assertNotNull(request.getAttribute("post"))
    assertNotNull(ModelAttributes.getBindingResult(request))
    assertEquals("post", request("modelAttribute"))
  }
}


object AbstractControllerTest {
  import validation.vspec.{ VSpec, Constraints }  

  val ERROR_VIEW = "post-form"
  val SUCCESS_VIEW = "post"

  class Post {
    var title: String = _
    var content: String = _
  }

  implicit object PostValidator extends VSpec[Post] with Constraints {
    ensure("title", hasText)
    ensure("content", hasText)
  }

  class PostController extends AbstractController {
    
    override def storeModelInSession = true
    override def formView = render(ERROR_VIEW)
    
    def showSessionForm() {
      model += new Post
    }
    
    def handleUsingValidate() = {
      val result = validate(new Post)
      if (result.hasErrors) formView else render(SUCCESS_VIEW)
    }
    
    def handleUsingIfValid() = ifValid(new Post) {
      post => render(SUCCESS_VIEW)
    }
    
    def handleUsingIfValidSession() = ifValid {
      post: Post => render(SUCCESS_VIEW)
    }
  }
}