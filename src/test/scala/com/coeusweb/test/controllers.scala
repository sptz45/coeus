/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb
package test

import org.junit.Assert._
import Assertions._
import annotation._
import view._

class BlogController extends Controller {
  
  @Get("/index")
  def showPosts() = NullView
    
  @Get
  def feed() = 
    new XmlView(<feed></feed>, "application/atom+xml")

  @Get
  def entry() = "hello"
  
  @Get
  def noView() = "noview"
}

class UploadController extends Controller {
  
  @Post("")
  def handleUpload() = {
    val f = request.files("document")
    assertSome(f, "The request must contain a file with name=document")
    assertEquals("scala.txt", f.get.filename)
    GlogalState.uploadedFile = f.get
    NullView
  }
}