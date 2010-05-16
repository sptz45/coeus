/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.config

import java.util.Locale
import javax.servlet.ServletConfig
import com.coeusweb.bind.{ ConverterRegistry, DefaultConverterRegistry }
import com.coeusweb.core._
import com.coeusweb.core.convention._
import com.coeusweb.core.factory.{ ControllerFactory, SimpleControllerFactory }
import com.coeusweb.error.{ ExceptionHandler, ErrorPageExceptionHandler }
import com.coeusweb.http.multipart.{ MultipartRequestParser, NullMultipartRequestParser }
import com.coeusweb.i18n.locale.{ LocaleResolver, AcceptHeaderLocaleResolver }
import com.coeusweb.i18n.msg.{ MessageBundle, ServletMessageBundle }
import com.coeusweb.validation.vspec.VSpecErrorFormatter
import com.coeusweb.view.ViewResolver
import com.coeusweb.view.helper.DefaultViewHelpers
import com.coeusweb.view.scalate.{ ScalateViewResolver, ScalateConfig }

/**
 * Holds the configuration for <code>DispatcherServlet</code>.
 *
 * <p>The instances of this class are immutable, users are expected to
 * subclass and override any val that they want to change.</p>
 * 
 * @param servletConfig the <code>ServletConfig</code> of the Servlet
 *                      that this instance holds the configuration for.
 * 
 * @see ConfigBuilder
 * @see {@link com.coeusweb.core.DispatcherServlet DispatcherServlet}
 */
class DispatcherConfig(val servletConfig: ServletConfig) {
  
  /**
   * The factory that creates the controller's instance in each request.
   */
  lazy val controllerFactory: ControllerFactory = new SimpleControllerFactory
  
  /**
   * Translates the controller's class into a base path.
   * 
   * <p>By default the first character of the cotroller's simple class name is transformed
   * into lower-case and the "Controller" suffix is removed. For example a controller class
   * with the name <code>UserRegistrationController</code> gets translated to
   * "/userRegistration".</p> 
   * 
   * @see ControllerClassNameTranslator
   * @see {@link com.coeusweb.annotation.Path Path}
   */
  lazy val classNameTranslator: ControllerClassNameTranslator = new DefaultControllerClassNameTranslator(Nil)
  
  /**
   * Translates the controller's annotated methods into paths.
   * 
   * <p>By default the handler method is used without any transformation. For example a handler
   * method with the name <code>submitForm</code> gets translated to "/submitForm".</p> 
   * 
   * @see MethodNameTranslator
   */
  lazy val methodNameTranslator: ControllerMethodNameTranslator = new DefaultControllerMethodNameTranslator
  
  /**
   * Finds the appropriate handler for a given request.
   */
  lazy val requestResolver: RequestResolver = new TreeBasedRequestResolver
  
  /**
   * Called when an exception occurs during the request processing.
   * 
   * <p>By default the exception gets propagated to the Servlet container.</p>
   */
  lazy val exceptionHandler: ExceptionHandler = ErrorPageExceptionHandler
  
  /**
   * Tells the <code>DispatcherServlet</code> to set the encoding of the Servlet
   * requests to the specified value.
   * 
   * <p>If {@code requestEncoding} is {@code None} then the {@code DispatcherServlet}
   * does not set the encoding of the request.</p>
   * 
   * <p>The default value is "UTF-8".</p>
   */ 
  lazy val requestEncoding: Option[String] = Some("UTF-8")
  
  /**
   * Tells the {@code DispatcherServlet} to send <em>404</em> (Not Found) instead
   * of <em>405</em> (Method Not Allowed) when a resource exist but does not support
   * the requested method.
   * 
   * <p>This is useful if for security reasons when we would like to hide the
   * existence of a resource.</p>
   * 
   * <p>The default value is {@code false}.</p>
   */
  lazy val hideResources = false
  
  /**
   * Resolves the user's locale for a given request.
   * 
   * <p>By default the user's locale is the locale specified in the <em>accept-language</em>
   * header of the HTTP request.</p>
   * 
   * @see LocaleResolver
   * @see AcceptHeaderLocaleResolver
   * @see Locale
   */
  lazy val localeResolver: LocaleResolver = new AcceptHeaderLocaleResolver
  
  /**
   * Loads the i18n messages.
   * 
   * <p>By default the messages are loaded from property files that are located under the
   * "WEB-INF" directory and are cached for one second.</p>
   * 
   * <p>The message files follow similar naming patterns with the files used in
   * {@link java.util.PropertyResourceBundle PropertyResourceBundle} using the base-name
   * "messages". For example if the user's locale is en_US then the <code>MessageBundle</code>
   * will use the files "WEB-INF/messages_en_US.properties" and "WEB-INF/messages_en.properties"
   * to load the messages.</p>
   * 
   * @see ServletMessageBundle
   * @see {@link com.coeusweb.i18n.msg.ClasspathMessageBundle ClasspathMessageBundle}
   * @see {@link java.util.ResourceBundle ResourceBundle}
   */
  lazy val messageBundle: MessageBundle = new ServletMessageBundle(servletConfig.getServletContext, 1000)
  
  /**
   * A collection with pre-configured converters to be used by default when binding
   * and formatting values. 
   * 
   * @see ConverterRegistry
   * @see ConverterRegistry#newRegistryWithDefaults
   * @see {@link com.coeusweb.WebRequest WebRequest}
   */
  lazy val converters: ConverterRegistry = DefaultConverterRegistry
  
  /**
   * Parses multipart requests (used in file uploads).
   * 
   * <p>The configured parser does not parse requests and throws {@code UnsupportedOperationException}.
   * This is done in order to avoid having a dependency to external library by default. If your application
   * handles multipart requests you must use another parser (such as {@code CommonsMultipartRequestParser}).</p>
   */
  lazy val multipartParser: MultipartRequestParser = new NullMultipartRequestParser

  /**
   * Maps view names to view instances.
   * 
   * <p>The default resolver is a <code>ScalateViewResolver</code> configured using the
   * default values. The <code>ScalateViewResolver</code> also makes available the
   * {@link #viewHelpers} object as a attribute to all {@code View} instances using the
   * name "c".</p>
   * 
   * @see DispatcherConfig#taglib
   * @see ScalateViewResolver
   * @see ScalateConfig
   */
  lazy val viewResolver: ViewResolver = {
    val config = new ScalateConfig
    config.bind[DefaultViewHelpers].to(viewHelpers).using(name="c", importMembers=false, isImplicit=false)
    new ScalateViewResolver(servletConfig.getServletContext, config)
  }
  
  /**
   * The default view helpers for Scalate views.
   * 
   * <p>The form helpers are configured to use {@code Vspec} for form validation. If
   * you want to use another {@code Validatior} implementation use must create a
   * {@code DefaultViewHelpers} with the correct {@code ErrorFormatter} implementation.</p>
   */
  lazy val viewHelpers = {
    val errorFormatter = new VSpecErrorFormatter(messageBundle, converters)
    new DefaultViewHelpers(servletConfig.getServletContext, messageBundle, errorFormatter)
  }
}
