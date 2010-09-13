/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.core

import java.io.PrintWriter
import javax.servlet.ServletOutputStream
import javax.servlet.http.{ HttpServletResponse, HttpServletResponseWrapper }


private class NullHttpServletResponse(res: HttpServletResponse) extends HttpServletResponseWrapper(res) {
  
  override def getOutputStream = new NullServletOutputStream
  
  override def getWriter = new PrintWriter(getOutputStream)
  
  class NullServletOutputStream extends ServletOutputStream {
    def write(c: Int) { }
  }
}