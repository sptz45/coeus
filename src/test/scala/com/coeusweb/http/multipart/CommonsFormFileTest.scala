/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.http.multipart

import org.junit.Test
import org.junit.Assert._
import org.apache.commons.fileupload.disk.DiskFileItem

class CommonsFormFileTest {
  
  @Test
  def filename_tests() {
    assertEquals("scala.html", file("file", "scala.html").filename)
    assertEquals("scala.html", file("file", "/home/spiros/scala.html").filename)
    assertEquals("scala.html", file("file", "C:\\files\\scala.html").filename)
    assertEquals("scala.html", file("file", "C:\\programming documents\\scala.html").filename)
    assertEquals("scala specification.pdf", file("file", "scala specification.pdf").filename)
  }
  
  private def file(name: String, filename: String) = {
    new CommonsFormFile(new DiskFileItem(name, "text/html", false, filename,
                        10240, new java.io.File(System.getProperty("java.io.tmpdir")))) 
  }
}