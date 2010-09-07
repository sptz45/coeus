/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb

import javax.servlet.ServletContext

/**
 * The deployment stage of an application (production or development).
 */
sealed abstract class Stage(val name: String) {
  override def toString = name
}

object Stage {
  
  /** The <em>production</em> stage. */ 
  case object Production extends Stage("production")
  
  /** The <em>development</em> stage. */
  case object Development extends Stage("development")
  
  /**
   * Get the stage configured in web.xml of the specified
   * {@code ServletContext}.
   * 
   * <p>This method will return a {@code Stage} object based on the configured
   * value of the <em>coeus-stage</em> ServletContext init parameter. If the
   * parameter is not set then this method returns {@code Development}.
   * 
   * <p>Example <em>web.xml</em> fragment for the <em>production</em> stage:</p>
   * <pre>
   * &lt;context-param&gt;
   *   &lt;param-name&gt;coeus-stage&lt;/param-name&gt;
   *   &lt;param-value&gt;production&lt;/param-value&gt;
   * &lt;/context-param&gt;
   * </pre>
   * 
   * @param context the ServletContext to check for the <em>coeus-stage</em>
   *                init parameter.
   */
  def of(context: ServletContext): Stage =
    of(context.getInitParameter("coeus-stage"))
  
  /**
   * Get the {@code Stage} object that corresponds to the specified string.
   * 
   * @param stage can be "production" or "stage"
   * 
   * @return {@code Production} if the input string in "production" else
   *         returns {@code Development}.
   */
  def of(stage: String): Stage = {
    if (stage eq null)
      return Development
    val trimmed = stage.trim.toLowerCase
    if (trimmed.startsWith("pro")) Production else Development
  }
}