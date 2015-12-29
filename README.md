# Typesafe Wrappers (in Scala)

Primitives like Strings, Ints, and Booleans are excellent at holding values, but in most use cases they are weakly typed. Primitives do not carry any semantics or invariants. For example, a string can store an email address but a string is not an email address. If you store an email address in a simple String, any invariants such as 'is it valid?', have to be assumed or checked every time the value is used. This encourages bugs at worst or bloated and overly defensive code at best.

We can strengthen primitive types by wrapping them with a simple type to encode our semantics and invariants. We verify the invariants only once when we create the value and the compiler proves they are true everywhere we use it.

## The Plan

* We are going to work so some simple Scala code.

* Together we will wrap some primitive types to add meaning and safety to our code.

* Then refactor the code to use our new wrapper types, discovering how we can lean on the compiler for correctness and where we cannot.

* Finally we will explore some more advanced scala to generalise our wrapper types into something more reuseable.

* Time allowing we will learn how to apply type-classes in Scala and the sort of problems they address.

## FAQ

#### Do I need to know Scala?

Absolutely not. Experience with any object oriented or functional language will do.  I will introduce all the scala features as we use them.

#### How functional is this?

Using types for correctness is common functional technique, but it can just as easily be applied to object oriented code.  The code we will be working on will be very simple, but time allowing we will explore more advanced functional concepts like type-classes.

#### What should I bring?

A laptop and a sense of adventure.

#### What tools will we use?

The source code we will hack on will be provided in a github repo.

We will use sbt to build and test our code on the command line (as a back up a gradle build will also be included which can download itself when first run)

You can use an IDE (eclipse or intellij) or an editor of your choice and the command line

To save time more detailed instructions will be provided before the workshop.

## More information

This workshop will be based on the following blog post and talk

* http://workday.github.io/scala/2015/02/05/scala-typesafe-wrappers/

* https://www.parleys.com/play/improving-correctness-with-types

* http://workday.github.io/2015/12/03/gotocon-berlin-improving-correctness-with-types/




# License

All code and documentation is licensed under the [Creative Commons Attribution 3.0 Unported License](https://creativecommons.org/licenses/by/3.0/).  See LICENSE.md for more information.
