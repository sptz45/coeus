/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 * 
 * Was originally based on code from Spring Framework
 * that was written by Arjen Poutsma.
 *
 * Author: Spiros Tzavellas
 */
package com.tzavellas.coeus.http

object HttpStatus { 
  
  // --- 1xx Informational ---

  val CONTINUE = 100
  val SWITCHING_PROTOCOLS = 101

  // --- 2xx Success ---

  val OK = 200
  val CREATED = 201
  val ACCEPTED = 202
  val NON_AUTHORITATIVE_INFORMATION = 203
  val NO_CONTENT = 204
  val RESET_CONTENT = 205
  val PARTIAL_CONTENT = 206
  
  // --- 3xx Redirection ---

  val MULTIPLE_CHOICES = 300
  val MOVED_PERMANENTLY = 301
  val MOVED_TEMPORARILY = 302
  val SEE_OTHER = 303
  val NOT_MODIFIED = 304
  val USE_PROXY = 305
  val TEMPORARY_REDIRECT = 307

  // --- 4xx Client Error ---

  val BAD_REQUEST = 400
  val UNAUTHORIZED = 401
  val PAYMENT_REQUIRED = 402
  val FORBIDDEN = 403
  val NOT_FOUND = 404
  val METHOD_NOT_ALLOWED = 405
  val NOT_ACCEPTABLE = 406
  val PROXY_AUTHENTICATION_REQUIRED = 407
  val REQUEST_TIMEOUT = 408
  val CONFLICT = 409
  val GONE = 410
  val LENGTH_REQUIRED = 411
  val PRECONDITION_FAILED = 412
  val REQUEST_TOO_LONG = 413
  val REQUEST_URI_TOO_LONG = 414
  val UNSUPPORTED_MEDIA_TYPE = 415
  val REQUESTED_RANGE_NOT_SATISFIABLE = 416
  val EXPECTATION_FAILED = 417

  // --- 5xx Server Error ---

  val INTERNAL_SERVER_ERROR = 500
  val NOT_IMPLEMENTED = 501
  val BAD_GATEWAY = 502
  val SERVICE_UNAVAILABLE = 503
  val GATEWAY_TIMEOUT = 504
  val HTTP_VERSION_NOT_SUPPORTED = 505
  
  // --- Series ---
  
  def isInformational(status: Int) = (status / 100 == 1)
  def isSuccessful(status: Int)    = (status / 100 == 2)
  def isRedirection(status: Int)   = (status / 100 == 3)
  def isClientError(status: Int)   = (status / 100 == 4)
  def isServerError(status: Int)   = (status / 100 == 5)
  
  def isError(status: Int) = isClientError(status) || isServerError(status)
}
