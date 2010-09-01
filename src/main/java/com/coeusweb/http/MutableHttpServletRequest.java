/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.http;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class MutableHttpServletRequest extends HttpServletRequestWrapper {

  private String method;
  
  public MutableHttpServletRequest(HttpServletRequest request) {
    super(request);
    this.method = request.getMethod();
  }
  
  public MutableHttpServletRequest(HttpServletRequest request, String method) {
    super(request);
    this.method = method;
  }
  
  @Override
  public String getMethod() {
    return method;
  }
  
  public void setMethod(String method) {
    this.method = method;
  }
}