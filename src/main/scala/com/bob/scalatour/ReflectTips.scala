package com.bob.scalatour

import scala.reflect.runtime.{universe => ru}

case class Person(name: String)

case class Purchase(name: String, orderNumber: Int, var shipped: Boolean)

/**
 * Scala runtime types carry along all type info from compile-time, avoiding these types mismatches between compile-time and run-time.
 * Environment,All reflection tasks require a proper environment to be set up --> run time or compile time
 * Universe is the entry point to Scala reflection. A universe provides an interface to all the principal concepts used in reflection, such as Types, Trees, and Annotations.
 * Mirrors,All information provided by reflection is made accessible through these so-called mirrors.
 *
 * ReflectiveMirror --> used to loading symbols by name and an entry point into invoker mirrors
 * InstanceMirror --> used for creating invoker mirrors for methods and fields and for inner classes and inner objects(modules)
 * MethodMirror --> used for invoking instance methods
 * FieldMirror --> used for getting/setting instance fields
 * ClassMirror --> used for creating invoker mirrors for constructors
 * ModuleMirror --> used for accessing instances of singleton objects
 *
 * Symbols --> used to establish bindings between a name and the entity it refers to(a class or a method)
 * --> TypeSymbols, represents a type,class and trait declarations,as well as type parameters
 * --> TermSymbols, representing val,var,def and object declarations as well as packages and value parameters
 * --> FreeTermSymbol/FreeTypeSymbol
 */
object ReflectTips {

  def main(args: Array[String]) {

    val l = List(1, 2, 3)
    val theTypeTag = getTypeTag(l)
    val theType: ru.Type = theTypeTag.tpe
    // using decls to get the method in this type
    theType.decls.take(10).foreach(println)

    // obtain a mirror m which makes all classes and types available that are loaded by the current classloader, including class Person.
    val m = ru.runtimeMirror(getClass.getClassLoader)
    // obtaining a ClassMirror for class Person using the reflectClassmethod. The ClassMirror provides access to the constructor of class Person.
    val classPerson = ru.typeOf[Person].typeSymbol.asClass
    val cm = m.reflectClass(classPerson)
    val ctor = ru.typeOf[Person].decl(ru.termNames.CONSTRUCTOR).asMethod
    val ctorm = cm.reflectConstructor(ctor)
    // here we make a peson
    val ap = ctorm("Mike")
    println(ap)

    val p = Purchase("Jeff Lebowski", 23819, false)
    val mp = ru.runtimeMirror(p.getClass.getClassLoader)
    val shippingTermSymb = ru.typeOf[Purchase].decl(ru.TermName("shipped")).asTerm
    val im = mp.reflect(p)
    val shippingFieldMirror = im.reflectField(shippingTermSymb)
    println(shippingFieldMirror.get) // false
    shippingFieldMirror.set(true) // here we change the shipped property
    println(shippingFieldMirror.get)

  }

  def getTypeTag[T: ru.TypeTag](obj: T) = ru.typeTag[T]

}