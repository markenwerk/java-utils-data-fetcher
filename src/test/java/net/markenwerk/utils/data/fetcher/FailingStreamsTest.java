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
 * @since 1.0.0
 */
public class FailingStreamsTest {

	private static final byte[] BYTES = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };

	private FailingInputStream in;

	private ByteArrayOutputStream outBuffer;

	private FailingOutputStream out;

	/**
	 * Create a new {@link ObservableStreams} from an {@link ByteArrayStreams}
	 * that reads some BYTES.
	 */
	@Before
	public void prepareInputStream() {
		in = new FailingInputStream(new ByteArrayInputStream(BYTES));
	}

	/**
	 * Close the {@link ObservableStreams} created by
	 * {@link FetchIntoByteArrayTest#prepareInputStream()}.
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
		out = new FailingOutputStream(outBuffer);
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
	 * Fetch BYTES into an {@link OutputStream}, failing on
	 * {@link InputStream#read()}, leaving both streams open.
	 * 
	 * @throws IOException
	 */
	@Test
	public void fetch_withFailingRead_leavStreamsOpen() throws IOException {

		in.setFailons(FailingInputStream.FailOn.READ);
		try {
			Fetcher.fetch(in, out, false, false);
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
	public void fetch_withFailingRead_autoCloseStreams() throws IOException {

		in.setFailons(FailingInputStream.FailOn.READ);
		try {
			Fetcher.fetch(in, out, true, true);
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
	public void fetch_withFailingWrite_leavStreamsOpen() throws IOException {

		out.setFailons(FailingOutputStream.FailOn.WRITE);
		try {
			Fetcher.fetch(in, out, false, false);
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
	public void fetch_withFailingWrite_autoCloseStreams() throws IOException {

		out.setFailons(FailingOutputStream.FailOn.WRITE);

		try {
			Fetcher.fetch(in, out, true, true);
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
	public void fetch_withFailingClose_leavStreamsOpen() throws IOException {

		in.setFailons(FailingInputStream.FailOn.CLOSE);
		out.setFailons(FailingOutputStream.FailOn.CLOSE);
		Fetcher.fetch(in, out, false, false);

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
	public void fetch_withFailingClose_autoCloseStreams() throws IOException {

		in.setFailons(FailingInputStream.FailOn.CLOSE);
		out.setFailons(FailingOutputStream.FailOn.CLOSE);
		Fetcher.fetch(in, out, true, true);

		Assert.assertTrue(in.isClosed());
		Assert.assertTrue(out.isClosed());

	}

}
