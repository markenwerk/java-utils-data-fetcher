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
 * {@link AbstractBufferedByteFetcher} is a sensible base implementation of
 * {@link ByteFetcher} that uses a {@code byte[]} as buffer, to while copying
 * all bytes from an {@link InputStream} to an {@link OutputStream} by
 * sequentially reading from the {@link InputStream} into the buffer and then
 * writing from the buffer to the {@link OutputStream}.
 * 
 * <p>
 * Implementers must only implement the methods
 * {@link AbstractBufferedByteFetcher#obtainBuffer()} and
 * {@link AbstractBufferedByteFetcher#returnBuffer(byte[])} that manage a
 * {@code byte[]} to be used as a buffer in
 * {@link AbstractBufferedByteFetcher#doCopy(InputStream, OutputStream, FetchProgressListener)}.
 * 
 * <p>
 * Implementers may also override
 * {@link AbstractBufferedByteFetcher#returnBuffer(byte[])}, which is called
 * after
 * {@link AbstractBufferedByteFetcher#doCopy(InputStream, OutputStream, FetchProgressListener)}
 * has finished using it.
 * 
 * @author Torsten Krause (tk at markenwerk dot net)
 * @since 2.0.0
 */
public abstract class AbstractBufferedByteFetcher extends AbstractByteFetcher {

	/**
	 * The default buffer size of 1024 bytes.
	 */
	protected static final int DEFAULT_BUFEFR_SIZE = 1024;

	/**
	 * Safely creates a new {@literal byte[]} to be used as a buffer.
	 * 
	 * @param bufferSize
	 *            The size of the {@literal byte[]} to be created. Defaults to
	 *            the {@link AbstractBufferedByteFetcher#DEFAULT_BUFEFR_SIZE
	 *            default} buffer size, if the given buffer size is not
	 *            positive.
	 * @return The new {@literal byte[]}.
	 */
	protected static final byte[] createBuffer(int bufferSize) {
		return new byte[bufferSize > 0 ? bufferSize : DEFAULT_BUFEFR_SIZE];
	}

	@Override
	protected final void doCopy(InputStream in, OutputStream out, FetchProgressListener listener) throws FetchException {
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

	private FetchException createException(FetchProgressListener listener, long total, IOException e) {
		FetchException fetchException = new FetchException("Fetch failed after " + total + " "
				+ (1 == total ? "byte has" : "bytes have") + " been copied successully.", e);
		listener.onFailed(fetchException, total);
		return fetchException;
	}

	/**
	 * Called by
	 * {@link AbstractBufferedByteFetcher#doCopy(InputStream, OutputStream, FetchProgressListener)}
	 * to obtain a {@code byte[]} to be used as a buffer.
	 * 
	 * <p>
	 * Every {@code byte[]} that is returned by this method will be passed as an
	 * argument of {@link AbstractBufferedByteFetcher#returnBuffer(byte[])}
	 * after
	 * {@link AbstractBufferedByteFetcher#doCopy(InputStream, OutputStream, FetchProgressListener)}
	 * has finished using it.
	 * 
	 * 
	 * @return The a {@code byte[]} to be used as a buffer.
	 */
	protected abstract byte[] obtainBuffer();

	/**
	 * Called by
	 * {@link AbstractBufferedByteFetcher#doCopy(InputStream, OutputStream, FetchProgressListener)}
	 * to return a {@code byte[]} that has previously been obtained from
	 * {@link AbstractBufferedByteFetcher#obtainBuffer()}.
	 * 
	 * @param buffer
	 *            The {@code byte[]} to be returned.
	 */
	protected abstract void returnBuffer(byte[] buffer);

}
