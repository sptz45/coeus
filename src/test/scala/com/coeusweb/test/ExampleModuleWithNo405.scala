/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.test

import javax.servlet.ServletConfig
import com.coeusweb.config.DispatcherConfig

class ExampleModuleWithNo405(sc: ServletConfig) extends ExampleModule(sc) {
  override def dispatcherConfig = new DispatcherConfig(sc) {
    override lazy val viewResolver = ExampleModule.viewResolver
    override lazy val hideExistingResources = true
  }
}