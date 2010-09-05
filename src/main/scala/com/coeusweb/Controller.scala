/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb

/**
 * Handles the user's request.
 * 
 * <p>A {@code Controller} class consists of a collection of <em>handler methods</em> whose
 * responsibility is to receive the current request, interact with collaborating and model
 * objects to handle the request and return the View that will be used for rendering the
 * response.</p>
 * 
 * <p>To find which handler method will be executed for a particular request the hander methods
 * of all registered controllers are matched against the request using a configured HTTP method
 * and URL pattern. If a handler method cannot be found for a particular URL then the framework
 * sets the HTTP status code of the response to <em>404</em> (Not-Found) and lets the Servlet
 * container render any configured error page in <em>web.xml</em>. If a handler method exists but not for
 * the particular HTTP method then the HTTP status code is set to <em>405</em> (Method-Not-Allowed).</p>
 *
 * <p>The binding between a handler method and a pair of HTTP method and URL pattern is configured
 * using annotations with the help of convention-over-configuration mechanisms.</p>
 * 
 * <h4>Handler Mappings</h4>
 *
 * <p>Handler methods are annotated with HTTP method annotations found in the {@code com.coeusweb.annotation}
 * package. That package defines the Get, Post, Put and Delete annotations that correspond to the most
 * frequently used HTTP methods and the HttpMethod annotation that can be used to map arbitrary HTTP
 * methods to handler methods.</p>
 * 
 * <pre>
 * class PagesController extends Controller {
 *
 *  {@literal @Get}
 *   def list() = // ...
 * 
 *  {@literal @Post}
 *   def create() = // ...
 *
 *  {@literal @HttpMethod(method="OPTIONS")}
 *   def showOptions() = // ...
 * }
 * </pre>
 *
 * <p>In the above Controller the list() method is mapped to a GET request, the create() method is mapped
 * to a POST and the showOptions() to an OPTIONS request.</p>
 * 
 * The URL pattern for a particular handler method is by default derived from the name of the controller's
 * class and the name of the method using the configured ControllerClassNameTranslator and
 * ControllerMethodNameTranslator. Using the default configuration, in the above controller the list() method
 * is mapped to a GET request at /<context-path>/pages/list, where <context-path> is the context path of the
 * deployed web application.</p>
 * 
 * <p>To override the URL pattern for a specific controller you can use the value attribute of the HTTP method
 * annotations, the path attribute of the HttpMethod annotation and the value attribute of the Path annotation.
 * The Path annotation is used to override the how the class name gets transformed into a base path for the URL.
 * To prevent the class name or the method name from appearing in the URL pattern you can use the empty "" as
 * the values for any of the aforementioned annotation attributes.</p>
 * 
 * <pre>
 *{@literal @Path("post")}
 * class AuthoringController extends Controller {
 *
 * {@literal @Get}
 *  def edit() = // ...
 *
 * {@literal @Post("")}
 *  def save() = // ...
 * }
 * </pre>
 *
 * Assuming the context path of the application is "/", in the AuthoringController above, the edit() method is
 * mapped to a GET request at "/post/edit". The "/post" part is derived from the value of the value attribute
 * of the Path annotation that overrode the name that would have been generated by default via the configured
 * ControllerClassNameTranslator. The "/edit" derives from the name of the handler method and the configured
 * ControllerMethodNameTranslator since the Get annotation on the edit() method does not set the value of the
 * value attribute. On the other hand the save() method is mapped to a POST request at "/post" since the value
 * attribute of the Post annotation is the empty String "" which tells the framework not to use the method name
 * when generating the mapping for the handler method.</p>
 * 
 * <h4>Controller Lifecycle</h4>
 * 
 * <p>For each request the Controller class of the matched handler method is found and a new instance of the
 * Controller is created via the configured ControllerFactory implementation. This controller instance is
 * used to invoke the handler method and it is disposed after the handling of the request. Since Controller
 * instances are created for each request it is thread-safe to update the controller's vars during request
 * execution because controller are thread confined.</p>
 * 
 * <p>Before the execution of the handler method the current WebRequest and WebResponse are injected into the
 * request and response vars of the Controller.</p>
 * 
 * <h4>Views</h4>
 * 
 * <p>Handler methods may return the View that will be used to render the response. For the programmer's
 * convenience the return type of the hander method is not restricted to {@code View} subclasses. A handler
 * method may return one of the following a {@code View} instance, a {@code String}, a {@code NodeSeq} or
 * nothing ({@code Unit}).</p>
 * 
 * <p>In the case of the {@code View} instance, the instance returned is used to render the response via an
 * invocation of the {@link View#render()} method. If a {@code String} is returned instead then the {@code String}
 * is used to lookup a {@code View} from the configured {@code ViewResolver} and that {@code View} is used to
 * render the request. If a hander method returns {@code NodeSeq} then the returned {@code NodeSeq} is wrapped
 * into an {@code XHtmlView} that is used to render the response. Last if the method returns {@code Unit} then
 * {@link com.coeusweb.core.convention.RequestToViewNameTranslator RequestToViewNameTranslator} is used to derive
 * a view name from the request.</p>
 * 
 * @see {@link com.coeusweb.controller.AbstractController AbstractController}
 * @see {@link com.coeusweb.core.Handler#handle Handler.handle(..)}
 * @see {@link com.coeusweb.core.factory.ControllerFactory ControllerFactory}
 */
abstract class Controller extends ScopeAccessor {
  
  implicit def thisScopeAccessor: ScopeAccessor = this
  
  /** The current web request. */
  var request: WebRequest = _
  
  /** The current web response. */
  var response: WebResponse = _
  
  /** The flash scope. */
  def flash = request.flash
  
  /** The web session. */
  def session = request.session
  
  /** The application scope. */
  def application = request.application

  /** The request parameters. */
  def params = request.params
  
  /** The path parameters. */
  def path = request.path
  
  /** The default converters as configured in {@code WebModule}. */
  def converters = request.converters
  
  /**
   * Get an i18n message for the specified code and arguments, using the locale
   * of the configured {@code LocaleResolver}.
   * 
   * @param code the code of the message.
   * @param args any arguments that will be interpolated into the message.
   * 
   * @return the interpolated message.
   * @throws MessageNotFoundException if a message does not exist for the
   *         specified code and Locale.
   */
  def messages(code: String, args: Any*) = request.messages(request.locale, code, args)
}
