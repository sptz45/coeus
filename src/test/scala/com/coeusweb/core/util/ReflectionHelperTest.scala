/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.core.util

import org.junit.{ Test, Ignore }
import org.junit.Assert._
import com.coeusweb.core.FrameworkException

class ReflectionHelperTest {
  import ReflectionHelper._
  import ReflectionHelperTest._

  @Test
  def get_the_type_arguments_from_collections() {
    assertEquals(classOf[Int], getTypeArgumentsOfCollection(HasCollections("getArrayOfInts"))(0))
    assertEquals(classOf[java.lang.Integer], getTypeArgumentsOfCollection(HasCollections("getListOfInts"))(0))
    assertEquals(classOf[Object], getTypeArgumentsOfCollection(HasCollections("getListOfSomething"))(0))
    assertTrue("Expected empty since the type has parameter but no arguments", getTypeArgumentsOfCollection(HasCollections("getListOfT")).isEmpty)
    assertEquals(classOf[java.lang.Integer], getTypeArgumentsOfCollection(HasCollections("getMapOfIntToString"))(0))
    assertEquals(classOf[String], getTypeArgumentsOfCollection(HasCollections("getMapOfIntToString"))(1))
  }
  
  @Test(expected=classOf[FrameworkException])
  def exception_when_no_generics() {
    getTypeArgumentsOfCollection(HasCollections("getString"))
  }
  
  @Test
  def test_when_a_class_is_abstract_or_interface() {
    assertTrue(isAbstract(classOf[Interface]))
    assertTrue(isAbstract(classOf[AbstractClass]))
    assertFalse(isAbstract(classOf[ConcreteClass]))
  }
}

object ReflectionHelperTest {
  
  trait Interface
  abstract class AbstractClass
  class ConcreteClass 
  
  case class SampleProduct(element: String)
  
  abstract class HasCollections {
    def getArrayOfInts: Array[Int]
    def getListOfInts: List[Int]
    def getListOfSomething: List[_]
    def getListOfT[T]: List[T]
    def getMapOfIntToString: Map[Int, String]
    def getString: String
  }
  object HasCollections {
    def apply(name: String) = classOf[HasCollections].getMethods.find(_.getName == name).get 
  }
}

