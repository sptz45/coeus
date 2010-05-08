/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.view.helper

import java.io.File
import javax.servlet.ServletContext
import scala.util.Random

/**
 * Contains helper methods for generating tags that link to asset files.
 * 
 * <p>Can generate tags for Javascript, CSS and image files.</p>
 */
trait AssetsHelper {
  
  val servletContext: ServletContext
  
  /**
   * A version string that gets appended to all asset paths to allow instant invalidation
   * of caches that are set to expire in the future.
   * 
   * <p>By default this value is not set and the the modification timestamp of the asset
   * files is used as a version.</p>
   */
  val version: Option[String] = None
  
  /**
   * A sequence of URLs to dedicated asset servers.
   */
  val assetHosts: Seq[String] = Nil
  
  /**
   * The parent directory of all Javascript files.
   * 
   * <p>The default value is: "/assets/".</p>
   */
  val scriptsPrefix = "/assets/"
  
  /**
   * The parent directory of all CSS files.
   * 
   * <p>The default value is: "/assets/".</p>
   */
  val stylesheetsPrefix = "/assets/"
  
  /**
   * The parent directory of all image files.
   * 
   * <p>The default value is: "/assets/".</p>
   */
  val imagesPrefix = "/assets/"
    
  
  /**
   * Generates a {@literal <script>} tag for linking to Javascript files.
   * 
   * <p>In the case where we've set the version to 42, we haven't set any asset hosts and the context
   * path of the application is "/app" then a call to {@code script("jquery")} will generate the
   * following XML: {@literal <script type="text/javascript" src="/app/assets/js/jquery.js?42"></script>}.  
   * 
   * @param name the name of the Javascript file without the ".js" extension.
   */
  def script(name: String) = <script type="text/javascript" src={url(scriptsPrefix, name, ".js")}></script>
  
  /**
   * Generates {@literal <script>} tags for the specified names using the {@link #script} method.
   * 
   * @param names a list of Javascript filenames without the ".js" extension.
   */
  def scripts(names: String*) = names.map(script(_)).mkString("\n")
  
  /**
   * Generates a {@literal <link>} tag for linking to CSS files.
   * 
   * <p>In the case where we've set the version to 42, we haven't set any asset hosts and the context
   * path of the application is "/app" then a call to {@code stylesheet("typography")} will generate the
   * following XML: {@literal <link href="/app/assets/css/typography.css?42" type="text/css" rel="stylesheet" media="screen"/>}.  
   * 
   * @param name the name of the CSS file without the ".css" extension.
   */
  def stylesheet(name: String, media: String = "screen") = {
    <link rel="stylesheet" type="text/css" media={media} href={url(stylesheetsPrefix, name, ".css")}/>
  }
  
  /**
   * Generates {@literal <link>} tags for the specified names using the {@link #stylesheet} method.
   * 
   * <p>The {@code media} attribute of the {@literal <link>} tags is set to "screen".</p>
   * 
   * @param names a list of CSS filenames without the ".css" extension.
   */
  def stylesheets(names: String*) = names.map(stylesheet(_, "screen")).mkString("\n")

  /**
   * Generates an {@literal <img>} tag for linking to image files.
   * 
   * @param name the filename of an image file
   * @param alt the alternative text for the image
   */
  def image(name: String, alt: String = "") = <img src={url(imagesPrefix, name, null)} alt={alt}/>
  
  /**
   * Generates an {@literal <img>} tag for linking to image files.
   * 
   * @param name the filename of an image file
   * @param width the width of the image
   * @param height the height of the image
   * @param alt the alternative text for the image
   */
  def image(name: String, alt: String, width: Int, height: Int) = {
    <img src={url(imagesPrefix, name, null)} alt={alt} width={width.toString} height={height.toString} />
  }
  
  /**
   * Returns one of the asset hosts defined in the {@link #assetHosts}
   * sequence.
   */
  protected def assetHost(name: String, suffix: String) = {
    if (assetHosts.isEmpty) null
    else assetHosts(Random.nextInt(assetHosts.length))
  }

  
  // -- private methods ----------------------------------------------------------------------
  
  private def url(prefix: String, name: String, suffix: String): String = {
    hostNameOrContextPath(name, suffix).append(pathWithVersion(prefix, name, suffix)).toString
  }
  
  private def hostNameOrContextPath(name: String, suffix: String) = {
    val builder = new StringBuilder
    assetHost(name, suffix) match {
      case null => builder.append(servletContext.getContextPath)
      case host => builder.append(host)
    }
    builder
  }
  
  private def pathWithVersion(prefix: String, name: String, suffix: String): StringBuilder = {
    val builder = new StringBuilder
    builder.append(prefix)
    builder.append(name)
    if (suffix ne null)
      builder.append(suffix)
    val v = version.getOrElse(lastModified(servletContext.getRealPath(builder.toString)))
    builder.append("?")
    builder.append(v)
    builder
  }
  
  private def lastModified(path: String)  =
    if (path eq null) 0 else (new File(path)).lastModified
}