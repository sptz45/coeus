/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.tzavellas.coeus.http.multipart

import org.junit.Test
import org.junit.Assert._
import com.tzavellas.coeus.test.Assertions._
import com.tzavellas.coeus.test.servlet._

class CommonsMultipartRequestParserTest {
  
  val builder = new MultipartRequestBuilder
  val parser = new CommonsMultipartRequestParser

  @Test
  def read_a_form_field_from_a_multipart_request() {
    
    builder.addFormField("username", "spiros")
    builder.addFormField("password", "topsecret")
    val multipart = parser.parse(builder.getRequest())
    
    assertEquals("spiros", multipart.getParameter("username"))
    assertEquals("topsecret", multipart.getParameter("password"))
    assertNull(multipart.getParameter("does-not-exist"))
  }
  
  @Test
  def read_file_data_from_a_multipart_request() {
    
    builder.addFormField("category", "programming")
    builder.addFormFile("file", "scala-programming.pdf", "Scala is really cool", "application/pdf")
    val multipart = parser.parse(builder.getRequest())
    
    assertEquals("programming", multipart.getParameter("category"))
    
    val file = multipart.getFile("file")
    assertNotNull(file)
    assertEquals("file", file.fieldName)
    assertEquals("scala-programming.pdf", file.filename)
    assertEquals("Scala is really cool", new String(file.contents))
    assertSome("application/pdf", file.contentType)
  }
  
  @Test
  def cleanupFiles_deletes_any_temp_files() {
    
    builder.addFormFile("file", "scala-programming.pdf", "Scala is really cool", "application/pdf")
    val m1 = parser.parse(builder.getRequest())
    parser.cleanupFiles(m1)
    assertTrue(m1.getFile("file").isAvailable) // it is in memory

    // files > 1 byte will be stored on disk
    val parser2 = new CommonsMultipartRequestParser(sizeThreshold=1)
    val m2 = parser2.parse(builder.getRequest())
    parser2.cleanupFiles(m2)
    assertFalse(m2.getFile("file").isAvailable)
  }
}