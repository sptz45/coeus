/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.mvc.controller

import java.util.Locale
import org.junit.{ Before, After, Test }
import org.junit.Assert._
import org.springframework.mock.web._
import com.coeusweb._
import bind.ConverterRegistry.{defaultConverters => converters} 
import i18n.msg.MessageBundle
import mvc.{ WebRequest, WebResponse }
import mvc.scope.MutableScopes
import mvc.view.{ ViewName, NullView }


class AbstractControllerTest {
  
  import AbstractControllerTest._
  import WebRequest.currentRequest

  val controller = new PostController
  controller.converters = converters
  
  @After
  def cleanupWebRequest() {
    WebRequest.currentRequest = null
    WebResponse.currentResponse = null
  }

  @Test
  def validate_with_errors() {
    currentRequest = request()
    assertErrors(controller.handleUsingValidate())
    assertBinding(controller.request)
  }
  
  @Test
  def validate_successful() {
    currentRequest = request("title" -> "title", "content" -> "content")
    assertSuccess(controller.handleUsingValidate())
    assertBinding(controller.request)
  }
  
  @Test
  def ifValid_with_errors() {
    currentRequest = request()
    assertErrors(controller.handleUsingIfValid())
    assertBinding(controller.request)
  }
  
  @Test
  def ifValid_successful() {
    currentRequest = request("title" -> "title", "content" -> "content")
    assertSuccess(controller.handleUsingIfValid())
    assertBinding(controller.request)
  }
  
  @Test
  def ifValid_session_successful() {
    // Simulates two web requests by reusing the same WebRequest and Controller
    // objects. Dirty but OK for this test.
    currentRequest = request("title" -> "title", "content" -> "content")
    controller.showSessionForm()
    assertNotNull(controller.request.session.getAttribute("post"))
    
    assertSuccess(controller.handleUsingIfValidSession())
    assertBinding(controller.request)
    assertNull(controller.request.session.getAttribute("post"))
  }

  @Test
  def not_modified_sets_the_status_code() {
    currentRequest = request()
    WebResponse.currentResponse = new WebResponse(new MockHttpServletResponse)
    assertEquals(NullView, controller.notModified)
    assertEquals(304, controller.response.status)
  }

  // -- Test Helper methods -----------------------------------------------------
  
  def request(params: (String, String)*) = {
    val mock = new MockHttpServletRequest("GET", "/post")
    for ((name, value) <- params) mock.setParameter(name, value)
    val resolver = new i18n.locale.FixedLocaleResolver(Locale.US)
    new WebRequest(null, mock, null, resolver, converters, null)
  }
  
  def assertSuccess(view: Any) {
    def doAssert(name: String) = assertEquals("Should have returned the success view", SUCCESS_VIEW, name)
    view match {
      case ViewName(name) => doAssert(name)
      case name: String   => doAssert(name)
    }
  }
  
  def assertErrors(view: Any) {
    def doAssert(name: String) = assertEquals("Should have returned the error view", ERROR_VIEW, name)
    view match {
      case ViewName(name) => doAssert(name)
      case name: String   => doAssert(name)
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

  val ERROR_VIEW   = "post-form"
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
      val result = bindAndValidate(new Post)
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
