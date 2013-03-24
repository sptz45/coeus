/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 * 
 * Ported from Apache Jakarta Commons Validator,
 * http://commons.apache.org/validator/
 *
 * Author: Spiros Tzavellas
 */
package com.tzavellas.coeus.validation.vspec.constraint

import org.junit.{Test, Ignore}
import org.junit.Assert._

class EmailConstraintTest {
  
  val validator = new EmailConstraint

  @Test
  def null_is_valid_email() {
    assertValid(null)
  }
  
  @Test
  def test_valid_email() {
    assertValid("jsmith@apache.org")
  }
  
  @Test
  def valid_emails_with_numeric_addresses() {
    assertValid("someone@[216.109.118.76]", "someone@yahoo.com")
  }
  
  @Test
  def constraint_validates_the_tlds_of_the_domain_part() {
    assertValid("jsmith@apache.org", "jsmith@apache.com", "jsmith@apache.net", "jsmith@apache.info")
    assertInvalid("jsmith@apache.", "jsmith@apache.c", "someone@yahoo.mu-seum", "someone@yahoo.museum")
  }
  
  @Test
  def validate_emails_that_contain_dashes() {
    assertValid("andy.noble@data-workshop.com")
    assertInvalid("andy-noble@data-workshop.-com",
                  "andy-noble@data-workshop.c-om",
                  "andy-noble@data-workshop.co-m")
  }
  
  @Test
  def an_email_ending_in_a_dot_is_invalid() {
    assertInvalid("andy.noble@data-workshop.com.");
  }
  
  @Test
  def emails_with_bogus_characters() {
    assertInvalid("andy.noble@\u008fdata-workshop.com")
    // The ' character is valid in an email username.
    assertValid("andy.o'reilly@data-workshop.com")
    // But not in the domain name.
    assertInvalid("andy@o'reilly.data-workshop.com")
    assertValid("foo+bar@i.am.not.in.us.example.com")
  }
  
  @Test
  def emails_with_commas_are_invalid() {
    assertInvalid("joeblow@apa,che.org", "joeblow@apache.o,rg", "joeblow@apache,org")
  }
  
  @Test
  def emails_with_spaces_are_invalid() {
    assertInvalid("joeblow @apache.org", "joeblow@ apache.org", "joe blow@apache.org ", "joeblow@apa che.org ")
    // before or after is OK
    assertValid(" joeblow@apache.org", "joeblow@apache.org ")
  }
  
  @Test
  def some_test() {
    assertInvalid("john@box@host.net", ".john@host.net", "john@-host.net", "john@[10.0.3.1999]")
    assertValid("\"John Gate\"@[10.0.3.19]")
  }
  
  @Test
  def more_tests() {
    assertInvalid("NotAnEmail")
    assertInvalid("@NotAnEmail")
    assertValid(""""test\\blah"@example.com""")
    assertInvalid(""""test"blah"@example.com""")
    assertValid("customer/department@example.com")
    assertValid("$A12345@example.com")
    assertValid("!def!xyz%abc@example.com")
    assertValid("_Yosemite.Sam@example.com")
    assertValid("~@example.com")
    assertInvalid(".wooly@example.com")
    assertInvalid("wo..oly@example.com")
    assertInvalid("pootietang.@example.com")
    assertInvalid(".@example.com")
    assertValid(""""Austin@Powers"@example.com""")
    assertValid("Ima.Fool@example.com")
    assertValid(""""Ima.Fool"@example.com""")
    assertValid(""""Ima Fool"@example.com""")
    assertInvalid("Ima Fool@example.com")
    
    //assertInvalid(""""test\blah"@example.com""")
    //assertValid(""""test\"blah"@example.com""")
  }
  
  /**
   * Tests the email validation with ascii control characters.
   * (i.e. Ascii chars 0 - 31 and 127)
   */
  def emails_with_control_chars_are_invalid() {
    for (c <- 0 until 32)
            assertFalse("Test control char " + c, validator.isValid("foo" + c.toChar + "bar@domain.com"))
    assertFalse("Test control char 127", validator.isValid("foo" + 127.toChar + "bar@domain.com"))
  }
  
  /**
   * Ignored, needs work!
   * 
   * These test values derive directly from RFC 822 &
   * Mail::RFC822::Address & RFC::RFC822::Address perl test.pl
   */
  @Ignore
  def perl_test_suite_from_rfc822() {
    assertValid("abigail@example.com")
    assertValid("abigail@example.com ")
    assertValid(" abigail@example.com")
    assertValid("abigail @example.com ")
    assertValid("*@example.net")
    assertValid("\"\\\"\"@foo.bar")
    assertValid("fred&barny@example.com")
    assertValid("---@example.com")
    assertValid("foo-bar@example.net")
    assertValid("\"127.0.0.1\"@[127.0.0.1]")
    assertValid("Abigail <abigail@example.com>")
    assertValid("Abigail<abigail@example.com>")
    assertValid("Abigail<@a,@b,@c:abigail@example.com>")
    assertValid("\"This is a phrase\"<abigail@example.com>")
    assertValid("\"Abigail \"<abigail@example.com>")
    assertValid("\"Joe & J. Harvey\" <example @Org>")
    assertValid("Abigail <abigail @ example.com>")
    assertValid("Abigail made this <  abigail   @   example  .    com    >")
    assertValid("Abigail(the bitch)@example.com")
    assertValid("Abigail <abigail @ example . (bar) com >")
    assertValid("Abigail < (one)  abigail (two) @(three)example . (bar) com (quz) >")
    assertValid("Abigail (foo) (((baz)(nested) (comment)) ! ) < (one)  abigail (two) @(three)example . (bar) com (quz) >")
    assertValid("Abigail <abigail(fo\\(o)@example.com>")
    assertValid("Abigail <abigail(fo\\)o)@example.com> ")
    assertValid("(foo) abigail@example.com")
    assertValid("abigail@example.com (foo)")
    assertValid("\"Abi\\\"gail\" <abigail@example.com>")
    assertValid("abigail@[example.com]")
    assertValid("abigail@[exa\\[ple.com]")
    assertValid("abigail@[exa\\]ple.com]")
    assertValid("\":sysmail\"@  Some-Group. Some-Org")
    assertValid("Muhammed.(I am  the greatest) Ali @(the)Vegas.WBA")
    assertValid("mailbox.sub1.sub2@this-domain")
    assertValid("sub-net.mailbox@sub-domain.domain")
    //assertValid("name:;")
    //assertValid("':;")
    //assertValid("name:   ;")
    assertValid("Alfred Neuman <Neuman@BBN-TENEXA>")
    assertValid("Neuman@BBN-TENEXA")
    assertValid("\"George, Ted\" <Shared@Group.Arpanet>")
    assertValid("Wilt . (the  Stilt) Chamberlain@NBA.US")
    assertValid("Cruisers:  Port@Portugal, Jones@SEA;")
    assertValid("$@[]")
    assertValid("*()@[]")
    assertValid("\"quoted ( brackets\" ( a comment )@example.com")
    assertValid("\"Joe & J. Harvey\"\\x0D\\x0A     <ddd\\@ Org>")
    assertValid("\"Joe &\\x0D\\x0A J. Harvey\" <ddd \\@ Org>")
    assertValid("Gourmets:  Pompous Person <WhoZiWhatZit\\@Cordon-Bleu>,\\x0D\\x0A" +
            "        Childs\\@WGBH.Boston, \"Galloping Gourmet\"\\@\\x0D\\x0A" +
            "        ANT.Down-Under (Australian National Television),\\x0D\\x0A" +
            "        Cheapie\\@Discount-Liquors;")
    assertInvalid("   Just a string")
    assertInvalid("string")
    assertInvalid("(comment)")
    assertInvalid("()@example.com")
    assertInvalid("fred(&)barny@example.com")
    assertInvalid("fred\\ barny@example.com")
    assertInvalid("Abigail <abi gail @ example.com>")
    assertInvalid("Abigail <abigail(fo(o)@example.com>")
    assertInvalid("Abigail <abigail(fo)o)@example.com>")
    assertInvalid("\"Abi\"gail\" <abigail@example.com>")
    assertInvalid("abigail@[exa]ple.com]")
    assertInvalid("abigail@[exa[ple.com]")
    assertInvalid("abigail@[exaple].com]")
    assertInvalid("abigail@")
    assertInvalid("@example.com")
    assertInvalid("phrase: abigail@example.com abigail@example.com ;")
    assertInvalid("invalid�char@example.com")
  }
  
  private def assertValid(emails: String*): Unit =
    for (email <- emails) assertTrue("[" + email + "] is a valid email address", validator.isValid(email))
  
  private def assertInvalid(emails: String*): Unit =
    for (email <- emails) assertFalse("[" + email + "] is a invalid email address", validator.isValid(email))
}