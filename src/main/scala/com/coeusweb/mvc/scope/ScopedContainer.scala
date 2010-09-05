/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.mvc
package scope

import util.Conventions

/**
 * An interface for the various scopes that hold attributes.
 * 
 * @see ApplicationScope
 * @see FlashScope
 */
trait ScopedContainer {
  
  /**
   * Retrieve the attribute with the specified name.
   * 
   * @param attribute the name or the attribute.
   * @return the attribute or null if an attribute with the specified name does
   *         exist in the scope.
   */
  def getAttribute[T](attribute: String): T
  
  def attributeNames: Iterator[String]
  
  /**
   * Retrieve the attribute with the specified name.
   * 
   * @param attribute the name or the attribute
   * @return the attribute with the specified name
   * @throws RequiredAttributeException if an attribute with the specified name does not exist
   */
  def apply[T](attribute: String): T = {
    val attr: T = getAttribute[T](attribute)
    if (attr == null) throw new RequiredAttributeException(attribute, this.getClass) else attr
  }
  
  /**
   * Retrieve an optional attribute with the specified name.
   * 
   * @param attribute the name or the attribute.
   * @return an <code>Option</code> containing the attribute if it exists.
   */
  def get[T](attribute: String): Option[T] = {
    val attr: T = getAttribute[T](attribute)
    if (attr == null) None else Some(attr)
  }
  
  /**
   * Test whether the attribute with the specified name exists in this
   * scoped container.
   * 
   * @param attribute the name or the attribute.
   * @return {@code true} if the attribute with the specified name exists, else {@code false}
   */
  def contains(attribute: String): Boolean = getAttribute(attribute) != null
  
  /**
   * Remove the attribute with the specified name from the scope and return
   * its value.
   * 
   * @param attribute the name or the attribute
   * @return the attribute with the specified name
   * @throws RequiredAttributeException if an attribute with the specified name does not exist
   */
  def take[T](attribute: String): T = {
    val attr: T = apply[T](attribute)
    this -= attribute
    attr
  }
  
  def attributes: Iterable[(String, Any)] = new Iterable[(String, Any)] {
    def iterator = new Iterator[(String, Any)] {
      val names = attributeNames
      def hasNext = names.hasNext
      def next = {
        val name = names.next
        (name -> getAttribute[Any](name))
      }
    }
  }

  /**
   * Put the specified attribute in the scope. 
   * 
   * @param attribute the name of the attribute
   * @param value the value of the attribute
   */
  def update(attribute: String, value: Any)

  /**
   * Remove the attribute with the specified name from the scope.
   */
  def -=(attribute: String)
  
  /**
   * Add the specified value in the scope using a generated name for the
   * attribute name.
   * 
   * @see Conventions#classToAttributeName
   */
  def +=(value: AnyRef) {
    update(Conventions.classToAttributeName(value.getClass), value)
  }
}
