/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.mvc
package scope

trait MutableScopes extends ScopeAccessor {
  
  private var _req: WebRequest = null
  private var _res: WebResponse = null
  
  override def request = _req
  override def response = _res
  
  def request_=(r: WebRequest) { _req = r }
  def response_=(r: WebResponse) { _res = r }
  
  override def flash       = request.flash
  override def session     = request.session
  override def application = request.application
  override def params      = request.params
  override def path        = request.path

}