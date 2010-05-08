/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.bind

/**
 * A binding/validation error.
 * 
 * @param code the message code of the error
 * @param args any arguments to be used for constructing the error's message
 */
case class Error(code: String, args: Any*)

/**
 * Factory methods for constructing {@code Error} instances.
 */
object Error {
  
  /**
   * Creates a new {@code Error} that is the result of a validation failure.
   * 
   * @param code the message code of the error
   * @param field the field of that target object that contains the invalid value
   * @param targetClass the class of the target object
   * @param value the value that caused the error
   * @param args any extra arguments to be used for constructing the error's message
   */
  def validationFailure(code: String, field: String, targetClass: Class[_], value: Any, args: Any*) =
    new Error(code, field, targetClass, value, args)
  
  /**
   * Creates a binding error that is the result of a type mismatch.
   * 
   * @param field the name of the field for which the binding failed
   * @param fieldClass the class that the input value could not get converted to
   * @param text the String value that could not get converted to {@code fieldClass} 
   */
  def typeMismatch(field: String, fieldClass: Class[_], text: String) =
    new Error(fieldClass.getSimpleName + ".type-mismatch", field, fieldClass, text)
}