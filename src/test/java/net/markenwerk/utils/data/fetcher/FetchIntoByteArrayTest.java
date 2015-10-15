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
import java.io.IOException;
import java.io.InputStream;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit tests for {@link Fetcher#fetch(java.io.InputStream)} methods that fetch
 * the content of an {@link InputStream} into a new {@code byte[]}.
 * 
 * @author Torsten Krause (tk at markenwerk dot net)
 * @since 1.0.0
 */
public class FetchIntoByteArrayTest {

	private static final byte[] BYTES = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };

	private ObservableInputStream in;

	/**
	 * Create a new {@link ObservableInputStream} from an
	 * {@link ByteArrayInputStream} that reads some BYTES.
	 */
	@Before
	public void prepareInputStream() {
		in = new ObservableInputStream(new ByteArrayInputStream(BYTES));
	}

	/**
	 * Close the {@link ObservableInputStream} created by
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
	 * Fetch BYTES into a new {@code byte[]}, leaving the input stream open by
	 * default.
	 * 
	 * @throws IOException
	 */
	@Test
	public void fetch_leaveStreamOpenByDefault() throws IOException {

		byte[] bytes = Fetcher.fetch(in);

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

		byte[] bytes = Fetcher.fetch(in, false);

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

		byte[] bytes = Fetcher.fetch(in, true);

		Assert.assertArrayEquals(BYTES, bytes);
		Assert.assertTrue(in.isClosed());

	}

	/**
	 * Fetch BYTES into a new {@code byte[]} with a small buffer (smaller than
	 * the content of the {@link InputStream}), leaving the input stream open by
	 * default.
	 * 
	 * @throws IOException
	 */
	@Test
	public void fetch_withSmallBuffer_leaveStreamOpenByDefault() throws IOException {

		byte[] bytes = Fetcher.fetch(in, 1);

		Assert.assertArrayEquals(BYTES, bytes);
		Assert.assertFalse(in.isClosed());

	}

	/**
	 * Fetch BYTES into a new {@code byte[]} with a small buffer (smaller than
	 * the content of the {@link InputStream}), leaving the input stream
	 * explicitly open.
	 * 
	 * @throws IOException
	 */
	@Test
	public void fetch_withSmallBuffer_leaveStreamOpen() throws IOException {

		byte[] bytes = Fetcher.fetch(in, 1, false);

		Assert.assertArrayEquals(BYTES, bytes);
		Assert.assertFalse(in.isClosed());

	}

	/**
	 * Fetch BYTES into a new {@code byte[]} with a small buffer (smaller than
	 * the content of the {@link InputStream}), closing the input stream.
	 * 
	 * @throws IOException
	 */
	@Test
	public void fetch_withSmallBuffer_autoCloseStream() throws IOException {

		byte[] bytes = Fetcher.fetch(in, 1, true);

		Assert.assertArrayEquals(BYTES, bytes);
		Assert.assertTrue(in.isClosed());

	}

}
