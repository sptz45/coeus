/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.bind.converter

import com.coeusweb.bind.Converter

/**
 * Contains various helper methods for implementing {@code Converter} classes.
 */
abstract class AbstractConverter[T] extends Converter[T]{
  
  /**
   * Parse the input {@code text} by filtering first if the {@code text}
   * is empty.
   * 
   * @param text the text to parse
   * @param default the value to return if the {@code text} is empty
   * @param parser the parser to use for parsing the text
   */
  def filterEmpty(text: String, default: T, parser: String => T): T = {
    if (text eq null) default
    else {
      val trimmed = text.trim
      if (trimmed.isEmpty) default else parser(trimmed)
    }
  }
}
