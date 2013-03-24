/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.tzavellas.coeus.mvc.scope

/**
 * Provides <i>thread-safe</i>, <i>check-and-act</i> operations on a an underlying scoped
 * container by synchronizing access to the container.
 * 
 * <p>The purpose of the methods in this trait is to ensure atomicity when manipulating mutable
 * objects that are set as attributes in scoped containers. This is done by synchronizing access
 * to the container using a proper lock. Users are generally advised to put immutable objects in
 * scoped containers and to use the methods of this trait only when necessary.</p> 
 * 
 * <p>For an in depth discussion of thread-safety issues in a Servlet environment read
 * <a href="http://www.ibm.com/developerworks/library/j-jtp09238.html">Are all stateful Web applications broken?</a>
 * by Brian Goetz.</p>
 * 
 * @see ScopedContainer
 * @see ApplicationScope
 * @see WebSession
 */
trait CheckAndActOperations {
  
  /** The underlying scoped container */
  this: ScopedContainer =>
  
  /** The mutex to be used for synchronizing access to the scoped container. */
  protected def mutex: AnyRef

  /**
   * Puts the specified value in the scoped container under the specified attribute
   * name if a value for that attribute does not already exist.
   * 
   * <p>This method synchronizes access to the underlying scoped container
   * providing atomicity guarantee for concurrently executed code.</p>
   * 
   * @param attribute the name of the attribute
   * @param value the value of the attribute to set
   * @return the value of the attribute
   */
  def putIfAbsent[T](attribute: String, value: => T): T = mutex.synchronized {
    val existing: T = getAttribute[T](attribute)
    if (existing == null) {
      val evaluated = value
      update(attribute, evaluated)
      evaluated
    } else {
      existing
    }
  }

  /**
   * Updates the value of an existing attribute with the specified name using the specified
   * <code>mutate</code> closure and puts the updated attribute back in the container.
   * 
   * <p>This method synchronized access to the underlying scoped container
   * providing atomicity guarantees for concurrently executed code.</p>
   * 
   * @param attribute the name of the attribute
   * @param mutate a mutator closure for updating the value of the attribute
   * @return the new value of the attribute
   */
  def updateAndPut[T](attribute: String)(mutate: T => Unit): T = mutex.synchronized {
    val value: T = apply[T](attribute)
    mutate(value)
    update(attribute, value)
    value
  }
  
  /**
   * Synchronizes access to the underlying container and executes the specified block
   * of code.
   * 
   * @param block the code to execute while the underlying container is synchronized
   */
  def synchronizeAndExecute[U](block: => U): U = mutex.synchronized(block)
}

/**
 * Support methods for the <code>CheckAndActOperations</code> trait.
 */ 
object CheckAndActOperations {
  
  /** Create an object suitable to be used as a mutex for scoped containers. */
  def newMutex: AnyRef = new Mutex
  
  @SerialVersionUID(1234567890)
  private class Mutex extends java.io.Serializable
}