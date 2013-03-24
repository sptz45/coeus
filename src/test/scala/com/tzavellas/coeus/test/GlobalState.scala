/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.tzavellas.coeus.test

import com.tzavellas.coeus.http.multipart.FormFile
import com.tzavellas.coeus.mvc.view.TextView

object GlogalState {
  
  val interceptor = new MockInterceptor
  
  val viewResolver = new MockViewResolver(Map("hello" -> new TextView("Hello World!", "text/html")))
  
  var uploadedFile: FormFile = _
}