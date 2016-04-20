/*
 * Copyright (c) 2015, 2016 Torsten Krause, Markenwerk GmbH
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package net.markenwerk.utils.data.fetcher;

import java.io.CharArrayReader;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.CharBuffer;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import net.markenwerk.utils.data.fetcher.mockups.FailableReader;
import net.markenwerk.utils.data.fetcher.mockups.FailableWriter;

/**
 * JUnit tests for {@link CharacterFetcher#fetch(Readable)} methods on stream
 * that fail.
 * 
 * @author Torsten Krause (tk at markenwerk dot net)
 * @param <ActualFetcher>
 *            The actual {@link CharacterFetcher} type to be tested.
 * @since 1.0.0
 */
public abstract class AbstractCharacterFetcherTests<ActualFetcher extends CharacterFetcher> {

	private static final char[] CHARS = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

	private CharacterFetcher fetcher;

	private FailableReader in;

	private CharArrayWriter outBuffer;

	private FailableWriter out;

	/**
	 * Create a new {@link CharacterFetcher}.
	 */
	@Before
	public void prepareFetcher() {
		fetcher = createFetcher();
	}

	/**
	 * Creates the {@link CharacterFetcher} to be tested.
	 * 
	 * @return The {@link CharacterFetcher} to be tested.
	 */
	protected abstract ActualFetcher createFetcher();

	/**
	 * Create a new {@link FailableReader} from an {@link CharArrayReader} that
	 * reads some.
	 */
	@Before
	public void prepareInputStream() {
		in = new FailableReader(new CharArrayReader(CHARS));
	}

	/**
	 * Close the {@link FailableReader} created by
	 * {@link AbstractCharacterFetcherTests#prepareInputStream()}.
	 */
	@After
	public void closeInputStream() {
		if (!in.isClosed()) {
			try {
				in.close();
			} catch (IOException e) {
			}
		}
	}

	/**
	 * Create a new {@link FailableWriter} from an {@link CharArrayWriter}.
	 */
	@Before
	public void prepareOutputStream() {
		outBuffer = new CharArrayWriter();
		out = new FailableWriter(outBuffer);
	}

	/**
	 * Close the {@link FailableWriter} created by
	 * {@link AbstractCharacterFetcherTests#prepareOutputStream()}.
	 */
	@After
	public void closeOutputStream() {
		if (!out.isClosed()) {
			try {
				out.close();
			} catch (IOException e) {
			}
		}
	}

	/**
	 * Fetch into a new {@code char[]}, leaving the input stream open by
	 * default.
	 * 
	 * @throws IOException
	 *             If the fetch process failed unexpectedly.
	 */
	@Test
	public void fetch_leaveStreamOpenByDefault() throws IOException {

		char[] characters = fetcher.fetch(in);

		Assert.assertArrayEquals(CHARS, characters);
		Assert.assertFalse(in.isClosed());

	}

	/**
	 * Fetch into a new {@code char[]}, leaving the input stream explicitly
	 * open.
	 * 
	 * @throws IOException
	 *             If the fetch process failed unexpectedly.
	 */
	@Test
	public void fetch_leaveStreamOpen() throws IOException {

		char[] characters = fetcher.fetch(in, false);

		Assert.assertArrayEquals(CHARS, characters);
		Assert.assertFalse(in.isClosed());

	}

	/**
	 * Fetch into a new {@code char[]}, closing the input stream.
	 * 
	 * @throws IOException
	 *             If the fetch process failed unexpectedly.
	 */
	@Test
	public void fetch_autoCloseStream() throws IOException {

		char[] characters = fetcher.fetch(in, true);

		Assert.assertArrayEquals(CHARS, characters);
		Assert.assertTrue(in.isClosed());

	}

	/**
	 * Fetch into a new {@code char[]}, {@literal null} as an
	 * {@link InputStream}, which should yield an empty array.
	 * 
	 * @throws IOException
	 *             If the fetch process failed unexpectedly.
	 */
	@Test
	public void fetch_withBadParameters_nullStream() throws IOException {

		char[] characters = fetcher.fetch(null);

		Assert.assertNotNull(characters);
		Assert.assertTrue(0 == characters.length);

	}

	/**
	 * Read into a new {@code String}, leaving the input stream open by default.
	 * 
	 * @throws IOException
	 *             If the fetch process failed unexpectedly.
	 */
	@Test
	public void read_leaveStreamOpenByDefault() throws IOException {

		String string = fetcher.read(in);

		Assert.assertEquals(new String(CHARS), string);
		Assert.assertFalse(in.isClosed());

	}

	/**
	 * Read into a new {@code String}, leaving the input stream explicitly open.
	 * 
	 * @throws IOException
	 *             If the fetch process failed unexpectedly.
	 */
	@Test
	public void read_leaveStreamOpen() throws IOException {

		String string = fetcher.read(in, false);

		Assert.assertEquals(new String(CHARS), string);
		Assert.assertFalse(in.isClosed());

	}

	/**
	 * Read into a new {@code String}, closing the input stream.
	 * 
	 * @throws IOException
	 *             If the fetch process failed unexpectedly.
	 */
	@Test
	public void read_autoCloseStream() throws IOException {

		String string = fetcher.read(in, true);

		Assert.assertEquals(new String(CHARS), string);
		Assert.assertTrue(in.isClosed());

	}

	/**
	 * Read into a new {@code String}, {@literal null} as an {@link InputStream}
	 * , which should yield an empty array.
	 * 
	 * @throws IOException
	 *             If the fetch process failed unexpectedly.
	 */
	@Test
	public void read_withBadParameters_nullStream() throws IOException {

		String string = fetcher.read(null);

		Assert.assertNotNull(string);
		Assert.assertTrue(0 == string.length());

	}

	/**
	 * Fetch into an {@link OutputStream}, leaving both streams open by default.
	 * 
	 * @throws IOException
	 *             If the copy process failed unexpectedly.
	 */
	@Test
	public void copy_leaveStreamsOpenByDefault() throws IOException {

		fetcher.copy(in, out);

		Assert.assertArrayEquals(CHARS, outBuffer.toCharArray());
		Assert.assertFalse(in.isClosed());
		Assert.assertFalse(out.isClosed());

	}

	/**
	 * Fetch into an {@link OutputStream}, leaving both streams explicitly open.
	 * 
	 * @throws IOException
	 *             If the copy process failed unexpectedly.
	 */
	@Test
	public void copy_leaveStreamsOpen() throws IOException {

		fetcher.copy(in, out, false, false);

		Assert.assertArrayEquals(CHARS, outBuffer.toCharArray());
		Assert.assertFalse(in.isClosed());
		Assert.assertFalse(out.isClosed());

	}

	/**
	 * Fetch into an {@link OutputStream}, closing both stream.
	 * 
	 * @throws IOException
	 *             If the copy process failed unexpectedly.
	 */
	@Test
	public void copy_autoCloseStreams() throws IOException {

		fetcher.copy(in, out, true, true);

		Assert.assertArrayEquals(CHARS, outBuffer.toCharArray());
		Assert.assertTrue(in.isClosed());
		Assert.assertTrue(out.isClosed());

	}

	/**
	 * Fetch into an {@link OutputStream}, closing both stream.
	 * 
	 * @throws IOException
	 *             If the copy process failed unexpectedly.
	 */
	@Test
	public void copy_autoCloseStreams_inputStreamOnly() throws IOException {

		fetcher.copy(in, out, true, false);

		Assert.assertArrayEquals(CHARS, outBuffer.toCharArray());
		Assert.assertTrue(in.isClosed());
		Assert.assertFalse(out.isClosed());

	}

	/**
	 * Fetch into an {@link OutputStream}, closing both stream.
	 * 
	 * @throws IOException
	 *             If the copy process failed unexpectedly.
	 */
	@Test
	public void copy_autoCloseStreams_outputStreamOnly() throws IOException {

		fetcher.copy(in, out, false, true);

		Assert.assertArrayEquals(CHARS, outBuffer.toCharArray());
		Assert.assertFalse(in.isClosed());
		Assert.assertTrue(out.isClosed());

	}

	/**
	 * Fetch nothing into an {@link OutputStream}, {@literal null} as an
	 * {@link InputStream}, which should yield an empty array.
	 * 
	 * @throws IOException
	 *             If the copy process failed unexpectedly.
	 */
	@Test
	public void copy_withBadParameters_nullInputStream() throws IOException {

		fetcher.copy(null, out);

		Assert.assertTrue(0 == outBuffer.toCharArray().length);

	}

	/**
	 * Fetch into an {@link OutputStream}, {@literal null} as an
	 * {@link OutputStream}, which should read the {@link InputStream} anyway.
	 * 
	 * @throws IOException
	 *             If the copy process failed unexpectedly.
	 */
	@Test
	public void copy_withBadParameters_nullOutputStream() throws IOException {

		fetcher.copy(in, null);

		Assert.assertTrue(-1 == in.read());

	}

	/**
	 * Fetch nothing into an {@link OutputStream}, {@literal null} as an
	 * {@link InputStream}, closing both streams, which should yield an empty
	 * array.
	 * 
	 * @throws IOException
	 *             If the copy process failed unexpectedly.
	 */
	@Test
	public void copy_withBadParameters_nullInputStream_autoCloseStreams() throws IOException {

		fetcher.copy(null, out, true, true);

		Assert.assertTrue(0 == outBuffer.toCharArray().length);
		Assert.assertTrue(out.isClosed());

	}

	/**
	 * Fetch into an {@link OutputStream}, {@literal null} as an
	 * {@link InputStream}, which should yield an empty array, closing both
	 * stream.
	 * 
	 * @throws IOException
	 *             If the copy process failed unexpectedly.
	 */
	@Test
	public void copy_withBadParameters_nullOutputStream_autoCloseStreams() throws IOException {

		fetcher.copy(in, null, true, true);

		Assert.assertTrue(in.isClosed());

	}

	/**
	 * Fetch nothing into an {@link OutputStream}, {@literal null} as an
	 * {@link InputStream}, which should yield an empty array, leaving both
	 * streams open.
	 * 
	 * @throws IOException
	 *             If the copy process failed unexpectedly.
	 */
	@Test
	public void copy_withBadParameters_nullStreams_leaveStreamsOpen() throws IOException {

		fetcher.copy(null, null, false, false);

		Assert.assertTrue(0 == outBuffer.toCharArray().length);

	}

	/**
	 * Fetch nothing into an {@link OutputStream}, {@literal null} as an
	 * {@link InputStream}, which should yield an empty array, closing both
	 * stream.
	 * 
	 * @throws IOException
	 *             If the copy process failed unexpectedly.
	 */
	@Test
	public void copy_withBadParameters_nullStreams_autoCloseStreams() throws IOException {

		fetcher.copy(null, null, true, true);

		Assert.assertTrue(0 == outBuffer.toCharArray().length);

	}

	/**
	 * Fetch into an {@link OutputStream}, failing on {@link InputStream#read()}
	 * , leaving both streams open.
	 * 
	 * @throws IOException
	 *             If the copy process failed unexpectedly.
	 */
	@Test
	public void copy_withFailingRead_leavStreamsOpen() throws IOException {

		in.setFailons(FailableReader.FailOn.READ);
		try {
			fetcher.copy(in, out, false, false);
		} catch (IOException e) {
		}

		Assert.assertFalse(in.isClosed());
		Assert.assertFalse(out.isClosed());

	}

	/**
	 * Fetch into an {@link OutputStream}, failing on {@link InputStream#read()}
	 * , closing both stream.
	 * 
	 * @throws IOException
	 *             If the copy process failed unexpectedly.
	 */
	@Test
	public void copy_withFailingRead_autoCloseStreams() throws IOException {

		in.setFailons(FailableReader.FailOn.READ);
		try {
			fetcher.copy(in, out, true, true);
		} catch (IOException e) {
		}

		Assert.assertTrue(in.isClosed());
		Assert.assertTrue(out.isClosed());

	}

	/**
	 * Fetch into an {@link OutputStream}, failing on
	 * {@link OutputStream#write(int)}, which should yield an empty array,
	 * leaving both streams open.
	 * 
	 * @throws IOException
	 *             If the copy process failed unexpectedly.
	 */
	@Test
	public void copy_withFailingWrite_leavStreamsOpen() throws IOException {

		out.setFailons(FailableWriter.FailOn.WRITE);
		try {
			fetcher.copy(in, out, false, false);
		} catch (IOException e) {
		}

		Assert.assertTrue(0 == outBuffer.toCharArray().length);
		Assert.assertFalse(in.isClosed());
		Assert.assertFalse(out.isClosed());

	}

	/**
	 * Fetch into an {@link OutputStream}, failing on
	 * {@link OutputStream#write(int)}, which should yield an empty array,
	 * closing both stream.
	 * 
	 * @throws IOException
	 *             If the copy process failed unexpectedly.
	 */
	@Test
	public void copy_withFailingWrite_autoCloseStreams() throws IOException {

		out.setFailons(FailableWriter.FailOn.WRITE);

		try {
			fetcher.copy(in, out, true, true);
		} catch (IOException e) {
		}

		Assert.assertTrue(0 == outBuffer.toCharArray().length);
		Assert.assertTrue(in.isClosed());
		Assert.assertTrue(out.isClosed());

	}

	/**
	 * Fetch into an {@link OutputStream}, failing on
	 * {@link InputStream#close()} and {@link OutputStream#close()}, leaving
	 * both streams open.
	 * 
	 * @throws IOException
	 *             If the copy process failed unexpectedly.
	 */
	@Test
	public void copy_withFailingClose_leavStreamsOpen() throws IOException {

		in.setFailons(FailableReader.FailOn.CLOSE);
		out.setFailons(FailableWriter.FailOn.CLOSE);
		fetcher.copy(in, out, false, false);

		Assert.assertFalse(in.isClosed());
		Assert.assertFalse(out.isClosed());

	}

	/**
	 * Fetch into an {@link OutputStream}, failing on
	 * {@link InputStream#close()} and {@link OutputStream#close()}, closing
	 * both stream.
	 * 
	 * @throws IOException
	 *             If the copy process failed unexpectedly.
	 */
	@Test
	public void copy_withFailingClose_autoCloseStreams() throws IOException {

		in.setFailons(FailableReader.FailOn.CLOSE);
		out.setFailons(FailableWriter.FailOn.CLOSE);
		fetcher.copy(in, out, true, true);

		Assert.assertTrue(in.isClosed());
		Assert.assertTrue(out.isClosed());

	}

	/**
	 * Read into a new {@code String}, leaving the input stream open by default,
	 * using the basic interfaces {@link Readable} and {@link Appendable}.
	 * 
	 * @throws IOException
	 *             If the fetch process failed unexpectedly.
	 */
	@Test
	public void read_basicInterfaces() throws IOException {

		final String string = "foobar";

		Readable readable = new Readable() {
			private int index = -1;

			@Override
			public int read(CharBuffer cb) throws IOException {
				if (index < string.length() - 1) {
					cb.put(string.charAt(++index));
					return 1;
				} else {
					return -1;
				}
			}
		};

		StringBuilder builder = new StringBuilder();
		fetcher.copy(readable, builder);

		Assert.assertEquals(string, builder.toString());

	}

}
