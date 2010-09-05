/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.mvc.scope

import org.junit.Test
import org.junit.Assert._

trait CheckAndActOperationsTest {
  
  val attributes: ScopedContainer with CheckAndActOperations

  def setAttributeToMock(name: String, value: Any)
  
  @Test
  def put_attribute_if_it_does_not_exist() {
    val input = new Entry("spiros")
    val result = attributes.putIfAbsent("name", input)
    assertSame(input, result)
    assertSame(input, attributes("name"))
  }
  
  @Test
  def the_attribute_does_not_get_added_if_already_exists() {
    val spiros = new Entry("spiros")
    setAttributeToMock("name", spiros)
    val input = new Entry("name")
    val result = attributes.putIfAbsent("name", input)
    assertNotSame(input, result)
    assertNotSame(input, attributes("name"))
    assertSame(spiros, result)
  }
  
  @Test
  def putIfAbsent_always_returns_the_same_object() {
    val first = attributes.putIfAbsent("spiros", new Entry("spiros"))
    assertSame(first, attributes.putIfAbsent("spiros", new Entry("spiros")))
    assertSame(first, attributes.putIfAbsent("spiros", new Entry("spiros")))
  }
  
  @Test
  def modify_existing_attribute() {
    val input = new Entry("Java")
    setAttributeToMock("name", input)
    val result = attributes.updateAndPut[Entry]("name"){ e => e.value = "Scala" }
    assertSame(input, result)
    assertSame(input, attributes("name"))
    assertEquals("Scala", attributes[Entry]("name").value)
  }
  
  private class Entry(var value: String)
}
