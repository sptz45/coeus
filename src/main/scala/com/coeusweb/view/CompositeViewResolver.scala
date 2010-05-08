/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.view

/**
 * A {@code ViewResolver} that consults a sequence of {@code ViewResolver}s for
 * finding the {@code View} to render.
 * 
 * @param resolvers the sequence of this implementation delegates to for finding
 *        a {@code View} to render.
 * 
 * @see ViewResolver
 */
class CompositeViewResolver(resolvers: ViewResolver*) extends ViewResolver {

  def resolve(name: String): View = {
    for (resolver <- resolvers) {
      val view = resolver.resolve(name)
      if (view ne null)
        return view
    }
    null
  }
}