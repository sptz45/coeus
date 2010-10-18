/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.mvc
package controller

import scala.util.control.NoStackTrace
import view.View

/**
 * Executes common logic <em>before</em> each handler method of a
 * {@code Controller}.
 * 
 * <p>For example in the below {@code Controller} the {@code BeforeFilter}
 * checks that the user is logged in before showing him the article's
 * editor.</p>
 * <pre>
 * class ArticleEditorController extends Controller with BeforeFilter {
 * 
 *   def before() =
 *     if (!AuthManager.isUserLoggedIn(request))
 *       stopAndRender(redirect("/login"))
 *   
 *  {@literal @Get}
 *   def show() = "editor"
 * }
 * </pre>
 * 
 * <p>{@code BeforeFilter}s can also be defined in reusable traits and
 * mixed in multiple {@code Controller} classes.</p>
 * 
 * <pre>
 * trait Secured extends BeforeFilter {
 *   def before() =
 *     if (!AuthManager.isUserLoggedIn(request))
 *       stopAndRender(redirect("/login"))
 * }
 * 
 * class ArticleEditorController extends Controller with Secured {
 *  {@literal @Get}
 *   def show() = "editor"
 * }
 * </pre>
 * 
 * @see Controller
 * @see {@link com.coeusweb.core.Handler#handle Handler.handle()}
 */
trait BeforeFilter {
  
  this: Controller =>
  
  /**
   * Gets executed <em>before</em> the {@code Controller}'s handler method that
   * corresponds to the current request.
   * 
   * <p>If this method throws {@code BeforeFilter.RequestInterruption} then the
   * handler method of the {@code Controller} does not get executed and the
   * view of the request interruption gets rendered in the response. This can
   * be used to implement security checks or caching where we may not want the
   * handler method to execute for a particular request.</p> 
   * 
   * @throws BeforeFilter.RequestInterruption
   */
  def before()
  
  /**
   * Throws a {@code RequestInterruption} to prevent the controller's handler
   * method from executing.
   * 
   * @param view the {@code View} to render.
   */
  protected def stopAndRender(view: View) {
    throw new BeforeFilter.RequestInterruption(view)
  }
}

object BeforeFilter {
  
  /**
   * When thrown from a {@code BeforeFilter} the controller's hander method
   * does not get executed and the specified view is rendered instead.
   * 
   * @param view the {@code View} to render.
   */
  class RequestInterruption(val view: View) extends Throwable with NoStackTrace
}
