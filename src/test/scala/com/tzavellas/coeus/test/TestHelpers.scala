/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.tzavellas.coeus.test

import org.mockito.Mockito

trait TestHelpers {

  def mock[T](implicit m: Manifest[T]): T = Mockito.mock(m.runtimeClass).asInstanceOf[T]
  
  def assertThrows[T <: Throwable](testCode: => Unit)(implicit m: Manifest[T]) {
    Assertions.assertThrows[T](testCode)(m)
  }
}
