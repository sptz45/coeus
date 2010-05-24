/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.test

import javax.servlet.ServletConfig
import com.coeusweb.config.DispatcherConfig

class ExampleContextWithNo405(sc: ServletConfig) extends ExampleDispatcherContext(sc) {
  override def dispatcherConfig = new DispatcherConfig(sc) {
    override lazy val viewResolver = TestsGlogalState.viewResolver
    override lazy val hideResources = true
  }
}