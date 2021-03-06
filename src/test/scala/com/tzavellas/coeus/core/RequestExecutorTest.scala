/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.tzavellas.coeus.core

import org.hamcrest.{ BaseMatcher, Description }
import org.junit.Test
import org.junit.Assert._
import org.mockito.Mockito._
import org.mockito.Matchers._
import org.springframework.mock.web.MockHttpServletResponse
import com.tzavellas.coeus.test.TestHelpers
import com.tzavellas.coeus.mvc.{ WebRequest, WebResponse }
import com.tzavellas.coeus.mvc.controller.Controller
import com.tzavellas.coeus.mvc.view._
import error.{ ExceptionHandler, ErrorPageView }
import interception.Interceptor

class RequestExecutorTest extends TestHelpers {
  
  val views = mock[ViewResolver]
  val exceptionHandler = mock[ExceptionHandler]
  val handler = mock[Handler]
  val interceptor = mock[Interceptor]
  val error = new RuntimeException
  
  
  @Test
  def call_handler_with_no_interceptors() {
    when(handler.handle()).thenReturn(NullView, Array[Object](): _*)

    execute()
    
    verifyZeroInteractions(exceptionHandler)
  }
  
  @Test
  def exception_in_handler_calls_exception_handler() {
    when(handler.handle()).thenThrow(error)
    when(exceptionHandler.handle(any())).thenReturn(NullView)
    
    execute()
    
    verify(exceptionHandler).handle(argThat(hasError(error)))
  }
  
  @Test
  def propagate_exception_to_servlet_container() {
    when(handler.handle()).thenThrow(error)
    when(exceptionHandler.handle(any())).thenReturn(ErrorPageView)
    
    assertThrows[RuntimeException] {
      execute()
    }
    
    verify(exceptionHandler).handle(argThat(hasError(error)))
  }
  
  @Test
  def no_view_found_throws_exception() {
    when(handler.handle()).thenReturn("does not exist", Array[Object](): _*)
    when(exceptionHandler.handle(any())).thenReturn(ErrorPageView)
    
    assertThrows[NoViewFoundException] {
      execute()
    }
    
    verify(exceptionHandler).handle(any())
  }
  
  @Test
  def interceptor_gets_called_around_handler() {
    when(handler.handle()).thenReturn(NullView, Array[Object](): _*)
    when(interceptor.preHandle(any())).thenReturn(true)
    
    execute(interceptor)
    
    verifyInterceptorAndHandler()
    verifyZeroInteractions(exceptionHandler)
  }
  
  @Test
  def interceptor_prevents_the_execution_of_handler() {
    when(interceptor.preHandle(any())).thenReturn(false)
    
    execute(interceptor)
    
    verifyInterceptor(interceptor)
    verifyZeroInteractions(handler)
  }
  
  @Test
  def interceptor_gets_called_despite_handler_exception() {
    when(exceptionHandler.handle(any())).thenReturn(NullView)
    when(handler.handle()).thenThrow(error)
    when(interceptor.preHandle(any())).thenReturn(true)
    
    execute(interceptor)
    
    verifyInterceptorAndHandler()
    verify(exceptionHandler).handle(argThat(hasError(error)))
    verify(interceptor).postHandle(argThat(hasError(error)))
    verify(interceptor).afterRender(argThat(hasError(error)))
  }
  
  @Test
  def exception_in_interceptor_prevents_further_execution() {
    when(interceptor.preHandle(any())).thenThrow(error)
    when(exceptionHandler.handle(any())).thenReturn(NullView)
    
    val interceptor_2 = mock[Interceptor]
    execute(interceptor, interceptor_2)

    verifyZeroInteractions(handler, interceptor_2)    
    verifyInterceptor(interceptor)
    verify(exceptionHandler).handle(argThat(hasError(error)))
    verify(interceptor).postHandle(argThat(hasError(error)))
    verify(interceptor).afterRender(argThat(hasError(error)))
  }
  
  @Test
  def exception_after_handler_calls_all_interceptors_for_cleanup() {
    val interceptor_2 = mock[Interceptor]
    when(interceptor.preHandle(any())).thenReturn(true)
    when(interceptor_2.preHandle(any())).thenReturn(true)
    when(interceptor_2.postHandle(any())).thenThrow(error)
    when(exceptionHandler.handle(any())).thenReturn(NullView)
    
    execute(interceptor, interceptor_2)

    verifyInterceptors(interceptor, interceptor_2)
    verify(handler).handle()
    verify(exceptionHandler).handle(argThat(hasError(error)))
  }
  
  @Test
  def request_contains_the_first_exception_if_multiple_occur() {
    val interceptor_2 = mock[Interceptor]
    when(interceptor.preHandle(any())).thenReturn(true)
    when(interceptor_2.preHandle(any())).thenThrow(error)
    when(interceptor.postHandle(any())).thenThrow(new IllegalArgumentException)
    when(exceptionHandler.handle(any())).thenReturn(NullView)
    
    execute(interceptor, interceptor_2)

    verifyInterceptors(interceptor, interceptor_2)
    verify(exceptionHandler).handle(argThat(hasError(error)))
    
    verify(interceptor).postHandle(argThat(hasError(error)))
    verify(interceptor).afterRender(argThat(hasError(error)))
    verify(interceptor_2).postHandle(argThat(hasError(error)))
    verify(interceptor_2).afterRender(argThat(hasError(error)))
  }
  
  
  @Test
  def exceptions_during_view_rendering_are_caught() {
    val view = mock[View]
    when(view.render(any(), any())).thenThrow(error)
    when(views.resolve(any())).thenReturn(view)
    when(exceptionHandler.handle(any())).thenReturn(NullView)
    when(handler.handle()).thenReturn("view-name", Array[Object](): _*)
    
    execute()
    
    verify(exceptionHandler).handle(argThat(hasError(error)))
  }
  
  @Test
  def exception_when_controller_method_has_invalid_return_type() {
    when(exceptionHandler.handle(any())).thenReturn(ErrorPageView)
    
    val executor = new RequestExecutor(Nil, exceptionHandler, null)
    val controller = new RequestExecutorTest.SampleController
    val cc = controller.getClass
    val handler = new Handler(controller, cc.getMethod("invalidReturnType"))
    
    assertThrows[InvalidControllerClassException] {
      executor.execute(makeRequestContext(handler))
    }
  }
  
  // -- Helpers ---------------------------------------------------------
  
  private def hasError(error: Throwable) = new BaseMatcher[RequestContext] {
    def matches(item: Any) = item match {
      case request: RequestContext => request.error == error 
      case _ => false
    }
    def describeTo(desc: Description) {
      desc.appendText("Request context did not contain the error value: ")
      desc.appendValue(error)
    }
  }
  
  private def verifyInterceptor(ri: Interceptor) {
    val order = inOrder(ri)
    order.verify(ri).preHandle(any())
    order.verify(ri).postHandle(any())
    order.verify(ri).afterRender(any())
  }
  
  private def verifyInterceptors(ris: Interceptor*) {
    val order = inOrder(ris: _*)
    for (ri <- ris) order.verify(ri).preHandle(any())
    for (ri <- ris) order.verify(ri).postHandle(any())
    for (ri <- ris) order.verify(ri).afterRender(any())
  }
  
  private def verifyInterceptorAndHandler() {
    val order = inOrder(interceptor, handler)
    order.verify(interceptor).preHandle(any())
    order.verify(handler).handle()
    order.verify(interceptor).postHandle(any())
    order.verify(interceptor).afterRender(any())
  }
  
  private def execute(ris: Interceptor*) {
    val executor = new RequestExecutor(aggregateInterceptors(ris: _*), exceptionHandler, views)
    executor.execute(makeRequestContext(handler))
  }
  
  private def aggregateInterceptors(is: Interceptor*): Iterable[Interceptor] = {
    val builder = new scala.collection.mutable.ListBuffer[Interceptor]
    is.foreach(builder += _)
    builder.result
  }
  
  private def makeRequestContext(handler: Handler) = {
    val response = new WebResponse(new MockHttpServletResponse)
    val request = new WebRequest(null, null, null, null, null, null) {
      override def locale = java.util.Locale.ENGLISH
    }
    new RequestContext(request, response, handler)
  }
}


object RequestExecutorTest {
  
  class SampleController extends Controller {
    
    def invalidReturnType = 43
  }
}
