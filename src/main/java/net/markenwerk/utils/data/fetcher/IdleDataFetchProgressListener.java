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

/**
 * An {@link IdleDataFetchProgressListener} is a
 * {@link DataFetchProgressListener} with empty methods. It is intended a base
 * implementation for custom {@link DataFetchProgressListener} implementations,
 * that don't need to implement all methods.
 * 
 * @author Torsten Krause (tk at markenwerk dot net)
 * @since 4.0.0
 */
public class IdleDataFetchProgressListener implements DataFetchProgressListener {

	@Override
	public void onStarted() {
	}

	@Override
	public void onProgress(long bytesFetched) {
	}

	@Override
	public void onSuccedded(Long bytesFetched) {
	}

	@Override
	public void onFailed(DataFetchException exception, Long bytesFetched) {
	}

	@Override
	public void onFinished() {
	}

}
