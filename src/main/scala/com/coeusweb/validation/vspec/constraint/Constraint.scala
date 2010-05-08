/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.validation.vspec.constraint

import com.coeusweb.bind.Error

/**
 * A validation rule.
 * 
 * @see {@link com.coeusweb.validation.VSpec VSpec}
 * @see {@link com.coeusweb.validation.Constraints Constraints}
 */
trait Constraint[-T] {

  /**
   * Check that the given value satisfies this constraint.
   */
  def isValid(value: T): Boolean
  
  /**
   * Get an <code>Error</code> object that describes the failure
   * of the given value to satisfy this constraint.
   * 
   * <p>The arguments of the returned {@link Error} object are:
   * 1)the field name, 2)the target class, 3)the value that
   * violated the constraint followed by any other constraint
   * specific values.</p>
   *
   * @param targetClass the class of the target object
   * @param field the field's name 
   * @param value the value that violates this constraint
   * 
   * @see Error
   */
  def getError(targetClass: Class[_], field: String, value: T): Error
}
