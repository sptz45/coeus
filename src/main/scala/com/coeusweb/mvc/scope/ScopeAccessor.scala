/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.mvc
package scope

import param._

/**
 * Provides access to request related objects.
 */
trait ScopeAccessor {

  /** The current web request. */
  def request: WebRequest
  
  /** The current web response. */
  def response: WebResponse
  
  /** The flash scope. */
  def flash: FlashScope
  
  /** The web session. */
  def session: WebSession
  
  /** The application scope. */
  def application: ApplicationScope

  /** The request parameters. */
  def params: RequestParameters
  
  /** The path parameters. */
  def path: PathParameters
}

/**
 * Factory methods for creating {@code ScopeAccessor} instances. 
 */
object ScopeAccessor {

  /**
   * Create a {@code ScopeAccessor} for the specified request and response
   * objects.
   */
  def apply(req: WebRequest, res: WebResponse): ScopeAccessor =
    new ScopeAccessor {
      def request     = req
      def response    = res
      def flash       = request.flash
      def session     = request.session
      def application = request.application
      def params      = request.params
      def path        = request.path
  }
}