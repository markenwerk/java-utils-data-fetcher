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

/**
 * @author Torsten Krause (tk at markenwerk dot net)
 * @since 2.1.0
 */
public interface FetchProgressListener {

	/**
	 * Indicates that the process of fetching bytes from an {@link InputStream}
	 * has started.
	 */
	public void onStarted();

	/**
	 * Indicates that the process of fetching bytes from an {@link InputStream}
	 * has progressed.
	 * 
	 * <p>
	 * The progress will only be reported if the {@link ByteFetcher} is capable of
	 * monitoring the progress.
	 * 
	 * @param bytesFetched
	 *            Total amount of bytes fetched so far.
	 */
	public void onProgress(long bytesFetched);

	/**
	 * Indicates that the process of fetching bytes from an {@link InputStream}
	 * has succeeded.
	 * 
	 * @param bytesFetched
	 *            Total total amount of bytes fetched or {@literal null}, if the
	 *            {@link ByteFetcher} is not capable of monitoring the progress.
	 */
	public void onSuccedded(Long bytesFetched);

	/**
	 * Indicates that the process of fetching bytes from an {@link InputStream}
	 * has failed.
	 * 
	 * @param exception
	 *            The {@link FetchException} that caused the process to fail.
	 *            This is the same {@link FetchException} that is thrown in the
	 *            failing method of {@link ByteFetcher} that this
	 *            {@link FetchProgressListener} has been given to.
	 * 
	 * @param bytesFetched
	 *            Total total amount of bytes fetched before the process failed
	 *            or {@literal null}, if the {@link ByteFetcher} is not capable of
	 *            monitoring the progress.
	 */
	public void onFailed(FetchException exception, Long bytesFetched);

	/**
	 * Indicates that the process of fetching bytes from an {@link InputStream}
	 * has finished.
	 * 
	 * <p>
	 * This method will be called after either
	 * {@link FetchProgressListener#onSuccedded(Long)} or
	 * {@link FetchProgressListener#onFailed(FetchException, Long)} has
	 * been called.
	 */
	public void onFinished();

}
