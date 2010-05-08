/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.core

import org.junit.Test
import org.junit.Assert._
import java.lang.annotation.Annotation

class AnnotationProcessorTest {
  import AnnotationProcessorTest._
  
  @Test
  def from_annotation_to_method() {
    assertEquals('GET, toHttpMethod("get"))
    assertEquals('POST, toHttpMethod("post"))
    assertEquals('PUT, toHttpMethod("put"))
    assertEquals('DELETE, toHttpMethod("delete"))
    assertEquals('HEAD, toHttpMethod("head"))
    assertEquals('OPTIONS, toHttpMethod("options"))
    assertEquals('TRACE, toHttpMethod("trace"))
    assertEquals('CONNECT, toHttpMethod("connect"))
  }
  
  @Test
  def is_defined_at_returns_false_for_unknown_annotations() {
    assertFalse(AnnotationProcessor.toHttpMethod.isDefinedAt(annotation("invalid")))
  }
  
  private def toHttpMethod(objectMethod: String) =
    AnnotationProcessor.toHttpMethod(annotation(objectMethod))

  private def annotation(name: String) =
    classOf[AnnoationTest].getMethod(name).getDeclaredAnnotations.head
}


object AnnotationProcessorTest {
  import javax.xml.bind.annotation.XmlAttribute
  import com.coeusweb.annotation._
  
  class AnnoationTest {
    @Get def get() { }
    @Post def post() { }
    @Put def put() { }
    @Delete def delete() { }
    @HttpMethod(method="head") def head() { }
    @HttpMethod(method="options") def options() { }
    @HttpMethod(method="trace") def trace() { }
    @HttpMethod(method="connect") def connect() { }
    @XmlAttribute def invalid() { }
  }
}
