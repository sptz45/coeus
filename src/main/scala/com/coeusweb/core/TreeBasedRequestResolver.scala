/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.coeusweb.core

import scala.collection.Map
import scala.collection.mutable.HashMap
import com.coeusweb.util.internal.Strings

/**
 * A <code>RequestResolver</code> that holds the path/handler mappings
 * in a tree-base data structure inspired by RadixTree.
 * 
 * @see RequestResolver
 */
class TreeBasedRequestResolver extends RequestResolver {
  import TreeBasedRequestResolver._

  /* The root node of the tree */
  private val root = new Node(Label("/"))
  
  def register(method: Symbol, path: String, handler: Handler) {
    require(isAbsolutePath(path), "All patterns must begin with /")
    if (path == "/") {
      root.putHandler(method, handler)
    } else {
      if (path == "/*")
        root.putHandler(method, handler)
      for (node <- UriTemplateParser.parse(Strings.stripEndChars(path.substring(1), '/'), method, handler))
        root.addToChildren(node)
    }
  }
  
  def resolve(path: String): (HandlerMap, Map[String, String]) = {
    val variables = new HashMap[String, String]
    var handlers = root.findHandlers(sanitizePath(path), variables)
    if (handlers eq null) {
      handlers = HandlerMap.empty
    }
    (handlers, variables)
  }
  
  private def sanitizePath(path: String) = {
    (if (path.length == 1) path else Strings.stripEndChars(path, '/')).toCharArray
  }
  
  private[core] def nodes = root.nodes - 1 // we don't count the root node
  
  private def isAbsolutePath(path: String) = path(0) == '/'
  
  override def toString = root.deepToString 
}

/**
 * A module with all the classes that support the operation of
 * <code>TreeBasedRequestResolver</code>.
 */
private object TreeBasedRequestResolver {

  /**
   * A class that represents a node in the tree structure. 
   */
  class Node(val label: Label, method: Symbol, handler: Handler) {
  
    val children = new NodeList

    lazy val handlers = new MutableHandlerMap

    if (handler ne null) {
      require(method ne null)
      handlers.put(method, handler)
    }

    def this(label: Label, map: MutableHandlerMap) {
      this(label, null, null)
      handlers.putAll(map)
    }

    def this(label: Label) {
      this(label, null, null)
    }
  
    def putHandler(httpMethod: Symbol, h: Handler) {
      handlers.put(httpMethod, h)
    }
    
    def findHandlers(input: Array[Char], variables: HashMap[String, String]): HandlerMap = {
      if (! label.equalsWithPrefixOf(input)) return null
      if (input.length == label.length) return handlers
      val remainingInput = input.slice(label.length, input.length)
      
      for (child <- children) {
        val childHandlers = child.findHandlers(remainingInput, variables)
        if (childHandlers ne null)
          return childHandlers
      }
      null
    }

    def canBeAddedAsChild(n: Node) = label.startsWith(n.label.chars(0))

    def addToChildren(node: Node) {
      for (child <- children if child.canBeAddedAsChild(node)) {
        child.add(this, node)
        return
      }
      this.children += node
    }

    def add(parent: Node, node: Node) {
      
      def replaceThisWith(newNode: Node) {
        parent.children -= this
        parent.children += newNode
        newNode.children ++= this.children
        newNode.handlers.addMissingHandlers(this.handlers)
      }
      
      def split(n: Node, to: Int): Node = {
        val upper = new Node(n.label.sublabel(0, to), null, null)
        val lower = new Node(n.label.sublabel(to, n.label.length), n.handlers)
        lower.children ++= n.children
        upper.children +=  lower
        upper
      } 
      
      def addAsChild(n: Node) {
        for (child <- this.children if child.canBeAddedAsChild(n)) {
          child.add(this, n)
          return
        }
        this.children += n
      }

      val commonPrefix = this.label.longestMatch(node.label)
      if (commonPrefix == this.label.length)
        if (commonPrefix == node.label.length) {
          if (node.handlers.hasHandlers) replaceThisWith(node)
          else addAsChild(node.children.head)
        } else {
          addAsChild(node.slice(commonPrefix, node.label.length))
        }
      else {
        val commonParent = split(this, commonPrefix)
        parent.children -= this
        parent.children += commonParent
        commonParent.add(parent, node)
      } 
    }

    private def slice(from: Int, to: Int): Node = {
      val newThis = new Node(label.sublabel(from, to), handlers)
      newThis.children ++= this.children
      newThis
    }

    override def toString = "[\"" + label + "\" --> \"" + handlers + "\"]" 

    def deepToString = toString + "\n" + { val sb = new StringBuilder; childrenToString(0, sb); sb.toString }

    private def childrenToString(level: Int, sb: StringBuilder) {
      for (child <- children) {
        for (i <- 0 to level) sb.append(" ")
        sb.append(child.toString).append("\n")
        child.childrenToString(level + 1, sb)
      }
    }
  
    def nodes: Int = (1 /: children) { (sum: Int, child: Node) => sum + child.nodes }
  }


  class WildcardNode(method: Symbol, handler: Handler) extends Node(WildcardLabel, method, handler) {

    def this() { this(null, null) } 
    
    override def findHandlers(input: Array[Char], variables: HashMap[String, String]): HandlerMap = {
      
      def findHandlersForChild(child: Node): HandlerMap = {
        val range = child.label.matchIgnoringPrefix(input)
        if (range eq null)
          return null          
        
        processSkippedPrefix(input, range.head - 1, variables)
            
        if (child.children.isEmpty)
          return child.handlers

        val remainingInput = input.slice(range.head, range.last)
        for (grandchild <- child.children) {
          val grandchildHandlers = grandchild.findHandlers(remainingInput, variables)
          if (grandchildHandlers ne null)
             return grandchildHandlers
        }

        null
      }
      
      for (child <- children) {
        val childHandlers = findHandlersForChild(child)
        if (childHandlers ne null)
          return childHandlers
      }

      processSkippedPrefix(input, input.length, variables)
      handlers
    }

    def processSkippedPrefix(input: Array[Char], to: Int, variables: HashMap[String, String]) { }
  }

  
  class CapturingWildcardNode(var variable: String, method: Symbol, handler: Handler)
    extends WildcardNode(method, handler) {
    
    override def processSkippedPrefix(input: Array[Char], to: Int, variables: HashMap[String, String]) {
      variables(variable) = input.slice(0, to).mkString
    }
  }

  import scala.math.Ordering
  
  /* The reverse of the String ordering of the node's labels. */
  implicit object NodeOrdering extends Ordering[Node] {
    def compare(n1: Node, n2: Node) =
     - n1.label.chars.mkString.compareTo(n2.label.chars.mkString)
  }

  class NodeList extends Iterable[Node] {
    import scala.collection.SortedSet
 
    @volatile
    private[this] var nodes = SortedSet.empty[Node]
  
    def iterator = nodes.iterator
  
    def -=(node: Node) {
      nodes = nodes - node
    }
  
    def +=(node: Node) {
      if (nodes.contains(node)) {
        nodes = nodes - node
      }
      nodes = nodes + node
    }
  
    def ++=(it: Iterable[Node]) {
      for (node <- it) this += node
    }
  }

  /**
   * Parses a Uri template into a tree structrure.
   */
  object UriTemplateParser {
  
    def parse(pattern: String, method: Symbol, handler: Handler): Option[Node] = {
      val iterator = new UriTemplateIterator(pattern, method, handler)
      if (! iterator.hasMoreNodes) return None
      val root = iterator.nextNode
      var node = root
      while (iterator.hasMoreNodes) {
        val tmp = iterator.nextNode
        node.children += tmp
        node = tmp
      }
      Some(root)
    }
  
    class UriTemplateIterator(pattern: String, method: Symbol, handler: Handler) {
      private var pos = 0

      def hasMoreNodes = pos < pattern.length

      def nextNode: Node = {
        if (isWildCard) {
          pos += 1;
          return new WildcardNode(method, handlerForNode)
        }
        if (isCapturingWildcard) {
          val variable = parseVariable
          incrementPositionForVariable(variable)
          return new CapturingWildcardNode(variable, method, handlerForNode)
        }
        val start = pos
        val nextWC = nextWildcardPosition
        pos = if (nextWC >= 0) nextWC else pattern.length
        return new Node(Label(pattern.substring(start, pos)), method, handlerForNode)
      }

      private def nextWildcardPosition = {
        val currentPattern = pattern.substring(pos)
        val star = currentPattern.indexOf('*')
        val curlyBracket = currentPattern.indexOf("{")
        val relPos =  
          (if (curlyBracket == -1) star
           else if (star == -1) curlyBracket
           else if (curlyBracket< star) curlyBracket else star)
        if (relPos == -1) -1 else relPos + pos
      }

      private def isWildCard = pattern(pos) == '*'

      private def remainingChars = pattern.length - pos

      private def charAt(i: Int) = pattern(i + pos)

      private def isCapturingWildcard = remainingChars > 2 && charAt(0) == '{' && pattern.contains('}') 

      private def parseVariable = {
        val currentPattern = pattern.substring(pos)
        currentPattern.substring(1, currentPattern.indexOf('}'))
      }

      private def incrementPositionForVariable(v: String) { pos += (v.length + 2) }

      private def handlerForNode = if (hasMoreNodes) null else handler
    }
  }

  /**
   * The label of a tree node.
   * 
   * @see Node
   */
  class Label(label: String) {
    require((label ne null) && label.length > 0, "You cannot create an empty label")

    val chars = label.toCharArray

    def startsWith(c: Char) = chars(0) == c

    def equalsWithPrefixOf(input: Array[Char]): Boolean = {
      if (input.length < chars.length) return false
      var i  = 0
      while (i < chars.length) {
        if (chars(i) != input(i)) return false
        i += 1
      }
      return true
    }

    def longestMatch(that: Label): Int = {
      var i = 0
      while (i < this.chars.length) {
        if (i == that.length) return i
        if (this.chars(i) != that.chars(i)) return i
        i += 1
      }
      return i
    }

    def matchIgnoringPrefix(input: Array[Char]): Range = {
      var i = 0
      while (i < input.length) {
        if (chars(0) == input(i) && equalsWithPrefixOf(input.slice(i, input.length)))
          return (i + 1) to input.length
          i += 1
      }
      return null
    }

    def sublabel(from: Int, until: Int) = Label(chars.slice(from, until).mkString)

    val length = chars.length

    override def equals(a: Any) = a match {
      case that: Label => this.chars.sameElements(that.chars)
      case _ => false
    }

    override def hashCode = chars.mkString.hashCode

    override def toString = chars.mkString
  }

  private object WildcardLabel extends Label("*")

  private object Label {
    def apply(label: String) = new Label(label)
  }
}
