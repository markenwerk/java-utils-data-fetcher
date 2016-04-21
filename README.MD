# A stream fetching helper for Java

[![Build Status](https://travis-ci.org/markenwerk/java-utils-data-fetcher.svg?branch=master)](https://travis-ci.org/markenwerk/java-utils-data-fetcher)
[![Coverage Status](https://coveralls.io/repos/markenwerk/java-utils-data-fetcher/badge.svg?branch=master&service=github)](https://coveralls.io/github/markenwerk/java-utils-data-fetcher?branch=master)
[![Dependency Status](https://www.versioneye.com/user/projects/562bcca036d0ab00160013f7/badge.svg)](https://www.versioneye.com/user/projects/562bcca036d0ab00160013f7)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/net.markenwerk/utils-data-fetcher/badge.svg)](https://maven-badges.herokuapp.com/maven-central/net.markenwerk/utils-data-fetcher)
[![Issues](https://img.shields.io/github/issues/markenwerk/java-utils-data-fetcher.svg)](https://github.com/markenwerk/java-utils-data-fetcher/issues)
[![MIT License](https://img.shields.io/badge/license-MIT-brightgreen.svg)](https://github.com/markenwerk/java-utils-data-fetcher/blob/master/LICENSE)

## Overview

This is a simple helper to fetch the complete content of an input stream or a readable. It is, among other things, intended as an alternative to using [`IOUtils#readFully`](http://grepcode.com/file/repository.grepcode.com/java/root/jdk/openjdk/7u40-b43/sun/misc/IOUtils.java#IOUtils.readFully%28java.io.InputStream%2Cint%2Cboolean%29) for this everyday task.

Consult the [documentation](http://markenwerk.github.io/java-utils-data-fetcher/index.html) and  the [usage description](#usage) for further information:

- [Fetching `byte` streams](#fetching-byte-streams)
- [Fetching `char` streams](#fetching-char-streams)
- [Automatic closing of supplied streams](#automatic-closing-of-supplied-streams)
- [Listen to fetch progress](#listen-to-fetch-progress)
- [Handling of `null` arguments](#handling-of-null-arguments)
- [Customizability](#customizability)

## Maven

This library is hosted in the [Maven Central Repository](https://maven-badges.herokuapp.com/maven-central/net.markenwerk/utils-data-fetcher). You can use it with the following coordinates:

```xml
<dependency>
	<groupId>net.markenwerk</groupId>
	<artifactId>utils-data-fetcher</artifactId>
	<version>3.0.0</version>
</dependency>
```

## Motivation

Copying the complete content of an [`InputStream`][InputStream] into a `byte[]` or into an [`OutputStream`][OutputStream] is a menial task, that has to be dealt with very commonly. While this is certainly not a difficult challenge for any programmer, it is still boring and annoying to do it over and over again, and – as pretty much everything – prone to be erroneously, if done in a hurry.

There are common solutions, beside coding it over and over again. The Oracle JRE provides the class [`IOUtils`](http://grepcode.com/file/repository.grepcode.com/java/root/jdk/openjdk/7u40-b43/sun/misc/IOUtils.java) for this simple task. But this class is not a part of the official Java specifications, which means that using it yields a compiler warning like

```
The type 'IOUtils' is not API.
```

and may break at runtime; i.e. if a runtime environment other than the Oracle JRE is used. Another solution to accomplishing this simple task, could be to bundle a major library like [Commons IO](http://commons.apache.org/proper/commons-io/). But doing so may appear like overkill, especially if no other functionality of the library is likely to be used.

This library provides a simple and lightweight alternative. For the sake of completeness, the equivalent  functionality for copying the complete content of an [`Readable`][Readable] into a [`String`][String] or into a `char[]` or into an [`Appendable`][Appendable] is included.
 
## Usage

### Fetching `byte` streams

This library provides the [`ByteFetcher`][ByteFetcher] which features two main functionalities to handle `byte` streams:

```java
// create a simple ByteFetcher
ByteFetcher fetcher = new BufferedByteFetcher();

// copy the content of an InputStream into a byte[]
byte[] bytes = fetcher.fetch(inputStream);

// copy the content of an InputStream into an OutputStream
fetcher.fetch(inputStream, outputStream);
```

### Fetching `char` streams

This library provides the [`CharacterFetcher`][CharacterFetcher] which features three main functionalities to handle `char` streams:

```java
// create a simple CharacterFetcher
CharacterFetcher fetcher = new BufferedCharacterFetcher();

// copy the content of a Readable into a char[]
char[] characters = fetcher.fetch(readable);

// copy the content of a Readable into a String
String string = fetcher.read(readable);

// copy the content of a Readable into an Appendable
fetcher.fetch(readable, appendable);
```

### Automatic closing of supplied streams

All methods have optional `boolean` parameters, that can be used to instruct the [`ByteFetcher`][ByteFetcher] or [`CharacterFetcher`][CharacterFetcher] to close the supplied streams. Doing so allows to write compact code like:

```java
foo.setContent(new BufferedByteFetcher().fetch(new FileInputStream(file), true));
```

Otherwise, it would be necessary to write unnecessarily verbose code like:

```java
InputStream in = new FileInputStream(file);
foo.setContent(new BufferedByteFetcher().fetch(in));
in.close();
```

### Listen to fetch progress

All methods optionally take a [`FetchProgressListener`][FetchProgressListener] that gets notified on various points in the lifecycle (`started`, `progressed`, `succeeded` or `failed`, `finished`) of a fetch operation. The [`FetchProgressAdapter`][FetchProgressAdapter] is a convenient base implementation with empty methods.

```java
foo.setContent(new BufferedByteFetcher().fetch(
	new FileInputStream(file),
	new FetchProgressAdapter() {
		public void onProgress(long bytesFetched) {
			System.out.println(bytesFetched + " bytes fetched so far.");
		}
	},
	true
);
```

### Handling of `null` arguments

Missing or invalid arguments are handled gracefully with the following behaviour:

 - If `null` is given as the [`InputStream`][InputStream], or [`Readable`][Readable] it is simply ignored and handled as if there was nothing to read. That means, nothing is written to the [`OutputStream`][OutputStream] or [`Appendable`][Appendable] and, if requested, the [`OutputStream`][OutputStream] or [`Appendable`][Appendable] will be closed. 
 - If `null` is given as the [`OutputStream`][OutputStream] or [`Appendable`][Appendable], it is simply ignored, but the content of given [`InputStream`][InputStream] or [`Readable`][Readable] is fetched anyway. If requested, the [`InputStream`][InputStream] or [`Readable`][Readable] will be closed. 
 
### Customizability
 
This library provides [`BufferedByteFetcher`][BufferedByteFetcher] as the simplest possible implementations of [`ByteFetcher`][ByteFetcher]. It eagerly allocates a `byte[]` during construction and uses it as a buffer to perform fetch operations by sequentially reading from the [`InputStream`][InputStream] into the buffer and then writing from the buffer to the [`OutputStream`][OutputStream]. It is not threadsafe.

Variations of the buffered copying strategy (e.g. lazy buffer creation, threadsafeness) or completely other copying strategies can easily be implemented by extending [`AbstractByteFetcher`][AbstractByteFetcher] or [`AbstractBufferedByteFetcher`][AbstractBufferedByteFetcher] respectively.

[`AbstractByteFetcher`][AbstractByteFetcher] reduces all methods from the [`ByteFetcher`][ByteFetcher] interface to the single method `doCopy(InputStream, OutputStream, FetchProgressListener)` where every parameter is guaranteed to be not `null`, and therefore greatly simplifies the implementation of new fetch strategies. [`AbstractBufferedByteFetcher`][AbstractBufferedByteFetcher] is such an implementation of the aforementioned buffering fetch strategy that handles the sequentially write-read cycles and notifies the [`FetchProgressListener`][FetchProgressListener] accordingly, but leaves the buffer allocation strategy to be specified.

[`AbstractCharacterFetcher`][AbstractCharacterFetcher], [`AbstractBufferedCharacterFetcher`][AbstractBufferedCharacterFetcher] and [`BufferedCharacterFetcher`][BufferedCharacterFetcher] provide the equivalent functionality for [`CharacterFetcher`][CharacterFetcher].

 
[AbstractBufferedByteFetcher]:  http://markenwerk.github.io/java-utils-data-fetcher/index.html?net/markenwerk/utils/data/fetcher/AbstractBufferedByteFetcher.html
[AbstractBufferedCharacterFetcher]: http://markenwerk.github.io/java-utils-data-fetcher/index.html?net/markenwerk/utils/data/fetcher/AbstractBufferedCharacterFetcher.html
[AbstractByteFetcher]: http://markenwerk.github.io/java-utils-data-fetcher/index.html?net/markenwerk/utils/data/fetcher/AbstractByteFetcher.html
[AbstractCharacterFetcher]: http://markenwerk.github.io/java-utils-data-fetcher/index.html?net/markenwerk/utils/data/fetcher/AbstractCharacterFetcher.html
[FetchProgressAdapter]: http://markenwerk.github.io/java-utils-data-fetcher/index.html?net/markenwerk/utils/data/fetcher/FetchProgressAdapter.html
[BufferedByteFetcher]: http://markenwerk.github.io/java-utils-data-fetcher/index.html?net/markenwerk/utils/data/fetcher/BufferedByteFetcher.html
[BufferedCharacterFetcher]: http://markenwerk.github.io/java-utils-data-fetcher/index.html?net/markenwerk/utils/data/fetcher/BufferedCharacterFetcher.html
[ByteFetcher]: http://markenwerk.github.io/java-utils-data-fetcher/index.html?net/markenwerk/utils/data/fetcher/ByteFetcher.html
[CharacterFetcher]: http://markenwerk.github.io/java-utils-data-fetcher/index.html?net/markenwerk/utils/data/fetcher/CharacterFetcher.html
[FetchProgressListener]: http://markenwerk.github.io/java-utils-data-fetcher/index.html?net/markenwerk/utils/data/fetcher/FetchProgressListener.html

[Appendable]: http://docs.oracle.com/javase/8/docs/api/index.html?java/lang/Appendable.html
[InputStream]: http://docs.oracle.com/javase/8/docs/api/index.html?java/io/InputStream.html
[OutputStream]: http://docs.oracle.com/javase/8/docs/api/index.html?java/io/OutputStream.html
[Readable]: http://docs.oracle.com/javase/8/docs/api/index.html?java/lang/Readable.html
[String]: http://docs.oracle.com/javase/8/docs/api/index.html?java/lang/String.html
