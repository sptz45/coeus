/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.validation.vspec.constraint

import org.junit.Test
import com.coeusweb.test.Assertions.assertThrows

class GenericConstraintsTest {
  
  object constraints extends GenericConstraints
  
  @Test
  def isNotNull_test_that_value_is_not_null() {
    val notNull = constraints.isNotNull[String]
    assert(! notNull.isValid(null))
    assert(  notNull.isValid(""))
  }

  @Test
  def isOneOf_tests_that_value_must_belongs_in_the_enumeration() {
    val oneOrTwo = constraints.isOneOf("one", "two")
    assert(! oneOrTwo.isValid(null))
    assert(  oneOrTwo.isValid("one"))
    assert(  oneOrTwo.isValid("two"))
    assert(! oneOrTwo.isValid("three"))
  }
  
  @Test
  def satisfies_ensures_value_satisfies_a_predicate_or_is_null() {
    val isUpperCase = constraints.satisfies { s: String => s.forall(_.isUpper) }
    assert(! isUpperCase.isValid("hello"))
    assert(  isUpperCase.isValid("HELLO"))
    assert(  isUpperCase.isValid(null))
  }
  
  @Test
  def isUnique_tests_whether_the_value_is_unique() {
    val users = List(User("spiros"))
    val uniqueUsername = constraints.isUnique { name: String => users.find(_.username == name) }
    assert(! uniqueUsername.isValid("spiros"))
    assert(  uniqueUsername.isValid("sptz45"))
  }
  
  case class User(username: String)
}