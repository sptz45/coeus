package com.tzavellas.coeus.core.error

import org.junit.Test
import org.junit.Assert._
import org.springframework.mock.web._
import com.tzavellas.coeus.core.RequestContext
import com.tzavellas.coeus.mvc.{ WebRequest, WebResponse }
import com.tzavellas.coeus.mvc.view.ViewFactory._
import com.tzavellas.coeus.mvc.param.MissingParameterException

class ExceptionHandlerTest {
  
  val context = {
    val req = new WebRequest(null, new MockHttpServletRequest, Map(), null, null, null)
    val res = new WebResponse(new MockHttpServletResponse)
    new RequestContext(req, res, null)
  }
  
  val missingParamView = render("client-error")
  
  val handler = ExceptionHandler.forServlet("test-servlet") {
    case e: MissingParameterException => missingParamView
  }
  
  @Test
  def the_default_handler_always_returns_ErrorPageView() {
    val defaultHandler = ExceptionHandler.defaultHandler("test-servlet")
    context.error = new RuntimeException
    val errorView = defaultHandler.handle(context)
    
    assertEquals(ErrorPageView, errorView)
    assertEquals(500, context.response.status)
    assertEquals(context.error, context.request("exception"))
  }
  
  @Test
  def the_handler_returns_a_view_from_the_partial_function() {
    context.error = new MissingParameterException("q")
    val errorView = handler.handle(context)
    
    assertEquals(missingParamView, errorView)
    assertEquals(400, context.response.status)
    assertEquals(context.error, context.request("exception"))
  }
  
  @Test
  def the_handler_returns_ErrorPageView_if_no_view_found_in_partial_function() {
    context.error = new IllegalStateException
    val errorView = handler.handle(context)
    
    assertEquals(ErrorPageView, errorView)
    assertEquals(500, context.response.status)
    assertEquals(context.error, context.request("exception"))
  }
}