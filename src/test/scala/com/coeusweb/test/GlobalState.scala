package com.coeusweb.test

import com.coeusweb.http.multipart.FormFile
import com.coeusweb.view.TextView

object GlogalState {
  val interceptor = new MockInterceptor
  val viewResolver = new MockViewResolver(Map("hello" -> new TextView("Hello World!", "text/html")))
  var uploadedFile: FormFile = _
}