/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.validation.bean

import org.junit.Test
import org.junit.Assert._
import javax.validation.{ Validation => JValidation }
import javax.validation.constraints.NotNull
import com.coeusweb.bind.{ BindingResult, Error }
import com.coeusweb.test.Assertions._

class BeanValidatorTest {
  import BeanValidatorTest._
  
  @Test
  def validate_returns_a_list_of_errors() {
  	val errors = validator.validate(new Post).iterator
  	assertTrue(errors.hasNext)
  	assertNotNull(errors.next.code)
  }
  
  @Test
  def validate_using_binding_result() {
  	val result = new BindingResult(null, new Post)
  	validator.validate(result)
  	assertTrue(result.hasErrors)
  	assertNone(result.error("content"))
  	assertSome(result.error("title"))
  	assertEquals(validator.errorFormatter, result.errorFormatter)
  }
  
  @Test
  def validate_field() {
  	assertNone(validator.validateField[Post]("title", "The title"))
  	assertSome(validator.validateField[Post]("title", null))
  }
}


object BeanValidatorTest {
  
  val validator = new BeanValidator(
    JValidation.buildDefaultValidatorFactory.getValidator)

  class Post {
    @NotNull
    var title: String = _
    var content: String = _
  }
}