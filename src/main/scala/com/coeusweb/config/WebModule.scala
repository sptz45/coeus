package com.coeusweb.config

import javax.servlet.ServletConfig

class WebModule(val servletConfig: ServletConfig) extends ConfigBuilder
                                                     with ControllerRegistry
                                                     with InterceptorRegistry