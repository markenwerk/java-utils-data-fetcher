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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * {@link AbstractFetcher} is a sensible base implementation of {@link Fetcher}.
 * 
 * <p>
 * Implementers must only implement a single simplified method that copies all
 * bytes from an {@link InputStream} to an {@link OutputStream}:
 * {@link AbstractFetcher#doCopy(InputStream, OutputStream, FetchProgressListener)}.
 * 
 * 
 * @author Torsten Krause (tk at markenwerk dot net)
 * @since 2.0.0
 */
public abstract class AbstractFetcher implements Fetcher {

	@Override
	public final byte[] fetch(InputStream in) throws FetchException {
		return fetch(in, NullFetchProgressListener.INSTANCE, false);
	}

	@Override
	public final byte[] fetch(InputStream in, boolean close) throws FetchException {
		return fetch(in, NullFetchProgressListener.INSTANCE, close);
	}

	@Override
	public final byte[] fetch(InputStream in, FetchProgressListener listener) throws FetchException {
		return fetch(in, listener, false);
	}

	@Override
	public final byte[] fetch(InputStream in, FetchProgressListener listener, boolean close) throws FetchException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		copy(in, out, listener, close, true);
		return out.toByteArray();
	}

	@Override
	public final void copy(InputStream in, OutputStream out) throws FetchException {
		copy(in, out, false, false);
	}

	@Override
	public final void copy(InputStream in, OutputStream out, boolean closeIn, boolean closeOut) throws FetchException {
		copy(in, out, NullFetchProgressListener.INSTANCE, closeIn, closeOut);
	}

	@Override
	public final void copy(InputStream in, OutputStream out, FetchProgressListener listener) throws FetchException {
		copy(in, out, listener, false, false);
	}

	@Override
	public final void copy(InputStream in, OutputStream out, FetchProgressListener listener, boolean closeIn,
			boolean closeOut) throws FetchException {
		doCopy(null != in ? in : NullInputStream.INSTANCE, null != out ? out : NullOutputStream.INSTANCE,
				null != listener ? listener : NullFetchProgressListener.INSTANCE, closeIn, closeOut);
	}

	private void doCopy(InputStream in, OutputStream out, FetchProgressListener listener, boolean closeIn,
			boolean closeOut) throws FetchException {
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
	 * Copies the content of a given {@link InputStream} into a given
	 * {@link OutputStream}.
	 * 
	 * <p>
	 * It is guaranteed that neither the given {@link InputStream} nor the given
	 * {@link OutputStream} nor the given {@link FetchProgressListener} is
	 * {@literal null}.
	 * 
	 * <p>
	 * Implementers must not close either of the given streams.
	 * 
	 * @param in
	 *            The {@link InputStream} to read from.
	 * @param out
	 *            The {@link OutputStream} to write to.
	 * @param listener
	 *            The {@link FetchProgressListener} to report to.
	 * @throws FetchException
	 *             If anything went wrong while reading from the given
	 *             {@link InputStream} or writing to the given
	 *             {@link OutputStream}.
	 */
	protected abstract void doCopy(InputStream in, OutputStream out, FetchProgressListener listener)
			throws FetchException;

}
