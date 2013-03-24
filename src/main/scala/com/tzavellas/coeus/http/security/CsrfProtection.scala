/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.tzavellas.coeus.http.security

import java.util.UUID
import com.tzavellas.coeus.mvc.WebRequest
import com.tzavellas.coeus.mvc.scope.WebSession

/**
 * Protection from Cross-site request forgery attacks.
 * 
 * @see <a href="http://en.wikipedia.org/wiki/Cross-site_request_forgery">Cross-site Request Forgery</a>
 */
object CsrfProtection {
  
  def tokenName = "csrf_token"
  
  /**
   * Assert that the given request is not a forged request originating from the
   * user's browser as the result of the HTML served by another site.
   * 
   * <p>A request is checked only if it has a <em>POST</em> HTTP method and it is not
   * an {@link WebRequest#isAjax AJAX request}. Requests that have a <em>GET</em> method
   * must not change any data and are considered safe. Requests with other methods are
   * used only in Web APIs that usually employ application specific method to guarantee the
   * authenticity of their requests. AJAX requests are immune to this attack because of the
   * <a href="http://en.wikipedia.org/wiki/Same_origin_policy">same-origin</a> policy.</p>
   * 
   * <p>If the given request must be checked for forgery then this method:
   * <ol>
   * <li>Checks that a WebSession exists otherwise throws an IllegalStateException</li>
   * <li>Checks that the session contains a CSRF token otherwise throws a RequiredAttributeException</li>
   * <li>Retrieves the submitted token from a request parameter</li>
   * <li>If a token was not submitted then it throws a MaliciousRequestException</li>
   * <li>Throws a MaliciousRequestException if the submitted token does not match the token found in session</li>
   * </ol>
   * </p>
   * 
   * @param request the request to check for forgery
   * 
   * @throws IllegalStateException if the request must be protected and a WebSession does not exist
   * @throws RequiredAttributeException if the value of the CSRF token in not found in session
   * @throws MaliciousRequestException if the token in the request parameter is missing or if it
   *         does not match the token found in session
   */
  def assertOrigin(request: WebRequest) {
    if (shouldRequestBeChecked(request)) {
      if (request.existingSession == None)
        throw new IllegalStateException(
          "Cannot protect from forged requests without an active WebSession.")
    
      val requestToken = request.params.getParameter(tokenName)
      val csrfToken = request.session[String](tokenName)
      if (requestToken != csrfToken)
        throw new MaliciousRequestException("Forged request detected!")
    }
  }
  
  private def shouldRequestBeChecked(request: WebRequest) =
    request.method == "POST" && !request.isAjax
  
  /**
   * Get the CSRF protection token for the specified session.
   * 
   * <p>If a token does not already exist for the given session
   * it is created and stored as an attribute.</p>
   * 
   * @param session the WebSession that contains the token
   * @return the CSRF token of the given session
   */
  def getToken(session: WebSession): String = {
    session.putIfAbsent(tokenName, generateNewToken)
  }

  private def generateNewToken = UUID.randomUUID.toString 
}