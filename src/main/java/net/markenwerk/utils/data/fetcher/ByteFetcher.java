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

import java.io.InputStream;
import java.io.OutputStream;

/**
 * A {@link ByteFetcher} can fetch the entire content of a given
 * {@link InputStream} into a fresh {@code byte[]} or copy it into a given
 * {@link OutputStream}.
 * 
 * <p>
 * It is, among other things, intended as a replacement for
 * {@code IOUtils.toByteArray()} to perform the everyday and menial task of
 * reading an entire {@code InputStream}.
 * 
 * <p>
 * Since the class {@code sun.misc.IOUtils}, which provides this functionality,
 * is not a part of the official Java specification, usages of this class will
 * usually yield a compiler warning like "{@code The type 'IOUtils' is not API}"
 * and might break at runtime, if a runtime environment other than the Oracle
 * JRE is used.
 * 
 * <p>
 * All methods take optional parameters to specify the buffer size and to
 * specify, whether the provided streams should be closed afterwards. This
 * allows to write compact code like
 * 
 * <pre>
 * {
 * 	foo.setContent(fetcher.fetch(new FileInputStream(file), true));
 * }
 * </pre>
 * 
 * instead of unnecessarily verbose code like
 * 
 * <pre>
 * {
 * 	InputStream in = new FileInputStream(file);
 * 	foo.setContent(fetcher.fetch(in));
 * 	in.close();
 * }
 * </pre>
 * 
 * @author Torsten Krause (tk at markenwerk dot net)
 * @since 2.0.0
 */
public interface ByteFetcher {

	/**
	 * Fetches the content of a given {@link InputStream} into a fresh byte[].
	 *
	 * <p>
	 * See {@link ByteFetcher#copy(InputStream, OutputStream, boolean, boolean)}
	 * for the handling of missing or invalid arguments.
	 * 
	 * @param in
	 *            The {@link InputStream} to read from.
	 * @return A new {@code byte[]}, containing the content of the given
	 *         {@link InputStream}.
	 * 
	 * @throws FetchException
	 *             If anything went wrong while reading from the given
	 *             {@link InputStream}.
	 */
	public byte[] fetch(InputStream in) throws FetchException;

	/**
	 * Fetches the content of a given {@link InputStream} into a fresh byte[].
	 *
	 * <p>
	 * See {@link ByteFetcher#copy(InputStream, OutputStream, boolean, boolean)}
	 * for the handling of missing or invalid arguments.
	 * 
	 * @param in
	 *            The {@link InputStream} to read from.
	 * @param close
	 *            Whether to close the given {@link InputStream}, after reading
	 *            from it.
	 * @return A new {@code byte[]}, containing the content of the given
	 *         {@link InputStream}.
	 * 
	 * @throws FetchException
	 *             If anything went wrong while reading from the given
	 *             {@link InputStream}. {@link FetchException FetchExceptions}
	 *             thrown while trying to close the given {@link InputStream},
	 *             if requested, are ignored.
	 */
	public byte[] fetch(InputStream in, boolean close) throws FetchException;

	/**
	 * Fetches the content of a given {@link InputStream} into a fresh byte[].
	 *
	 * <p>
	 * See {@link ByteFetcher#copy(InputStream, OutputStream, boolean, boolean)}
	 * for the handling of missing or invalid arguments.
	 * 
	 * @param in
	 *            The {@link InputStream} to read from.
	 * @param listener
	 *            The {@link FetchProgressListener} to report to.
	 * @param close
	 *            Whether to close the given {@link InputStream}, after reading
	 *            from it.
	 * @return A new {@code byte[]}, containing the content of the given
	 *         {@link InputStream}.
	 * 
	 * @throws FetchException
	 *             If anything went wrong while reading from the given
	 *             {@link InputStream}. {@link FetchException FetchExceptions}
	 *             thrown while trying to close the given {@link InputStream},
	 *             if requested, are ignored.
	 */
	public byte[] fetch(InputStream in, FetchProgressListener listener, boolean close) throws FetchException;

	/**
	 * Fetches the content of a given {@link InputStream} into a fresh byte[].
	 *
	 * <p>
	 * See {@link ByteFetcher#copy(InputStream, OutputStream, boolean, boolean)}
	 * for the handling of missing or invalid arguments.
	 * 
	 * @param in
	 *            The {@link InputStream} to read from.
	 * @param listener
	 *            The {@link FetchProgressListener} to report to.
	 * @return A new {@code byte[]}, containing the content of the given
	 *         {@link InputStream}.
	 * 
	 * @throws FetchException
	 *             If anything went wrong while reading from the given
	 *             {@link InputStream}.
	 */
	public byte[] fetch(InputStream in, FetchProgressListener listener) throws FetchException;

	/**
	 * Copies the content of a given {@link InputStream} into a given
	 * {@link OutputStream}.
	 *
	 * <p>
	 * See {@link ByteFetcher#copy(InputStream, OutputStream, boolean, boolean)}
	 * for the handling of missing or invalid arguments.
	 * 
	 * @param in
	 *            The {@link InputStream} to read from.
	 * @param out
	 *            The {@link OutputStream} to write to.
	 * 
	 * @throws FetchException
	 *             If anything went wrong while reading from the given
	 *             {@link InputStream} or writing to the given
	 *             {@link OutputStream}.
	 */
	public void copy(InputStream in, OutputStream out) throws FetchException;

	/**
	 * Copies the content of a given {@link InputStream} into a given
	 * {@link OutputStream}.
	 * 
	 * <p>
	 * Missing or invalid arguments are handled gracefully with the following
	 * behaviour.
	 * 
	 * <p>
	 * A {@code null} is given as an {@link InputStream}, it is simply ignored
	 * and handled as if there was nothing to read. If {@code closeOut} is
	 * {@literal true}, the given {@link OutputStream} will be closed anyway
	 * 
	 * <p>
	 * A {@code null} is given as an {@link OutputStream}, it is simply ignored,
	 * but the content of given {@link InputStream} is fetched anyway. If
	 * {@code closeIn} is {@literal true}, the given {@link InputStream} will be
	 * closed anyway
	 * 
	 * @param in
	 *            The {@link InputStream} to read from.
	 * @param out
	 *            The {@link OutputStream} to write to.
	 * @param closeIn
	 *            Whether to close the given {@link InputStream}, after reading
	 *            from it.
	 * @param closeOut
	 *            Whether to close the given {@link OutputStream}, after writing
	 *            to it.
	 * 
	 * @throws FetchException
	 *             If anything went wrong while reading from the given
	 *             {@link InputStream} or writing to the given
	 *             {@link OutputStream}. {@link FetchException FetchExceptions}
	 *             thrown while trying to close one of the given streams, if
	 *             requested, are ignored.
	 */
	public void copy(InputStream in, OutputStream out, boolean closeIn, boolean closeOut) throws FetchException;

	/**
	 * Copies the content of a given {@link InputStream} into a given
	 * {@link OutputStream}.
	 *
	 * <p>
	 * See {@link ByteFetcher#copy(InputStream, OutputStream, boolean, boolean)}
	 * for the handling of missing or invalid arguments.
	 * 
	 * @param in
	 *            The {@link InputStream} to read from.
	 * @param out
	 *            The {@link OutputStream} to write to.
	 * @param listener
	 *            The {@link FetchProgressListener} to report to.
	 * 
	 * @throws FetchException
	 *             If anything went wrong while reading from the given
	 *             {@link InputStream} or writing to the given
	 *             {@link OutputStream}.
	 */
	public void copy(InputStream in, OutputStream out, FetchProgressListener listener) throws FetchException;

	/**
	 * Copies the content of a given {@link InputStream} into a given
	 * {@link OutputStream}.
	 * 
	 * <p>
	 * Missing or invalid arguments are handled gracefully with the following
	 * behaviour.
	 * 
	 * <p>
	 * A {@code null} is given as an {@link InputStream}, it is simply ignored
	 * and handled as if there was nothing to read. If {@code closeOut} is
	 * {@literal true}, the given {@link OutputStream} will be closed anyway
	 * 
	 * <p>
	 * A {@code null} is given as an {@link OutputStream}, it is simply ignored,
	 * but the content of given {@link InputStream} is fetched anyway. If
	 * {@code closeIn} is {@literal true}, the given {@link InputStream} will be
	 * closed anyway
	 * 
	 * @param in
	 *            The {@link InputStream} to read from.
	 * @param out
	 *            The {@link OutputStream} to write to.
	 * @param listener
	 *            The {@link FetchProgressListener} to report to.
	 * @param closeIn
	 *            Whether to close the given {@link InputStream}, after reading
	 *            from it.
	 * @param closeOut
	 *            Whether to close the given {@link OutputStream}, after writing
	 *            to it.
	 * 
	 * @throws FetchException
	 *             If anything went wrong while reading from the given
	 *             {@link InputStream} or writing to the given
	 *             {@link OutputStream}. {@link FetchException FetchExceptions}
	 *             thrown while trying to close one of the given streams, if
	 *             requested, are ignored.
	 */
	public void copy(InputStream in, OutputStream out, FetchProgressListener listener, boolean closeIn, boolean closeOut)
			throws FetchException;

}
