/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 * 
 * Ported from Apache Jakarta Commons Validator,
 * http://commons.apache.org/validator/
 * 
 * It was originally based in on php script by Debbie
 * Dyer, validation.php v1.2b, Date: 03/07/02,
 * http://javascript.internet.com.
 *
 * Author: Spiros Tzavellas
 */
package com.tzavellas.coeus.validation.vspec.constraint

import com.tzavellas.coeus.bind.Error

/**
 * Validates URLs.
 * 
 * @see <a href='http://www.ietf.org/rfc/rfc2396.txt'>RFC2396 Uniform Resource Identifiers</a>
 */
class UrlConstraint(val allowedSchemes: String*) extends Constraint[String] {
  import UriConstraint._

  val inetAddrValidator = new InternetAddressConstraint
  
  /**
   * Checks if the specified String is a valid URL.
   */
  def isValid(url: String) = url match {
    case null => true
    case _ if hasIllegalCharacters(url) => false
    case Url(_, scheme, _, authority, path, _, query, _, fragment) => 
      isValidScheme(scheme) && isValidAuthority(authority) && isValidPath(path) && isValidQuery(query) && isValidFragment(fragment)
    case _ => false
  }
  
  def getError(targetClass: Class[_], field: String, value: String) = 
    Error.validationFailure("url.invalid", field, targetClass, value)
  
  private def hasIllegalCharacters(url: String) = LegalAscii.findFirstIn(url) == None
  
  private def isValidScheme(scheme: String): Boolean = {
    if (scheme == null) return false
    if (allowedSchemes.isEmpty) Scheme.findFirstIn(scheme) != None
    else allowedSchemes.contains(scheme)
  }
  
  /*
   * Returns true if the authority is properly formatted.  An authority is
   * the combination of hostname and port.
   */
  private def isValidAuthority(authority: String): Boolean = {
    def isValidAddress(addr: String): Boolean = inetAddrValidator.isValid(addr)
    
    def isValidPort(port: String): Boolean = if (port == null || port == "") true else 0 to 65535 contains port.toInt
      
    def isValidExtra(extra: String): Boolean = extra.trim() == ""
    
    authority match {
      case Authority(address, _, port, extra) => 
        isValidAddress(address) && isValidPort(port) && isValidExtra(extra)
      case _ => false
    }
  }
  
  private def isValidPath(path: String): Boolean = {
    def countToken(token: String, target: String): Int = {
        var tokenIndex = 0
        var count = 0
        while (tokenIndex != -1) {
            tokenIndex = target.indexOf(token, tokenIndex);
            if (tokenIndex > -1) {
                tokenIndex += 1
                count += 1
            }
        }
        return count
    }
    
    if (Path.findFirstIn(path) == None) return false
    if (path.contains("//")) return false
    
    val slashes = countToken("/", path)
    val doubleSlashes = countToken("/", path)
    val dots = countToken("..", path)
    if (dots > 0 && (slashes - doubleSlashes- 1) <= dots) return false
    
    return true
  }
  
  private def isValidQuery(query: String): Boolean = if (query != null) Query.findFirstIn(query) != None else true
  
  private def isValidFragment(fragment: String): Boolean = true
}


private object UriConstraint {
  
  val LegalAscii = """\p{ASCII}+""".r
  
  /** This expression derived/taken from the BNF for URI (RFC2396). */
  val Url = """^(([^:/?#]+):)?(//([^/?#]*))?([^?#]*)(\?([^#]*))?(#(.*))?""".r
  
  /** Protocol (ie. http:, ftp:,https:) */
  val Scheme = """^\p{Alpha}\p{Alnum}*$""".r
  
  val Authority = """^([a-zA-Z\d\-\.]*)(:(\d*))?(.*)?""".r
  
  val Ipv4 = """^(\d{1,3})[.](\d{1,3})[.](\d{1,3})[.](\d{1,3})$""".r
  
  private val atom = """[^\s;/@&=,.?:+$]+"""
  
  val Domain = ("^" + atom + "(\\." + atom + ")*$").r

  val Tld = """^\p{Alpha}\p{Alnum}{1,3}""".r
  
  val Path = """^(/[-\w:@&?=+,.!/~*'%$_;]*)?$""".r
  
  val Query = "^(.*)$".r
}
