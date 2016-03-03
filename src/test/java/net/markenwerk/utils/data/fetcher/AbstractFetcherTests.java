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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit tests for {@link Fetcher#fetch(java.io.Streams)} methods on stream that
 * fail.
 * 
 * @author Torsten Krause (tk at markenwerk dot net)
 * @param <ActualFetcher>
 *            The actual {@link Fetcher} type to be tested.
 * @since 1.0.0
 */
public abstract class AbstractFetcherTests<ActualFetcher extends Fetcher> {

	private static final byte[] BYTES = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };

	private Fetcher fetcher;

	private FailableInputStream in;

	private ByteArrayOutputStream outBuffer;

	private FailableOutputStream out;

	/**
	 * Create a new {@link Fetcher}.
	 */
	@Before
	public void prepareFetcher() {
		fetcher = createFetcher();
	}

	protected abstract ActualFetcher createFetcher();

	/**
	 * Create a new {@link ObservableStreams} from an {@link ByteArrayStreams}
	 * that reads some BYTES.
	 */
	@Before
	public void prepareInputStream() {
		in = new FailableInputStream(new ByteArrayInputStream(BYTES));
	}

	/**
	 * Close the {@link ObservableStreams} created by
	 * {@link AbstractFetcherTests#prepareInputStream()}.
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
	 * Create a new {@link ObservableOutputStream} from an
	 * {@link ByteArrayOutputStream}..
	 */
	@Before
	public void prepareOutputStream() {
		outBuffer = new ByteArrayOutputStream();
		out = new FailableOutputStream(outBuffer);
	}

	/**
	 * Close the {@link ObservableOutputStream} created by
	 * {@link AbstractFetcherTests#prepareOutputStream()}.
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
	 * Fetch BYTES into a new {@code byte[]}, leaving the input stream open by
	 * default.
	 * 
	 * @throws IOException
	 */
	@Test
	public void fetch_leaveStreamOpenByDefault() throws IOException {

		byte[] bytes = fetcher.fetch(in);

		Assert.assertArrayEquals(BYTES, bytes);
		Assert.assertFalse(in.isClosed());

	}

	/**
	 * Fetch BYTES into a new {@code byte[]}, leaving the input stream
	 * explicitly open.
	 * 
	 * @throws IOException
	 */
	@Test
	public void fetch_leaveStreamOpen() throws IOException {

		byte[] bytes = fetcher.fetch(in, false);

		Assert.assertArrayEquals(BYTES, bytes);
		Assert.assertFalse(in.isClosed());

	}

	/**
	 * Fetch BYTES into a new {@code byte[]}, closing the input stream.
	 * 
	 * @throws IOException
	 */
	@Test
	public void fetch_autoCloseStream() throws IOException {

		byte[] bytes = fetcher.fetch(in, true);

		Assert.assertArrayEquals(BYTES, bytes);
		Assert.assertTrue(in.isClosed());

	}

	/**
	 * Fetch BYTES into a new {@code byte[]}, {@literal null} as an
	 * {@link InputStream}, which should yield an empty array.
	 * 
	 * @throws IOException
	 */
	@Test
	public void fetch_withBadParameters_nullStream() throws IOException {

		byte[] bytes = fetcher.fetch(null);

		Assert.assertNotNull(bytes);
		Assert.assertTrue(0 == bytes.length);

	}

	/**
	 * Fetch BYTES into an {@link OutputStream}, leaving both streams open by
	 * default.
	 * 
	 * @throws IOException
	 */
	@Test
	public void copy_leaveStreamsOpenByDefault() throws IOException {

		fetcher.copy(in, out);

		Assert.assertArrayEquals(BYTES, outBuffer.toByteArray());
		Assert.assertFalse(in.isClosed());
		Assert.assertFalse(out.isClosed());

	}

	/**
	 * Fetch BYTES into an {@link OutputStream}, leaving both streams explicitly
	 * open.
	 * 
	 * @throws IOException
	 */
	@Test
	public void copy_leaveStreamsOpen() throws IOException {

		fetcher.copy(in, out, false, false);

		Assert.assertArrayEquals(BYTES, outBuffer.toByteArray());
		Assert.assertFalse(in.isClosed());
		Assert.assertFalse(out.isClosed());

	}

	/**
	 * Fetch BYTES into an {@link OutputStream}, closing both stream.
	 * 
	 * @throws IOException
	 */
	@Test
	public void copy_autoCloseStreams() throws IOException {

		fetcher.copy(in, out, true, true);

		Assert.assertArrayEquals(BYTES, outBuffer.toByteArray());
		Assert.assertTrue(in.isClosed());
		Assert.assertTrue(out.isClosed());

	}

	/**
	 * Fetch BYTES into an {@link OutputStream}, closing both stream.
	 * 
	 * @throws IOException
	 */
	@Test
	public void copy_autoCloseStreams_inputStreamOnly() throws IOException {

		fetcher.copy(in, out, true, false);

		Assert.assertArrayEquals(BYTES, outBuffer.toByteArray());
		Assert.assertTrue(in.isClosed());
		Assert.assertFalse(out.isClosed());

	}

	/**
	 * Fetch BYTES into an {@link OutputStream}, closing both stream.
	 * 
	 * @throws IOException
	 */
	@Test
	public void copy_autoCloseStreams_outputStreamOnly() throws IOException {

		fetcher.copy(in, out, false, true);

		Assert.assertArrayEquals(BYTES, outBuffer.toByteArray());
		Assert.assertFalse(in.isClosed());
		Assert.assertTrue(out.isClosed());

	}

	/**
	 * Fetch nothing into an {@link OutputStream}, {@literal null} as an
	 * {@link InputStream}, which should yield an empty array.
	 * 
	 * @throws IOException
	 */
	@Test
	public void copy_withBadParameters_nullInputStream() throws IOException {

		fetcher.copy(null, out);

		Assert.assertTrue(0 == outBuffer.toByteArray().length);

	}

	/**
	 * Fetch BYTES into an {@link OutputStream}, {@literal null} as an
	 * {@link OutputStream}, which should read the {@link InputStream} anyway.
	 * 
	 * @throws IOException
	 */
	@Test
	public void copy_withBadParameters_nullOutputStream() throws IOException {

		fetcher.copy(in, null);

		Assert.assertTrue(0 == in.available());

	}

	/**
	 * Fetch nothing into an {@link OutputStream}, {@literal null} as an
	 * {@link InputStream}, closing both streams, which should yield an empty
	 * array.
	 * 
	 * @throws IOException
	 */
	@Test
	public void copy_withBadParameters_nullInputStream_autoCloseStreams() throws IOException {

		fetcher.copy(null, out, true, true);

		Assert.assertTrue(0 == outBuffer.toByteArray().length);
		Assert.assertTrue(out.isClosed());

	}

	/**
	 * Fetch BYTES into an {@link OutputStream}, {@literal null} as an
	 * {@link InputStream}, which should yield an empty array, closing both
	 * stream.
	 * 
	 * @throws IOException
	 */
	@Test
	public void copy_withBadParameters_nullOutputStream_autoCloseStreams() throws IOException {

		fetcher.copy(in, null, true, true);

		Assert.assertTrue(0 == in.available());
		Assert.assertTrue(in.isClosed());

	}

	/**
	 * Fetch nothing into an {@link OutputStream}, {@literal null} as an
	 * {@link InputStream}, which should yield an empty array, leaving both
	 * streas open.
	 * 
	 * @throws IOException
	 */
	@Test
	public void copy_withBadParameters_nullStreams_leaveStreamsOpen() throws IOException {

		fetcher.copy(null, null, false, false);

		Assert.assertTrue(0 == outBuffer.toByteArray().length);

	}

	/**
	 * Fetch nothing into an {@link OutputStream}, {@literal null} as an
	 * {@link InputStream}, which should yield an empty array, closing both
	 * stream.
	 * 
	 * @throws IOException
	 */
	@Test
	public void copy_withBadParameters_nullStreams_autoCloseStreams() throws IOException {

		fetcher.copy(null, null, true, true);

		Assert.assertTrue(0 == outBuffer.toByteArray().length);

	}

	/**
	 * Fetch BYTES into an {@link OutputStream}, failing on
	 * {@link InputStream#read()}, leaving both streams open.
	 * 
	 * @throws IOException
	 */
	@Test
	public void copy_withFailingRead_leavStreamsOpen() throws IOException {

		in.setFailons(FailableInputStream.FailOn.READ);
		try {
			fetcher.copy(in, out, false, false);
		} catch (IOException e) {
		}

		Assert.assertTrue(BYTES.length == in.available());
		Assert.assertFalse(in.isClosed());
		Assert.assertFalse(out.isClosed());

	}

	/**
	 * Fetch BYTES into an {@link OutputStream}, failing on
	 * {@link InputStream#read()}, closing both stream.
	 * 
	 * @throws IOException
	 */
	@Test
	public void copy_withFailingRead_autoCloseStreams() throws IOException {

		in.setFailons(FailableInputStream.FailOn.READ);
		try {
			fetcher.copy(in, out, true, true);
		} catch (IOException e) {
		}

		Assert.assertTrue(BYTES.length == in.available());
		Assert.assertTrue(in.isClosed());
		Assert.assertTrue(out.isClosed());

	}

	/**
	 * Fetch BYTES into an {@link OutputStream}, failing on
	 * {@link OutputStream#write(int)}, which should yield an empty array,
	 * leaving both streams open.
	 * 
	 * @throws IOException
	 */
	@Test
	public void copy_withFailingWrite_leavStreamsOpen() throws IOException {

		out.setFailons(FailableOutputStream.FailOn.WRITE);
		try {
			fetcher.copy(in, out, false, false);
		} catch (IOException e) {
		}

		Assert.assertTrue(0 == outBuffer.toByteArray().length);
		Assert.assertFalse(in.isClosed());
		Assert.assertFalse(out.isClosed());

	}

	/**
	 * Fetch BYTES into an {@link OutputStream}, failing on
	 * {@link OutputStream#write(int)}, which should yield an empty array,
	 * closing both stream.
	 * 
	 * @throws IOException
	 */
	@Test
	public void copy_withFailingWrite_autoCloseStreams() throws IOException {

		out.setFailons(FailableOutputStream.FailOn.WRITE);

		try {
			fetcher.copy(in, out, true, true);
		} catch (IOException e) {
		}

		Assert.assertTrue(0 == outBuffer.toByteArray().length);
		Assert.assertTrue(in.isClosed());
		Assert.assertTrue(out.isClosed());

	}

	/**
	 * Fetch BYTES into an {@link OutputStream}, failing on
	 * {@link InputStream#close()} and {@link OutputStream#close()}, leaving
	 * both streams open.
	 * 
	 * @throws IOException
	 */
	@Test
	public void copy_withFailingClose_leavStreamsOpen() throws IOException {

		in.setFailons(FailableInputStream.FailOn.CLOSE);
		out.setFailons(FailableOutputStream.FailOn.CLOSE);
		fetcher.copy(in, out, false, false);

		Assert.assertFalse(in.isClosed());
		Assert.assertFalse(out.isClosed());

	}

	/**
	 * Fetch BYTES into an {@link OutputStream}, failing on
	 * {@link InputStream#close()} and {@link OutputStream#close()}, closing
	 * both stream.
	 * 
	 * @throws IOException
	 */
	@Test
	public void copy_withFailingClose_autoCloseStreams() throws IOException {

		in.setFailons(FailableInputStream.FailOn.CLOSE);
		out.setFailons(FailableOutputStream.FailOn.CLOSE);
		fetcher.copy(in, out, true, true);

		Assert.assertTrue(in.isClosed());
		Assert.assertTrue(out.isClosed());

	}

}
