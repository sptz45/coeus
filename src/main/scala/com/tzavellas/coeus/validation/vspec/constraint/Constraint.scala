/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.tzavellas.coeus.validation.vspec.constraint

import com.tzavellas.coeus.bind.Error

/**
 * A validation rule.
 * 
 * @see {@link com.tzavellas.coeus.validation.VSpec VSpec}
 * @see {@link com.tzavellas.coeus.validation.Constraints Constraints}
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
  
  /* Why Constraint needs to have the getError(..) method?
   * 
   * You might think that the error construction is a separate concern from
   * constraint validation and thus by having the getError method this trait
   * violates the single responsibility principle. The getError method needs
   * to be on this trait because sometimes the returned Error must contain
   * information specific to the Constraint instance and since that instance
   * is the only object that knows that information it also has to construct
   * the Error.
   * 
   * For example in a constraint that we test whether a number is
   * greater that another number (minimum) we might want to include the
   * minimum number in the Error instance so it can be used in the error
   * messages. That number will probably be know only by the Constraint
   * instance that will validate the input number (see:
   * NumericConstraints.isGreaterThan(..)).
   */
}
