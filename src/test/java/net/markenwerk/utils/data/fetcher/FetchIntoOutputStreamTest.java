/*
 * Copyright (c) 2015 Torsten Krause, Markenwerk GmbH
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
import java.io.OutputStream;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit tests for {@link Fetcher#fetch(java.io.Streams)} methods that fetch the
 * content of an {@link Streams} into an {@link OutputStream}.
 * 
 * @author Torsten Krause (tk at markenwerk dot net)
 * @since 1.0.0
 */
public class FetchIntoOutputStreamTest {

	private static final byte[] BYTES = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };

	private ObservableInputStream in;

	private ByteArrayOutputStream outBuffer;

	private ObservableOutputStream out;

	/**
	 * Create a new {@link ObservableStreams} from an {@link ByteArrayStreams}
	 * that reads some BYTES.
	 */
	@Before
	public void prepareStreams() {
		in = new ObservableInputStream(new ByteArrayInputStream(BYTES));
	}

	/**
	 * Close the {@link ObservableStreams} created by
	 * {@link FetchIntoByteArrayTest#prepareStreams()}.
	 */
	@After
	public void closeStreams() {
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
		out = new ObservableOutputStream(outBuffer);
	}

	/**
	 * Close the {@link ObservableOutputStream} created by
	 * {@link FetchIntoByteArrayTest#prepareOutputStream()}.
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
	 * Fetch BYTES into an {@link OutputStream}, leaving both streams open by
	 * default.
	 * 
	 * @throws IOException
	 */
	@Test
	public void fetch_leaveStreamsOpenByDefault() throws IOException {

		Fetcher.fetch(in, out);

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
	public void fetch_leaveStreamsOpen() throws IOException {

		Fetcher.fetch(in, out, false, false);

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
	public void fetch_autoCloseStreams() throws IOException {

		Fetcher.fetch(in, out, true, true);

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
	public void fetch_autoCloseStreamsInputStreamOnly() throws IOException {

		Fetcher.fetch(in, out, true, false);

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
	public void fetch_autoCloseStreamsOutputStreamOnly() throws IOException {

		Fetcher.fetch(in, out, false, true);

		Assert.assertArrayEquals(BYTES, outBuffer.toByteArray());
		Assert.assertFalse(in.isClosed());
		Assert.assertTrue(out.isClosed());

	}

	/**
	 * Fetch BYTES into an {@link OutputStream} with a small buffer (smaller
	 * than the content of the {@link Streams}), leaving both streams open by
	 * default.
	 * 
	 * @throws IOException
	 */
	@Test
	public void fetch_withSmallBuffer_leaveStreamsOpenByDefault() throws IOException {

		Fetcher.fetch(in, out, 1);

		Assert.assertArrayEquals(BYTES, outBuffer.toByteArray());
		Assert.assertFalse(in.isClosed());
		Assert.assertFalse(out.isClosed());

	}

	/**
	 * Fetch BYTES into an {@link OutputStream} with a small buffer (smaller
	 * than the content of the {@link Streams}), leaving both streams explicitly
	 * open.
	 * 
	 * @throws IOException
	 */
	@Test
	public void fetch_withSmallBuffer_leaveStreamsOpen() throws IOException {

		Fetcher.fetch(in, out, 1, false, false);

		Assert.assertArrayEquals(BYTES, outBuffer.toByteArray());
		Assert.assertFalse(in.isClosed());
		Assert.assertFalse(out.isClosed());

	}

	/**
	 * Fetch BYTES into an {@link OutputStream} with a small buffer (smaller
	 * than the content of the {@link Streams}), closing both streams.
	 * 
	 * @throws IOException
	 */
	@Test
	public void fetch_withSmallBuffer_autoCloseStreams() throws IOException {

		Fetcher.fetch(in, out, 1, true, true);

		Assert.assertArrayEquals(BYTES, outBuffer.toByteArray());
		Assert.assertTrue(in.isClosed());
		Assert.assertTrue(out.isClosed());

	}

}
