/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.param

import org.junit.Test
import org.junit.Assert._
import com.coeusweb.test.Assertions._
import com.coeusweb.bind.Parser
import com.coeusweb.bind.converter.CurrencyConverter

abstract class AbstractParametersTest {
  
  /* The scope under test */
  val params: Parameters
  
  /* Sets a parameter in the scope under test */
  def setParameter(name: String, value: String)

  @Test(expected=classOf[MissingParameterException])
  def read_nonexisting_parameter() {
    assertNull(params.getParameter("does not exists"))
    params("does not exist")
  }
  
  @Test(expected=classOf[MissingParameterException])
  def empty_string_is_the_same_as_a_missing_parameter() {
    setParameter("empty", "")
    assertNull(params.getParameter("empty"))
    params("empty")
  }
  
  @Test
  def nonexisting_parameter_and_custom_parsrer_with_no_default_value() {
    val parser: Parser[Int] = null
    assertNone(params.parse("does not exist", parser))
  }
  
  @Test
  def return_string_if_no_type_argument_specified() {
    setParameter("message", "hello")
    assertSome("hello", params.parse("message"))
  }
  
  @Test
  def read_parameter_using_default_parsers() {
    import java.util._, java.net.URI, java.math.BigDecimal
    
    setParameter("message", "hello")
    assertSome("hello", params.parse[String]("message"))
    
    setParameter("ten", "10")
    assertSome(10, params.parse[Int]("ten"))
    assertSome(10, params.parse[java.lang.Integer]("ten"))
    assertSome(10L, params.parse[Long]("ten"))
    
    setParameter("half", "0.5")
    assertEquals(0.5, params.parse[Float]("half").get, 0.01)
    assertEquals(0.5, params.parse[Double]("half").get, 0.01)
    
    setParameter("birthDate", "1980/4/15")
    assertSome(birthDate.getTime, params.parse[Date]("birthDate"))
    assertSome(birthDate, params.parse[Calendar]("birthDate"))
    
    setParameter("locale", Locale.US.toString)
    assertSome(Locale.US, params.parse[Locale]("locale"))
    
    setParameter("blog", "http://tzavellas.com/techblog")
    assertSome(new URI("http://tzavellas.com/techblog"), params.parse[URI]("blog"))
    
    setParameter("decimal", "123.4567890")
    assertSome(new BigDecimal("123.4567890"), params.parse[BigDecimal]("decimal"))
  }
  
  @Test
  def read_parameter_using_custom_parser() {
    import java.math.BigDecimal
    setParameter("10 dollars", "$10")
    assertSome(new BigDecimal("10.00"),
                 params.parse("10 dollars", new CurrencyConverter))
  }
  
  
  private def birthDate = {
    val cal = java.util.Calendar.getInstance
    cal.clear()
    cal.set(1980, 3, 15)
    cal
  }
}
