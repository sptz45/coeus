/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.mvc.scope.support

import javax.servlet.http.HttpSessionEvent
import org.junit.Test
import org.junit.Assert._
import org.springframework.mock.web.MockHttpSession

class HttpSessionMutexListenerTest {

  val session = new MockHttpSession
  val listener = new HttpSessionMutexListener
  val event = new HttpSessionEvent(session)
  
  @Test
  def listener_adds_a_serializable_object_in_session() {
    assertFalse(session.getAttributeNames.hasMoreElements)
    listener.sessionCreated(event)
    assertTrue(session.getAttributeNames.hasMoreElements)
    
    val attr = session.getAttribute(session.getAttributeNames.nextElement)
    assertTrue(attr.isInstanceOf[java.io.Serializable])
  }
}
