# A stream fetching helper for Java

[![Build Status](https://travis-ci.org/markenwerk/java-utils-data-fetcher.svg?branch=master)](https://travis-ci.org/markenwerk/java-utils-data-fetcher)
[![Coverage Status](https://coveralls.io/repos/markenwerk/java-utils-data-fetcher/badge.svg?branch=master&service=github)](https://coveralls.io/github/markenwerk/java-utils-data-fetcher?branch=master)
[![Dependency Status](https://www.versioneye.com/user/projects/562bcca036d0ab00160013f7/badge.svg)](https://www.versioneye.com/user/projects/562bcca036d0ab00160013f7)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/net.markenwerk/utils-data-fetcher/badge.svg)](https://maven-badges.herokuapp.com/maven-central/net.markenwerk/utils-data-fetcher)

This is a simple helper to copy the content of input stream into output streams or byte arrays. It is, among other things, intended as an alternative to using [`IOUtils#readFully`](http://grepcode.com/file/repository.grepcode.com/java/root/jdk/openjdk/7u40-b43/sun/misc/IOUtils.java#IOUtils.readFully%28java.io.InputStream%2Cint%2Cboolean%29) for this everyday task.

## Overview

This library is hosted in the [Maven Central Repositoy](http://search.maven.org/#artifactdetails|net.markenwerk|utils-data-fetcher|2.1.1|jar). You can use it with the following coordinates:

```xml
<dependency>
	<groupId>net.markenwerk</groupId>
	<artifactId>utils-data-fetcher</artifactId>
	<version>2.1.1</version>
</dependency>
```

Consult the [usage description](#usage) and [Javadoc](http://markenwerk.github.io/java-utils-data-fetcher/javadoc/2.1.1/index.html) for further information.

## Motivation

Copying the complete content of an [`InputStream`][InputStream] into a `byte[]` or into an [`OutputStream`][OutputStream] is a menial task, that has to be dealt with very commonly. While this is certainly not a difficult challenge for any programmer, it is still boring and annoying to do it over and over again, and – as pretty much everything – prone to be erroneously, if done in a hurry.

There are common solutions, beside coding it over and over again. The Oracle JRE provides the class [`IOUtils`](http://grepcode.com/file/repository.grepcode.com/java/root/jdk/openjdk/7u40-b43/sun/misc/IOUtils.java) for this simple task. But this class is not a part of the official Java specifications, which means that using it yields a compiler warning like

```
The type 'IOUtils' is not API.
```

and may break at runtime; i.e. if a runtime environment other than the Oracle JRE is used. Another solution to accomplishing this simple task, could be to bundle a major library like [Commons IO](http://commons.apache.org/proper/commons-io/). But doing so may appear like overkill, especially if no other functionality of the library is likely to be used.


## Usage

This library is intended to be a simple and convenient to use solution for the mentioned problem. It features two main functionalities:

```java
// create a simple fetcher
Fetcher fetcher = new BufferedFetcher();

// copy the content of an InputStream into a byte[]
byte[] bytes = fetcher.fetch(inputStream);

// copy the content of an InputStream into an OutputStream
fetcher.fetch(inputStream, outputStream);
```

### Automatic closing of supplied streams

Both methods have optional `boolean` parameters, that can be used to instruct the [`Fetcher`][Fetcher] to close the supplied streams. Doing so allows to write compact code like:

```java
foo.setContent(new BufferedFetcher().fetch(new FileInputStream(file), true));
```

Otherwise, it would be necessary to write unnecessarily verbose code like:

```java
InputStream in = new FileInputStream(file);
foo.setContent(new BufferedFetcher().fetch(in));
in.close();
```

### Listen to fetch progress

Both methods optionally take a [`FetchProgressListener`][FetchProgressListener] that gets notified on various points in the lifecycle (`started`, `progressed`, `succeded` or `failed`, `finished`) of a fetch operation. The [`BaseFetchProgressListener`][BaseFetchProgressListener] is a convenient base implementation with empty methods.

```java
foo.setContent(new BufferedFetcher().fetch(new FileInputStream(file),
	new BaseFetchProgressListener() {
		public void onFetchProgress(long bytesFetched) {
			System.out.println(bytesFetched + " bytes fetched so far.");
		}
	},
	true
);
```

### Handling of bad parameters

Missing or invalid arguments are handled gracefully with the following behaviour:

 - If `null` is given as the [`InputStream`][InputStream], it is simply ignored and handled as if there was nothing to read. That means, nothing is written to the [`OutputStream`][OutputStream] and, if requested, the [`OutputStream`][OutputStream] will be closed. 
 - If `null` is given as the [`OutputStream`][OutputStream], it is simply ignored, but the content of given [`InputStream`][InputStream] is fetched anyway. If requested, the [`InputStream`][InputStream] will be closed. 
 
### Custom implementations
 
This library provides [`BufferedFetcher`][BufferedFetcher] as the simpelest possible implementations of [`Fetcher`][Fetcher]. It eagerly allocates a `byte[]` during constructor and uses it as a buffer to perform fetch operations by sequentually reading from the [`InputStream`][InputStream] into the buffer and then writing from the buffer to the [`OutputStream`][OutputStream]. It is not threadsafe.

Variations of the bufferd copying stratigy (e.g. lazy buffer creation, threadsafeness) or compleatly other copying stratagies can easily be implemented by extending [`AbstractBufferedFetcher`][AbstractBufferedFetcher] or [`AbstractFetcher`][AbstractFetcher] respectively.
 
[AbstractBufferedFetcher]: http://static.javadoc.io/net.markenwerk/utils-data-fetcher/2.1.1/index.html?net/markenwerk/utils/data/fetcher/AbstractBufferedFetcher.html
[AbstractFetcher]: http://static.javadoc.io/net.markenwerk/utils-data-fetcher/2.1.1/index.html?net/markenwerk/utils/data/fetcher/AbstractFetcher.html
[BaseFetchProgressListener]: http://static.javadoc.io/net.markenwerk/utils-data-fetcher/2.1.1/index.html?net/markenwerk/utils/data/fetcher/BaseFetchProgressListener.html
[BufferedFetcher]: http://static.javadoc.io/net.markenwerk/utils-data-fetcher/2.1.1/index.html?net/markenwerk/utils/data/fetcher/BufferedFetcher.html
[Fetcher]: http://static.javadoc.io/net.markenwerk/utils-data-fetcher/2.1.1/index.html?net/markenwerk/utils/data/fetcher/Fetcher.html
[FetchProgressListener]: http://static.javadoc.io/net.markenwerk/utils-data-fetcher/2.1.1/index.html?net/markenwerk/utils/data/fetcher/FetchProgressListener.html

[InputStream]: http://docs.oracle.com/javase/6/docs/api/index.html?java/io/InputStream.html
[OutputStream]: http://docs.oracle.com/javase/6/docs/api/index.html?java/io/OutputStream.html
