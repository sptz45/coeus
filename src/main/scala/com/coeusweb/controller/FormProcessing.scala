/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.controller

import com.coeusweb.Controller
import com.coeusweb.bind.{ Binder, BindingResult }
import com.coeusweb.core.convention.{ Conventions, RequestToViewNameTranslator }
import com.coeusweb.scope.RequiredAttributeException
import com.coeusweb.validation.Validator
import com.coeusweb.view.{ View, ViewName }

trait FormProcessing {
  
  this: Controller =>
  
  /**
   * Whether to store the model attributes in session.
   * 
   * <p>The default value is {@code false}.</p>
   */
  def storeModelInSession = false
  
  /** The model attributes. */
  lazy val model = new ModelAttributes(binder, request, storeModelInSession)
  
  /**
   * The <code>Binder</code> to use when binding request parameters to
   * target objects.
   * 
   * <p>The <code>Binder</code> returned by this method gets used in the
   * the {@link #validate} and {@link #ifValid} methods to bind the request
   * parameters to the model object.</p>
   * 
   * @see Binder
   */
  lazy val binder = new Binder(request.converters)
  

  /**
   * Bind and validate the specified target object.
   * 
   * <p>This method, regardless of the validation outcome, adds the specified target object
   * as a request attribute using the specified <code>modelName</code>. It also adds as request
   * attributes the resulting <code>BindingResult</code> and the "modelAttribute".</p> 
   * 
   * @param modelName the attribute name of the model object
   * @param target the object to bind and validate
   * @param validator the validator to use for validating the object
   * @return the result of the validation
   * @see BindingResult
   */
  def validate[T <: AnyRef](modelName: String, target: T)(implicit validator: Validator[T]): BindingResult[T] = {
    val result = binder.bind(request.params, target, request.locale)
    validator.validate(result)
    model.addBindingResult(modelName, result)
    result
  }
  
  /**
   * Bind and validate the specified target object.
   * 
   * <p>Same as the {@link #validate(modelName, target, validator)} except that the
   * <code>modelName</code> gets automatically generated from the target object's class.</p>
   * 
   * @param target the object to bind and validate
   * @param validator the validator to use for validating the object
   * @return the result of the validation
   */
  def validate[T <: AnyRef](target: T)(implicit validator: Validator[T]): BindingResult[T] = {
    validate(null, target)(validator)
  }
  
  /**
   * Bind, validate and execute the specified closure if no errors occur.
   * 
   * <p>This method, regardless of the validation outcome, adds the specified target object
   * as a request attribute using the specified <code>modelName</code>. It also adds as request
   * attributes the resulting <code>BindingResult</code> and the "modelAttribute".</p> 
   * 
   * @param modelName the attribute name of the model object
   * @param target the object to bind and validate
   * @param validator the validator to use for validating the object
   * @param onSuccess the closure to execute if the binding and validation succeeds
   * @return the <code>View</code> object to use for rendering the response
   */
  def ifValid[T <: AnyRef](target: T, modelName: String = null)(onSuccess: T => View)(implicit validator: Validator[T]): View = {
    val result = validate(modelName, target)(validator)
    if (result.hasErrors) formView else onSuccess(target)
  }
 
  /**
   * Bind, validate and execute the specified closure if no errors occur.
   * 
   * <p>Same as {@code ifValid(target, modelName)(onSuccess)(validator)} method
   * except that this method retrieves the target object form the <code>WebSession</code>
   * using the specified <code>modelName</code> and if no errors occur removes it from the
   * <code>WebSession</code> before calling the <code>onSuccess</code> closure.</p>  
   * 
   * @param modelName the attribute name of the model object
   * @param validator the validator to use for validating the object
   * @param onSuccess the closure to execute if the binding and validation succeeds
   * @return the <code>View</code> object to use for rendering the response
   */
  def ifValid[T <: AnyRef](modelName: String)(onSuccess: T => View)(implicit validator: Validator[T]): View = {
    try {
      val target: T = session[T](modelName)
      val result = validate(modelName, target)(validator)
      if (result.hasErrors) formView else { session -= modelName; onSuccess(target) }
    } catch {
      case e: RequiredAttributeException if !storeModelInSession =>
        throw new RequiredAttributeException(e.attribute, e.scopeClass,
          "You need to set 'storeModelInSession' to 'true' when you call the ifValid methods that retrieve the model object from the session.")
    }
  }
  
  /**
   * Bind, validate and execute the specified closure if no errors occur.
   * 
   * <p>Same as the {@code ifValid(modelName)(onSuccess)(validator)} method except
   * that the {@code modelName} is derived from the class of the specified {@code T}
   * parameter.</p>
   * 
   * @param onSuccess the closure to execute if the binding and validation succeeds
   * @param validator the validator to use for validating the object
   * @return the <code>View</code> object to use for rendering the response
   */
  def ifValid[T <: AnyRef](onSuccess: T => View)(implicit validator: Validator[T], m: Manifest[T]): View = {
    val modelName = Conventions.classToAttributeName(m.erasure)
    ifValid(modelName)(onSuccess)(validator)
  }
  
  /**
   * Return a <code>View</code> to be used when the validation fails in the {@link #ifValid} methods.
   * 
   * <p>This method returns a view name by invoking the {@link RequestToViewNameTranslator}. Subclasses
   * can override this method to return a different view name.</p>
   */
  def formView: View = new ViewName(RequestToViewNameTranslator.viewNameForRequest(request))
}
