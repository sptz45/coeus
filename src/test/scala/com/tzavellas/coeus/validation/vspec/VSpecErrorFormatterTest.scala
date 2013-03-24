/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.tzavellas.coeus.validation.vspec

import java.util.Locale
import org.junit.Test
import org.junit.Assert._
import org.mockito.Mockito._
import org.mockito.Matchers._
import scala.collection.mutable.HashMap
import com.tzavellas.coeus.bind.Error
import com.tzavellas.coeus.bind.ConverterRegistry.{defaultConverters => converters}
import com.tzavellas.coeus.i18n.msg._
import com.tzavellas.coeus.test.TestHelpers

class VSpecErrorFormatterTest extends TestHelpers {
  import VSpecErrorFormatterTest._

  val locale: Locale = null
  val messages = mock[MessageBundle]
  val format = VSpecErrorFormatter.format(_: Error, locale, messages, converters)
  
  @Test
  def per_object_error_code() {
    val expectedCode = "user.email.hasText.invalid"
      
    when(messages.get(locale, expectedCode)).thenReturn(Some("Email is required"))
    
    assertEquals("Email is required", format(emailHasNoText))
  }
  
  @Test
  def default_error_code_for_constraint() {
    val perObjectCode = "user.email.hasText.invalid"
    val defaultCode = "constraint.hasText.invalid"
    
    when(messages.get(locale, perObjectCode)).thenReturn(None)
    when(messages(locale, defaultCode)).thenReturn("Field is required")
    
    assertEquals("Field is required", format(emailHasNoText))
  }
  
  @Test(expected=classOf[MessageNotFoundException])
  def exception_when_no_message_found() {
    when(messages.get(any(), any(), any())).thenReturn(None)
    when(messages(any(), any(), any())).thenThrow(new MessageNotFoundException("any", locale, null))
    format(emailHasNoText)
  }
  
  @Test
  def message_with_no_internpolation_variables() {
    when(messages.get(any(), any(), any())).thenReturn(Some("is required"))
    assertEquals("is required", format(emailHasNoText))
  }
  
  @Test
  def message_with_internpolation_variables() {
    when(messages.get(any(), any(), any())).thenReturn(None)
    when(messages(any(), any(), any())).thenReturn("{1} in {2} cannot be {3}")
                                                  .thenReturn("{1}{2}{3}")
                                                  .thenReturn(" {3} {2} {1} ")
                                                  .thenReturn("error:{1} is {3}")
    
    assertEquals("email in User cannot be null", format(emailHasNoText))
    assertEquals("emailUsernull", format(emailHasNoText))
    assertEquals(" null User email ", format(emailHasNoText))
    assertEquals("error:email is null", format(emailHasNoText))
  }
}


object VSpecErrorFormatterTest {
  val emailHasNoText = new Error("hasText.invalid", "email", classOf[User], null)
  class User(var name: String, var email: String)
}