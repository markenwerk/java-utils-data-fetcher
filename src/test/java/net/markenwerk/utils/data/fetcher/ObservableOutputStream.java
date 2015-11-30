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
import java.io.OutputStream;

/**
 * Wrapper for an another {@link OutputStream} that can tell, if
 * {@link OutputStream#close()} has been called;
 * 
 * @author Torsten Krause (tk at markenwerk dot net)
 * @since 1.0.0
 */
class ObservableOutputStream extends OutputStream {

	private final OutputStream out;

	private boolean closed;

	/**
	 * Creates a new {@link ObservableOutputStream} from a given
	 * {@link OutputStream}.
	 * 
	 * @param out
	 *            The {@link OutputStream} to wrap.
	 */
	public ObservableOutputStream(OutputStream out) {
		this.out = out;
	}

	@Override
	public void write(int b) throws IOException {
		out.write(b);
	}

	@Override
	public void close() throws IOException {
		if (closed) {
			throw new IllegalStateException("Outputstream has already been closed");
		}
		super.close();
		closed = true;
	}

	/**
	 * Returns whether {@link OutputStream#close()} has been called on this
	 * object.
	 * 
	 * @return {@literal true}, if {@link OutputStream#close()} has been called,
	 *         {@literal false} otherwise.
	 */
	public boolean isClosed() {
		return closed;
	}

}
