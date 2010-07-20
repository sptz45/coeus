/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.bind

import java.lang.reflect.Method
import java.util.Locale
import java.util.concurrent.ConcurrentHashMap
import com.coeusweb.core.util.ReflectionHelper


object ExpressionLanguage {
  
  private[this] val MapRx = """(\p{Alnum}*)\[(.*)\]""".r
  private[this] val SeqRx = """(\p{Alnum}*)\[(\p{Digit}*)\]""".r
  
  private[this] val readerCache = new ConcurrentHashMap[(Class[_], String), Method]
  private[this] val writerCache = new ConcurrentHashMap[(Class[_], String, Class[_]), Method]
  
  
  def clearCache() = {
    writerCache.clear()
    readerCache.clear()
  }
  
  def eval(target: AnyRef, expr: String, unwrapOption: Boolean = true): Any = {
    require(target ne null, "Cannot evaluate expression using a null target")
    require((expr ne null) && ! expr.isEmpty, "The expression cannot be empty or null")
    try {
      walk(target, split(expr.trim), unwrapOption)
    } catch {
      case be: BindingException => throw be
      case ex: Exception => throw new ExpressionException("Error while evaluating expression: " + expr, ex)
    }
  }
  
  def evalTo[T](target: AnyRef, expr: String): T = eval(target, expr).asInstanceOf[T]
  
  private def split(expr: String): List[String] = expr.split("\\.").toList
  
  private def walk(target: AnyRef, expr: List[String], unwrapOption: Boolean): Any = expr match {
      
    case Nil           => assert(false, "cannot be empty")
    
    case name :: Nil   => read(target, name, unwrapOption) 
    
    case name :: names => read(target, name, unwrapOption) match {
      case null        => throw new ExpressionException("'%s' in %s was null while evaluating '%s'".format(name, target, expr.mkString(".")))
      case ref: AnyRef => walk(ref, names, unwrapOption)
      case _           => throw new ExpressionException("Error while evaluating ["+expr.mkString(".")+"]")
    }
  }
  
  private def read(target: AnyRef, expr: String, unwrapOption: Boolean): Any = {
    
    def readFromSeq(seq: AnyRef, index: Int): Any = seq match {
      case a: scala.Array[_]          => a(index)
      case s: scala.collection.Seq[_] => s(index)
      case p: scala.Product           => p.productElement(index)
      case j: java.util.List[_]       => j.get(index)
      case _                          =>
        throw new ExpressionException("Attempt to read from unrecognised indexed collection. Collection class is: " + seq.getClass) 
    }

    def readFromMap(map: AnyRef, key: String): Any = map match {
      case m: scala.collection.Map[_, _] => m.asInstanceOf[scala.collection.Map[String, _]](key)
      case m: java.util.Map[_, _]        => m.get(key)
      case _                             =>
        throw new ExpressionException("Attempt to read from unrecognised map collection. Collection class is: " + map.getClass)
    }
    
    if (expr.last != ']')
      return readFromVal(target, expr, unwrapOption)
    
    expr match {
      case SeqRx(method, index) => readFromSeq(readRef(target, method, unwrapOption), index.toInt)
      case MapRx(method, key)   => readFromMap(readRef(target, method, unwrapOption), key)
    }
  }  
  
  private def readRef(t: AnyRef, m: String, unwrapOption: Boolean) = readFromVal(t, m, unwrapOption).asInstanceOf[AnyRef]
  
  private def readFromVal(target: AnyRef, method: String, unwrapOption: Boolean): Any = {
    val value = getReaderMethod(target.getClass, method).invoke(target)
    
    if (unwrapOption) value match {
      case None    => null
      case Some(a) => a
      case any     => any 
    } else {
      value
    }
  }
  
  private def getReaderMethod(klass: Class[_], method: String) = {
    var reader = readerCache.get((klass, method))
    if (reader eq null) {
      reader = klass.getMethod(method)
      readerCache.put((klass, method), reader)
    }  
    reader
  }
  
  private def getWriterMethod(klass: Class[_], method: String, argClass: Class[_]) = {
    var writer = writerCache.get((klass, method, argClass))
    if (writer eq null) {
      writer = klass.getMethod(method, argClass)
      writerCache.put((klass, method, argClass), writer)
    }  
    writer
  }
  
  def bind(target: AnyRef, expr: String, value: String, locale: Locale, parsers: ConverterRegistry) {
    
    def writeToVar(hasVar: AnyRef, varName: String) {
      var valueClass: Class[_] = null
      try {
        valueClass = getReaderMethod(hasVar.getClass, varName).getReturnType
        val writer = getWriterMethod(hasVar.getClass, varName+"_$eq", valueClass)
        if (isOption(valueClass)) {
          writeToOption(hasVar, writer, typeArgForSeq(hasVar, varName))
        } else {
          writer.invoke(hasVar, parsers(valueClass).parse(value, locale).asInstanceOf[AnyRef])
        }
      } catch {
        case ignored: NoSuchMethodException => ()
        case e @ (_: BindingException | _: NullPointerException) => throw e
        case e: Exception => throw new ParserException(valueClass, e)
      }
    }
    
    def isOption(klass: Class[_]) = classOf[Option[_]].isAssignableFrom(klass)
    
    def writeToOption(option: AnyRef, writer: Method, valueClass: Class[_]) {
      value match {
        case null | "None" => writer.invoke(option, None)
        case other => writer.invoke(option, Some(parsers(valueClass).parse(value, locale).asInstanceOf[AnyRef]))
      }
    }
    
    def writeToSeq(seq: AnyRef, index: Int, c: Class[_]) {
      val newValue = parsers(c).parse(value, locale)
      seq match {
        case a: scala.Array[_] => ReflectionHelper.updateArray(a, index, newValue)
        case s: scala.collection.mutable.Seq[_] =>
          s.asInstanceOf[scala.collection.mutable.Seq[Any]](index) = newValue 
        
        case j: java.util.List[_] =>
          j.asInstanceOf[java.util.List[Any]].set(index, newValue)
        
        case _ => throw new ExpressionException("Attempt to write to unrecognised indexed collection. Collection class is: " + seq.getClass) 
      }
    }
    
    def writeToMap(map: AnyRef, key: String, c: Class[_]) {
      val newValue = parsers(c).parse(value, locale)
      map match {
        case s: scala.collection.mutable.Map[_, _] =>
          s.asInstanceOf[scala.collection.mutable.Map[String, Any]](key) = newValue 
        
        case j: java.util.Map[_, _] =>
          j.asInstanceOf[java.util.Map[String, Any]].put(key, newValue)
        
        case _ => throw new ExpressionException("Attempt to write to unrecognised mapped collection. Collection class is: " + map.getClass) 
      }
    }
    
    def typeArgForSeq(hasSeq: AnyRef, seqName: String) = {
      val getter = hasSeq.getClass.getMethod(seqName)
      ReflectionHelper.getTypeArgumentsOfCollection(getter).apply(0)
    }
      
    def typeArgForMap(hasMap: AnyRef, mapName: String) = {
      val getter = hasMap.getClass.getMethod(mapName)
      ReflectionHelper.getTypeArgumentsOfCollection(getter).apply(1)
    }
    
    if (expr.isEmpty) return
    
    val parsed = split(expr)
    val actualTarget =
      if (parsed.length == 1) target
      else walk(target, parsed.dropRight(1), true).asInstanceOf[AnyRef]
    val name = parsed.last
    
    if (name.last != ']') {
      writeToVar(actualTarget, name)
      return
    }
    
    name match {
      
      case SeqRx(seqName, index) =>
        writeToSeq(readRef(actualTarget, seqName, false),
                   index.toInt,
                   typeArgForSeq(actualTarget, seqName))
      
      case MapRx(mapName, key) =>
        writeToMap(readRef(actualTarget, mapName, false),
                   key,
                   typeArgForMap(actualTarget, mapName))
    }
  }
}
