/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.test.servlet

import org.springframework.mock.web._
import com.coeusweb.mvc.WebRequest

class MultipartRequestBuilder {
  
  private val boundary = "10383234"
  private val newLine = "\r\n"
  private val body = new StringBuilder
  
  def addFormField(name: String, value: Any): this.type = {
    body.append("--" + boundary + newLine)
    body.append("Content-Disposition: form-data; name=\""+name+"\"" + newLine)
    body.append(newLine)
    body.append(value)
    body.append(newLine)
    this
  }
  
  def addFormFile(name: String, filename: String, data: String, contentType: String = null): this.type = {
    body.append("--" + boundary + newLine)
    body.append("Content-Disposition: form-data; name=\""+name+"\"; filename=\""+filename+"\"" + newLine)
    if (contentType != null) {
      body.append("Content-Type: " + contentType + newLine)
    }
    body.append(newLine)
    body.append(data)
    body.append(newLine)
    this
  }

  def getRequest(uri: String = "/") = {
    val request = new MockHttpServletRequest("POST", uri)
    request.setContentType("multipart/form-data; boundary="+boundary)
    request.addHeader("Content-Length", body.toString.getBytes("US-ASCII").length)
    
    body.append("--" + boundary + "--\r\n")
    request.setContent(body.toString.getBytes("US-ASCII"))
    
    assert(WebRequest.isMultipart(request))
    request
  }
  
  def printBody() {
    println(body.toString)
  }
}