/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.tzavellas.coeus.http.multipart

import java.io.{ File, InputStream }

/**
 * An uploaded file.
 */
trait FormFile {
  
  /**
   * The name of the field in the multipart form corresponding to this
   * file.
   */
  def fieldName: String
  
  /**
   * The headers of this form file
   */
  def headers: FormFile.Headers
  
  /**
   * The content-type of the file if available.
   */
  def contentType: Option[String]
  
  /**
   * The character set of the file if available
   */
  def charSet: Option[String]
  
  /**
   * The name of that this file has in the client's filesystem.
   */
  def filename: String
  
  /**
   * The temporary file where the contents of this FormFile are stored.
   */
  def tmpFile: Option[File]
  
  /**
   * Tests whether the contents
   */
  def isAvailable: Boolean
  
  /**
   * Get an {@code InputStream} to read the files contents.
   */
  def inputStream: InputStream
  
  /**
   * The contents of the file as a byte array.
   */ 
  def contents: Array[Byte]
  
  /**
   * Write the contents of this FormFile to the specified file.
   */
  def writeTo(file: File)
  
  /**
   * Write the contents of this FormFile to the specified file.
   */
  def writeTo(path: String)
  
  /**
   * Delete any temporary file used to store this files contents.
   */
  def delete()
}

object FormFile {
  
  trait Headers {
  
    def apply(name: String): String
  
    def values(name: String): Iterator[String]
  
    def names: Iterator[String]
  }
}
