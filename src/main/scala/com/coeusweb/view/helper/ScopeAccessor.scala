/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.view.helper

import com.coeusweb.{ WebResponse, WebRequest }

class ScopeAccessor(val request: WebRequest, val response: WebResponse) {
  
  def application = request.application
  def session = request.session
  def flash = request.flash
  def params = request.params
  def path = request.path
}