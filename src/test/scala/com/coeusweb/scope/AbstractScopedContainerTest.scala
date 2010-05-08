/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.scope

import org.junit.Test
import org.junit.Assert._

trait AbstractScopedContainerTest {
  
  val attributes: ScopedContainer
  
  def setAttributeToMock(name: String, value: Any)
  
  @Test(expected=classOf[RequiredAttributeException])
  def if_an_attribute_does_not_exits_an_exception_gets_thrown() {
    assertNull(attributes[Int]("does not exist"))
  }
  
  @Test
  def getAttribute_returns_null_if_an_attribute_does_not_exits() {
    assertNull(attributes.getAttribute[Int]("does not exist"))
  }
  
  @Test
  def read_an_existing_attribute() {
    setAttributeToMock("ten", 1L)
    assertEquals(1L, attributes("ten"))
  }
  
  @Test
  def reading_an_optional_attribute() {
    assertEquals(None, attributes.get[Int]("ten"))
    setAttributeToMock("ten", 10)
    assertEquals(Some(10), attributes.get[Int]("ten"))
  }
  
  @Test
  def adding_removing_updating_attributes() {
    attributes("ten") = 10L
    assertEquals(10, attributes("ten"))
    attributes("ten") = "ten"
    assertEquals("ten", attributes("ten"))
    attributes -= "ten"
    assertNull(attributes.getAttribute("ten"))
  }
  
  @Test
  def add_an_attribute_using_generated_name() {
    val now = new java.util.Date
    attributes += now
    assertEquals(now, attributes("date"))
  }
}
