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
import java.util.Arrays;
import java.util.EnumSet;

/**
 * Wrapper for an another {@link InputStream} that will fail on specific
 * operations;
 * 
 * @author Torsten Krause (tk at markenwerk dot net)
 * @since 1.0.0
 */
class FailableInputStream extends ObservableInputStream {

	/**
	 * Enumeration of operations, a {@link FailableInputStream} can fail on.
	 * 
	 * @author Torsten Krause (tk at markenwerk dot net)
	 * @since 1.1.1
	 *
	 */
	public static enum FailOn {

		/**
		 * Lets a {@link FailingInputStream} fail on
		 * {@link FailingInputStream#read()}.
		 */
		READ,

		/**
		 * Lets a {@link FailingInputStream} fail on
		 * {@link FailingInputStream#close()}.
		 */
		CLOSE;

	}

	private final EnumSet<FailOn> failOns = EnumSet.noneOf(FailOn.class);

	/**
	 * Creates a new {@link FailableInputStream} from a given {@link InputStream}
	 * .
	 * 
	 * @param in
	 *            The {@link OutputStream} to wrap.
	 */
	public FailableInputStream(InputStream in) {
		super(in);
	}

	/**
	 * Specify the operation this {@link FailableInputStream} will fail on.
	 * 
	 * @param failOns
	 *            The operations to fail on.
	 */
	public void setFailons(FailOn... failOns) {
		this.failOns.clear();
		this.failOns.addAll(Arrays.asList(failOns));
	}

	@Override
	public int read() throws IOException {
		failOn(FailOn.READ);
		return super.read();
	}

	@Override
	public void close() throws IOException {
		super.close();
		failOn(FailOn.CLOSE);
	}

	private void failOn(FailOn failOn) throws IOException {
		if (failOns.contains(failOn)) {
			throw new IOException(getClass().getSimpleName() + " was asked to fail on " + failOn.name().toLowerCase()
					+ ".");
		}
	}

}
