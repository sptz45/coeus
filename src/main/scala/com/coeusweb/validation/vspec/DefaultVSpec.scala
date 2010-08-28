/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.validation.vspec

/**
 * A {@code VSpec} that also contains the default constraints.
 */
class DefaultVSpec[-T <: AnyRef](implicit m: Manifest[T])
  extends VSpec[T]
     with Constraints