/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.test

import javax.servlet.ServletConfig
import com.coeusweb.config._
import com.coeusweb.http.multipart.{ CommonsMultipartRequestParser, MultipartRequestParser, FormFile }
import com.coeusweb.view.{ ViewResolver, TextView }


class ExampleDispatcherContext(sc: ServletConfig) extends ConfigBuilder(sc)
                                          with ControllerRegistry
                                          with InterceptorRegistry {
  
  register[BlogController]
  register[UploadController]
  register(TestsGlogalState.interceptor)
  
  override def dispatcherConfig = new DispatcherConfig(sc) {
    override lazy val viewResolver = TestsGlogalState.viewResolver
    override lazy val multipartParser: MultipartRequestParser = new CommonsMultipartRequestParser(1) // 1 byte threshold
  }
}

object TestsGlogalState {
  val interceptor = new MockInterceptor
  val viewResolver = new MockViewResolver(Map("hello" -> new TextView("Hello World!", "text/html")))
  var uploadedFile: FormFile = _
}


