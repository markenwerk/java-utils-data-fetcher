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

import java.io.Closeable;

/**
 * A {@link CharacterFetcher} can fetch the entire content of a given
 * {@link Readable} into a fresh {@code char[]} or {@link String} or copy it
 * into a given {@link Appendable}.
 * 
 * <p>
 * All methods take optional parameters to specify the buffer size and to
 * specify, whether the provided streams should be closed afterwards. This
 * allows to write compact code like
 * 
 * <pre>
 * {
 * 	foo.setContent(fetcher.fetch(new FileReadable(file), true));
 * }
 * </pre>
 * 
 * instead of unnecessarily verbose code like
 * 
 * <pre>
 * {
 * 	Readable in = new FileReadable(file);
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
	 * Fetches the content of a given {@link Readable} into a fresh char[].
	 *
	 * <p>
	 * See {@link CharacterFetcher#copy(Readable, Appendable, boolean, boolean)}
	 * for the handling of missing or invalid arguments.
	 * 
	 * @param in
	 *            The {@link Readable} to read from.
	 * @return A new {@code char[]}, containing the content of the given
	 *         {@link Readable}.
	 *         
	 * @throws FetchException
	 *             If anything went wrong while reading from the given
	 *             {@link Readable}.
	 */
	public char[] fetch(Readable in) throws FetchException;

	/**
	 * Fetches the content of a given {@link Readable} into a fresh char[].
	 *
	 * <p>
	 * See {@link CharacterFetcher#copy(Readable, Appendable, boolean, boolean)}
	 * for the handling of missing or invalid arguments.
	 * 
	 * @param in
	 *            The {@link Readable} to read from.
	 * @param close
	 *            Whether to close the given {@link Readable}, after reading
	 *            from it, if the given {@link Readable} is {@link Closeable}.
	 * @return A new {@code char[]}, containing the content of the given
	 *         {@link Readable}.
	 *         
	 * @throws FetchException
	 *             If anything went wrong while reading from the given
	 *             {@link Readable}. {@link FetchException FetchExceptions}
	 *             thrown while trying to close the given {@link Readable}, if
	 *             requested, are ignored.
	 */
	public char[] fetch(Readable in, boolean close) throws FetchException;

	
	/**
	 * Fetches the content of a given {@link Readable} into a fresh char[].
	 *
	 * <p>
	 * See {@link CharacterFetcher#copy(Readable, Appendable, boolean, boolean)}
	 * for the handling of missing or invalid arguments.
	 * 
	 * @param in
	 *            The {@link Readable} to read from.
	 * @param listener
	 *            The {@link FetchProgressListener} to report to.
	 * @return A new {@code char[]}, containing the content of the given
	 *         {@link Readable}.
	 *         
	 * @throws FetchException
	 *             If anything went wrong while reading from the given
	 *             {@link Readable}.
	 */
	public char[] fetch(Readable in, FetchProgressListener listener) throws FetchException;

	
	/**
	 * Fetches the content of a given {@link Readable} into a fresh char[].
	 *
	 * <p>
	 * See {@link CharacterFetcher#copy(Readable, Appendable, boolean, boolean)}
	 * for the handling of missing or invalid arguments.
	 * 
	 * @param in
	 *            The {@link Readable} to read from.
	 * @param listener
	 *            The {@link FetchProgressListener} to report to.
	 * @param close
	 *            Whether to close the given {@link Readable}, after reading
	 *            from it, if the given {@link Readable} is {@link Closeable}.
	 * @return A new {@code char[]}, containing the content of the given
	 *         {@link Readable}.
	 *         
	 * @throws FetchException
	 *             If anything went wrong while reading from the given
	 *             {@link Readable}. {@link FetchException FetchExceptions}
	 *             thrown while trying to close the given {@link Readable}, if
	 *             requested, are ignored.
	 */
	public char[] fetch(Readable in, FetchProgressListener listener, boolean close) throws FetchException;

	
	
	/**
	 * Reads the content of a given {@link Readable} as a {@link String}.
	 *
	 * <p>
	 * See {@link CharacterFetcher#copy(Readable, Appendable, boolean, boolean)}
	 * for the handling of missing or invalid arguments.
	 * 
	 * @param in
	 *            The {@link Readable} to read from.
	 * @return A new {@code char[]}, containing the content of the given
	 *         {@link Readable}.
	 *         
	 * @throws FetchException
	 *             If anything went wrong while reading from the given
	 *             {@link Readable}.
	 */
	public String read(Readable in) throws FetchException;

	/**
	 * Reads the content of a given {@link Readable} as a {@link String}
	 *
	 * <p>
	 * See {@link CharacterFetcher#copy(Readable, Appendable, boolean, boolean)}
	 * for the handling of missing or invalid arguments.
	 * 
	 * @param in
	 *            The {@link Readable} to read from.
	 * @param close
	 *            Whether to close the given {@link Readable}, after reading
	 *            from it, if the given {@link Readable} is {@link Closeable}.
	 * @return A new {@code char[]}, containing the content of the given
	 *         {@link Readable}.
	 *         
	 * @throws FetchException
	 *             If anything went wrong while reading from the given
	 *             {@link Readable}. {@link FetchException FetchExceptions}
	 *             thrown while trying to close the given {@link Readable}, if
	 *             requested, are ignored.
	 */
	public String read(Readable in, boolean close) throws FetchException;


	/**
	 * Reads the content of a given {@link Readable} as a {@link String}.
	 *
	 * <p>
	 * See {@link CharacterFetcher#copy(Readable, Appendable, boolean, boolean)}
	 * for the handling of missing or invalid arguments.
	 * 
	 * @param in
	 *            The {@link Readable} to read from.
	 * @param listener
	 *            The {@link FetchProgressListener} to report to.
	 * @return A new {@code char[]}, containing the content of the given
	 *         {@link Readable}.
	 *         
	 * @throws FetchException
	 *             If anything went wrong while reading from the given
	 *             {@link Readable}.
	 */
	public String read(Readable in, FetchProgressListener listener) throws FetchException;

	/**
	 * Reads the content of a given {@link Readable} as a {@link String}.
	 *
	 * <p>
	 * See {@link CharacterFetcher#copy(Readable, Appendable, boolean, boolean)}
	 * for the handling of missing or invalid arguments.
	 * 
	 * @param in
	 *            The {@link Readable} to read from.
	 * @param listener
	 *            The {@link FetchProgressListener} to report to.
	 * @param close
	 *            Whether to close the given {@link Readable}, after reading
	 *            from it, if the given {@link Readable} is {@link Closeable}.
	 * @return A new {@code char[]}, containing the content of the given
	 *         {@link Readable}.
	 *         
	 * @throws FetchException
	 *             If anything went wrong while reading from the given
	 *             {@link Readable}. {@link FetchException FetchExceptions}
	 *             thrown while trying to close the given {@link Readable}, if
	 *             requested, are ignored.
	 */
	public String read(Readable in, FetchProgressListener listener, boolean close) throws FetchException;

	
	/**
	 * Copies the content of a given {@link Readable} into a given
	 * {@link Appendable}.
	 *
	 * <p>
	 * See {@link CharacterFetcher#copy(Readable, Appendable, boolean, boolean)}
	 * for the handling of missing or invalid arguments.
	 * 
	 * @param in
	 *            The {@link Readable} to read from.
	 * @param out
	 *            The {@link Appendable} to write to.
	 *            
	 * @throws FetchException
	 *             If anything went wrong while reading from the given
	 *             {@link Readable} or appending to the given {@link Appendable}.
	 */
	public void copy(Readable in, Appendable out) throws FetchException;

	/**
	 * Copies the content of a given {@link Readable} into a given
	 * {@link Appendable}.
	 * 
	 * <p>
	 * Missing or invalid arguments are handled gracefully with the following
	 * behaviour.
	 * 
	 * <p>
	 * A {@code null} is given as an {@link Readable}, it is simply ignored and
	 * handled as if there was nothing to read. If {@code closeOut} is
	 * {@literal true}, the given {@link Appendable} will be closed anyway
	 * 
	 * <p>
	 * A {@code null} is given as an {@link Appendable}, it is simply ignored,
	 * but the content of given {@link Readable} is fetched anyway. If
	 * {@code closeIn} is {@literal true}, the given {@link Readable} will be
	 * closed anyway
	 * 
	 * @param in
	 *            The {@link Readable} to read from.
	 * @param out
	 *            The {@link Appendable} to write to.
	 * @param closeIn
	 *            Whether to close the given {@link Readable}, after reading
	 *            from it, if the given {@link Readable} is {@link Closeable}.
	 * @param closeOut
	 *            Whether to close the given {@link Appendable}, after appending
	 *            to it, if the given {@link Appendable} is {@link Closeable}.
	 *            
	 * @throws FetchException
	 *             If anything went wrong while reading from the given
	 *             {@link Readable} or appending to the given {@link Appendable}.
	 *             {@link FetchException FetchExceptions} thrown while trying to
	 *             close one of the given streams, if requested, are ignored.
	 */
	public void copy(Readable in, Appendable out, boolean closeIn, boolean closeOut) throws FetchException;

	/**
	 * Copies the content of a given {@link Readable} into a given
	 * {@link Appendable}.
	 *
	 * <p>
	 * See {@link CharacterFetcher#copy(Readable, Appendable, boolean, boolean)}
	 * for the handling of missing or invalid arguments.
	 * 
	 * @param in
	 *            The {@link Readable} to read from.
	 * @param out
	 *            The {@link Appendable} to write to.
	 * @param listener
	 *            The {@link FetchProgressListener} to report to.
	 *            
	 * @throws FetchException
	 *             If anything went wrong while reading from the given
	 *             {@link Readable} or appending to the given {@link Appendable}.
	 */
	public void copy(Readable in, Appendable out, FetchProgressListener listener) throws FetchException;

	/**
	 * Copies the content of a given {@link Readable} into a given
	 * {@link Appendable}.
	 * 
	 * <p>
	 * Missing or invalid arguments are handled gracefully with the following
	 * behaviour.
	 * 
	 * <p>
	 * A {@code null} is given as an {@link Readable}, it is simply ignored and
	 * handled as if there was nothing to read. If {@code closeOut} is
	 * {@literal true}, the given {@link Appendable} will be closed anyway
	 * 
	 * <p>
	 * A {@code null} is given as an {@link Appendable}, it is simply ignored,
	 * but the content of given {@link Readable} is fetched anyway. If
	 * {@code closeIn} is {@literal true}, the given {@link Readable} will be
	 * closed anyway
	 * 
	 * @param in
	 *            The {@link Readable} to read from.
	 * @param out
	 *            The {@link Appendable} to write to.
	 * @param listener
	 *            The {@link FetchProgressListener} to report to.
	 * @param closeIn
	 *            Whether to close the given {@link Readable}, after reading
	 *            from it, if the given {@link Readable} is {@link Closeable}.
	 * @param closeOut
	 *            Whether to close the given {@link Appendable}, after appending
	 *            to it, if the given {@link Appendable} is {@link Closeable}.
	 *            
	 * @throws FetchException
	 *             If anything went wrong while reading from the given
	 *             {@link Readable} or appending to the given {@link Appendable}.
	 *             {@link FetchException FetchExceptions} thrown while trying
	 *             to close one of the given streams, if requested, are ignored.
	 */
	public void copy(Readable in, Appendable out, FetchProgressListener listener, boolean closeIn, boolean closeOut)
			throws FetchException;

}
