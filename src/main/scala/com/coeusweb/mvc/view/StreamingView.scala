/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.mvc
package view

import java.io.{ InputStream, File, FileInputStream }

/**
 * Streams the specified <code>InputStream</code> to the client.
 * 
 * <p>The length of the data is used to used to set the <em>Content-Length</em> HTTP
 * header in the response. If the length specified has a negative value then the
 * <em>Content-Length</em> HTTP header is not set.</p>
 * 
 * <p>The filename is used in the <em>Content-Disposition</em> HTTP header. If the filename
 * specified is the empty string then the <em>Content-Disposition</em> HTTP header is not
 * set.</p> 
 * 
 * @param input the <code>InputStream</code> to send to the client
 * @param length the length of the data, or a negative value to disable
 * @param filename the filename to use for the Content-Disposition header, or the empty string to disable
 * @param contentType the response's content-type
 * @param disposition can be "attachment" or "inline" and it is used in the Content-Disposition header 
 */
//TODO needs integration testing
class StreamingView(
  input: InputStream,
  length: Long = -1,
  filename: String = "",
  val contentType: String = "application/octet-stream",
  disposition: String = "attachment") extends View {
  
  def render(request: WebRequest, response: WebResponse) {
    
    def setHeaders() {
      response.contentType = contentType
      if (length >= 0)
        response.header("Content-Length", length.toString)
      if (filename != "")
        response.header("Content-Disposition", "%s; file=\"%s\"".format(disposition, filename))
    }
    
    def stream() {
      val buffer = new Array[Byte](512)
      try {
        val out = response.outputStream
        var bytesRead = input.read(buffer)
        while (bytesRead != -1) {
          out.write(buffer, 0, bytesRead)
          bytesRead = input.read(buffer)
        }
      } finally {
        try { input.close() } catch { case ignore: Exception => () } 
      }
    }
    
    setHeaders()
    stream()
  }
}

/**
 * Factories for creating <code>StreamingView</code> instances.
 */
object StreamingView {
  
  /**
   * Construct a {@link StreamingView} to send the specified <code>File</code> to the client.
   * 
   * <p>The <code>View</code> returned by this method, when rendered, will set the
   * <em>Content-Disposition</em> HTTP header to "attachment" and the <em>Content-Length</em>
   * HTTP header to the size of the specified file.</p>  
   * 
   * @param file the <code>File</code> to send to the client
   * @param contentType the response's content-type 
   */
  def streamFile(file: File, contentType: String): StreamingView = { 
    require(file.canRead, "Unable to stream file '"+file+"', because the file is unreadable.")
    new StreamingView(input = new FileInputStream(file),
                      length = file.length(),
                      filename = file.getName,
                      contentType = contentType)
  }
}
