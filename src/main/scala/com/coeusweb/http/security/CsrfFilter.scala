/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.http.security

import com.coeusweb.Controller
import com.coeusweb.controller.BeforeFilter

/**
 * Protects the Controller from CSRF attacks.
 * 
 * @see CsrfProtection
 */
trait CsrfFilter extends BeforeFilter {

  this: Controller =>
  
  def before() = {
    CsrfProtection.assertOrigin(request)
    None
  }
}