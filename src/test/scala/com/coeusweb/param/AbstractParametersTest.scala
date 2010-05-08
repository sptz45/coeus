/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.param

import org.junit.Test
import org.junit.Assert._
import com.coeusweb.bind.Parser
import com.coeusweb.bind.converter.CurrencyConverter

abstract class AbstractParametersTest {
  
  /* The scope under test */
  val param: Parameters
  
  /* Sets a parameter in the scope under test */
  def setParameter(name: String, value: String)

  @Test(expected=classOf[MissingParameterException])
  def read_nonexisting_parameter() {
    param.parse[String]("does not exist")
  }
  
  @Test(expected=classOf[MissingParameterException])
  def nonexisting_parameter_and_custom_parserer_with_no_default_value() {
    val parser: Parser[Int] = null
    param.parse("does not exist", parser, None)
  }
  
  @Test
  def return_string_if_no_type_argument_specified() {
    setParameter("message", "hello")
    assertEquals("hello", param("message"))
  }
  
  @Test
  def read_parameter_using_default_parsers() {
    import java.util._, java.net.URI, java.math.BigDecimal
    
    setParameter("message", "hello")
    assertEquals("hello", param.parse[String]("message"))
    
    setParameter("ten", "10")
    assertEquals(10, param.parse[Int]("ten"))
    assertEquals(10, param.parse[java.lang.Integer]("ten"))
    assertEquals(10, param.parse[Long]("ten"))
    
    setParameter("half", "0.5")
    assertEquals(0.5, param.parse[Float]("half"), 0.01)
    assertEquals(0.5, param.parse[Double]("half"), 0.01)
    
    setParameter("birthDate", "1980/4/15")
    assertEquals(birthDate.getTime, param.parse[java.util.Date]("birthDate"))
    assertEquals(birthDate, param.parse[java.util.Calendar]("birthDate"))
    
    setParameter("locale", Locale.US.toString)
    assertEquals(Locale.US, param.parse[Locale]("locale"))
    
    setParameter("blog", "http://tzavellas.com/techblog")
    assertEquals(new URI("http://tzavellas.com/techblog"), param.parse[URI]("blog"))
    
    setParameter("decimal", "123.4567890")
    assertEquals(new BigDecimal("123.4567890"), param.parse[BigDecimal]("decimal"))
  }
  
  @Test
  def read_parameter_using_custom_parser() {
    import java.math.BigDecimal
    setParameter("10 dollars", "$10")
    assertEquals(new BigDecimal("10.00"),
                 param.parse("10 dollars", new CurrencyConverter))
  }
  
  
  private def birthDate = {
    val cal = java.util.Calendar.getInstance
    cal.clear()
    cal.set(1980, 3, 15)
    cal
  }
}
