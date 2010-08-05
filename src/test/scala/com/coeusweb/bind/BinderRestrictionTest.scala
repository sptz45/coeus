package com.coeusweb.bind

import org.junit.Test
import org.junit.Assert._

class BinderRestrictionTest {

  import Binder._
  
  @Test
  def allow_all_always_returns_true() {
    assertTrue(allowAll.canBindTo(null))
  }
  
  @Test
  def deny_vars_allows_all_except() {
    val restriction = denyVars("forbidden")
    assertFalse(restriction.canBindTo("forbidden"))
    assertTrue(restriction.canBindTo("allowed"))
  }
  
  @Test
  def allow_vars_denies_all_except() {
    val restriction = allowVars("allowed")
    assertTrue(restriction.canBindTo("allowed"))
    assertFalse(restriction.canBindTo("forbidden"))
  }
}
