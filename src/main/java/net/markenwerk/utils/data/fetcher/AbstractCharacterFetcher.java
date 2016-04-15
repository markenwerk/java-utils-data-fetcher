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

import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

/**
 * {@link AbstractCharacterFetcher} is a sensible base implementation of
 * {@link CharacterFetcher}.
 * 
 * <p>
 * Implementers must only implement a single simplified method that copies all
 * characters from an {@link Reader} to an {@link Writer}:
 * {@link AbstractCharacterFetcher#doCopy(Reader, Writer, FetchProgressListener)}.
 * 
 * 
 * @author Torsten Krause (tk at markenwerk dot net)
 * @since 3.0.0
 */
public abstract class AbstractCharacterFetcher implements CharacterFetcher {

	@Override
	public final char[] fetch(Reader in) throws FetchException {
		return fetch(in, null, false);
	}

	@Override
	public final char[] fetch(Reader in, boolean close) throws FetchException {
		return fetch(in, null, close);
	}

	@Override
	public final char[] fetch(Reader in, FetchProgressListener listener) throws FetchException {
		return fetch(in, listener, false);
	}

	@Override
	public final char[] fetch(Reader in, FetchProgressListener listener, boolean close) throws FetchException {
		CharArrayWriter out = new CharArrayWriter();
		copy(in, out, listener, close, true);
		return out.toCharArray();
	}

	@Override
	public final String read(Reader in) throws FetchException {
		return new String(fetch(in, null, false));
	}

	@Override
	public final String read(Reader in, boolean close) throws FetchException {
		return new String(fetch(in, null, close));
	}

	@Override
	public final String read(Reader in, FetchProgressListener listener) throws FetchException {
		return new String(fetch(in, listener, false));
	}

	@Override
	public final String read(Reader in, FetchProgressListener listener, boolean close) throws FetchException {
		return new String(fetch(in, listener, close));
	}

	@Override
	public final void copy(Reader in, Writer out) throws FetchException {
		copy(in, out, false, false);
	}

	@Override
	public final void copy(Reader in, Writer out, boolean closeIn, boolean closeOut) throws FetchException {
		copy(in, out, null, closeIn, closeOut);
	}

	@Override
	public final void copy(Reader in, Writer out, FetchProgressListener listener) throws FetchException {
		copy(in, out, listener, false, false);
	}

	@Override
	public final void copy(Reader in, Writer out, FetchProgressListener listener, boolean closeIn, boolean closeOut)
			throws FetchException {
		if (null == in) {
			in = NullReader.INSTANCE;
		}
		if (null == out) {
			out = NullWriter.INSTANCE;
		}
		if (null == listener) {
			listener = NullFetchProgressListener.INSTANCE;
		}
		doCopy(in, out, listener, closeIn, closeOut);
	}

	private void doCopy(Reader in, Writer out, FetchProgressListener listener, boolean closeIn, boolean closeOut)
			throws FetchException {
		try {
			doCopy(in, out, listener);
		} catch (FetchException e) {
			throw e;
		} finally {
			if (closeIn) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
			if (closeOut) {
				try {
					out.close();
				} catch (IOException e) {
				}
			}
		}

	}

	/**
	 * Copies the content of a given {@link Reader} into a given {@link Writer}.
	 * 
	 * <p>
	 * It is guaranteed that neither the given {@link Reader} nor the given
	 * {@link Writer} nor the given {@link FetchProgressListener} is
	 * {@literal null}.
	 * 
	 * <p>
	 * Implementers must not close either of the given streams.
	 * 
	 * @param in
	 *            The {@link Reader} to read from.
	 * @param out
	 *            The {@link Writer} to write to.
	 * @param listener
	 *            The {@link FetchProgressListener} to report to.
	 * @throws FetchException
	 *             If anything went wrong while reading from the given
	 *             {@link Reader} or writing to the given {@link Writer}.
	 */
	protected abstract void doCopy(Reader in, Writer out, FetchProgressListener listener) throws FetchException;

}
