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

import java.io.Flushable;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.CharBuffer;

/**
 * {@link AbstractBufferedCharacterFetcher} is a sensible base implementation of
 * {@link CharacterFetcher} that uses a {@code char[]} as buffer, to while
 * copying all chars from an {@link Reader} to an {@link Writer} by sequentially
 * reading from the {@link Reader} into the buffer and then writing from the
 * buffer to the {@link Writer}.
 * 
 * <p>
 * If
 * {@link AbstractBufferedCharacterFetcher#doCopy(Readable, Appendable, FetchProgressListener)}
 * is called with a {@link Reader} and a {@link Writer}, which should be the
 * most common case, the {@code char[]} buffer is used directly, otherwise a
 * {@link CharBuffer} is wrapped around the {@code char[]} buffer.
 * 
 * <p>
 * Implementers must only implement the methods
 * {@link AbstractBufferedCharacterFetcher#obtainBuffer()} and
 * {@link AbstractBufferedCharacterFetcher#returnBuffer(char[])} that manage a
 * {@code char[]} to be used as a buffer in
 * {@link AbstractBufferedCharacterFetcher#doCopy(Readable, Appendable, FetchProgressListener)}.
 * 
 * <p>
 * Implementers may also override
 * {@link AbstractBufferedCharacterFetcher#returnBuffer(char[])}, which is
 * called after
 * {@link AbstractBufferedCharacterFetcher#doCopy(Readable, Appendable, FetchProgressListener)}
 * has finished using it.
 * 
 * @author Torsten Krause (tk at markenwerk dot net)
 * @since 3.0.0
 */
public abstract class AbstractBufferedCharacterFetcher extends AbstractCharacterFetcher {

	/**
	 * The default buffer size of 1024 characters.
	 */
	protected static final int DEFAULT_BUFEFR_SIZE = 1024;

	/**
	 * Safely creates a new {@literal char[]} to be used as a buffer.
	 * 
	 * @param bufferSize
	 *            The size of the {@literal char[]} to be created. Defaults to
	 *            the
	 *            {@link AbstractBufferedCharacterFetcher#DEFAULT_BUFEFR_SIZE
	 *            default} buffer size, if the given buffer size is not
	 *            positive.
	 * @return The new {@literal char[]}.
	 */
	protected static final char[] createBuffer(int bufferSize) {
		return new char[bufferSize > 0 ? bufferSize : DEFAULT_BUFEFR_SIZE];
	}

	@Override
	protected final void doCopy(Readable in, Appendable out, FetchProgressListener listener) throws FetchException {
		char[] buffer = obtainBuffer();
		listener.onFetchStarted();
		try {
			if (in instanceof Reader && out instanceof Writer) {
				doDirectIoCopy(buffer, (Reader) in, (Writer) out, listener);
			} else {
				doCharBufferCopy(buffer, in, out, listener);
			}
		} finally {
			listener.onFetchFinished();
			returnBuffer(buffer);
		}
	}

	private final void doDirectIoCopy(char[] buffer, Reader in, Writer out, FetchProgressListener listener)
			throws FetchException {
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
			listener.onFetchProgress(total);
			listener.onFetchSuccedded(total);
		} catch (IOException e) {
			throw createException(listener, total, e);
		}
	}

	private void doCharBufferCopy(char[] buffer, Readable in, Appendable out, FetchProgressListener listener)
			throws FetchException {
		CharBuffer charBuffer = CharBuffer.wrap(buffer);
		long total = 0;
		try {
			int length = in.read(charBuffer);
			while (length != -1) {
				total += length;
				for (int i = 0; i < length; i++) {
					out.append(charBuffer.get(i));
				}
				charBuffer.clear();
				listener.onFetchProgress(total);
				length = in.read(charBuffer);
			}
			if (out instanceof Flushable) {
				((Flushable) out).flush();
			}
			listener.onFetchProgress(total);
			listener.onFetchSuccedded(total);
		} catch (IOException e) {
			throw createException(listener, total, e);
		}
	}

	private FetchException createException(FetchProgressListener listener, long total, IOException exception) {
		FetchException fetchException = new FetchException("Fetch failed after " + total + " "
				+ (1 == total ? "char has" : "chars have") + " been copied successully.", exception);
		listener.onFetchFailed(fetchException, total);
		return fetchException;
	}

	/**
	 * Called by
	 * {@link AbstractBufferedCharacterFetcher#doCopy(Readable, Appendable, FetchProgressListener)}
	 * to obtain a {@code char[]} to be used as a buffer.
	 * 
	 * <p>
	 * Every {@code char[]} that is returned by this method will be passed as an
	 * argument of {@link AbstractBufferedCharacterFetcher#returnBuffer(char[])}
	 * after
	 * {@link AbstractBufferedCharacterFetcher#doCopy(Readable, Appendable, FetchProgressListener)}
	 * has finished using it.
	 * 
	 * 
	 * @return The a {@code char[]} to be used as a buffer.
	 */
	protected abstract char[] obtainBuffer();

	/**
	 * Called by
	 * {@link AbstractBufferedCharacterFetcher#doCopy(Readable, Appendable, FetchProgressListener)}
	 * to return a {@code char[]} that has previously been obtained from
	 * {@link AbstractBufferedCharacterFetcher#obtainBuffer()}.
	 * 
	 * @param buffer
	 *            The {@code char[]} to be returned.
	 */
	protected abstract void returnBuffer(char[] buffer);

}