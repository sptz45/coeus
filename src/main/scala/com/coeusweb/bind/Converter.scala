/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.bind

import java.util.Locale

/**
 * Aggregate trait for {@code Parser} and {@code Formatter}.
 * 
 * <p>{@code Converter} implementations must be <em>thread-safe</em>.</p>
 */
trait Converter[T] extends Parser[T] with Formatter[T]

/**
 * Converts and formats object values to Strings.
 * 
 * <p>{@code Formatter} implementations must be <em>thread-safe</em>.</p>
 * 
 * @param T the type of objects this {@code Formatter} knows how to format. 
 */
trait Formatter[-T] {
	
	/**
	 * Format the specified value into a String.
	 * 
	 * @param value the value to format into a String
	 * @param locale the {@code Locale} to use for formatting {@code value}
	 * @return a String representation for {@code value} (never {@code null})
	 */
  def format(value: T, locale: Locale): String
}

/**
 * Parse String values into objects.
 * 
 * <p>{@code Parser} implementations must be <em>thread-safe</em>.</p>
 * 
 * @param T the type of objects this {@code Parser} knows how to create. 
 */
trait Parser[+T] {
	
	/**
	 * Converts the specified String into an object of type {@code T}.
	 * 
	 * @param text the String to convert
	 * @param locale the {@code Locale} to use for converting the String
	 * @return a valid {@code T} object (never {@code null})
	 */
  def parse(text: String, locale: Locale): T  
}
