/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.mvc.controller

import language.implicitConversions

trait ParameterConversions {

  this: Controller =>
  
  implicit def _param2Char(s: String): Char= if (s.isEmpty) 0 else s.charAt(0)
  implicit def _param2Boolean(s: String): Boolean= java.lang.Boolean.parseBoolean(s)
  implicit def _param2Short(s: String): Short = java.lang.Short.parseShort(s)
  implicit def _param2Int(s: String): Int = java.lang.Integer.parseInt(s)
  implicit def _param2Long(s: String): Long = java.lang.Long.parseLong(s)
  implicit def _param2Float(s: String): Float = java.lang.Float.parseFloat(s)
  implicit def _param2Double(s: String): Double= java.lang.Double.parseDouble(s)
}