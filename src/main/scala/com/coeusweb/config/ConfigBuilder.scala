/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.config

import javax.servlet.ServletConfig
import com.coeusweb.view.scalate.ScalateViewResolver

/**
 * Creates the configuration for {@code DispatcherServlet}.
 *
 * @param servletConfig the {@code ServletConfig} of the Servlet
 *        that this instance configures.
 *
 * @see DispatcherConfig
 * @see {@link com.coeusweb.core.DispatcherServlet DispatcherServlet}
 */
class ConfigBuilder(servletConfig: ServletConfig) {
  
  /**
   * The configuration of {@code DispatcherServlet}.
   * 
   *@return the default {@code DispatcherConfig}.
   */
  def dispatcherConfig: DispatcherConfig = new DispatcherConfig(servletConfig)
}
