/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.tzavellas.coeus.core.config

/**
 * Performs all the necessary setup to allow the Cake pattern to be used for
 * instantiating the controllers of this web module.
 * 
 * <p>This trait registers any {@code Controller} classes found inside a cake
 * "component registry". The {@code Controller} classes must be defined as
 * inner classes inside inside cake components (traits) and have a single
 * no-arg constructor.</p>
 * 
 * <p>Below we have a full example of a controller ({@code BlogController})
 * defined inside a cake component ({@code BlogControllerComponent}) that
 * depends on another cake component ({@code BlogDAOComponent}). The components
 * are then mixed together in the {@code ComponentRegistry} object.</p>
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
 * this trait to the {@code WebModule} class and override the
 * {@code components} method.
 * <pre>
 * class BlogModule(sc: ServletConfig) extends WebModule(sc) with CakeSupport {
 *   lazy val components = List(ComponentRegistry)
 * }
 * </pre>
 */
trait CakeSupport {

  this: ControllerRegistry =>

  /**
   * The collection of cake component registries that contain the controllers
   * classes for this web module.
   */
  def components: Traversable[AnyRef]
  
  components foreach { CakeRegistrar.registerControllers(this, _) }
}