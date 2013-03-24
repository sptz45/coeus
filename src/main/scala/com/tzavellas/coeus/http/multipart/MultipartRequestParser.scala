/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.tzavellas.coeus.http.multipart

import javax.servlet.{ ServletContext, ServletContextEvent }
import javax.servlet.http.HttpServletRequest

/**
 * Parses the contents of the multipart requests to extract any request
 * parameters and files.
 */
trait MultipartRequestParser {
  
  /**
   * Performs any necessary initialization actions.
   */
  def init(context: ServletContext)
  
  /**
   * Performs any cleanups during application shutdown.
   */
  def destroy(context: ServletContext)
  
  /**
   * Parse the contents of the specified request and extract any request
   * parameters and files.
   * 
   * @param request a multipart request
   * @return a {@code MultipartHttpServletRequest} instance that contains
   *         the submitted request parameters and files
   */
  def parse(request: HttpServletRequest): MultipartHttpServletRequest
  
  /**
   * Delete any temporary files created for the specified request.
   * 
   * <p>Called by the framework after each multipart request.</p>
   * 
   * @param request the current multipart request
   */
  def cleanupFiles(request: MultipartHttpServletRequest)
}