/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.core.factory

import java.lang.reflect.Constructor
import com.coeusweb.Controller
import com.coeusweb.core.FrameworkException

/**
 * Creates controller instances by instantiating inner classes from a Cake component registry.
 * 
 * <p>Controllers must be defined as inner classes, with a single no-arg constructor, inside
 * cake components (traits).
 * 
 * <p>Below we have a full example of a controller (<code>BlogController</code>) defined inside a
 * cake component (<code>BlogControllerComponent</code>) that depends on another cake component
 * (<code>BlogDAOComponent</code>). The components are then mixed together in the
 * <code>ComponentRegistry</code> object.</p>
 * 
 * <pre>
 * trait BlogControllerComponent {
 *   this: BlogDAOComponent =>
 *   class BlogController extends Controller {
 *     {@literal @Get} def index {
 *        request("posts") = blogDao.findRecentPosts()
 *     }
 *   }
 * }
 * 
 * trait BlogDAOComponent {
 *   val blogDao: BlogDAO
 *   trait BlogDAO {
 *     def findRecentPosts(): Seq[Post]
 *   }
 * }
 * 
 * trait JdbcBlogDAOComponent extends BlogDAOComponent {
 *   this: DataSource =>
 *   val blogDao: BlogDAO = new BlogDAO {
 *     def findRecentPosts(): Seq[Post] = ...
 *   }
 * }  
 * 
 * object ComponentRegistry
 *   extends DataSource
 *      with JdbcBlogDAOComponent
 *      with BlogControllerComponent
 * </pre>
 * 
 * <p>To use the above objects in a Coeus application all we have to do is define a
 * <code>DispatcherContext</code> class like the following:</p>
 * <pre>
 * class DispatcherContext(sc: ServletConfig) extends ConfigBuilder(sc) with ControllerRegistry {
 *   CakeRegistrar.registerControllers(this, ComponentRegistry.getClass)
 *   override def dispatcherConfig = new DispatcherConfig(sc) {
 *     override lazy val controllerFactory = new CakeControllerFactory(ComponentRegistry)
 *   }
 * }
 * </pre>
 * 
 * <p>The <code>DispatcherContext</code> class uses
 * {@link com.coeusweb.config.CakeRegistrar CakeRegistrar} to register the controllers defined
 * in the {@code ComponentRegistry} object and configures the {@code DispatcherServlet} to use a
 * {@code CakeControllerFactory} for creating the controllers.</p>
 * 
 * @see {@link com.coeusweb.config.CakeRegistrar CakeRegistrar}
 * @see Controller
 */
class CakeControllerFactory(componentRegistries: AnyRef*) extends ControllerFactory {
  require(componentRegistries.length > 0,
          "Cannot create CakeControllerFactory. " +
          "You must supply at least one \"component registry\" object.") 
  
  private[this] val cache =
    if (componentRegistries.length == 1) new SingleRegistryCache(componentRegistries(0))
    else new MultipleRegistriesCache(componentRegistries:_*)

  def controllerRegistered[C <: Controller](controllerClass: Class[C]) {
    try {
      val constructor = controllerClass.getConstructors.apply(0)
      cache.add(controllerClass, constructor)
    } catch {
      case cause: Exception => throw new FrameworkException(
        "Failed to register controller with class: %s. The the class must be an inner class with a public no-arg constructor."
          .format(controllerClass.getName), cause)
    }
  }
  
  def createController[C <: Controller](klass: Class[C]): C = {
    try {
      cache.newInstance(klass)
    } catch {
      case e: NoSuchElementException =>
        throw new FrameworkException(
          "Could not instantiate a controller with class: %s because the class hasn't been registered"
            .format(klass.getName))
    }
  }
  
  private trait Cache {
    def add(klass: Class[_], cons: Constructor[_])
    def newInstance[C](klass: Class[C]): C
  }
  
  private class SingleRegistryCache(registry: AnyRef) extends Cache {
    
    var cache = Map[Class[_], Constructor[_]]()
    
    def add(klass: Class[_], cons: Constructor[_]) {
      require(isNoArgConstructorOfInnerClass(cons),
        "Class "+klass.getName+" is not an inner class or does not contain a no-arg constructor")
      cache = cache + (klass -> cons)
    }
    
    def newInstance[C](klass: Class[C]): C = {
      cache(klass).newInstance(registry).asInstanceOf[C]
    }
    
    /* Constructors of inner classes take the outer class as the first parameter.
     * For that reason we must check that the constructor takes one parameter
     * (instead of zero). */
    def isNoArgConstructorOfInnerClass(cons: Constructor[_]) = {
      val paramTypes = cons.getParameterTypes
      paramTypes.length == 1 && paramTypes(0).isAssignableFrom(registry.getClass)
    }
  }
  
  private class MultipleRegistriesCache(registries: AnyRef*) extends Cache {
    
    var cache = Map[Class[_], (AnyRef, Constructor[_])]()
    
    def add(klass: Class[_], cons: Constructor[_]) {
      val paramTypes = cons.getParameterTypes
      require(paramTypes.length == 1, "Class "+klass.getName+" is not an inner class or does not contain a no-arg constructor")
      val registry = registries.find(r => paramTypes(0).isAssignableFrom(r.getClass))
      require(registry != None, "Class "+klass.getName+" is not a inner class in any of ["+registries.map(_.getClass.getName).mkString(",")+"]")
      cache = cache + (klass -> (registry.get -> cons))
    }
    
    def newInstance[C](klass: Class[C]) = cache(klass) match {
      case (registry, constructor) => constructor.newInstance(registry).asInstanceOf[C]
    }
  }
}
