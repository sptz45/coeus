/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.mvc.controller

import org.junit.Test
import org.junit.Assert._
import com.coeusweb.mvc.view.{ View, ViewName, NullView }
import com.coeusweb.core.Handler

class BeforeAfterFilterTest {
  import BeforeAfterFilterTest._

  private var handler: Handler = _
  
  @Test
  def controller_interception() {
    setupHandler(new InterceptedController, "index")
    handler.handle(null, null) match {
      case ViewName(name) => assertEquals("set-from-interceptor", name)
      case r => fail("result must be a view name but was: '"+r+"'")
    }
  }
  
  @Test
  def interceptor_prevented_execution_of_hander_method() {
    setupHandler(new NoHandlerExecutionController, "index")
    handler.handle(null, null) match {
      case ViewName(name) => assertEquals("intercepted", name)
      case r => fail("result must be a view name but was: '"+r+"'")
    }
  }
  
  @Test(expected=classOf[IllegalArgumentException])
  def after_gets_called_even_when_before_returns_a_view() {
    setupHandler(new EnsureAfterCalledController, "index")
    handler.handle(null, null)
  }
  
  @Test(expected=classOf[IllegalArgumentException])
  def after_gets_called_with_an_occurred_exception() {
    setupHandler(new ErroneousController, "throwException")
    handler.handle(null, null)
  }
  
  @Test
  def handle_returns_the_view_of_after_filter() {
    setupHandler(new ControllerWithExceptionHandler, "raiseError")
    assertEquals(NullView, handler.handle(null, null))
  }
  
  def setupHandler(c: Controller, method: String) = {
    val controllerClass = c.getClass
    val handlerMethod = controllerClass.getMethod(method)
    handler = new Handler(c, handlerMethod)
  }
}


object BeforeAfterFilterTest {

  class ErroneousController extends Controller with AfterFilter {

    def throwException() = throw new IllegalStateException

    def after(error: Option[Exception]): Option[View] = {
      assertTrue(error.isDefined)
      assertTrue(error.get.isInstanceOf[IllegalStateException])
      throw new IllegalArgumentException 
    }
  }

  class InterceptedController extends Controller with BeforeFilter with AfterFilter {

    var result: View = _

    def index(): View = result

    def before(): Option[View] = {
      result = ViewName("set-from-interceptor")
      None
    }
    
    def after(e: Option[Exception]): Option[View] = None
  }

  class NoHandlerExecutionController extends InterceptedController {
    override def before() = Some(ViewName("intercepted"))
    override def index(): View = throw new AssertionError("interceptor should have prevented the execution of this method")
  }

  class EnsureAfterCalledController extends NoHandlerExecutionController {
    override def after(e: Option[Exception]): Option[View] = {
      throw new IllegalArgumentException
    }
  }

  class ControllerWithExceptionHandler extends Controller with AfterFilter {

    def raiseError() = throw new RuntimeException

    def after(error: Option[Exception]) = {
      assertTrue("excpected runtime exception!", error.get.isInstanceOf[RuntimeException])
      Some(NullView)
    }
  }
}