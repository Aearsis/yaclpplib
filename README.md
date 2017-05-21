# yaclpplib
[![Build Status](https://travis-ci.org/Aearsis/yaclpplib.svg?branch=master)](https://travis-ci.org/Aearsis/yaclpplib)

Yet another command-line parameters parser library.

# What's that?

A library for parsing command line options and parameters. The main goals are ease of use and flexibility.

There are no dependencies, you can use it with any project using Java&trade; 8.

Originally a school assignment, currently WIP, including this readme. Stay tuned.

# Quick start
```java
class Main {
    class MyOptions implements Options {
        @Option("--verbose")
        boolean verbose;
    }

    public static void main(String[] args) {
        ArgumentParser parser = ArgumentParserFactory.create();
        final MyOptions options = parser.addOptions(new MyOptions());
        
        parser.parse(args);
        
        if (options.verbose) {
            // do something
        }
    }
}
```

As simple as that:

1) You create a class, annotate its fields and methods with @Option annotation.
2) Obtain an instance of ArgumentParser from the factory, set it up with your MyOptions instance.
3) Call the parse method, which magically fills the fields up.
4) Use the fields as you want!

# Features

We think the best way to demonstrate options is by examples. What can the library do for you:

## Basic long options

```java
class MyOptions implements Options {
    
    // We know all basic datatypes:
    
    @Option("--int")
    int intField;
    
    @Option("--float")
    float floatField;
    
    @Option("--string")
    String stringField;
    
    @Option("--boolean")    
    boolean boolField;
}
```

An example of valid arguments doing what would you expect:

```bash
$ ./example --int=42 --float=3.1415 --string="A string" --boolean=true
```

## Short options

```java
class MyOptions implements Options {
    
    // We know also short options:
    @Option("-s")
    int shortOption;
    
    @Option("-D")
    String anotherShortOption;
}
```

These two types of options behave as in this example, similarly to GNU getopt:

```bash
$ ./exec -s 42 -DKey=Value
```

## Positional arguments

Of course, options are not everything what makes a command line arguments. Working with positional arguments is very simple:

```java
class Main {
    public static void main(String[] args) {
        ArgumentParser parser = ArgumentParserFactory.create();
        List<String> positional = parser.requestPositionalArguments();
        
        parser.parse(args);
        
        for (String arg : positional) {
            // do something
        }
    }
}
```

Positional arguments can of course be used together with options. The library will do its best with matching options with their values, and returning the rest.

## Aliases

You can specify more @Option annotations at once.

```java
class MyOptions implements Options {
    @Option("-n")
    @Option("--count")
    int count;
}
```

Now, these two are equivalent:

```bash
$ ./exec -n 42
$ ./exec --count=42
```

Note that you are not limited to one short and one long option, but be considerate to your users.

## Using an option multiple times

When you want the user to be able to specify an option multiple times, just declare it as an array.

```java
class MyOptions implements Options {
    @Option("-D")
    String[] defs;
}
```

```bash
$ ./exec -D alfa=a -D beta=b
```

If you don't make the option an array, the last option on the command line will be used. This behavior is similar to other implementations.

## Default options

These are simply working. When you set a value before calling parse, the library won't touch it, if it's not specified on the command line.

Please note that this does not hold true for arrays - empty arrays are also considered a value on the command line.

## More flexibility - methods

```java
class MyOptions implements Options {
    @Option("--usage")
    void printUsage() {
        System.err.println("Usage: ./exec");
        System.exit(0);
    }
}
```

Now, when you call it:
```bash
$ ./exec --usage
Usage: ./exec
$
```

Methods can also have parameters:

```java
class MyOptions implements Options {
    private File file;
    
    @Option("--file")
    void setFile(String filename) {
        // check that filename exists
        // set this.file
    }
}
```

But beware: do not access other fields annotated with @Option. The order of evaluating options is not defined.
For example, this may generally not work, and there is no way for the library to recognize it:

```java
class MyOptions implements Options {
    @Option("--verbosity")
    int verbosity;
    
    @Option("--debug")
    void setDebugVerbosity() {
        verbosity = 10;
    }
}
```

Instead, you should use boolean fields and compute the outcome yourself. Which brings us to...

## Calling methods after parsing

Often you want to check some postconditions or to resolve some conflicts. To correct the previous example:

```java
class MyOptions implements Options {
    
    @Option("--verbosity")
    private int verbosityArg = -1;
    
    @Option("--debug")
    private boolean debugArg = false;
    
    public int verbosity;
    
    @AfterParse
    void resolveVerbosity() {
        if (debugArg) {
            if (verbosityArg != -1)
                throw new IllegalOptionValue("You may specify either --debug or --verbosity=X, not both.");
            
            verbosity = 10;
        }
        else if (verbosityArg != -1) {
            verbosity = verbosityArg;
        }
        else {
            verbosity = 0;
        }
    }
}
```

Two things to note:

1) The library needs access to your fields and methods. It will use Java&trade; power to access private fields, so you usually do not have to worry. But when you configure the SecurityManager, the access can be denied, then the library will fail.
2) In this example, we throw an IllegalOptionValue. This is a prepared exception which you will use the most. But generally, you are allowed to throw any RuntimeException subclass. You are expected to catch it and handle them yourselves.

You can use these methods to check postconditions, do some postprocessing or whatever you want. It is generally not recommended to write any "business" logic there, as it makes your code unreadable.

## Exceptions thrown by the library

It may happen, that the library will complain about some conditions. There are several exceptions the library will throw:

<dl>
<dt>InvalidSetupError</dt>
<dd>This exception will be thrown everytime the library recognizes the setup as invalid, even before calling the parse method. Cases include for example using the same option name for two fields, unknown type of field (or method argument), methods with unexpected arguments or so. You should generally not handle it, but correct the setup instead.</dd>
<dt>InvalidOptionValue</dt>
<dd>Whenever a user supplied value cannot be parsed into the given type of field. For example, if you give "forty-two" into an int field. When you care, you should catch it and print a user-friendly notice.</dd>
<dt>IllegalOptionValue</dt>
<dd>When the value is valid for given type, but fails to meet some other conditions.</dd>
<dt>MissingMandatoryOptionException</dt>
<dd>When an option annotated by @Mandatory is missing. See below.</dd>
<dt>InternalError</dt>
<dd>Some preconditions that have been checked now do not hold true, or some other serious error happened. If you mangle with internals, or use reflection to do weird things, check if you don't interfere with the library. Otherwise, submit a bug report!
</dl>

## Optional values

Sometimes you want to use one option both without value and with it. To give an example, the verbosity level:

```bash
$ ./exec --debug
$ ./exec --debug=10
```

This is something you can achieve with yaclpplib. How?

```java
class MyOptions implements Options {
    
    public int debugLevel = 0;
    
    @Option("--debug")
    @OptionalValue
    void setDebugLevel(Integer level) {
        if (level == null) {
            debugLevel = 10;
        }
        else {
            debugLevel = level;
        }
    }
}
```

First, you have to use methods, because fields just have to hold a value.<sup>1</sup>
You annotate your method with @OptionalValue.
Then, a `null` is passed with the semantics of no value given.
Unless you use this annotation, you will never receive `null` in the argument.

Naturally, you cannot use primitive types, because there is no such thing as `null` there.

## The boolean shorthand

Whenever you have a boolean option, no value is needed. Therefore, your users don't have to specify:

```java
$ ./exec --verbose=true
```

Instead, all boolean options have an optional value by default, where "no value" means "true". But you can still pass `--verbose=false`.

<sup>1</sup>) This is the only case when field option have an optional value.

## Mandatory options

Sometimes you want to make sure some option is present on the command line. It is fairly simple to do:

```java
class MyOptions implements Options {
    
    @Option("--really-do-it")
    @Mandatory
    boolean dummy;
    
}
```

Unless the user specifies this option, a `MissingMandatoryOptionException` with all the missing options will be thrown.

## Range checking

Though the library's main responsibility is to parse command line arguments, we had to implement one validator for integer fields. There you have it:

```java
class MyOptions implements Options {
    @Option("--hour")
    @Range(minimumValue = 0, maximumValue = 24)
    int hour;
}
```

You can use it for all integral types: `byte`, `short`, `int`, `long`, and all their boxed counterparts (`Integer`, &hellip;).

There are two better options how to impose some requirements on values. Either check them in an `@AfterParse` method (possibly define validators and call them, use your software architects' common sense), or, you can use custom types.

## Custom types

Yaclpplib gives you a lot of freedom in types you can use for fields. The default setup made by `ArgumentParserFactory` include:

* primitive types (and their boxed variants)
* `String`s
* all `enums` (with their specifcs, see below)
* everything that has a constructor from String

The last one is the most powerful. If you want your domain-constrained variable, you can simply define a type for it. To recall the previous example:

```java
class HourType {
    private int value;
    
    HourType(String text) {
        value = Integer.parseInt(text);
        if (value < 0 || value > 24)
            throw new IllegalOptionValue("Hour is expected to be between 0-24.");
    }
    
    public int getValue() { return value; }
}
```

This is enough to use it in options:

```java
class MyOptions implements Options {
    @Option("--hour")
    Hour hour;
}
```

## Enum specifics

When you use enums, you often follow the Java&trade; language habits:

```java
class MyOptions implements Options {
    enum Choice {
        NEVER, NO, MAYBE, YES, ALWAYS
    }
    
    @Option("--choice")
    Choice choice;
}
```

This way, the user would have to type:

```bash
$ ./exec --choice MAYBE
```

So, by default, **enums are parsed case-insensitively**. If you really want them to be case sensitive, you can annotate the enum:

```java
@CaseSensitive
enum Case {
    PascalCase, camelCase, under_scores
}
```

## Help

Usually, the option parsing library has enough information to create a sensible help text. Yaclpplib gives you this:

```java
ArgumentParser parser = ArgumentParserFactory.create();
String helpText = parser.getHelp();
parser.printHelp(); // prints the help text on standard error output
```

To be even more comfortable, the default setup includes an option "--help", printing the help and exiting sucessfully.

You should customize the help output with another annotations:

```java
@Help("My program options")
class MyOptions implements Options {
    @Option("--value")
    @Help("Set the value this program uses")
    int value;
}
```

Which results to:
```bash
$ ./exec --help
Default options
  --help               Print a usage on standard output and exit successfully.

My program options
  --value              Set the value this program uses
```

# How to handle the code

The code is equipped with Ant's `build.xml`. So, there are following ant targets:

```bash
$ ant build         # Builds the classes
$ ant doc           # Build javadoc
$ ant test          # Run all the tests
$ ant               # Do everything above
```

After running `ant`, the build directory should contain:
```
yaclpplib
├── build           Built classes
├── doc             Javadoc documentation
├── src             Source files
├── test            Test source files
└── testbuild       Built tests
```

We use JUnit4 as the testing framework, tests are split into Unit tests
and feature tests, though invoked in a single command.