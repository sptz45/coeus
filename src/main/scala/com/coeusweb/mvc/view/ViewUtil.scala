/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.mvc.view

import com.coeusweb.WebResponse

/**
 * Helper methods for creating {@code View} implementations.
 */
object ViewUtil {
  
  /**
   * Set the content-type of the response only if it hasn't been
   * set before. 
   */
  def applyContentType(contentType: String, response: WebResponse) {
    if (response.contentType eq null)
      response.contentType = contentType
  }
}
