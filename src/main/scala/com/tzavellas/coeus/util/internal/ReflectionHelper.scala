/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.tzavellas.coeus.util.internal

import java.lang.reflect.{ Method, ParameterizedType, Modifier } 
import com.tzavellas.coeus.FrameworkException

/**
 * Various utility methods for working with reflection APIs.
 */
object ReflectionHelper {

  /**
   * Tests whether the specified class is an abstract class or interface.
   */
  def isAbstract(klass: Class[_]) = {
    val modifiers = klass.getModifiers
    Modifier.isAbstract(modifiers) || Modifier.isInterface(modifiers) 
  }

  /**
   * Tries to find the type argument of the collection or array that gets returned
   * from the given method.
   * 
   * @param collectionGetter a method that returns a collection or array
   * @throws FrameworkException if a type argument cannot be found 
   */
  def getTypeArgumentsOfCollection(collectionGetter: Method): Array[Class[_]] = { 
    
    collectionGetter.getGenericReturnType match {
      
      case paramType: ParameterizedType =>
        paramType.getActualTypeArguments.collect { case c: Class[_] => c }
      
      case array: Class[_] if array.isArray =>
        Array(array.getComponentType)
      
      case unknown =>
        throw new FrameworkException("Error while reading generic type argument from type: " + unknown)
    }
  }
  
  /**
   * Updates an array using {@link java.lang.reflect.Array} methods.
   * 
   * @param array the array to update
   * @param index the index of the element to update
   * @param value the new value of the element
   */
  def updateArray(array: Array[_], index: Int, value: Any) {
    import java.lang.reflect.Array._
    
    value match {
      case b: Boolean => setBoolean(array, index, b)
      case b: Byte    => setByte(array, index, b)
      case c: Char    => setChar(array, index, c)
      case d: Double  => setDouble(array, index, d)
      case f: Float   => setFloat(array, index, f)
      case i: Int     => setInt(array, index, i)
      case l: Long    => setLong(array, index, l)
      case s: Short   => setShort(array, index, s)
      case _          => set(array, index, value)
    }
  }
}
