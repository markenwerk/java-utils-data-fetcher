# A stream fetching helper for Java

This is a simple helper to copy the content of input stream into output streams or byte arrays. It is, among other things, intended as an alternative to using [`IOUtils#readFully`](http://grepcode.com/file/repository.grepcode.com/java/root/jdk/openjdk/7u40-b43/sun/misc/IOUtils.java#IOUtils.readFully%28java.io.InputStream%2Cint%2Cboolean%29) for this everyday task.

## Overview

This library is hosted in the [Maven Central Repositoy](http://search.maven.org/#artifactdetails|net.markenwerk|utils-data-fetcher|1.0.0|jar). You can use it with the following coordinates:

```xml
<dependency>
	<groupId>net.markenwerk</groupId>
	<artifactId>utils-data-fetcher</artifactId>
	<version>1.0.0</version>
</dependency>
```

Consult the [usage description](#usage) and [Javadoc](http://markenwerk.github.io/java-utils-data-fetcher/javadoc/1.0.0/index.html) for further information.

## Motivation

Copying the complete content of an [`InputStream`](http://docs.oracle.com/javase/7/docs/api/index.html?java/io/InputStream.html) into a `byte[]` or into an [`OutputStream`](http://docs.oracle.com/javase/7/docs/api/index.html?java/io/OutputStream.html) is a menial task, that has to be dealt with very commonly. While this is certainly not a difficult challenge for any programmer, it is still boring and annoying to do it over and over again, and -- as pretty much everything -- prone to be erroneously, if done in a hurry.

There are common solutions, beside coding it over and over again. The Oracle JRE provides the class [`IOUtils`](http://grepcode.com/file/repository.grepcode.com/java/root/jdk/openjdk/7u40-b43/sun/misc/IOUtils.java) for this simple task. But this class is not a part of the official Java specifications, which means that using it yields a compiler warning like

```
The type 'IOUtils' is not API.
```

and may break at runtime; i.e. if a runtime environment other than the Oracle JRE is used. Another solution to accomplishing this simple task, could be to bundle a major library like [Commons IO](http://commons.apache.org/proper/commons-io/). But doing so may appears like overkill, especially if no other functionality of the library is likely to be used.

## Usage

this library is intended to be a simple and convenient to use lightweight solution for the mentioned problem. It features two main functionalities:

```java
// copy the content of an InputStream into a byte[]
byte[] bytes = Fetcher.fetch(inputStream);

// copy the content of an InputStream into an OutputStream
Fetcher.fetch(inputStream, outputStream);
```

### Specifying the buffer size

The default buffer size used, is one [mebibyte](https://en.wikipedia.org/wiki/Mebibyte), but any other positive buffer size may be specified with an optional integer parameter.

```java
// copy the content of an InputStream into a byte[]
byte[] bytes = Fetcher.fetch(inputStream, bufferSize);

// copy the content of an InputStream into an OutputStream
Fetcher.fetch(inputStream, outputStream, bufferSize);
```

### Automatic closing of supplied streams

Both methods have optional parameters, that can be used to instruct the [`Fetcher`](http://markenwerk.github.io/java-utils-data-fetcher/javadoc/1.0.0/index.html?net/markenwerk/utils/data/fetcher/Fetcher.html) to close the supplied streams.

This allows to write compact code like:

```java
foo.setContent(Fetcher.fetch(new FileInputStream(file), true));
```

instead of unnecessarily verbose code like

```java
InputStream in = new FileInputStream(file);
foo.setContent(Fetcher.fetch(in));
in.close();
```

There are also version of both methods that take an integer parameter to adjust the buffer size as well.


### Handling of bad parameters

Missing or invalid arguments are handled gracefully with the following behaviour.

 - If `null` is given as the [`InputStream`](http://docs.oracle.com/javase/7/docs/api/index.html?java/io/InputStream.html), it is simply ignored and handled as if there was nothing to read. That means, nothing is written to the [`OutputStream`](http://docs.oracle.com/javase/7/docs/api/index.html?java/io/OutputStream.html) and, if requested, the [`OutputStream`](http://docs.oracle.com/javase/7/docs/api/index.html?java/io/OutputStream.html) will be closed. 
  - If `null` is given as the [`OutputStream`](http://docs.oracle.com/javase/7/docs/api/index.html?java/io/OutputStream.html), it is simply ignored, but the content of given [`InputStream`](http://docs.oracle.com/javase/7/docs/api/index.html?java/io/InputStream.html) is fetched anyway. If requested, the [`InputStream`](http://docs.oracle.com/javase/7/docs/api/index.html?java/io/InputStream.html) will be closed. 
 - A non-positive buffer size is replaced with the default buffer size of one [mebibyte](https://en.wikipedia.org/wiki/Mebibyte).
