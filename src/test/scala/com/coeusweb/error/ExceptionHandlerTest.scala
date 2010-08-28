package com.coeusweb
package error

import org.junit.Test
import org.junit.Assert._

import test.servlet._
import view.ViewFactory._
import param.MissingParameterException

class ExceptionHandlerTest {
  
  val context = {
    val req = new WebRequest(new MockHttpServletRequest, Map(), null, null)
    val res = new WebResponse(new MockHttpServletResponse)
    new core.RequestContext(req, res, null)
  }
  
  val missingParam = render("client-error")
  
  val handler = ExceptionHandler.forServlet("test-servlet") {
    case e: MissingParameterException => missingParam
  }
  
  @Test
  def return_a_view_from_the_partial_function() {
    context.error = new MissingParameterException("q")
    val errorView = handler.handle(context)
    
    assertEquals(missingParam, errorView)
    assertEquals(400, context.response.status)
  }
  
  @Test
  def the_default_error_view_is_ErrorPageView() {
    context.error = new IllegalStateException
    val errorView = handler.handle(context)
    
    assertEquals(ErrorPageView, errorView)
    assertEquals(500, context.response.status)
  }
}