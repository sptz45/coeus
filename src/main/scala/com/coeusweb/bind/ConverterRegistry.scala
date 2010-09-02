/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.bind


class ConverterRegistry private (converterMap: Map[Class[_], Converter[_]]) {
  
  def this() = this(Map())
  
  def add[T](conv: Converter[T])(implicit m: Manifest[T]): ConverterRegistry = {
    add(m.erasure, conv)
  }
  
  def add(c: Class[_], conv: Converter[_]) = {
    var convs = converterMap + (c -> conv)
    
    if (c == classOf[Boolean])     convs = convs + (classOf[java.lang.Boolean] -> conv)
    else if (c == classOf[Byte])   convs = convs + (classOf[java.lang.Byte] -> conv)
    else if (c == classOf[Char])   convs = convs + (classOf[java.lang.Character] -> conv)
    else if (c == classOf[Double]) convs = convs + (classOf[java.lang.Double] -> conv)
    else if (c == classOf[Float])  convs = convs + (classOf[java.lang.Float] -> conv)
    else if (c == classOf[Int])    convs = convs + (classOf[java.lang.Integer] -> conv)
    else if (c == classOf[Long])   convs = convs + (classOf[java.lang.Long] -> conv)
    else if (c == classOf[Short])  convs = convs + (classOf[java.lang.Short] -> conv)
    else if (c == classOf[java.util.Date]) {
      convs = convs + (classOf[java.sql.Date] -> conv)
      convs = convs + (classOf[java.sql.Timestamp] -> conv)
    }
    
    new ConverterRegistry(convs)
  }
  
  def apply[T](klass: Class[T]) = converter(klass)
  
  def converter[T](klass: Class[T]): Converter[T] = {
    try {
      converterMap(klass).asInstanceOf[Converter[T]]
    } catch {
      case e: NoSuchElementException => throw new NoConverterAvailableException(klass)
    }
  }
  
  def formatter(klass: Class[_]) = converter(klass).asInstanceOf[Formatter[Any]]
  
  def parser(klass: Class[_]) = converter(klass).asInstanceOf[Parser[Nothing]]
}


object ConverterRegistry {
  
  import converter._
  
  val defaultConverters = new ConverterRegistry()
    .add(classOf[String], new StringConverter)
    .add(classOf[Boolean], new BooleanConverter)
    .add(classOf[Byte], new SimpleByteConverter)
    .add(classOf[Char], new CharConverter)
    .add(classOf[Double], new SimpleDoubleConverter)
    .add(classOf[Float], new SimpleFloatConverter)
    .add(classOf[Int], new SimpleIntConverter) 
    .add(classOf[Long], new SimpleLongConverter)
    .add(classOf[Short], new SimpleShortConverter)
    .add(classOf[BigDecimal], new SBigDecimalConverter)
    .add(classOf[java.math.BigDecimal] , new BigDecimalConverter)
    .add(classOf[java.net.URI], new UriConverter) 
    .add(classOf[java.util.Locale] , new LocaleConverter)
    .add(classOf[java.util.Date], new DateConverter)
    .add(classOf[java.util.Calendar], new CalendarConverter)
}
