/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 * 
 * Ported from Apache Jakarta Commons Validator,
 * http://commons.apache.org/validator/
 * 
 * It was originally based on a scipt by
 * <a href="mailto:stamhankar@hotmail.com">Sandeep V. Tamhankar</a>
 * found at http://javascript.internet.com
 *
 * Author: Spiros Tzavellas
 */
package com.tzavellas.coeus.validation.vspec.constraint

import com.tzavellas.coeus.bind.Error

/**
 * Perform email validations.
 * 
 */
class EmailConstraint extends Constraint[String] {
  import EmailConstraint._
  
  def isValid(email: String): Boolean = {
    email == null || (allCharactersAreLegal(email) && isValidEmail(email))
  }
  
  def getError(targetClass: Class[_], field: String, value: String) = 
    Error.validationFailure("email.invalid", field, targetClass, value)
  
  private def isValidEmail(email: String) = email.trim match {
    case Email(localPart, domain) => hasValidLocalPart(localPart) && hasValidDomain(domain)
    case _ => false
  }
  
  private def allCharactersAreLegal(email: String) = LegalAscii.findFirstIn(email) != None
  
  private def hasValidLocalPart(localPart: String) = LocalPart.pattern.matcher(localPart).matches
  
  private def hasValidDomain(domain: String): Boolean = {
    var filtered = domain
    if (filtered(0) == '[') filtered = filtered.substring(1)
    if (filtered(filtered.length -1 ) == ']') filtered = filtered.substring(0, filtered.length - 1)
    inetAddrValidator.isValid(filtered)
  }
  
  private val inetAddrValidator = new InternetAddressConstraint
}

private object EmailConstraint {
  
  private val SpecialChars = """[\000-\037]\(\)<>@,;:'\\"\.\[\]\0177""";
  private val ValidChars   = "[^\\s" + SpecialChars + "]";
  private val Atom = ValidChars + "+"
  private val QuotedUser   = """("[^"]*")""";
  private val Word         = "((" + ValidChars + "|')+|" + QuotedUser + ")";
  
  val LegalAscii = """^[\0000-\0177]+$""".r
  
  val Email = """^(.+)@(.+)[^\\.]*$""".r
  
  val LocalPart    = ("^\\s*" + Word + "(\\." + Word + ")*$").r
}
