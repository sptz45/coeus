/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.test

import org.springframework.mock.web

package object servlet {
  
  type MockHttpServletRequest = web.MockHttpServletRequest
  
  type MockHttpServletResponse = web.MockHttpServletResponse
  
  type MockHttpSession = web.MockHttpSession
  
  type MockServletContext = web.MockServletContext
  
  type MockServletConfig = web.MockServletConfig
}
