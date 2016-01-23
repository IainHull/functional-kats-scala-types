# Typesafe Wrappers (in Scala)

Primitives like Strings, Ints, and Booleans are excellent at holding values, but in most use cases they are weakly typed. Primitives do not carry any semantics or invariants. For example, a string can store an email address but a string is not an email address. If you store an email address in a simple String, any invariants such as 'is it valid?', have to be assumed or checked every time the value is used. This encourages bugs at worst or bloated and overly defensive code at best.

We can strengthen primitive types by wrapping them with a simple type to encode our semantics and invariants. We verify the invariants only once when we create the value and the compiler proves they are true everywhere we use it.

## The Plan

* We are going to work so some simple Scala code.

* Together we will wrap some primitive types to add meaning and safety to our code.

* Then refactor the code to use our new wrapper types, discovering how we can lean on the compiler for correctness and where we cannot.

* Finally we will explore some more advanced Scala to generalise our wrapper types into something more reusable.

* Time allowing we will learn how to apply type-classes in Scala and the sort of problems they address.

## Development Environment

I am going to use git and IntelliJ IDE for the tutorial. Some of you will have strong opinions about what tools you want to use I will support as many as possible.  There is also a docker image containing the code and a full command-line development environment.

**Please download and configure your development environment before the meet-up. If everyone tries to download this at the same time on the night the wi-fi is going to be very slow.**

### Java

Scala is a JVM language you will need to install the [Java Development Kit](https://www.java.com/en/download/help/index_installing.xml) to follow this tutorial. If installing this for the first time select version 8.  You can also use version 7 if its installed already.


### Git

The source code for this tutorial is available on [github](https://github.com/IainHull/functional-kats-scala-types). Ensure that git is installed on you local machine, there are [detailed instructions here](https://git-scm.com/book/en/v2/Getting-Started-Installing-Git).

Then to download the code use the following command line

```
git clone https://github.com/IainHull/functional-kats-scala-types.git
cd functional-kats-scala-types
```

This repository contains the code for each step of the tutorial on separate branches.  To list branches type

```
git branch
```

To checkout the code for a particular branch.

```
git checkout <branch name>
```

If you cannot use Git you can download the source code for each step as a [zip file here](https://github.com/IainHull/functional-kats-scala-types/releases/download/steps-0-to-5/functional-kats-scala-types.zip).

### IntelliJ IDE

* Download the [IntelliJ IDE from here](https://www.jetbrains.com/idea/download/) (I use the community edition).  
* Then [configure IntelliJ for Scala](https://www.jetbrains.com/idea/help/creating-and-running-your-scala-application.html).
* Import `functional-kats-scala-types` source directory as an SBT project (accept the details).

### SBT

If you are not use IntelliJ you will have to use SBT to continue.

Full details on how to [download and install SBT are here](http://www.scala-sbt.org/0.13/docs/Setup.html). If you do not want to install SBT onto your system follow the [manual instructions](http://www.scala-sbt.org/0.13/docs/Manual-Installation.html) to download and run SBT from a temp directory.

Run SBT from the command-line, then in the SBT shell run `compile`, `test` and `console`.  Console runs the Scala REPL.  To exit the REPL type `:quit`.  To exit the SBT shell type 'exit'

```
$ sbt

> compile

> test

> console

scala> :quit

> exit
```

### Eclipse

[Scala IDE](http://scala-ide.org/) is a preconfigured Eclipse distribution for
Scala development ([download it here](http://scala-ide.org/download/sdk.html)).
Or use their [update site](http://scala-ide.org/download/current.html).

First use `sbt` to create the eclipse project files.  From the command line enter:

```
sbt eclipse
```
Now import the project into an eclipse workspace.

### Docker  

Pull and run the functional-kats-scala-types docker image.  Be warned this image is about 800Mb, **download this in advance**.

```
docker pull iainhull/functional-kats-scala-types
docker run -it iainhull/functional-kats-scala-types
```

Now run sbt from the docker command-line.

```
# sbt
```



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
