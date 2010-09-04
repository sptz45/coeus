/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.validation.bean

import java.util.Locale
import org.junit.Test
import org.junit.Assert._
import com.coeusweb.bind.{ BindingResult, Error }
import com.coeusweb.i18n.msg.MessageBundle

class BeanErrorFormatterTest {

  @Test
  def binding_errors_are_resolved_using_the_message_bundle() {
    val notInt = Error.typeMismatch("age", classOf[Int], "five")
    assertEquals("Not an Int", format(notInt))
  }
  
  @Test
  def validation_errors_are_already_formatted_by_the_jsr303_impl() {
    val error = Error("the error message")
    assertEquals("the error message", format(error))
  }

  def format(error: Error) = BeanErrorFormatter.format(error, null, messages, null)
  
  object messages extends MessageBundle {
    val msgs = Map("int.type-mismatch" -> "Not an Int")
    def apply(locale: Locale, code: String, args: Any*) = msgs.apply(code)
    def get(locale: Locale, code: String, args: Any*) = msgs.get(code)
  }
}
