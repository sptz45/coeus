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

import com.tzavellas.coeus.bind.Error

/**
 * A class for validating 10 digit ISBN codes.
 * 
 * <p>Based on this 
 * <a href="http://www.isbn.org/standards/home/isbn/international/html/usm4.htm">algorithm</a>.</p>
 */
class IsbnConstraint extends Constraint[String] {
  import IsbnConstraint._
  
  /**
   * Validates if the specified <code>String</code> is a valid ISBN number.
   * 
   * </p>If the ISBN is formatted with space or dash separators its format is
   * validated. Then the digits in the number are weighted, summed, and
   * divided by 11 according to the ISBN algorithm.  If the result is zero,
   * the ISBN is valid. This method accepts formatted or raw ISBN codes.</p>
   *
   * @param isbn Candidate ISBN number to be validated. <code>null</code> is
   *        considered valid.
   * @return true if the string is a valid ISBN code.
   */
  def isValid(isbn: String) = isbn match {
    case null                        => true
    case _ if hasInvalidLength(isbn) => false
    case _ if isFormatted(isbn)      => hasValidFormat(isbn)
    case _                           => cleanAndValidate(isbn)
  }
  
  def getError(targetClass: Class[_], field: String, value: String) = 
    Error.validationFailure("isbn.invalid", field, targetClass, value)
  
  private def hasInvalidLength(value: String) = value.length < 10 || value.length > 13
  
  /* Returns true if the ISBN contains one of the separator characters space or dash. */
  private def isFormatted(isbn: String) = isbn.contains("-") || isbn.contains(" ")
  
  /* Returns true if the ISBN is formatted properly. */
  private def hasValidFormat(isbn: String) = pattern.findFirstIn(isbn) != None
  
  /* First remove all characters that are non digit and not equal to 'X' and then validate the resulting ISBN. */
  private def cleanAndValidate(value: String) = {
    /*
     * Returns the numeric value represented by the character. Based on the ISBN validation
     * algorithm, if the character is not a digit but an 'X', 10 is returned.
     */
    def toInt(ch: Char): Int = if (ch == 'X') 10 else ch.asDigit
  
    /* Remove all characters that are non digit and not equal to 'X' */
    def clean(isbn: String): String = isbn.toUpperCase.filter(c => c.isDigit || c == 'X').mkString
  
    /* Returns the sum of the weighted ISBN characters. */
    def sum(isbn: String): Int = {
      var total = 0
      for (i <- 0 until 9) {
        val weight = 10 - i
        total += (weight * toInt(isbn(i)))
      }
      total += toInt(isbn.charAt(9)) // add check digit
      total
    }
    
    val cleanedIsbn = clean(value)
    (cleanedIsbn.length() == 10) && ((sum(cleanedIsbn) % 11) == 0)
  }
}

/*
 * ISBN consists of 4 groups of numbers separated by either dashes (-)
 * or spaces.  The first group is 1-5 characters, second 1-7, third 1-6,
 * and fourth is 1 digit or an X or an x.
 */
private object IsbnConstraint {
  val pattern = """^(\d{1,5})(\-|\s)(\d{1,7})(\-|\s)(\d{1,6})(\-|\s)([0-9Xx])$""".r
  // START + GROUP + SEP + PUBLISHER + SEP + TITLE + SEP + CHECK + END
}
