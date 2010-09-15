/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.test

import javax.servlet.ServletConfig
import com.coeusweb.core.config.WebModule
import com.coeusweb.http.multipart.CommonsMultipartRequestParser


class ExampleWebModule(sc: ServletConfig) extends WebModule(sc) {
  
  viewResolver    = GlogalState.viewResolver
  multipartParser = new CommonsMultipartRequestParser(sizeThreshold = 1 /* byte */)
  
  controllers += new BlogController
  controllers += new UploadController
  
  interceptors += GlogalState.interceptor  
}

class WebModuleWithNo405(sc: ServletConfig) extends ExampleWebModule(sc) {
  hideResources = true
}

class WebModuleWithOverrideMethod(sc: ServletConfig) extends ExampleWebModule(sc) {
  overrideHttpMethod = true
}

class WebModuleWithMethodsEnabled(sc: ServletConfig) extends ExampleWebModule(sc) {
  allowHttpHead = true
  allowHttpOptions = true
}

