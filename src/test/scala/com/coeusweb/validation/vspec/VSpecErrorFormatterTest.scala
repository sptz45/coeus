/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.validation.vspec

import java.util.Locale
import org.junit.Test
import org.junit.Assert._
import org.mockito.Mockito._
import org.mockito.Matchers._
import scala.collection.mutable.HashMap
import com.coeusweb.bind.{ ConverterRegistry, Error }
import com.coeusweb.i18n.msg._
import com.coeusweb.test.TestHelpers

class VSpecErrorFormatterTest extends TestHelpers {
  import VSpecErrorFormatterTest._

  val locale: Locale = null
  val messages = mock[MessageBundle]
  val format = new VSpecErrorFormatter(messages, ConverterRegistry.defaultConverters ).format(_, _)
  
  @Test
  def per_object_error_code() {
    val expectedCode = "user.email.hasText.invalid"
      
    when(messages.get(locale, expectedCode)).thenReturn(Some("Email is required"))
    
    assertEquals("Email is required", format(emailHasNoText, locale))
  }
  
  @Test
  def default_error_code_for_constraint() {
    val perObjectCode = "user.email.hasText.invalid"
    val defaultCode = "constraint.hasText.invalid"
    
    when(messages.get(locale, perObjectCode)).thenReturn(None)
    when(messages(locale, defaultCode)).thenReturn("Field is required")
    
    assertEquals("Field is required", format(emailHasNoText, locale))
  }
  
  @Test(expected=classOf[MessageNotFoundException])
  def exception_when_no_message_found() {
    when(messages.get(any(), any(), any())).thenReturn(None)
    when(messages(any(), any(), any())).thenThrow(new MessageNotFoundException("any", locale, null))
    format(emailHasNoText, locale)
  }
  
  @Test
  def message_with_no_internpolation_variables() {
    when(messages.get(any(), any(), any())).thenReturn(Some("is required"))
    assertEquals("is required", format(emailHasNoText, locale))
  }
  
  @Test
  def message_with_internpolation_variables() {
    when(messages.get(any(), any(), any())).thenReturn(None)
    when(messages(any(), any(), any())).thenReturn("{1} in {2} cannot be {3}")
                                                  .thenReturn("{1}{2}{3}")
                                                  .thenReturn(" {3} {2} {1} ")
                                                  .thenReturn("error:{1} is {3}")
    
    assertEquals("email in User cannot be null", format(emailHasNoText, locale))
    assertEquals("emailUsernull", format(emailHasNoText, locale))
    assertEquals(" null User email ", format(emailHasNoText, locale))
    assertEquals("error:email is null", format(emailHasNoText, locale))
  }
}


object VSpecErrorFormatterTest {
  val emailHasNoText = new Error("hasText.invalid", "email", classOf[User], null)
  class User(var name: String, var email: String)
}