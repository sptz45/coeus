/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.tzavellas.coeus.mvc.view

/**
 * Maps view names (returned from {@code Controller} handler methods) to
 * {@code View} instances that will be used to render a response.
 * 
 * @see View
 */
trait ViewResolver {
  
  /**
   * Returns a view corresponding to the specified name.
   * 
   * @param name the name of the view.
   * 
   * @return a {@code View} to render the request or {@code null} if a
   *         {@code View} cannot get resolved using the specified name.
   */
  def resolve(name: String): View
}


/**
 * Factory methods for creating {@code ViewResolver} instances.
 */
object ViewResolver {
  
  /**
   * Create a {@code ViewResolver} that consults the specified sequence of
   * resolvers for finding the {@code View} to render.
   * 
   * @param resolvers the sequence of {@code ViewResolver}s.
   */
  def chainResolvers(resolvers: ViewResolver*): ViewResolver = new ViewResolver {
    def resolve(name: String): View = {
      for (resolver <- resolvers) {
        val view = resolver.resolve(name)
        if (view ne null)
          return view
      }
      null
    }
  }
}