/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.mvc
package view

/**
 * A <code>View</code> that when rendered will set the <em>X-Sendfile</em> HTTP header
 * telling the web server to serve the specified file.
 * 
 * <p><strong>Important:</strong>The view returned by this method will not stream the
 * data of the specified file to the client. It will just set an HTTP header telling the
 * web server to send the file. Make sure your web/application server supports the
 * <em>X-Sendfile</em> HTTP header before using this method.</p>  
 * 
 * @param file the path to the file to use for the <em>X-Sendfile</em> HTTP header
 * @param contentType the response's content-type (might get ignored by the web server)
 * @param disposition can be "attachment" or "inline" and it is used in the Content-Disposition
 *        header to specify whether the file will be shown inline ("inline") or downloaded
 *        ("attachment")
 */
//TODO needs integration testing
class SendfileView(file: String, val contentType: String, disposition: String) extends View {
  
  def render(request: WebRequest, response: WebResponse) {
    response.contentType = contentType
    response.header("X-Sendfile",  file)
    response.header("Content-Disposition", "%s; file=\"%s\"".format(disposition, file))
  }
}
