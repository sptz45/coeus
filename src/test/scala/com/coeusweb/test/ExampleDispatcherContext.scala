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
  
  controllers += classOf[BlogController]
  controllers += classOf[UploadController]
  interceptors += GlogalState.interceptor
  
  override def dispatcherConfig = new DispatcherConfig(sc) {
    override lazy val viewResolver = GlogalState.viewResolver
    override lazy val multipartParser: MultipartRequestParser = new CommonsMultipartRequestParser(1) // 1 byte threshold
  }
}



