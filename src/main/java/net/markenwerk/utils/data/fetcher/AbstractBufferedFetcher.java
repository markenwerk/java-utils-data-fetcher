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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * {@link AbstractBufferedFetcher} is a sensible base implementation of
 * {@link Fetcher} that uses a {@code byte[]} as buffer, to while copying all
 * bytes from an {@link InputStream} to an {@link OutputStream} by sequentually
 * reading from the {@link InputStream} into the buffer and then writing from
 * the buffer to the {@link OutputStream}.
 * 
 * <p>
 * Implementers must only implment a single method that provides a
 * {@code byte[]} to be used as a buffer in
 * {@link AbstractBufferedFetcher#doCopy(InputStream, OutputStream)}:
 * {@link AbstractBufferedFetcher#obtainBuffer()}.
 * 
 * <p>
 * Implementers may also override
 * {@link AbstractBufferedFetcher#returnBuffer(byte[])}, which is called after
 * {@link AbstractBufferedFetcher#doCopy(InputStream, OutputStream)} has
 * finished using it.
 * 
 * @author Torsten Krause (tk at markenwerk dot net)
 * @since 2.0.0
 */
public abstract class AbstractBufferedFetcher extends AbstractFetcher {

	protected static final int DEFAULT_BUFEFR_SIZE = 1024;

	protected static final byte[] createBuffer(int bufferSize) {
		return new byte[bufferSize > 0 ? bufferSize : DEFAULT_BUFEFR_SIZE];
	}

	@Override
	protected final void doCopy(InputStream in, OutputStream out, FetchProgressListener listener) throws FetchException {
		byte[] buffer = obtainBuffer();
		listener.onFetchStarted();
		long total = 0;
		try {
			int length = in.read(buffer);
			while (length != -1) {
				total += length;
				out.write(buffer, 0, length);
				listener.onFetchProgress(total);
				length = in.read(buffer);
			}
			out.flush();
			listener.onFetchSuccedded(total);
		} catch (IOException e) {
			FetchException fetchException = new FetchException(e);
			listener.onFetchFailed(fetchException, total);
			throw fetchException;
		} finally {
			listener.onFetchFinished();
			returnBuffer(buffer);
		}
	}

	/**
	 * Called by
	 * {@link AbstractBufferedFetcher#doCopy(InputStream, OutputStream)} to
	 * obtain a {@code byte[]} to be used as a buffer.
	 * 
	 * <p>
	 * Every {@code bute[]} that is returned by this method will be passed as an
	 * argument of {@link AbstractBufferedFetcher#returnBuffer(byte[])} after
	 * {@link AbstractBufferedFetcher#doCopy(InputStream, OutputStream)} has
	 * finished using it.
	 * 
	 * 
	 * @return The a {@code byte[]} to be used as a buffer.
	 */
	protected abstract byte[] obtainBuffer();

	/**
	 * Called by
	 * {@link AbstractBufferedFetcher#doCopy(InputStream, OutputStream)} to
	 * return a {@code byte[]} that has previously been obtained from
	 * {@link AbstractBufferedFetcher#obtainBuffer()}.
	 * 
	 * @param buffer
	 *            The {@code byte[]} to be returned.
	 */
	protected void returnBuffer(byte[] buffer) {
	}

}
