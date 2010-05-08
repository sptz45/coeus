/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.interceptor

/**
 * An ordered and immutable collection of <code>RequestInterceptor</code>s.
 * 
 * @param builder the builder that is used to populate this collection
 */
class Interceptors(builder: Interceptors.Builder) extends Iterable[RequestInterceptor] {
  
  private val interceptors: List[RequestInterceptor] = builder.result
  
  def iterator = interceptors.iterator
}

/**
 * Helper methods for creating instances of <code>Interceptors</code>
 */
object Interceptors {
  import scala.collection.mutable.{ Builder => SBuilder, ListBuffer }
  
  /** The type of Builder that is used to create the Interceptors collection */ 
  type Builder = SBuilder[RequestInterceptor, List[RequestInterceptor]]
  
  /** Creates a new Builder for constructing an <code>Interceptors</code> collections. */
  def newBuilder: Builder = new ListBuffer
}
