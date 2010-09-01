/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.bind

object DefaultConverterRegistry extends ConverterRegistry(ConverterRegistry.builderWithDefaults)

class ConverterRegistry(converterMap: Map[Class[_], Converter[_]]) {
  
  def this(builder: ConverterRegistry.Builder) = this(builder.result)
  
  def add(c: Class[_], conv: Converter[_]) = new ConverterRegistry(converterMap + (c -> conv))
  
  def add[T](conv: Converter[T])(implicit m: Manifest[T]) = new ConverterRegistry(converterMap + (m.erasure -> conv))
  
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
  
  import scala.collection.mutable.{ MapBuilder, Builder => SBuilder }
  import converter._
  
  type Builder = SBuilder[(Class[_], Converter[_]), Map[Class[_], Converter[_]]]
  
  def newBuilder: Builder = new MapBuilder[Class[_], Converter[_], Map[Class[_], Converter[_]]](Map()) {
    
    override def += (entry: (Class[_], Converter[_])): this.type = {    
      super.+=(entry)
      
      val (c, fmt) = entry
      if (c == classOf[Boolean]) super.+=(classOf[java.lang.Boolean] -> fmt)
      else if (c == classOf[Byte]) super.+=(classOf[java.lang.Byte] -> fmt)
      else if (c == classOf[Char]) super.+=(classOf[java.lang.Character] -> fmt)
      else if (c == classOf[Double]) super.+=(classOf[java.lang.Double] -> fmt)
      else if (c == classOf[Float]) super.+=(classOf[java.lang.Float] -> fmt)
      else if (c == classOf[Int]) super.+=(classOf[java.lang.Integer] -> fmt)
      else if (c == classOf[Long]) super.+=(classOf[java.lang.Long] -> fmt)
      else if (c == classOf[Short]) super.+=(classOf[java.lang.Short] -> fmt)
      else if (c == classOf[java.util.Date]) {
        super.+=(classOf[java.sql.Date] -> fmt)
        super.+=(classOf[java.sql.Timestamp] -> fmt)
      }
      
      this
    }
  }
  
  def builderWithDefaults = {
    val converters = newBuilder
    converters += (classOf[String] -> new StringConverter)
    converters += (classOf[Boolean] -> new BooleanConverter)
    converters += (classOf[Byte] -> new SimpleByteConverter)
    converters += (classOf[Char] -> new CharConverter)
    converters += (classOf[Double] -> new SimpleDoubleConverter)
    converters += (classOf[Float] -> new SimpleFloatConverter)
    converters += (classOf[Int] -> new SimpleIntConverter) 
    converters += (classOf[Long] -> new SimpleLongConverter)
    converters += (classOf[Short] -> new SimpleShortConverter)
    converters += (classOf[BigDecimal] -> new SBigDecimalConverter)
    converters += (classOf[java.math.BigDecimal] -> new BigDecimalConverter)
    converters += (classOf[java.net.URI] -> new UriConverter) 
    converters += (classOf[java.util.Locale] -> new LocaleConverter)
    converters += (classOf[java.util.Date] -> new DateConverter)
    converters += (classOf[java.util.Calendar] -> new CalendarConverter)
    converters
  }
}
