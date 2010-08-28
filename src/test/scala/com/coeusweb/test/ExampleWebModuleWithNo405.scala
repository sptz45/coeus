/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.test

import javax.servlet.ServletConfig

class ExampleWebModuleWithNo405(sc: ServletConfig) extends ExampleWebModule(sc) {
  
  hideResources = true
}