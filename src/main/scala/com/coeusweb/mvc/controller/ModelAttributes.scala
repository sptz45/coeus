/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.mvc
package controller

import com.coeusweb.bind.{ Binder, BindingResult }
import util.Conventions
import scope.RequiredAttributeException

/**
 * Provides methods for handling the objects associated with the binding and
 * validation workflow of a {@link Controller}.
 */
class ModelAttributes(binder: Binder, storeInSession: Boolean) {
  
  import ModelAttributes._

  /**
   * Return the model attribute with the specified name.
   * 
   * <p>The attribute is looked up in the request scope and then in the
   * session scope.</p>
   *  
   * @throws RequiredAttributeException if an attribute with the specified name
   *         cannot be found
   */
  def apply[T](modelName: String): T = {
    
    var model: T = request.getAttribute[T](modelName)
    if (model != null)
      return model 
    
    model = request.session.getAttribute[T](modelName)
    if (model != null)
      return model
    
    throw new RequiredAttributeException(modelName, this.getClass)
  }
  
  /**
   * Add a model attribute using the specified name and object.
   * 
   * <p>This method adds three request attributes: 1) the specified
   * object under the specified name, 2) a corresponding {@code BindingResult}
   * and 3) sets the "modelAttribute" request attribute to the specified
   * {@code modelName}.</p> 
   * 
   * @param modelName the name of the attribute.
   * @param target    the object to add as a model attribute.  
   */
  def update(modelName: String, target: AnyRef) {
    addBindingResult(modelName, binder.bind(Nil, target, null))
  }

  /**
   * Add a model attribute using the specified object.
   * 
   * <p>The name of the attribute is the simple name of the class of the
   * specified object with the first letter converted to lower case.</p>
   * 
   * @param target the object to add as a model attribute.
   * 
   * @see {@link #update(modelName, target}
   */
  def +=(target: AnyRef) {
    addBindingResult(null, binder.bind(Nil, target, null))
  }

  /**
   * Remove the model attribute with the specified name.
   * 
   * <p>This method removes the attribute with the specified name from the
   * request scope, the session scope and also removes the corresponding
   * {@code BindingResult} from the request scope.</p>
   * 
   * @param modelName the name of the attribute to remove
   */
  def -=(modelName: String) {
    request -= modelName
    request -= (modelName + "BindingResult")
    for (session <- request.existingSession) {
      session -= modelName
    }
  }
  
  /**
   * Remove the specified attribute
   * 
   * @param model the attribute to remove
   */
  def -=(target: AnyRef) {
    this -= getModelNameFromTarget(target)
  }
  
  /**
   * Add the target object of the specified {@code BindingResult} as a model
   * attribute using the specified name.
   * 
   * @param modelName the attribute name or {@code null} to use a generated name
   * @param result    contains the value of the attribute
   */
  def addBindingResult(modelName: String, result: BindingResult[AnyRef]) {
    val name = if (modelName != null) modelName else getModelNameFromTarget(result.target) 
    request(name) = result.target
    request(getResultName(name)) = result
    setCurrentModelName(name, request)
    if (storeInSession) {
    	request.session(name) = result.target
    }
  }
  
  def currentBindingResult[T <: AnyRef]: Option[BindingResult[T]] = {
    if (containsBindingResult(request)) Some(getBindingResult(request).asInstanceOf[BindingResult[T]])
    else None
  }
  
  def hasErrors = {
    if (containsBindingResult(request)) getBindingResult(request).hasErrors
    else false
  }
  
  private def request = WebRequest.currentRequest
}

object ModelAttributes {
  
  private val MODEL_ATTRIBUTE_NAME = "modelAttribute" 
  
  def setCurrentModelName(name: String, request: WebRequest) {
    request(MODEL_ATTRIBUTE_NAME) = name
  }
  
  def getCurrentModelName(request: WebRequest) = request[String](MODEL_ATTRIBUTE_NAME)
  
  def containsBindingResult(request: WebRequest) = request.contains(MODEL_ATTRIBUTE_NAME)
  
  def getBindingResult(request: WebRequest): BindingResult[AnyRef] = {
    request(getResultName(getCurrentModelName(request)))
  }
  
  private def getResultName(targetName: String) = targetName + "BindingResult" 
  
  private def getModelNameFromTarget(target: AnyRef) = Conventions.classToAttributeName(target.getClass)
}
