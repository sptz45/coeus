/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.tzavellas.coeus.validation

import com.tzavellas.coeus.bind._

/**
 * Validates objects.
 * 
 * @see BindingResult
 */
trait Validator[-T <: AnyRef] {

  /**
   * Validate the specified target object.
   * 
   * @param target the object to validate.
   * @return a sequence of <code>Error</code> objects or an empty sequence if
   *         the specified object is valid.
   */
  def validate(target: T): Iterable[Error]
  
  /**
   * Validate the target object of the specified <code>BindingResult</code>. 
   * 
   * <p>This method adds any validation errors in the specified
   * <code>BindingResult</code>.</p>
   */
  def validate(result: BindingResult[T])
  
  /**
   * Validate the specified field value.
   * 
   * <p>This method takes a type parameter ({@code S}) that corresponds to the type of
   * the target object and is usually the same as the type parameter {@code T}. This
   * parameter is needed because {@code T} is contravariant and this method needs to
   * know the exact class of the target object.</p>
   * 
   * @param field the field's name
   * @param value the field's value
   * @return an <code>Option</code> containing an error if the value is invalid,
   *        else <code>None</code>.
   */
  def validateField[F](field: String, value: Any)(implicit m: Manifest[F]): Option[Error]
  
  val errorFormatter: ErrorFormatter
}