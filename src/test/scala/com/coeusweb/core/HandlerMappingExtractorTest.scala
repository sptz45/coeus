/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.core

import org.junit.Test
import org.junit.Assert._
import com.coeusweb.{ WebRequest, WebResponse, Controller }
import com.coeusweb.view.View
import config._

class HandlerMappingExtractorTest {  
  import HandlerMappingExtractorTest._
  
  private val extractor = new HandlerMappingExtractor(
    ControllerConventions.useClassName(),
    ControllerConventions.useMethodName)

  @Test(expected=classOf[InvalidControllerClassException])
  def exception_if_the_class_does_not_have_annotations() {
    assertTrue(extractor.extract(classOf[Controller]).isEmpty)
  }
  
  @Test
  def create_mappings_for_framework_annotations_only() {
    val mappings = extractor.extract(classOf[BlogController])
    assertEquals(2, mappings.size)
    assertTrue(mappings.exists(_.httpMethod == 'GET))
    assertTrue(mappings.exists(_.httpMethod == 'POST))
  }
  
  @Test(expected=classOf[InvalidControllerClassException])
  def exception_if_more_than_one_http_method_annotation_per_method() {
    extractor.extract(classOf[MoreThenOneAnnotationPerMethod])
  }
  
  @Test
  def class_name_gets_used_as_base_path() {
    val mappings = extractor.extract(classOf[BlogController])
    assertTrue(mappings.forall(_.path.startsWith("/blog/")))
  }
  
  @Test
  def override_class_name_in_base_path_via_annotation() {
    val mappings = extractor.extract(classOf[PageController])
    assertTrue(mappings.forall(_.path.startsWith("/pages/")))
  }
  
  @Test
  def do_not_use_the_class_name_in_path() {
    val mappings = extractor.extract(classOf[NoClassNameInPath])
    assertEquals("/index", mappings.head.path)
  }
  
  @Test
  def method_name_in_path() {
    val mappings = extractor.extract(classOf[BlogController])
    assertTrue(mappings.exists(_.path.endsWith("index")))
    assertTrue(mappings.exists(_.path.endsWith("post")))
  }
  
  @Test
  def override_method_name_via_annotation() {
    val mappings = extractor.extract(classOf[PageController])
    assertEquals("/pages/list", mappings.head.path)
  }
  
  @Test
  def get_mappings_from_super_class() {
    val mappings = extractor.extract(classOf[SearchablePageController])
    assertTrue(mappings.exists(_.path == "/searchablePage/list"))
    assertTrue(mappings.exists(_.path == "/searchablePage/search"))
  }
  
  @Test
  def get_mappings_from_traits() {
    val mappings = extractor.extract(classOf[DirectoryController])
    assertTrue(mappings.exists(_.path == "/directory/list"))
    assertTrue(mappings.exists(_.path == "/directory/create"))
  }
}

object HandlerMappingExtractorTest {
  import javax.xml.bind.annotation.XmlAttribute
  import com.coeusweb.annotation._
  
  class BlogController extends Controller {
    @Get def index(): View = null
    @Post def post(): View = null
    @XmlAttribute def xml() { }
  }
  
  @Path("/pages")
  class PageController extends Controller {
    @Get("list") def get() = null
  }
  
  class SearchablePageController extends PageController {
    @Get def search(): View = null
  }
  
  trait CrudSupport {
    @Put def create(): View = null
  }
  
  class DirectoryController extends PageController with CrudSupport 
  
  class MoreThenOneAnnotationPerMethod extends Controller {
    @Get @Post
    def method(): View = null
  }
  
  @Path("")
  class NoClassNameInPath extends Controller {
    @Get def index(): View = null
  }
}