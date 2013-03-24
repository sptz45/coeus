/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.tzavellas.coeus.test

import org.junit.Assert._

object Assertions {
  
  def assertThrows[T <: Throwable](testCode: => Unit)(implicit m: Manifest[T]) {
    val expectedClass = m.runtimeClass
    try {
      testCode
      throw new AssertionError("Expected exception: " + expectedClass.getName)
    } catch {
      case actual: Throwable =>
        if (! expectedClass.isAssignableFrom(actual.getClass))
          throw new AssertionError("\nExpected exception: " + expectedClass.getName +
          		                     "\n... but got: " + actual.getClass.getName +
          		                     "\nActual Stacktrace:\n" + actual.getStackTraceString) 
    }
  }
  
  def assertNone[T](actual: Option[T]) {
  	assertEquals(None, actual)
  }
  
  def assertSome[T](actual: Option[T], message: String = "Expected Some but got None") {
  	assert(actual != None, message)
  }

  def assertSome[T](expected: T, actual: Option[T]) {
  	assert(actual != None, "Expected Some but got None")
  	assertEquals(expected, actual.get)
  }
}
