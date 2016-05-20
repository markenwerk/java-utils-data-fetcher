# A data stream fetching helper for Java

[![Build Status](https://travis-ci.org/markenwerk/java-utils-data-fetcher.svg?branch=master)](https://travis-ci.org/markenwerk/java-utils-data-fetcher)
[![Coverage Status](https://coveralls.io/repos/markenwerk/java-utils-data-fetcher/badge.svg?branch=master&service=github)](https://coveralls.io/github/markenwerk/java-utils-data-fetcher?branch=master)
[![Dependency Status](https://www.versioneye.com/user/projects/562bcca036d0ab00160013f7/badge.svg)](https://www.versioneye.com/user/projects/562bcca036d0ab00160013f7)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/net.markenwerk/utils-data-fetcher/badge.svg)](https://maven-badges.herokuapp.com/maven-central/net.markenwerk/utils-data-fetcher)
[![Issues](https://img.shields.io/github/issues/markenwerk/java-utils-data-fetcher.svg)](https://github.com/markenwerk/java-utils-data-fetcher/issues)
[![MIT License](https://img.shields.io/badge/license-MIT-brightgreen.svg)](https://github.com/markenwerk/java-utils-data-fetcher/blob/master/LICENSE)

## Overview

This is a simple helper to fetch the complete content of an input stream. It is, among other things, intended as an alternative to using [`IOUtils#readFully`](http://grepcode.com/file/repository.grepcode.com/java/root/jdk/openjdk/7u40-b43/sun/misc/IOUtils.java#IOUtils.readFully%28java.io.InputStream%2Cint%2Cboolean%29) for this everyday task.

Consult the [documentation](http://markenwerk.github.io/java-utils-data-fetcher/index.html) and  the [usage description](#usage) for further information:

- [Fetching data streams](#fetching-byte-streams)
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
	<version>3.1.0</version>
</dependency>
```

## Motivation

Copying the complete content of an [`InputStream`][InputStream] into a `byte[]` or into an [`OutputStream`][OutputStream] is a menial task, that has to be dealt with very commonly. While this is certainly not a difficult challenge for any programmer, it is still boring and annoying to do it over and over again, and – as pretty much everything – prone to be erroneously, if done in a hurry.

There are common solutions, beside coding it over and over again. The Oracle JRE provides the class [`IOUtils`](http://grepcode.com/file/repository.grepcode.com/java/root/jdk/openjdk/7u40-b43/sun/misc/IOUtils.java) for this simple task. But this class is not a part of the official Java specifications, which means that using it yields a compiler warning like

```
The type 'IOUtils' is not API.
```

and may break at runtime; i.e. if a runtime environment other than the Oracle JRE is used. Another solution to accomplishing this simple task, could be to bundle a major library like [Commons IO](http://commons.apache.org/proper/commons-io/). But doing so may appear like overkill, especially if no other functionality of the library is likely to be used.

This library provides a simple and lightweight alternative.
 
## Usage

### Fetching data streams

This library provides the [`DataFetcher`][DataFetcher] which features two main functionalities to handle `byte` streams:

```java
// create a simple DataFetcher
DataFetcher fetcher = new BufferedDataFetcher();

// copy the content of an InputStream into a byte[]
byte[] bytes = fetcher.fetch(inputStream);

// copy the content of an InputStream into an OutputStream
fetcher.fetch(inputStream, outputStream);
```

### Automatic closing of supplied streams

All methods have optional `boolean` parameters, that can be used to instruct the [`DataFetcher`][DataFetcher] to close the supplied streams. Doing so allows to write compact code like:

```java
foo.setContent(new BufferedDataFetcher().fetch(new FileInputStream(file), true));
```

Otherwise, it would be necessary to write unnecessarily verbose code like:

```java
InputStream in = new FileInputStream(file);
foo.setContent(new BuffereddataFetcher().fetch(in));
in.close();
```

### Listen to fetch progress

All methods optionally take a [`DataFetchProgressListener`][DataFetchProgressListener] that gets notified on various points in the lifecycle (`started`, `progressed`, `succeeded` or `failed`, `finished`) of a fetch operation. The [`IdleDataFetchProgressListener`][IdleDataFetchProgressListener] is a convenient base implementation with empty methods.

```java
foo.setContent(new BufferedDataFetcher().fetch(
	new FileInputStream(file),
	new IdleDataFetchProgressListener() {
		public void onProgress(long bytesFetched) {
			System.out.println(bytesFetched + " bytes fetched so far.");
		}
	},
	true
);
```

### Handling of `null` arguments

Missing or invalid arguments are handled gracefully with the following behaviour:

 - If `null` is given as the [`InputStream`][InputStream], it is simply ignored and handled as if there was nothing to read. That means, nothing is written to the [`OutputStream`][OutputStream] and, if requested, the [`OutputStream`][OutputStream] will be closed. 
 - If `null` is given as the [`OutputStream`][OutputStream], it is simply ignored, but the content of the given [`InputStream`][InputStream] is fetched anyway. If requested, the [`InputStream`][InputStream] be closed. 
 
### Customizability
 
This library provides [`BufferedDataFetcher`][BufferedDataFetcher] as the simplest possible implementations of [`DataFetcher`][DataFetcher]. It eagerly allocates a `byte[]` during construction and uses it as a buffer to perform fetch operations by sequentially reading from the [`InputStream`][InputStream] into the buffer and then writing from the buffer to the [`OutputStream`][OutputStream]. It is not threadsafe.

Variations of the buffered copying strategy (e.g. lazy buffer creation, threadsafeness) or completely other copying strategies can easily be implemented by extending [`AbstractDataFetcher`][AbstractDataFetcher] or [`AbstractBufferedDataFetcher`][AbstractBufferedDataFetcher] respectively.

[`AbstractDataFetcher`][AbstractDataFetcher] reduces all methods from the [`DataFetcher`][DataFetcher] interface to the single method `doCopy(InputStream, OutputStream, DataFetchProgressListener)` where every parameter is guaranteed to be not `null`, and therefore greatly simplifies the implementation of new fetch strategies. [`AbstractBufferedDataFetcher`][AbstractBufferedDataFetcher] is such an implementation of the aforementioned buffering fetch strategy that handles the sequentially write-read cycles and notifies the [`DataFetchProgressListener`][DataFetchProgressListener] accordingly, but leaves the buffer allocation strategy to be specified.

 
[AbstractBufferedDataFetcher]:  http://markenwerk.github.io/java-utils-data-fetcher/index.html?net/markenwerk/utils/data/fetcher/AbstractBufferedDataFetcher.html
[AbstractDataFetcher]: http://markenwerk.github.io/java-utils-data-fetcher/index.html?net/markenwerk/utils/data/fetcher/AbstractDataFetcher.html
[BufferedDataFetcher]: http://markenwerk.github.io/java-utils-data-fetcher/index.html?net/markenwerk/utils/data/fetcher/BufferedDataFetcher.html
[DataFetcher]: http://markenwerk.github.io/java-utils-data-fetcher/index.html?net/markenwerk/utils/data/fetcher/DataFetcher.html
[DataFetchProgressListener]: http://markenwerk.github.io/java-utils-data-fetcher/index.html?net/markenwerk/utils/data/fetcher/DataFetchProgressListener.html
[IdleDataFetchProgressListener]: http://markenwerk.github.io/java-utils-data-fetcher/index.html?net/markenwerk/utils/data/fetcher/IdleDataFetchProgressListener.html

[InputStream]: http://docs.oracle.com/javase/8/docs/api/index.html?java/io/InputStream.html
[OutputStream]: http://docs.oracle.com/javase/8/docs/api/index.html?java/io/OutputStream.html