/*
 * Copyright (c) 2016 Torsten Krause, Markenwerk GmbH
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

import java.io.Reader;
import java.io.Writer;

/**
 * {@link AbstractBufferedCharacterFetcher} is a sensible base implementation of
 * {@link CharacterFetcher} that uses a {@code char[]} as buffer, while copying
 * all chars from an {@link Reader} to an {@link Writer} by sequentially reading
 * from the {@link Reader} into the buffer and then writing from the buffer to
 * the {@link Writer}.
 * 
 * <p>
 * The buffer is eagerly allocated in the constructor just once and then used
 * for every operation. A {@link BufferedCharacterFetcher} is therefore not
 * threadsafe.
 * 
 * <p>
 * After a buffer has been used, it is overwritten with zeros, to remove any
 * buffered information from the memory.
 * 
 * @author Torsten Krause (tk at markenwerk dot net)
 * @since 3.0.0
 */
public final class BufferedCharacterFetcher extends AbstractBufferedCharacterFetcher {

	private final char[] buffer;

	/**
	 * Creates a new {@link BufferedCharacterFetcher} with the default buffer
	 * size of 1024 characters.
	 */
	public BufferedCharacterFetcher() {
		this(DEFAULT_BUFEFR_SIZE);
	}

	/**
	 * Creates a new {@link BufferedCharacterFetcher} with the given buffer
	 * size.
	 * 
	 * @param bufferSize
	 *            The buffer size.
	 */
	public BufferedCharacterFetcher(int bufferSize) {
		buffer = createBuffer(bufferSize);
	}

	@Override
	protected char[] obtainBuffer() {
		return buffer;
	}

	@Override
	protected void returnBuffer(char[] buffer) {
		cleanBuffer();
	}

	private void cleanBuffer() {
		for (int i = 0, n = buffer.length; i < n; i++) {
			buffer[i] = 0;
		}
	}

}
