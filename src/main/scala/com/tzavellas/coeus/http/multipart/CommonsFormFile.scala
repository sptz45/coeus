/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.tzavellas.coeus.http.multipart

import java.io.File
import org.apache.commons.fileupload.disk.DiskFileItem
import scala.collection.JavaConversions._

private class CommonsFormFile(val item: DiskFileItem) extends FormFile {
  
  def fieldName = item.getFieldName
  
  def contentType: Option[String] = Option(item.getContentType)
  
  def charSet: Option[String] = Option(item.getCharSet)
  
  def filename: String = {
   val name = item.getName
   if (name == null) return ""
   // remove path
   var sep = name.lastIndexOf("/") // unix
   if (sep == -1) {
     sep = name.lastIndexOf("\\")  // windows
    }
    if (sep == -1) name else name.substring(sep + 1) 
  }

  def tmpFile: Option[File] = Option(item.getStoreLocation)
  
  def inputStream = item.getInputStream
  
  def contents = item.get
  
  def isAvailable: Boolean = {
    if (item.isInMemory) return true
    for (file <- tmpFile) if (file.canRead) return true
    false
  }
  
  lazy val headers: FormFile.Headers = new FormFile.Headers {
    val ih = item.getHeaders
    
    def apply(name: String) = ih.getHeader(name)
    
    def values(name: String) = ih.getHeaders(name).asInstanceOf[java.util.Iterator[String]]
    
    def names = ih.getHeaderNames.asInstanceOf[java.util.Iterator[String]]
  }
  
  def writeTo(file: File) {
    item.write(file)
  }
  
  def writeTo(path: String) {
    item.write(new File(path))
  }
  
  def delete() {
    item.delete()
  }
}
