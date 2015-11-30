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
 * 
 * @author Torsten Krause (tk at markenwerk dot net)
 * @since 2.0.0
 */
public abstract class AbstractFetcher implements Fetcher {

	@Override
	public final byte[] fetch(InputStream in) throws FetchException {
		return fetch(in, false);
	}

	@Override
	public final byte[] fetch(InputStream in, boolean close) throws FetchException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		copy(in, out, close, true);
		return out.toByteArray();
	}

	@Override
	public final void copy(InputStream in, OutputStream out) throws FetchException {
		copy(in, out, false, false);
	}

	@Override
	public final void copy(InputStream in, OutputStream out, boolean closeIn, boolean closeOut) throws FetchException {
		try {
			if (null != in) {
				if (null == out) {
					out = new NullOutputStream();
					closeOut = true;
				}
				doCopy(in, out);
				out.flush();
			}
		} catch (IOException e) {
			throw new FetchException(e);
		} finally {
			if (closeIn && null != in) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
			if (closeOut && null != out) {
				try {
					out.close();
				} catch (IOException e) {
				}
			}
		}

	}

	protected abstract void doCopy(InputStream in, OutputStream out) throws IOException;

}
