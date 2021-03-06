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

import java.io.InputStream;
import java.io.OutputStream;

/**
 * {@link AbstractBufferedDataFetcher} is a sensible base implementation of
 * {@link DataFetcher} that uses a {@code byte[]} as buffer, while copying all
 * bytes from an {@link InputStream} to an {@link OutputStream} by sequentially
 * reading from the {@link InputStream} into the buffer and then writing from
 * the buffer to the {@link OutputStream}.
 * 
 * <p>
 * The buffer is eagerly allocated in the constructor just once and then used
 * for every operation. A {@link BufferedDataFetcher} is therefore not
 * threadsafe.
 * 
 * <p>
 * After a buffer has been used, it is overwritten with zeros, to remove any
 * buffered information from the memory.
 * 
 * @author Torsten Krause (tk at markenwerk dot net)
 * @since 4.0.0
 */
public final class BufferedDataFetcher extends AbstractBufferedDataFetcher {

	private final byte[] buffer;

	/**
	 * Creates a new {@link BufferedDataFetcher} with the default buffer size of
	 * 1024 bytes.
	 */
	public BufferedDataFetcher() {
		this(DEFAULT_BUFEFR_SIZE);
	}

	/**
	 * Creates a new {@link BufferedDataFetcher} with the given buffer size.
	 * 
	 * @param bufferSize
	 *            The buffer size.
	 */
	public BufferedDataFetcher(int bufferSize) {
		buffer = createBuffer(bufferSize);
	}

	@Override
	protected byte[] obtainBuffer() {
		return buffer;
	}

	@Override
	protected void returnBuffer(byte[] buffer) {
		cleanBuffer();
	}

	private void cleanBuffer() {
		for (int i = 0, n = buffer.length; i < n; i++) {
			buffer[i] = 0;
		}
	}

}
