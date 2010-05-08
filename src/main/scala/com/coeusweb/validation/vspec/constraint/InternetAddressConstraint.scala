/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.validation.vspec.constraint

import com.coeusweb.bind.Error

class InternetAddressConstraint extends Constraint[String] {
  import InternetAdderssConstraint._
  
  def isValid(addr: String) = addr match {
    case null                 => true
    case Ipv4(a, b, c, d)     => areAllIpSegmentsValid(a, b, c, d)
    case Domain(domain, null) => isValidDomain(domain)
    case Domain(domain, tld)  => isValidDomain(domain) && isValidTld(tld.substring(1))
    case _                    => false
  }
  
  def getError(targetClass: Class[_], field: String, value: String) = 
    Error.validationFailure("internet.address.invalid", field, targetClass, value)
  
  private def areAllIpSegmentsValid(segments: String*) = {
    val validRange = 0 to 255
    segments.forall(validRange contains _.toInt)
  }
  
  private def isValidDomain(domain: String) =
    domain.split("\\.").forall(DomainSegment.findFirstIn(_) != None) 
   
  private def isValidTld(tld: String) = Tld.findFirstIn(tld) != None
}


private object InternetAdderssConstraint {
  
  private val atom = """[^\s;/@&=,.?:+$]+"""
  
  val Ipv4 = """^(\d{1,3})[.](\d{1,3})[.](\d{1,3})[.](\d{1,3})$""".r
  
  val Domain = ("^(" + atom + "(\\." + atom + ")*)$").r

  val Tld = """^\p{Alpha}\p{Alnum}{1,3}$""".r
  
  val DomainSegment = """^\p{Alnum}([\p{Alnum}-]*\p{Alnum})*$""".r
}
