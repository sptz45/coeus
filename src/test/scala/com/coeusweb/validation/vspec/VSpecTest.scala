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
    assertValid(v.validate(nullUser))
    assertValid(v.validate(new User("spring", "email", 29)))
  }
  
 @Test
  def null_is_valid_unless_explicit_isNotNull() {
    val validator = new VSpec[User] {
      ensure("username", isNotNull)
      ensure("email",    isEmail)
    }
    val user = new User(null, null, 0)
    assertErrors(1, validator.validate(user))
  }
  
  @Test
  def validate_an_object() {
    val validator = new VSpec[User] {
      ensure("username", isNotNull)
      ensure("email",    hasText, isEmail)
      ensure("age",      isGreaterThan(18))
    }
    val user = new User(null, "", 12)
    assertErrors(3, validator.validate(user))
  }
  
  @Test
  def multiple_invocations_of_ensure_for_the_same_field_add_all_the_constraits() {
    var firstCalled = false
    var secondCalled = false
    val validator = new VSpec[User] {
      ensure("username", satisfies { s: String => firstCalled = true; true })
      ensure("username", satisfies { s: String => secondCalled = true; true })
    }
    validator.validate(new User(null, null, 12))
    assertTrue("first constraint not called", firstCalled)
    assertTrue("second constraint not called", secondCalled)
  }
  
  @Test
  def none_is_always_valid() {
    val validator = new VSpec[OptionalInt] {
      ensure("value", isGreaterThan(0))
    }
    assertValid(validator.validate(new OptionalInt(None)))
  }
  
  @Test
  def some_gets_extracted_before_passing_to_the_constraints() {
    val validator = new VSpec[OptionalInt] {
      ensure("value", isGreaterThan(0))
    }
    assertValid(validator.validate(new OptionalInt(Some(2))))
    assertErrors(1, validator.validate(new OptionalInt(Some(-2))))
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
    assertErrors(1, validator.validate(user))
  }
  
  @Test
  def validate_using_custom_constraints() {
    val validator = new VSpec[User] { 
      ensure("username", isNotNull, satisfies { s: String => s == s.reverse })
      ensure("email",    isNullOrSatisfies    { s: String => s == s.reverse })
    }
    val user = new User("spiros", null, 0)
    assertErrors(1, validator.validate(user))
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
    assertErrors(3, validator.validate(post))
  }
  
  @Test
  def validate_an_object_from_an_association() {
    val validator = new VSpec[Post] {
      ensure("author.username", isNotNull)
      ensure("author.email",    hasText, isEmail)
      ensure("author.age",      isGreaterThan(18))
    }
    val post = new Post("title", "content", new User(null, "", 12))
    assertErrors(3, validator.validate(post))
  }
  
  @Test(expected=classOf[com.coeusweb.bind.ExpressionException])
  def exception_raised_when_trying_to_access_null_association() {
    val validator = new VSpec[Post] {
      ensure("author.email", isNotNull)
    }
    val post = new Post("title", "content", null)
    validator.validate(post)
  }
  
  @Test
  def multiple_invocations_of_ensure_for_the_same_field_add_all_the_validators() {
    var firstCalled = false
    var secondCalled = false
    val first = new VSpec[User] {
      ensure("username", satisfies { s: String => firstCalled = true; true })
    }
    val second = new VSpec[User] {
      ensure("username", satisfies { s: String => secondCalled = true; true })
    }
    val validator = new VSpec[Post] {
      ensure("author", validatesWith(first))
      ensure("author", validatesWith(second))
      
    }
    validator.validate(new Post(null, null, new User(null, "", 12)))
    assertTrue("first validator not called", firstCalled)
    assertTrue("second validator not called", secondCalled)
  }

  def assertValid(errors: Iterable[Error]) {
    assertTrue("Must be valid", errors.isEmpty)
  }

  def assertErrors(expected: Int, errors: Iterable[Error]) {
    assertEquals(expected, errors.size)
  }
}

object VSpecTest {
  class User(var username: String, var email: String, var age: Int)
  class Post(var title: String, var content: String, var author: User)
  class OptionalInt(var value: Option[Int])
}