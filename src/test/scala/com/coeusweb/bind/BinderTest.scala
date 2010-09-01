/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.bind

import org.junit.Test
import org.junit.Assert._

class BinderTest {
  import BinderTest._
  
  val binder = new Binder(DefaultConverterRegistry)
  val locale = null

  @Test(expected=classOf[IllegalArgumentException])
  def bind_to_null_raises_exception() {
    binder.bind(Map("id" -> "12"), null, locale)
  }
  
  @Test
  def bind_to_object() {
    val user = new User
    binder.bind(Map("id" -> "12",
                    "name" -> "spiros",
                    "roles[0]" -> "admin",
                    "prefs[font-size]" -> "11"), user, locale)
    
    assertEquals(12, user.id)
    assertEquals("spiros", user.name)
    assertEquals("admin", user.roles(0))
    assertEquals(11, user.prefs("font-size"))
  }

  @Test
  def bind_to_object_using_fewer_params() {
    val user = new User
    binder.bind(Map("id" -> "12"), user, locale)
    assertEquals(12, user.id)
  }
  
  @Test
  def bind_to_object_using_param_not_in_object() {
    val user = new User
    binder.bind(Map("id" -> "12", "param" -> "value"), user, locale)
    assertEquals(12, user.id)
  }
  
  @Test
  def bind_to_object_using_more_params() {
    val user = new User
    binder.bind(Map("id" -> "12",
                    "name" -> "spiros",
                    "roles[0]" -> "admin",
                    "prefs[font-size]" -> "11",
                    "param" -> "value"), user, locale)
    
    assertEquals(12, user.id)
    assertEquals("spiros", user.name)
    assertEquals("admin", user.roles(0))
    assertEquals(11, user.prefs("font-size"))
  }
  
  @Test
  def bind_to_subclass() {
    val empl = new Employee
    binder.bind(Map("name" -> "spiros"), empl, locale)
    assertEquals("spiros", empl.name)
  }
  
  @Test
  def bind_to_association() {
    val empl = new Employee
    binder.bind(Map("supervisor.name" -> "spiros"), empl, locale)
    assertEquals("spiros", empl.supervisor.name)
  }
  
  @Test(expected=classOf[NullPointerException])
  def bind_to_null_association() {
    val empl = new Employee
    empl.supervisor = null
    binder.bind(Map("supervisor.name" -> "spiros"), empl, locale)
  }
  
  @Test
  def type_mismatch() {
    val user = new User
    val result = binder.bind(Map("id" -> "abc"), user, locale)
    assertTrue(result.hasErrors)
    assertEquals("long.type-mismatch", result.error("id").get.code)
  }
  
  @Test
  def construct_using_deny_vars() {
    val binder = new Binder(DefaultConverterRegistry, denyVars="id")
    val user = new User
    val idBeforeBinding = user.id
    val result = binder.bind(Map("id" -> "42", "name" -> "spiros"), user, locale)
    assertEquals(idBeforeBinding, user.id)
    assertEquals("spiros", user.name)
  }
}

object BinderTest {
  
  import scala.collection.mutable.HashMap
  
  class User {
    var id: Long = _
    var name: String = _
    var roles = new Array[String](5)
    var prefs = HashMap[String, Int]()
  }
  
  class Employee extends User {
    var supervisor: User = new User
  }
}
