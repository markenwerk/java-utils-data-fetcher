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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * {@link AbstractBufferedDataFetcher} is a sensible base implementation of
 * {@link DataFetcher} that uses a {@code byte[]} as buffer, to while copying
 * all bytes from an {@link InputStream} to an {@link OutputStream} by
 * sequentially reading from the {@link InputStream} into the buffer and then
 * writing from the buffer to the {@link OutputStream}.
 * 
 * <p>
 * Implementers must only implement the methods
 * {@link AbstractBufferedDataFetcher#obtainBuffer()} and
 * {@link AbstractBufferedDataFetcher#returnBuffer(byte[])} that manage a
 * {@code byte[]} to be used as a buffer in
 * {@link AbstractBufferedDataFetcher#doCopy(InputStream, OutputStream, DataFetchProgressListener)}.
 * 
 * <p>
 * Implementers may also override
 * {@link AbstractBufferedDataFetcher#returnBuffer(byte[])}, which is called
 * after
 * {@link AbstractBufferedDataFetcher#doCopy(InputStream, OutputStream, DataFetchProgressListener)}
 * has finished using it.
 * 
 * @author Torsten Krause (tk at markenwerk dot net)
 * @since 4.0.0
 */
public abstract class AbstractBufferedDataFetcher extends AbstractDataFetcher {

	/**
	 * The default buffer size of 1024 bytes.
	 */
	protected static final int DEFAULT_BUFEFR_SIZE = 1024;

	/**
	 * Safely creates a new {@literal byte[]} to be used as a buffer.
	 * 
	 * @param bufferSize
	 *            The size of the {@literal byte[]} to be created. Defaults to
	 *            the {@link AbstractBufferedDataFetcher#DEFAULT_BUFEFR_SIZE
	 *            default} buffer size, if the given buffer size is not
	 *            positive.
	 * @return The new {@literal byte[]}.
	 */
	protected static final byte[] createBuffer(int bufferSize) {
		return new byte[bufferSize > 0 ? bufferSize : DEFAULT_BUFEFR_SIZE];
	}

	@Override
	protected final void doCopy(InputStream in, OutputStream out, DataFetchProgressListener listener) throws DataFetchException {
		byte[] buffer = obtainBuffer();
		listener.onStarted();
		long total = 0;
		try {
			int length = in.read(buffer);
			while (length != -1) {
				total += length;
				out.write(buffer, 0, length);
				listener.onProgress(total);
				length = in.read(buffer);
			}
			out.flush();
			listener.onProgress(total);
			listener.onSuccedded(total);
		} catch (IOException e) {
			throw createException(listener, total, e);
		} finally {
			listener.onFinished();
			returnBuffer(buffer);
		}
	}

	private DataFetchException createException(DataFetchProgressListener listener, long total, IOException e) {
		DataFetchException fetchException = new DataFetchException("Fetch failed after " + total + " "
				+ (1 == total ? "byte has" : "bytes have") + " been copied successully.", e);
		listener.onFailed(fetchException, total);
		return fetchException;
	}

	/**
	 * Called by
	 * {@link AbstractBufferedDataFetcher#doCopy(InputStream, OutputStream, DataFetchProgressListener)}
	 * to obtain a {@code byte[]} to be used as a buffer.
	 * 
	 * <p>
	 * Every {@code byte[]} that is returned by this method will be passed as an
	 * argument of {@link AbstractBufferedDataFetcher#returnBuffer(byte[])}
	 * after
	 * {@link AbstractBufferedDataFetcher#doCopy(InputStream, OutputStream, DataFetchProgressListener)}
	 * has finished using it.
	 * 
	 * 
	 * @return The a {@code byte[]} to be used as a buffer.
	 */
	protected abstract byte[] obtainBuffer();

	/**
	 * Called by
	 * {@link AbstractBufferedDataFetcher#doCopy(InputStream, OutputStream, DataFetchProgressListener)}
	 * to return a {@code byte[]} that has previously been obtained from
	 * {@link AbstractBufferedDataFetcher#obtainBuffer()}.
	 * 
	 * @param buffer
	 *            The {@code byte[]} to be returned.
	 */
	protected abstract void returnBuffer(byte[] buffer);

}
