/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.core.config

import com.coeusweb.core.factory.CakeControllerFactory

/**
 * Performs all the necessary setup to allow the Cake pattern to be used for
 * instantiating the controllers of this web module.
 * 
 * <p>This trait sets the module's <code>controllerFactory</code> to
 * <code>CakeControllerFactory</code> and registers any <code>Controller</code>
 * class found inside a cake component registry.</p> 
 * 
 * <p>The <code>Controller</code> classes must be defined as inner classes inside
 * inside cake components (traits) and have a single no-arg constructor.</p>
 * 
 * <p>Below we have a full example of a controller (<code>BlogController</code>)
 * defined inside a cake component (<code>BlogControllerComponent</code>) that
 * depends on another cake component (<code>BlogDAOComponent</code>). The
 * components are then mixed together in the <code>ComponentRegistry</code>
 * object.</p>
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
 * <p>To use the above objects in a Coeus application all we have to do is mix
 * this trait to the <code>WebModule</code> class and override the
 * <code>components</code> method.
 * <pre>
 * class BlogModule(sc: ServletConfig) extends WebModule(sc) with CakeSupport {
 *   lazy val components = List(ComponentRegistry)
 * }
 * </pre>
 * 
 * @see CakeControllerFactory
 */
trait CakeSupport {

  this: WebModule =>

  /**
   * The collection of cake component registries that contain the controllers
   * classes for this web module.
   * 
   * <p>This method must always return the same instance. To override correctly
   * use a <strong>lazy val</strong>. Overriding with <em>val</em> may lead to
   * <code>NullPointerException</code>.</p>
   */
  def components: Traversable[AnyRef]
  
  controllerFactory = new CakeControllerFactory(components.toSeq:_*)
  for (cc <- components.map(_.getClass))
    CakeRegistrar.registerControllers(this, cc)
}