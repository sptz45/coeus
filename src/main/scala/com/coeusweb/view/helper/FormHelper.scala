/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.view.helper

import scala.xml.{ Elem, Text }
import com.coeusweb.bind.{ BindingResult, ErrorFormatter }
import com.coeusweb.controller.ModelAttributes
import com.coeusweb.i18n.msg.MessageBundle
import com.coeusweb.http.security.CsrfProtection

/**
 * Methods for generating HTML tags related to forms.
 */
trait FormHelper {
  
  private[this] val EmptyElem = Text("")
  
  /** Contains the i18n messages. */
  val messages: MessageBundle
  
  /** Formats the error messages of validators. */
  val errorFormatter: ErrorFormatter
  
  
  /**
   * Returns a {@code span} element that contains the error for the specified field or
   * an empty XML node if the field has no error.
   * 
   * <p>The XML returned has the following format:
   * {@literal <span class="error">error-message</span>}.</p>
   * 
   * @param field the name of the field
   */
  def error(field: String)(implicit scopes: ScopeAccessor) = {
    bindingResult.error(field) match {
      case None      => EmptyElem
      case Some(err) =>
        <span class="error">{ errorFormatter.format(err, scopes.request.locale) }</span>
    }
  }
  
  private def labelMessage(field: String, code: String, scopes: ScopeAccessor) = {
    val request = scopes.request
    val msgCode = if (code ne null) code else field
    if (ModelAttributes.containsModelAttribute(request)) {
      val modelName= ModelAttributes.getModelAttributeName(scopes.request)
      messages(scopes.request.locale, modelName + "." + msgCode)
    } else {
      messages(scopes.request.locale, msgCode)
    }
  }

  /**
   * Generates an HTML {@code label} tag for the specified field.
   * 
   * <p>The XML returned has the following format:
   * {@literal <label for="field">field-message</label>}.</p>
   * 
   * @param field the name of the field
   * @param code  an i18n message code to use for looking up the label's text or
   *              {@code null} to use the field's name as a i18n message code.
   */
  def label(field: String, code: String = null)(implicit scopes: ScopeAccessor) = {
    <label for={field}>{ labelMessage(field, code, scopes) }</label>
  }
  
  /**
   * Generates a {@code label} tag for the specified field that may also contain a {@code span}
   * element with the error of the specified field.
   * 
   * <p>The XML returned has the following format:<br/>
   * {@literal <label for="field">field-message <span class="error">error-message</span></label>}.</p>
   * 
   * @param field the name of the field
   * @param code  an i18n message code to use for looking up the label's text or
   *              {@code null} to use the field's name as a i18n message code.
   * 
   * @see {@link #label}
   * @see {@link #error}
   */
  def label_and_error(field: String, code: String = null)(implicit scopes: ScopeAccessor) = {
    <label for={field}>{ labelMessage(field, code, scopes) } { error(field) }</label>
  }

  /**
   * Returns the specified String only if the current {@code BindingResult} has
   * errors.
   * 
   * @param body the String to return is the {@code BindingResult} has errors
   */
  def show_on_error(body: => String)(implicit scopes: ScopeAccessor): String = {
    if (bindingResult.hasErrors) body else ""
  }

  /**
   * Changes the "modelAttibute" request attribute to the specified value.
   * 
   * <p>This method is useful in pages that have more than one form.</p>
   */
  def change_model_name_to(newName: String)(implicit scopes: ScopeAccessor) {
    ModelAttributes.setModelAttributeName(newName, scopes.request)
  }
  
  def checkbox(field: String, value: Any, checkedValue: String = "true", uncheckedValue: String = "false") = {
    val isChecked = value.toString == checkedValue
    val checkedAttr = if (isChecked) Some("checked") else None
    <xml:group>
    <input type="checkbox" name={field} value={checkedValue} checked={checkedAttr.map(c => Seq(Text(c)))} />
    <input type="hidden" name={field} value={uncheckedValue} />
    </xml:group>
  }
  
  def format_field(field: Symbol)(implicit scopes: ScopeAccessor): String = {
    format_field(field.toString)(scopes)
  }
  
  def format_field(field: String)(implicit scopes: ScopeAccessor): String = {
    bindingResult.format(field, scopes.request.locale)
  }
  
  def format_value(value: Any)(implicit scopes: ScopeAccessor): String = {
    bindingResult.formatValue(value, scopes.request.locale)
  }
  
  /**
   * Creates a hidden input tag that contains the CSRF protection token.
   */
  def csrf_token(implicit scopes: ScopeAccessor) = {
    <input type="hidden" name={CsrfProtection.tokenName} value={CsrfProtection.getToken(scopes.session)}/>
  }
  
  def csrf_meta_tags(implicit scopes: ScopeAccessor) = {
    <xml:group>
      <meta name="csrf-param" content={CsrfProtection.tokenName}/>
      <meta name="csrf-token" content={CsrfProtection.getToken(scopes.session)}/>
    </xml:group>
  }
  
  def action_link(href: String, text: String, attrs: (Symbol, String)* ) = {
    val a = new java.lang.StringBuilder("a href=\"")
    a.append(href).append("\" rel=\"nofollow\"")
    for ((attr, value) <- attrs) {
      val name = attr.name match {
        case "method"       => "data-method"
        case "remote"       => "data-remote"
        case "confirm"      => "data-confirm"
        case "disable-with" => "data-disable-with"
        case name           => name
      }
      a.append(" ").append(name).append("=\"").append(value).append("\"")
    }
    a.append(">")
    a.append(text)
    a.append("</a>")
    a.toString
  }
  
  private def bindingResult(implicit s: ScopeAccessor) = ModelAttributes.getBindingResult(s.request)
}