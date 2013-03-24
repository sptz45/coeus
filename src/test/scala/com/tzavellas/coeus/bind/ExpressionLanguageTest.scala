/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.tzavellas.coeus.bind

import org.junit.Test
import org.junit.Assert._
import com.tzavellas.coeus.test.Assertions.assertThrows

class ExpressionLanguageTest {
  import ExpressionLanguage.{eval, bind}
  
  val target = new Target("t", new B(11))
  val parsers = ConverterRegistry.defaultConverters 

  @Test
  def test_eval_precondictions() {
    assertThrows[IllegalArgumentException] { eval(null, "test") }
    assertThrows[IllegalArgumentException] { eval(target, null) }
    assertThrows[IllegalArgumentException] { eval(target, "") }
  }
  
  @Test
  def read_a_value() {
    assertEquals("t", eval(target, "a"))
  }
  
  @Test
  def read_a_value_using_method_from_cache() {
    assertEquals("t", eval(target, "a"))
    assertEquals("t", eval(target, "a"))
  }
  
  @Test(expected=classOf[ExpressionException])
  def attempt_to_read_nonexisting_value() {
    eval(target, "does_not_exitst")
  }
  
  @Test
  def read_value_from_association() {
    assertEquals(11, eval(target, "b.i"))
  }
  
  @Test(expected=classOf[ExpressionException])
  def attempt_to_read_from_null_association() {
    target.b = null
    assertEquals(11, eval(target, "b.i"))
  }
  
  @Test
  def read_unwrapping_option() {
    val t = new HasOptionalValue
    t.optional = Some(42)
    assertEquals(42, eval(t, "optional"))

    t.optional = None
    assertNull("None gets converted into null", eval(t, "optional"))
  }
  
    @Test
  def read_option_without_unwrapping() {
    val t = new HasOptionalValue
    t.optional = Some(42)
    assertEquals(Some(42), eval(t, "optional", unwrapOption=false))

    t.optional = None
    assertEquals(None, eval(t, "optional", unwrapOption=false))
  }
  
  @Test
  def read_values_from_indexed_collections() {
    val itarget = new IndexedTarget(List(0), java.util.Arrays.asList(0), Array(0), (0, target))
    assertEquals(0, eval(itarget, "slist[0]"))
    assertEquals(0, eval(itarget, "jlist[0]"))
    assertEquals(0, eval(itarget, "array[0]"))
    assertEquals(0, eval(itarget, "product[0]"))
    assertEquals("t", eval(itarget, "product[1].a"))
  }
  
  @Test
  def read_values_from_maps() {
    val jmap = new java.util.HashMap[String, Target]
    jmap.put("zero", target)
    val props = new java.util.Properties
    props.put("zero", "zero")
    val mtarget = new MappedTarget(Map("zero" -> 0), jmap, props, Num(12))
    assertEquals(0, eval(mtarget, "smap[zero]"))
    assertEquals("t", eval(mtarget, "jmap[zero].a"))
    assertEquals("zero", eval(mtarget, "props[zero]"))
    //XXX Ignored because Product.productElementName is temporarily disabled in scala trunk
    //assertEquals(12, eval(mtarget, "prod[x]"))
  }
 
  @Test
  def bind_value() {
    bind(target, "a", "new value", null, parsers)
    assertEquals("new value", target.a)
  }
  
  @Test
  def no_error_when_binding_using_nonexisting_var() {
    bind(target, "doesNotExist", "new value", null, parsers)
  }
  
  @Test
  def bind_to_association() {
    bind(target, "b.i", "15", null, parsers)
    assertEquals(15, target.b.i)
  }
  
  @Test
  def bind_to_indexed_collection() {
    val itarget = new IndexedTarget(scala.collection.mutable.ListBuffer(0),
                                    java.util.Arrays.asList(0),
                                    Array(0),
                                    null)
    
    bind(itarget, "slist[0]", "1", null, parsers)
    assertEquals(1, eval(itarget, "slist[0]"))
    
    bind(itarget, "jlist[0]", "1", null, parsers)
    assertEquals(1, eval(itarget, "jlist[0]"))
    
    bind(itarget, "array[0]", "1", null, parsers)
    assertEquals(1, eval(itarget, "array[0]"))
  }
  
  @Test
  def bind_to_mapped_collection() {
    val props = new java.util.Properties
    props.put("zero", "zero")
    val mtarget = new MappedTarget(scala.collection.mutable.Map("zero" -> 0), null, props, null)
    
    bind(mtarget, "smap[zero]", "1", null, parsers)
    assertEquals(1, eval(mtarget, "smap[zero]"))
  }
  
  @Test
  def bind_to_option() {
    val t = new HasOptionalValue
    bind(t, "optional", "42", null, parsers)
    assertEquals(Some(42), t.optional)
  }
  
  // -- Test classes -----------------------------------------------------
  
  class HasOptionalValue {
    var optional: Option[Int] = None
  }

  class IndexedTarget(
    val slist: scala.collection.Seq[Int],
    val jlist: java.util.List[Int],
    val array: Array[Int],
    val product: Product)

  class MappedTarget(
    val smap: scala.collection.Map[String, Int],
    val jmap: java.util.Map[String, Target],
    val props: java.util.Properties,
    val prod: Product)
  
  case class Num(x: Int)
  class Target(var a: String, var b: B)
  class B(var i: Int)
}
