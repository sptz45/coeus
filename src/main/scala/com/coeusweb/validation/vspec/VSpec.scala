/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.validation
package vspec

import com.coeusweb.bind.{ BindingResult, Error, ExpressionLanguage => EL }
import constraint.Constraint

/**
 * The default <code>Validator</code> implementation.
 *
 * <p>A VSpec instance validates objects of a particular class using a set
 * of specified constraints. To validate an object you first have to create
 * a VSpec instance specifying the type of object that you want to validate.
 * Then you have to use one of the <code>ensure(..)</code> methods to specify
 * any constraints that the fields of a valid object must satisfy and finally
 * call any of the methods of the <code>Validator</code> trait.</p>
 * 
 * <p>For example:</p>
 * <pre>
 * import com.coeusweb.validator.VSpec
 * import com.coeusweb.validator.Constraints._
 *
 * val spiros = new User("spiros", "spiros@example.com")
 * val validator = new VSpec[User]
 * validator.ensure("username", hasText)
 * validator.ensure("email",    hasText, isEmail)
 * val errors = validator.validate(spiros)
 * </pre>
 * 
 * <p>The above code creates a validator that validates objects of the class
 * <code>User</code>. It also specifies that the "username" var should not be
 * empty and that the "email" var should not be empty and that it should also be a
 * valid email address. The methods {@link com.coeusweb.validation.constraint.StringConstraints#hasText hasText}
 * and {@link com.coeusweb.validation.constraint.StringConstraints#isEmail isEmail} are defined in the
 * {@link Constraints} object and return {@link com.coeusweb.validation.constraint.Constraint Constraint}
 * instances.</p>
 * 
 * <h4>Simplified Syntax</h4>
 * 
 * <p>To remove some of the syntactic noise in the above example we could define the
 * user validator using the following ways:</p>
 * <pre>
 * val validator = new VSpec[User] {
 *   ensure("username", hasText)
 *   ensure("email",    hasText, isEmail)
 * }
 * </pre>
 * 
 * <p>or using an object:</p>
 * <pre>
 * object validator extends VSpec[User] {
 *   ensure("username", hasText)
 *   ensure("email",    hasText, isEmail)
 * }
 * </pre>
 * 
 * <p>Another alternative way is when you don't want to do a static import on the
 * {@link Constraints} object there is a {@link Constraints} trait that you can mix into
 * your validators instead:</p>
 * <pre>
 * object validator extends VSpec[User] with Constraints {
 *   ensure("username", hasText)
 *   ensure("email",    hasText, isEmail)
 * }
 * </pre>
 * 
 * <h4>Validating Associations</h4>
 * <p>There are two ways to validate associations, using expressions with the
 * association name and re-using an existing validator for the object in the
 * association.</p>
 * Using expressions:
 * <pre>
 * val postValidator = new VSpec[Post] {
 *   ensure("author.username", isNotNull)
 *   ensure("author.email",    hasText, isEmail)
 * }
 * </pre>
 * Re-using a validator:
 * <pre>
 * val postValidator = new VSpec[Post] {
 *   ensure("title",  isNotNull, hasText)
 *   ensure("author", validatesWith(userValidator))
 * }
 * </pre>
 * 
 * <h4>Special handling of <code>Option[T]</code> fields</h4>
 * 
 * <p>When a field is of type <code>Option[T]</code> then it is considered as
 * optional and the value of <code>None</code> is always considered valid.
 * Furthermore, if a field value is of type <code>Some[T]</code> then it gets
 * extracted so that the predefined <code>Constraint</code> classes can be used
 * for validating the field's value.</p>
 * 
 * <p>For example in the below code we use the <code>NumberConstraints.isGreatedThan</code>
 * constraint that is defined for <code>Int</code> to validate the values of the
 * <code>age</code> field (that is of type <code>Option[Int]</code>) since if the
 * value of that field is <code>None</code> then it will automatically considered
 * valid and if it is of type <code>Some[Int]</code> then the <code>Int</code> will
 * get extracted before it gets passed in the specified constraints.</p>
 * 
 * <pre>
 * class Person(var name: String, var age: Option[Int])
 * val validator = new VSpec[Person] {
 *   ensure("name", hasText)
 *   ensure("age",  isGreaterThan(0))
 * }
 * </pre> 
 * 
 * <h4>Thread-safety</h4>
 * <p>VSpec instances can be safely accessed by multiple threads only after
 * they have been configured with all the needed constraints. This works well
 * because the constraints are usually added inside the constructor (when using
 * the simplified syntax) and the object after the constructor invocation can
 * be considered "effectively immutable".</p> 
 *
 * @see ExpressionLanguage
 * @see Constraints
 * @see Constraint
 */
class VSpec[-T <: AnyRef](implicit m: Manifest[T]) extends Validator[T] {
  
  private val targetClass: Class[_] = m.erasure
  private var constraintsMap = Map[String, List[Constraint[_]]]()
  private var associationValidators = Map[String, List[VSpec[_]]]()
  
  val errorFormatter = VSpecErrorFormatter
  
  def validate(target: T): Iterable[Error] = {
    val result = new BindingResult(null, target)
    validate(result)
    result.errors
  }
  
  def validate(result: BindingResult[T]) {
    result.errorFormatter = errorFormatter
    doValidate(null, result.target, result)
    extraValidation(result)
  }
  
  def validateField[F](field: String, value: Any)(implicit m: Manifest[F]) = {
    constraintsMap.get(field) match {
      case None => None
      case Some(constraints) => doValidateField(null, field, value, constraints)
    }
  }
  
  /**
   * Subclasses can override this method to specify validation rules
   * that cannot be easily expressed using <code>Constraint</code>s.
   * 
   * <p>One frequent use case for this method is when a validation rule
   * must know the value of more than one field in the target object.</p>
   * 
   * @param result a <code>BindingResult</code> that contains the target
   *               object and any validation errors that have already
   *               occurred
   */
  def extraValidation(result: BindingResult[T]) { }
  
  
  private def doValidate(path: String, target: T, result: BindingResult[T]) {
    
    for ((field, constraints) <- constraintsMap) {
      val potentialError = doValidateField(path, field, EL.eval(target, field, false), constraints)
      for (error <- potentialError) result.addError(field, error)
    }
    
    for ((field, validators) <- associationValidators; validator <- validators) {
      val assocValidator = validator.asInstanceOf[VSpec[AnyRef]]
      assocValidator.doValidate(path, EL.evalTo[AnyRef](target, field), result)
    }
  } 
  
  
  private def doValidateField(path: String, field: String, value: Any, constraints: Seq[Constraint[_]]): Option[Error] = {

    def getField = if (path eq null) field else path + "." + field
    
    if (value == None) return None
    
    val fieldValue = value match {
      case Some(x) => x
      case _       => value
    }
    
    for (cons <- constraints) {
      val constraint = cons.asInstanceOf[Constraint[Any]] 
      if (! constraint.isValid(fieldValue))
        return Some(constraint.getError(targetClass, getField, fieldValue))
    }
    None
  }
  
  /**
   * Ensure that the specified field satisfies all the specified constraints.
   * 
   * @param field the field to validate
   * @param constraints the constraints 
   */
  final def ensure[B](field: String, constraints: Constraint[B]*) {
    constraintsMap =
      if (constraintsMap.contains(field)) {
        constraintsMap + (field -> (constraintsMap(field) ::: constraints.toList))
      } else { 
        constraintsMap + (field -> constraints.toList)
      }
  }
  
  /**
   * Ensures that the specified field of the target object will validate
   * against the specified validator.
   * 
   * @param field the field to validate
   * @param validator the validator of the field
   */
  final def ensure[C <: AnyRef](field: String, validator: VSpec[C]) {
    associationValidators =
      if (associationValidators.contains(field)) {
        associationValidators + (field -> (associationValidators(field) ::: List(validator)))
      } else { 
        associationValidators + (field -> List(validator))
      }
  }
  
  /**
   * Returns the specified VSpec object.
   * 
   * <p>This method is used as syntactic sugar in conjunction with the
   * {@link VSpec#ensure(String, VSpec)} method. Users instead of writing:</p>
   * <pre>
   * val postValidator = new VSpec[Post] {
   *   ensure("author", userValidator)
   * }
   * </pre>
   * <p>can use this method to enhance the readability of their code like in the
   * following code:</p>
   * <pre>
   * val postValidator = new VSpec[Post] {
   *   ensure("author", validatesWith(userValidator))
   * }
   * </pre>
   */
  final def validatesWith[C <: AnyRef](validator: VSpec[C]) = validator
}
