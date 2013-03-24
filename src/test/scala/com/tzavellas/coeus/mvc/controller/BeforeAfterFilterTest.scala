/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.tzavellas.coeus.mvc.controller

import org.junit.Test
import org.junit.Assert._
import com.tzavellas.coeus.mvc.view.{ View, ViewName, NullView }
import com.tzavellas.coeus.core.Handler

class BeforeAfterFilterTest {
  import BeforeAfterFilterTest._
  
  @Test
  def controller_interception() {
    execute(new InterceptedController, "index") match {
      case ViewName(name) => assertEquals("set-from-interceptor", name)
      case r => fail("result must be a view name but was: '"+r+"'")
    }
  }
  
  @Test
  def interceptor_prevented_execution_of_hander_method() {
    execute(new NoHandlerExecutionController, "index") match {
      case ViewName(name) => assertEquals("intercepted", name)
      case r => fail("result must be a view name but was: '"+r+"'")
    }
  }
  
  @Test(expected=classOf[IllegalArgumentException])
  def after_gets_called_even_when_before_returns_a_view() {
    execute(new EnsureAfterCalledController, "index")
  }
  
  @Test(expected=classOf[IllegalArgumentException])
  def after_gets_called_with_an_occurred_exception() {
    execute(new ErroneousController, "throwException")
  }
  
  @Test
  def handle_returns_the_view_of_after_filter() {
    val result = execute(new ControllerWithExceptionHandler, "raiseError")
    assertEquals(NullView, result)
  }
  
  private def execute(c: Controller, handlerMethod: String) = {
    val controllerClass = c.getClass
    val method = controllerClass.getMethod(handlerMethod)
    (new Handler(c, method)).handle()
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

    def before() {
      result = ViewName("set-from-interceptor")
    }
    
    def after(e: Option[Exception]): Option[View] = None
  }

  class NoHandlerExecutionController extends InterceptedController {
    override def before() { stopAndRender(ViewName("intercepted")) }
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
      assertTrue("expected runtime exception!", error.get.isInstanceOf[RuntimeException])
      Some(NullView)
    }
  }
}