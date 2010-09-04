/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.controller

import com.coeusweb.Controller
import com.coeusweb.validation.vspec.VSpecFormatting

abstract class AbstractController extends Controller
                                     with FormProcessing
                                     with VSpecFormatting
                                     with ResponseHelpers
                                     with ParameterConversions