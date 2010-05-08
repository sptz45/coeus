/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.test

import com.coeusweb.core.ControllerRegistry
import javax.servlet.ServletConfig
import com.coeusweb.config._
import com.coeusweb.http.multipart.{ CommonsMultipartRequestParser, MultipartRequestParser, FormFile }
import com.coeusweb.view.{ ViewResolver, TextView }


class ExampleModule(sc: ServletConfig) extends ConfigBuilder(sc) with ControllerRegistrar {
  
  override def dispatcherConfig = new DispatcherConfig(sc) {
    override lazy val viewResolver = ExampleModule.viewResolver
    override lazy val multipartParser: MultipartRequestParser = new CommonsMultipartRequestParser(1) // 1 byte threshold
  }
 
  def register(registry: ControllerRegistry) {
    registry.register[BlogController]
    registry.register[UploadController]
    registry.interceptors += ExampleModule.interceptor
  }
}

object ExampleModule {
  val interceptor = new MockInterceptor
  val viewResolver = new MockViewResolver(Map("hello" -> new TextView("Hello World!", "text/html")))
  var uploadedFile: FormFile = _
}


