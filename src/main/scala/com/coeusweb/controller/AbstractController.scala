/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.controller

import com.coeusweb.Controller

abstract class AbstractController extends Controller
                                     with FormProcessing
                                     with ResponseHelpers
                                     with ParameterConversions