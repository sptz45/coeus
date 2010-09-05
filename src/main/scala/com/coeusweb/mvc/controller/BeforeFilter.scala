/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.mvc.controller

import com.coeusweb.Controller
import com.coeusweb.view.View

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
 *     if (AuthManager.isUserLoggedIn(request)) None
 *     else redirect("/login")
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
 *     if (AuthManager.isUserLoggedIn(request)) None
 *     else render("/login")
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
   * <p>If this method returns some {@code View} then the handler method
   * of the {@code Controller} does not get executed and the returned {@code View}
   * gets rendered in the response. This can be used to implement security checks or
   * caching where we may not want the handler method to execute for a particular
   * request.</p> 
   * 
   * @return may return some {@code View} to prevent the controller's handler
   *         method from executing or {@code None} to continue with normal request
   *         execution. 
   */
  def before(): Option[View]
}
