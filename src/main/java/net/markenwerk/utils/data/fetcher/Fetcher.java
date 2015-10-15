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
 * A convenient replacement for {@code IOUtils.toByteArray()} to perform the
 * everyday and menial task of reading an entire {@code InputStream} into a
 * {@code byte[]} with the method {@link Fetcher#fetch(InputStream)}.
 * 
 * <p>
 * Since the class {@code sun.misc.IOUtils}, which provides this functionality,
 * is not a part of the official Java specification, usages of this class will
 * usually yield a compiler warning like "{@code The type 'IOUtils' is not API}"
 * and might break at runtime, if a runtime environment other than the Oracle
 * JRE is used.
 * 
 * <p>
 * Furthermore, this class allows to copy the content of an {@link InputStream}
 * directly into an {@link OutputStream}, using the method
 * {@link Fetcher#fetch(InputStream, OutputStream)}.
 * 
 * <p>
 * All methods take optional parameters to specify the buffer size and to
 * specify, whether the provided streams should be closed afterwards. This
 * allows to write compact code like
 * 
 * <pre>
 * {@code
 * foo.setContent(Fetcher.fetch(new FileInputStream(file), true));
 * }
 * </pre>
 * 
 * instead of unnecessarily verbose code like
 * 
 * <pre>
 * {
 * 	&#064;code
 * 	InputStream in = new FileInputStream(file);
 * 	foo.setContent(Fetcher.fetch(in));
 * 	in.close();
 * }
 * </pre>
 * 
 * @author Torsten Krause (tk at markenwerk dot net)
 * @since 1.0.0
 */
public final class Fetcher {

	private static final int DEFAULT_BUFEFR_SIZE = 1024;

	private Fetcher() {

	}

	/**
	 * Copies the content of a given {@link InputStream} into a fresh byte[],
	 * using {@code byte[]} as a buffer of default buffer size of one <a
	 * href="https://en.wikipedia.org/wiki/Kibibyte">kibibyte</a> (1024 bytes).
	 *
	 * <p>
	 * See
	 * {@link Fetcher#fetch(InputStream, OutputStream, int, boolean, boolean)}
	 * for the handling of missing or invalid arguments.
	 * 
	 * @param in
	 *            The {@link InputStream} to read from.
	 * @return A new {@code byte[]}, containing the content of the given
	 *         {@link InputStream}.
	 * @throws IOException
	 *             If anything went wrong while reading from the given
	 *             {@link InputStream}.
	 */
	public static byte[] fetch(InputStream in) throws IOException {
		return fetch(in, DEFAULT_BUFEFR_SIZE, false);
	}

	/**
	 * Copies the content of a given {@link InputStream} into a fresh byte[],
	 * using {@code byte[]} as a buffer.
	 *
	 * <p>
	 * See
	 * {@link Fetcher#fetch(InputStream, OutputStream, int, boolean, boolean)}
	 * for the handling of missing or invalid arguments.
	 * 
	 * @param in
	 *            The {@link InputStream} to read from.
	 * @param bufferSize
	 *            The size of the buffer.
	 * @return A new {@code byte[]}, containing the content of the given
	 *         {@link InputStream}.
	 * @throws IOException
	 *             If anything went wrong while reading from the given
	 *             {@link InputStream}.
	 */
	public static byte[] fetch(InputStream in, int bufferSize) throws IOException {
		return fetch(in, bufferSize, false);
	}

	/**
	 * Copies the content of a given {@link InputStream} into a fresh byte[],
	 * using {@code byte[]} as a buffer of default buffer size of one <a
	 * href="https://en.wikipedia.org/wiki/Kibibyte">kibibyte</a> (1024 bytes).
	 *
	 * <p>
	 * See
	 * {@link Fetcher#fetch(InputStream, OutputStream, int, boolean, boolean)}
	 * for the handling of missing or invalid arguments.
	 * 
	 * @param in
	 *            The {@link InputStream} to read from.
	 * @param close
	 *            Whether to close the given {@link InputStream}, after reading
	 *            from it.
	 * @return A new {@code byte[]}, containing the content of the given
	 *         {@link InputStream}.
	 * @throws IOException
	 *             If anything went wrong while reading from the given
	 *             {@link InputStream}. {@link IOException IOExceptions} thrown
	 *             while trying to close the given {@link InputStream}, if
	 *             requested, are ignored.
	 */
	public static byte[] fetch(InputStream in, boolean close) throws IOException {
		return fetch(in, DEFAULT_BUFEFR_SIZE, close);
	}

	/**
	 * Copies the content of a given {@link InputStream} into a fresh byte[],
	 * using a {@code byte[]} as a buffer.
	 *
	 * <p>
	 * See
	 * {@link Fetcher#fetch(InputStream, OutputStream, int, boolean, boolean)}
	 * for the handling of missing or invalid arguments.
	 * 
	 * @param in
	 *            The {@link InputStream} to read from.
	 * @param bufferSize
	 *            The size of the buffer.
	 * @param close
	 *            Whether to close the given {@link InputStream}, after reading
	 *            from it.
	 * @return A new {@code byte[]}, containing the content of the given
	 *         {@link InputStream}.
	 * @throws IOException
	 *             If anything went wrong while reading from the given
	 *             {@link InputStream}. {@link IOException IOExceptions} thrown
	 *             while trying to close the given {@link InputStream}, if
	 *             requested, are ignored.
	 */
	public static byte[] fetch(InputStream in, int bufferSize, boolean close) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream(bufferSize);
		fetch(in, out, bufferSize, close, true);
		return out.toByteArray();
	}

	/**
	 * Copies the content of a given {@link InputStream} into a given
	 * {@link OutputStream}, using {@code byte[]} as a buffer of default buffer
	 * size of one <a href="https://en.wikipedia.org/wiki/Kibibyte">kibibyte</a>
	 * (1024 bytes).
	 *
	 * <p>
	 * See
	 * {@link Fetcher#fetch(InputStream, OutputStream, int, boolean, boolean)}
	 * for the handling of missing or invalid arguments.
	 * 
	 * @param in
	 *            The {@link InputStream} to read from.
	 * @param out
	 *            The {@link OutputStream} to write to.
	 * @throws IOException
	 *             If anything went wrong while reading from the given
	 *             {@link InputStream} or writing to the given
	 *             {@link OutputStream}.
	 */
	public static void fetch(InputStream in, OutputStream out) throws IOException {
		fetch(in, out, DEFAULT_BUFEFR_SIZE, false, false);
	}

	/**
	 * Copies the content of a given {@link InputStream} into a given
	 * {@link OutputStream}, using {@code byte[]} as a buffer of default buffer
	 * size of one <a href="https://en.wikipedia.org/wiki/Kibibyte">kibibyte</a>
	 * (1024 bytes).
	 *
	 * <p>
	 * See
	 * {@link Fetcher#fetch(InputStream, OutputStream, int, boolean, boolean)}
	 * for the handling of missing or invalid arguments.
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
	 * @throws IOException
	 *             If anything went wrong while reading from the given
	 *             {@link InputStream} or writing to the given
	 *             {@link OutputStream}. {@link IOException IOExceptions} thrown
	 *             while trying to close one of the given streams, if requested,
	 *             are ignored.
	 */
	public static void fetch(InputStream in, OutputStream out, boolean closeIn, boolean closeOut) throws IOException {
		fetch(in, out, DEFAULT_BUFEFR_SIZE, closeIn, closeOut);
	}

	/**
	 * Copies the content of a given {@link InputStream} into a given
	 * {@link OutputStream}, using a {@code byte[]} as a buffer.
	 * 
	 * <p>
	 * See
	 * {@link Fetcher#fetch(InputStream, OutputStream, int, boolean, boolean)}
	 * for the handling of missing or invalid arguments.
	 * 
	 * @param in
	 *            The {@link InputStream} to read from.
	 * @param out
	 *            The {@link OutputStream} to write to.
	 * @param bufferSize
	 *            The size of the buffer.
	 * @throws IOException
	 *             If anything went wrong while reading from the given
	 *             {@link InputStream} or writing to the given
	 *             {@link OutputStream}.
	 */
	public static void fetch(InputStream in, OutputStream out, int bufferSize) throws IOException {
		fetch(in, out, bufferSize, false, false);
	}

	/**
	 * Copies the content of a given {@link InputStream} into a given
	 * {@link OutputStream}, using a {@code byte[]} as a buffer.
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
	 * <p>
	 * A non-positive buffer size is replaced with the default buffer size of
	 * one <a href="https://en.wikipedia.org/wiki/Kibibyte">kibibyte</a> (1024
	 * bytes).
	 * 
	 * @param in
	 *            The {@link InputStream} to read from.
	 * @param out
	 *            The {@link OutputStream} to write to.
	 * @param bufferSize
	 *            The size of the buffer.
	 * @param closeIn
	 *            Whether to close the given {@link InputStream}, after reading
	 *            from it.
	 * @param closeOut
	 *            Whether to close the given {@link OutputStream}, after writing
	 *            to it.
	 * @throws IOException
	 *             If anything went wrong while reading from the given
	 *             {@link InputStream} or writing to the given
	 *             {@link OutputStream}. {@link IOException IOExceptions} thrown
	 *             while trying to close one of the given streams, if requested,
	 *             are ignored.
	 */
	public static void fetch(InputStream in, OutputStream out, int bufferSize, boolean closeIn, boolean closeOut)
			throws IOException {
		if (bufferSize < 1) {
			bufferSize = DEFAULT_BUFEFR_SIZE;
		}
		try {
			if (null != in) {
				if (null == out) {
					out = new NullOutputStream();
					closeOut = true;
				}
				byte[] buffer = new byte[bufferSize];
				int length = in.read(buffer);
				while (length != -1) {
					out.write(buffer, 0, length);
					length = in.read(buffer);
				}
			}
		} catch (IOException e) {
			throw e;
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

}
