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

import java.io.Reader;
import java.io.Writer;

/**
 * A {@link CharacterFetcher} can fetch the entire content of a given
 * {@link Reader} into a fresh {@code char[]} or {@link String} or copy it into
 * a given {@link Writer}.
 * 
 * <p>
 * All methods take optional parameters to specify the buffer size and to
 * specify, whether the provided streams should be closed afterwards. This
 * allows to write compact code like
 * 
 * <pre>
 * {
 * 	foo.setContent(fetcher.fetch(new FileReader(file), true));
 * }
 * </pre>
 * 
 * instead of unnecessarily verbose code like
 * 
 * <pre>
 * {
 * 	Reader in = new FileReader(file);
 * 	foo.setContent(fetcher.fetch(in));
 * 	in.close();
 * }
 * </pre>
 * 
 * @author Torsten Krause (tk at markenwerk dot net)
 * @since 3.0.0
 */
public interface CharacterFetcher {

	/**
	 * Fetches the content of a given {@link Reader} into a fresh char[].
	 *
	 * <p>
	 * See {@link CharacterFetcher#copy(Reader, Writer, boolean, boolean)} for
	 * the handling of missing or invalid arguments.
	 * 
	 * @param in
	 *            The {@link Reader} to read from.
	 * @return A new {@code char[]}, containing the content of the given
	 *         {@link Reader}.
	 * @throws FetchException
	 *             If anything went wrong while reading from the given
	 *             {@link Reader}.
	 */
	public char[] fetch(Reader in) throws FetchException;

	/**
	 * Fetches the content of a given {@link Reader} into a fresh char[].
	 *
	 * <p>
	 * See {@link CharacterFetcher#copy(Reader, Writer, boolean, boolean)} for
	 * the handling of missing or invalid arguments.
	 * 
	 * @param in
	 *            The {@link Reader} to read from.
	 * @param close
	 *            Whether to close the given {@link Reader}, after reading from
	 *            it.
	 * @return A new {@code char[]}, containing the content of the given
	 *         {@link Reader}.
	 * @throws FetchException
	 *             If anything went wrong while reading from the given
	 *             {@link Reader}. {@link FetchException FetchExceptions} thrown
	 *             while trying to close the given {@link Reader}, if requested,
	 *             are ignored.
	 */
	public char[] fetch(Reader in, boolean close) throws FetchException;

	/**
	 * Fetches the content of a given {@link Reader} into a fresh char[].
	 *
	 * <p>
	 * See {@link CharacterFetcher#copy(Reader, Writer, boolean, boolean)} for
	 * the handling of missing or invalid arguments.
	 * 
	 * @param in
	 *            The {@link Reader} to read from.
	 * @param listener
	 *            The {@link FetchProgressListener} to report to.
	 * @param close
	 *            Whether to close the given {@link Reader}, after reading from
	 *            it.
	 * @return A new {@code char[]}, containing the content of the given
	 *         {@link Reader}.
	 * @throws FetchException
	 *             If anything went wrong while reading from the given
	 *             {@link Reader}. {@link FetchException FetchExceptions} thrown
	 *             while trying to close the given {@link Reader}, if requested,
	 *             are ignored.
	 */
	public char[] fetch(Reader in, FetchProgressListener listener, boolean close) throws FetchException;

	/**
	 * Fetches the content of a given {@link Reader} into a fresh char[].
	 *
	 * <p>
	 * See {@link CharacterFetcher#copy(Reader, Writer, boolean, boolean)} for
	 * the handling of missing or invalid arguments.
	 * 
	 * @param in
	 *            The {@link Reader} to read from.
	 * @param listener
	 *            The {@link FetchProgressListener} to report to.
	 * @return A new {@code char[]}, containing the content of the given
	 *         {@link Reader}.
	 * @throws FetchException
	 *             If anything went wrong while reading from the given
	 *             {@link Reader}.
	 */
	public char[] fetch(Reader in, FetchProgressListener listener) throws FetchException;

	/**
	 * Reads the content of a given {@link Reader} as a {@link String}.
	 *
	 * <p>
	 * See {@link CharacterFetcher#copy(Reader, Writer, boolean, boolean)} for
	 * the handling of missing or invalid arguments.
	 * 
	 * @param in
	 *            The {@link Reader} to read from.
	 * @return A new {@code char[]}, containing the content of the given
	 *         {@link Reader}.
	 * @throws FetchException
	 *             If anything went wrong while reading from the given
	 *             {@link Reader}.
	 */
	public String read(Reader in) throws FetchException;

	/**
	 * Reads the content of a given {@link Reader} as a {@link String}
	 *
	 * <p>
	 * See {@link CharacterFetcher#copy(Reader, Writer, boolean, boolean)} for
	 * the handling of missing or invalid arguments.
	 * 
	 * @param in
	 *            The {@link Reader} to read from.
	 * @param close
	 *            Whether to close the given {@link Reader}, after reading from
	 *            it.
	 * @return A new {@code char[]}, containing the content of the given
	 *         {@link Reader}.
	 * @throws FetchException
	 *             If anything went wrong while reading from the given
	 *             {@link Reader}. {@link FetchException FetchExceptions} thrown
	 *             while trying to close the given {@link Reader}, if requested,
	 *             are ignored.
	 */
	public String read(Reader in, boolean close) throws FetchException;

	/**
	 * Reads the content of a given {@link Reader} as a {@link String}.
	 *
	 * <p>
	 * See {@link CharacterFetcher#copy(Reader, Writer, boolean, boolean)} for
	 * the handling of missing or invalid arguments.
	 * 
	 * @param in
	 *            The {@link Reader} to read from.
	 * @param listener
	 *            The {@link FetchProgressListener} to report to.
	 * @param close
	 *            Whether to close the given {@link Reader}, after reading from
	 *            it.
	 * @return A new {@code char[]}, containing the content of the given
	 *         {@link Reader}.
	 * @throws FetchException
	 *             If anything went wrong while reading from the given
	 *             {@link Reader}. {@link FetchException FetchExceptions} thrown
	 *             while trying to close the given {@link Reader}, if requested,
	 *             are ignored.
	 */
	public String read(Reader in, FetchProgressListener listener, boolean close) throws FetchException;

	/**
	 * Reads the content of a given {@link Reader} as a {@link String}.
	 *
	 * <p>
	 * See {@link CharacterFetcher#copy(Reader, Writer, boolean, boolean)} for
	 * the handling of missing or invalid arguments.
	 * 
	 * @param in
	 *            The {@link Reader} to read from.
	 * @param listener
	 *            The {@link FetchProgressListener} to report to.
	 * @return A new {@code char[]}, containing the content of the given
	 *         {@link Reader}.
	 * @throws FetchException
	 *             If anything went wrong while reading from the given
	 *             {@link Reader}.
	 */
	public String read(Reader in, FetchProgressListener listener) throws FetchException;

	/**
	 * Copies the content of a given {@link Reader} into a given {@link Writer}.
	 *
	 * <p>
	 * See {@link CharacterFetcher#copy(Reader, Writer, boolean, boolean)} for
	 * the handling of missing or invalid arguments.
	 * 
	 * @param in
	 *            The {@link Reader} to read from.
	 * @param out
	 *            The {@link Writer} to write to.
	 * @throws FetchException
	 *             If anything went wrong while reading from the given
	 *             {@link Reader} or writing to the given {@link Writer}.
	 */
	public void copy(Reader in, Writer out) throws FetchException;

	/**
	 * Copies the content of a given {@link Reader} into a given {@link Writer}.
	 * 
	 * <p>
	 * Missing or invalid arguments are handled gracefully with the following
	 * behaviour.
	 * 
	 * <p>
	 * A {@code null} is given as an {@link Reader}, it is simply ignored and
	 * handled as if there was nothing to read. If {@code closeOut} is
	 * {@literal true}, the given {@link Writer} will be closed anyway
	 * 
	 * <p>
	 * A {@code null} is given as an {@link Writer}, it is simply ignored, but
	 * the content of given {@link Reader} is fetched anyway. If {@code closeIn}
	 * is {@literal true}, the given {@link Reader} will be closed anyway
	 * 
	 * @param in
	 *            The {@link Reader} to read from.
	 * @param out
	 *            The {@link Writer} to write to.
	 * @param closeIn
	 *            Whether to close the given {@link Reader}, after reading from
	 *            it.
	 * @param closeOut
	 *            Whether to close the given {@link Writer}, after writing to
	 *            it.
	 * @throws FetchException
	 *             If anything went wrong while reading from the given
	 *             {@link Reader} or writing to the given {@link Writer}.
	 *             {@link FetchException FetchExceptions} thrown while trying to
	 *             close one of the given streams, if requested, are ignored.
	 */
	public void copy(Reader in, Writer out, boolean closeIn, boolean closeOut) throws FetchException;

	/**
	 * Copies the content of a given {@link Reader} into a given {@link Writer}.
	 *
	 * <p>
	 * See {@link CharacterFetcher#copy(Reader, Writer, boolean, boolean)} for
	 * the handling of missing or invalid arguments.
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
	public void copy(Reader in, Writer out, FetchProgressListener listener) throws FetchException;

	/**
	 * Copies the content of a given {@link Reader} into a given {@link Writer}.
	 * 
	 * <p>
	 * Missing or invalid arguments are handled gracefully with the following
	 * behaviour.
	 * 
	 * <p>
	 * A {@code null} is given as an {@link Reader}, it is simply ignored and
	 * handled as if there was nothing to read. If {@code closeOut} is
	 * {@literal true}, the given {@link Writer} will be closed anyway
	 * 
	 * <p>
	 * A {@code null} is given as an {@link Writer}, it is simply ignored, but
	 * the content of given {@link Reader} is fetched anyway. If {@code closeIn}
	 * is {@literal true}, the given {@link Reader} will be closed anyway
	 * 
	 * @param in
	 *            The {@link Reader} to read from.
	 * @param out
	 *            The {@link Writer} to write to.
	 * @param listener
	 *            The {@link FetchProgressListener} to report to.
	 * @param closeIn
	 *            Whether to close the given {@link Reader}, after reading from
	 *            it.
	 * @param closeOut
	 *            Whether to close the given {@link Writer}, after writing to
	 *            it.
	 * @throws FetchException
	 *             If anything went wrong while reading from the given
	 *             {@link Reader} or writing to the given {@link Writer}.
	 *             {@link FetchException FetchExceptions} thrown while trying to
	 *             close one of the given streams, if requested, are ignored.
	 */
	public void copy(Reader in, Writer out, FetchProgressListener listener, boolean closeIn, boolean closeOut)
			throws FetchException;

}
