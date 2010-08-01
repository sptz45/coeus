/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.view.helper

import com.coeusweb.{ WebResponse, WebRequest }

/**
 * Provides access to request related objects.
 * 
 * @param request the current web request
 * @param response the current web response
 */
class ScopeAccessor(val request: WebRequest, val response: WebResponse) {
  
  /** Returns the application scope. */
  def application = request.application
  
  /** Return the WebSession and optionally creates one if it doesn't exist. */
  def session = request.session
  
  /** Returns the flash scope. */
  def flash = request.flash
  
  /** Returns the request parameters. */
  def params = request.params
  
  /** Returns the path variables. */
  def path = request.path
}