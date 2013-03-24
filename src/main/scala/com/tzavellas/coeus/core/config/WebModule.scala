/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.tzavellas.coeus.core.config

import javax.servlet.ServletConfig

/**
 * The central configuration class of a DispatcherServlet.
 * 
 * <p>For each <code>DispatcherServlet</code> in your web application you must
 * define a <code>WebModule</code> class that contains the configuration,
 * controller classes and interceptors for the servlet. The <code>WebModuel</code>
 * class must be specified as an init parameter with the name <em>web-module</em>
 * in the servlet definition in <em>web.xml</em>.</p>
 *
 * Sample XML for a <code>DispatcherServlet</code> configuration in web.xml:
 * <pre>
 * &lt;servlet&gt;
 *   &lt;servlet-name&gt;my-app&lt;/servlet-name&gt;
 *   &lt;servlet-class&gt;com.tzavellas.coeus.core.DispatcherServlet&lt;/servlet-class&gt;
 *   &lt;init-param&gt;
 *     &lt;param-name&gt;web-module&lt;/param-name&gt;
 *     &lt;param-value&gt;com.example.myapp.web.ApplicationModule&lt;/param-value&gt;
 *   &lt;/init-param&gt;
 * &lt;/servlet&gt;
 * 
 * &lt;servlet-mapping&gt;
 *   &lt;servlet-name&gt;my-app&lt;/servlet-name&gt;
 *   &lt;url-pattern&gt;/app&lt;/url-pattern&gt;
  &lt;/servlet-mapping&gt;
 * </pre>
 */
class WebModule(val servletConfig: ServletConfig) extends DispatcherConfig
                                                     with ControllerRegistry
                                                     with InterceptorRegistry