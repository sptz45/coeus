/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.validation.vspec

import org.junit.Test
import org.junit.Assert._
import com.coeusweb.bind.{ BindingResult, Error }

class VSpecTest {
  import VSpecTest._
  import Constraints._

  private val nullUser: User = null
  
  @Test
  def all_objects_are_valid_if_validator_has_no_constraints() {
    val v = new VSpec[User]
    assertTrue(v.validate(nullUser).isEmpty)
    assertTrue(v.validate(new User("spring", "email", 29)).isEmpty)
  }
  
 @Test
  def null_is_valid_unless_explicit_isNotNull() {
    val validator = new VSpec[User] {
      ensure("username", isNotNull)
      ensure("email",    isEmail)
    }
    val user = new User(null, null, 0)
    assertEquals(1, validator.validate(user).size)
  }
  
  @Test
  def validate_an_object() {
    val validator = new VSpec[User] {
      ensure("username", isNotNull)
      ensure("email",    hasText, isEmail)
      ensure("age",      isGreaterThan(18))
    }
    val user = new User(null, "", 12)
    assertEquals(3, validator.validate(user).size)
  }
  
  @Test
  def none_is_always_valid() {
    val validator = new VSpec[OptionalInt] {
      ensure("value", isGreaterThan(0))
    }
    assertEquals(0, validator.validate(new OptionalInt(None)).size)
  }
  
  @Test
  def some_gets_extracted_before_passing_to_the_constraints() {
    val validator = new VSpec[OptionalInt] {
      ensure("value", isGreaterThan(0))
    }
    assertEquals(0, validator.validate(new OptionalInt(Some(2))).size)
    assertEquals(1, validator.validate(new OptionalInt(Some(-2))).size)
  }
  
  @Test
  def validate_using_custom_validations() {
    val validator = new VSpec[User] {
      ensure("username", isNotNull)
      override def extraValidation(result: BindingResult[User]) {
        val user = result.target
        if (user.email == null) {
          result.addError("email", "invalid.email")
        }
      }
    }
    val user = new User("spiros", null, 0)
    assertEquals(1, validator.validate(user).size)
  }
  
  @Test
  def validate_using_custom_constraints() {
    val validator = new VSpec[User] { 
      ensure("username", isNotNull, satisfies { s: String => s == s.reverse })
      ensure("email",    isNullOrSatisfies    { s: String => s == s.reverse })
    }
    val user = new User("spiros", null, 0)
    assertEquals(1, validator.validate(user).size)
  }
  
  @Test
  def validate_an_object_from_an_association_using_validator() {
    val userValidator = new VSpec[User] {
      ensure("username", isNotNull)
      ensure("email",    hasText, isEmail)
      ensure("age",      isGreaterThan(18))
    }
    val validator = new VSpec[Post] {
      ensure("author", validatesWith(userValidator))
    }
    
    val post = new Post("title", "content", new User(null, "", 12))
    assertEquals(3, validator.validate(post).size)
  }
  
  @Test
  def validate_an_object_from_an_association() {
    val validator = new VSpec[Post] {
      ensure("author.username", isNotNull)
      ensure("author.email",    hasText, isEmail)
      ensure("author.age",      isGreaterThan(18))
    }
    val post = new Post("title", "content", new User(null, "", 12))
    assertEquals(3, validator.validate(post).size)
  }
  
  @Test(expected=classOf[com.coeusweb.bind.ExpressionException])
  def exception_raised_when_trying_to_access_null_association() {
    val validator = new VSpec[Post] {
      ensure("author.email", isNotNull)
    }
    val post = new Post("title", "content", null)
    validator.validate(post)
  }
}

object VSpecTest {
  class User(var username: String, var email: String, var age: Int)
  class Post(var title: String, var content: String, var author: User)
  class OptionalInt(var value: Option[Int])
}