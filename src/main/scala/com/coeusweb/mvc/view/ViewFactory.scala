/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.mvc.view

import java.io.{ File, InputStream }
import scala.xml.NodeSeq
import com.coeusweb.http.HttpStatus


object ViewFactory extends ViewFactory

/**
 * Helper methods for constructing {@code View} objects.
 */
trait ViewFactory {
  
  /**
   * Returns a {@link NullView}.
   * 
   * <p>This method is used to convey that the controller's handler method
   * has already done the view processing and that the framework should not try
   * to render a view.</p>
   */
  def rendered = NullView
  
  /**
   * Returns the {@link ViewName} with the specified name.
   * 
   * <p>This method is useful when you want to return a view name from the
   * {@link #ifValid()} and the {@link BeforeFilter#before()} methods that
   * must always return a <code>View</code> object.</p>
   * 
   * @param viewName the name of the view
   * @see {@link com.coeusweb.view.ViewResolver ViewResolver}
   */
  def render(viewName: String) = new ViewName(viewName) 
  
  /**
   * Returns a <code>View</code> that when rendered will redirect to the
   * specified <code>location</code> using the specified HTTP status code.
   * 
   * <p>If you want to include all the request parameters in the redirect
   * URL you can use the following code:
   * <pre>
   * redirect(location, status, params.toSeq: _*)
   * </pre>
   * </p>
   * 
   * @param location the location to redirect to
   * @param status the HTTP status code of the response (must be a redirection, 300 series)
   * @param parameters any request parameters to include in the redirect URL
   */
  def redirect(location: String, status: Int, parameters: (String, Any)*) =
    new RedirectView(location, status, parameters: _*)
  
  /**
   * Returns a <code>View</code> that when rendered will redirect using the
   * <em>303 (See Other)</em> HTTP status code.
   * 
   * @param location the location to redirect to
   * @param parameters any request parameters to include in the redirect URL
   */
  def redirect(location: String, parameters: (String, Any)*) =
    new RedirectView(location, HttpStatus.SEE_OTHER, parameters: _*)

  /**
   * Construct a {@link TextView} that renders the specified value.
   * 
   * @param any to value to render in the <code>View</code>
   * @param contentType the response's content-type
   */
  def send(any: Any, contentType: String = "text/html") = {
    val text = if (any == null) "" else any.toString
    new TextView(text, contentType)
  }  
  
  /**
   * Construct a {@link StreamingView} to send the specified {@code InputStream}
   * to the client.
   * 
   * <p>The length of the data is used to used to set the <em>Content-Length</em> HTTP
   * header in the response. If the length specified has a negative value then the
   * <em>Content-Length</em> HTTP header is not set.</p>
   * 
   * <p>The filename is used in the <em>Content-Disposition</em> HTTP header. If the filename
   * specified is the empty string then the <em>Content-Disposition</em> HTTP header is not
   * set.</p> 
   * 
   * @param input the <code>InputStream</code> to send to the client
   * @param length the length of the data, or a negative value to disable
   * @param filename the filename to use for the Content-Disposition header, or the empty string to disable
   * @param contentType the response's content-type
   * @param disposition can be "attachment" or "inline" and it is used in the Content-Disposition header 
   */
  def stream(
    input: InputStream,
    length: Long = -1,
    filename: String = "",
    contentType: String = "application/octet-stream",
    disposition: String = "attachment") =
      new StreamingView(input, length, filename, contentType, disposition)
  
  /**
   * Construct a {@link StreamingView} to send the specified <code>File</code> to the client.
   * 
   * <p>The <code>View</code> returned by this method, when rendered, will set the
   * <em>Content-Disposition</em> HTTP header to "attachment" and the <em>Content-Length</em>
   * HTTP header to the size of the specified file.</p>  
   * 
   * @param file the <code>File</code> to send to the client
   * @param contentType the response's content-type 
   */
  def streamFile(file: File, contentType: String = "application/octet-stream") =
    StreamingView.streamFile(file, contentType)
  
  /**
   * Returns a {@link SendfileView} that when rendered will set the <em>X-Sendfile</em>
   * HTTP header telling the web server to serve the specified file.
   * 
   * <p><strong>Important:</strong>The view returned by this method will not stream the
   * data of the specified file to the client. It will just set an HTTP header telling the
   * web server to send the file. Make sure your web/application server supports the
   * <em>X-Sendfile</em> HTTP header before using this method.</p>  
   * 
   * @param file the path to the file to use for the <em>X-Sendfile</em> HTTP header
   * @param contentType the response's content-type
   * @param disposition can be "attachment" or "inline" and it is used in the Content-Disposition header
   */
  def sendfile(file: String, contentType: String = "application/octet-stream", disposition: String = "attachment") =
    new SendfileView(file, contentType, disposition)

}
