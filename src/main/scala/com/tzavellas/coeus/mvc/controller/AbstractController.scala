/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.tzavellas.coeus.mvc.controller

abstract class AbstractController extends Controller
                                     with FormProcessing
                                     with ResponseHelpers
                                     with ParameterConversions