/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.core

import java.lang.annotation.Annotation
import com.coeusweb.annotation._

/**
 * Utility methods for working with the annotations defined in the
 * <code>com.coeusweb.annotation</code> package.
 */
private object AnnotationProcessor {
  
  /**
   * Convert an annotation representing an HTTP method to the corresponding
   * <code>HttpMethod</code> object.
   */
  val toHttpMethod: PartialFunction[Annotation, Symbol] = {
    case _: Get        => 'GET
    case _: Post       => 'POST
    case _: Put        => 'PUT
    case _: Delete     => 'DELETE
    case a: HttpMethod => Symbol(a.method.toUpperCase)
  }
  
  /**
   * Get the value of the <code>value</code> field from an HTTP method
   * annotation.
   */
  def getValueFrom(a: Annotation): String = a match {
    case v: Get        => v.value
    case v: Post       => v.value
    case v: Put        => v.value
    case v: Delete     => v.value
    case v: HttpMethod => v.path
    case _             =>
      throw new IllegalArgumentException(
        "The specified annotation ["+a+"] does not correspond to an HTTP method")
  }
}
