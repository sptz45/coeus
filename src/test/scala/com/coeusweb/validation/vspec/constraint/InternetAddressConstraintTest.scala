/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.validation.vspec.constraint

import org.junit.Test

class InternetAddressConstraintTest {
  import ConstraintAssertions._

  val constraint = new InternetAddressConstraint
  
  @Test
  def nulls_are_valid_and_empty_string_is_invalid() {
    assertValid(constraint, null)
    assertInvalid(constraint, "")
  }
  
  @Test
  def validate_ipv4_adresses() {
    assertValid(constraint, "0.0.0.0", "255.255.255.255", "127.0.0.1")
    assertInvalid(constraint, "256.256.256.256", "1.2.3.4.5", "1.2.3.4.", "1.2.3", ".1.2.3.4")
  }
  
  @Test
  def validate_domain_names() {
    assertValid(constraint, "www.google.com", "aaa","go.a1a", "go.com", "go.au", "255.com")
    assertInvalid(constraint, "go.a", "go.2aa", "aaa.", ".aaa", "yahoo.museum")
  }
  
  @Test
  def validate_domain_names_with_dashes() {
    assertValid(constraint, "example-domain.com")
    assertInvalid(constraint, "-example.com", "example-.com", "example.-com", "example.c-m", "example.co-")
  }
  
  @Test
  def validate_domains_with_one_character() {
    assertValid(constraint, "a", "1.2.3.gr", "a.gr", "a.b.gr")
    assertInvalid(constraint, "example.x", "test.example.x")
  }
}
