/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.core

import scala.reflect.ClassManifest
import com.coeusweb.Controller
import com.coeusweb.config._
import com.coeusweb.interceptor.{ Interceptors, ThreadLocalRequestInterceptor }
import com.coeusweb.scope.support.FlashScopeInterceptor
import util.ReflectionHelper

/**
 * Register <code>Controller</code>s and <code>RequestInterceptor</code>s.
 */
class ControllerRegistry(config: DispatcherConfig) {
  
  private val extractor = new HandlerMappingExtractor(config.classNameTranslator, config.methodNameTranslator)
  
  /**
   * Register the specified class as a <code>Controller</code> for the
   * <code>DispatcherServlet</code>.
   * 
   * <p>If the specified class is abstract this method does nothing.</p>
   * 
   * @throws InvalidControllerClassException if the controller class has invalid structure
   *         or if it doesn't have handler annotations.
   */
  def register[C <: Controller](implicit c: ClassManifest[C]) {
    // get the class from the manifest
    val controllerClass = c.erasure.asInstanceOf[Class[Controller]]
    register(controllerClass)
  }
  
  /**
   * Register the specified class as a <code>Controller</code> for the
   * <code>DispatcherServlet</code>.
   * 
   * <p>If the specified class is abstract this method does nothing.</p>
   * 
   * @throws InvalidControllerClassException if the controller class has invalid structure
   *         or if it doesn't have handler annotations.
   */
  def register[C <: Controller](klass: Class[C]) {
    
    val controllerClass = klass.asInstanceOf[Class[Controller]]
    if (ReflectionHelper.isAbstract(controllerClass)) return
    
    // extract any handler mappings from the annotated methods
    val mappings = extractor.extract(controllerClass)
    
    // register the mappings in request resolver
    for (mapping <- mappings) {
      config.requestResolver.register(
        mapping.path,
        mapping.httpMethod,
        new Handler(controllerClass, mapping.controllerMethod))
    }
    // tell the controller factory about the new controller
    config.controllerFactory.registerClass(controllerClass)
  }
  
  /**
   * A builder that allows the registration of custom <code>RequestInterceptor</code>s. 
   * 
   * @see Interceptors
   * @see {@link com.coeusweb.interceptor.RequestInterceptor RequestInterceptor}
   */
  val interceptors = {
    val defaults = Interceptors.newBuilder
    defaults += new ThreadLocalRequestInterceptor
    defaults += new FlashScopeInterceptor
    defaults
  }
}
