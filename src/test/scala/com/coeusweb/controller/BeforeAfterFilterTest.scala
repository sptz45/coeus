/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.controller

import org.junit.Test
import org.junit.Assert._
import com.coeusweb.Controller
import com.coeusweb.view.{ View, ViewReference, NullView }
import com.coeusweb.core.Handler
import com.coeusweb.core.factory.SimpleControllerFactory

class BeforeAfterFilterTest {
  import BeforeAfterFilterTest._
  
  private val factory = new SimpleControllerFactory
  
  @Test
  def controller_interception() {
    val controllerClass = classOf[InterceptedController]
    val handlerMethod = controllerClass.getMethod("index")
    val handler = new Handler(controllerClass, handlerMethod)
    
    handler.handle(factory, null, null) match {
      case ViewReference(name) => assertEquals("set-from-interceptor", name)
      case r => fail("result must be a view name but was: '"+r+"'")
    }
  }
  
  @Test
  def interceptor_prevented_execution_of_hander_method() {
    val controllerClass = classOf[NoHandlerExecutionController]
    val handlerMethod = controllerClass.getMethod("index")
    val handler = new Handler(controllerClass, handlerMethod)
    
    handler.handle(factory, null, null) match {
      case ViewReference(name) => assertEquals("intercepted", name)
      case r => fail("result must be a view name but was: '"+r+"'")
    }
  }
  
  @Test(expected=classOf[IllegalArgumentException])
  def after_gets_called_even_when_before_returns_a_view() {
    val controllerClass = classOf[EnsureAfterCalledController]
    val handlerMethod = controllerClass.getMethod("index")
    val handler = new Handler(controllerClass, handlerMethod)
    handler.handle(factory, null, null)
  }
  
  @Test(expected=classOf[IllegalArgumentException])
  def after_gets_called_with_an_occurred_exception() {
    val controllerClass = classOf[ErroneousController]
    val handlerMethod = controllerClass.getMethod("throwException")
    val handler = new Handler(controllerClass, handlerMethod)
    handler.handle(factory, null, null)
  }
  
  @Test
  def handle_returns_the_view_of_after_filter() {
    val controllerClass = classOf[ControllerWithExceptionHandler]
    val handlerMethod = controllerClass.getMethod("raiseError")
    val handler = new Handler(controllerClass, handlerMethod)
    assertEquals(NullView, handler.handle(factory, null, null))
  }
}


object BeforeAfterFilterTest {

  class ErroneousController extends Controller with AfterFilter {

    def throwException() = throw new IllegalStateException

    def after(error: Option[Throwable]): Option[View] = {
      assertTrue(error.isDefined)
      assertTrue(error.get.isInstanceOf[IllegalStateException])
      throw new IllegalArgumentException 
    }
  }

  class InterceptedController extends Controller with BeforeFilter with AfterFilter {

    var result: View = _

    def index(): View = result

    def before(): Option[View] = {
      result = ViewReference("set-from-interceptor")
      None
    }
    
    def after(e: Option[Throwable]): Option[View] = None
  }

  class NoHandlerExecutionController extends InterceptedController {
    override def before() = Some(ViewReference("intercepted"))
    override def index(): View = throw new AssertionError("interceptor should have prevented the execution of this method")
  }

  class EnsureAfterCalledController extends NoHandlerExecutionController {
    override def after(e: Option[Throwable]): Option[View] = {
      throw new IllegalArgumentException
    }
  }

  class ControllerWithExceptionHandler extends Controller with AfterFilter {

    def raiseError() = throw new RuntimeException

    def after(error: Option[Throwable]) = {
      assertTrue("excpected runtime exception!", error.get.isInstanceOf[RuntimeException])
      Some(NullView)
    }
  }
}