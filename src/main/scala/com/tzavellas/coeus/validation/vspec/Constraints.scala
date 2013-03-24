/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.tzavellas.coeus.validation.vspec

import constraint._

/**
 * Various constraints for constructing <code>VSpec</code> validators.
 */
trait Constraints extends GenericConstraints
                     with CalendarConstraints
                     with DateConstraints
                     with StringConstraints
                     with NumericConstraints 
                     with JavaNumericConstraints


/**
 * Various constraints for constructing <code>VSpec</code> validators.
 */
object Constraints extends Constraints
