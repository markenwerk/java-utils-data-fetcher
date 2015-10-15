package net.markenwerk.utils.data.fetcher;

import java.io.IOException;
import java.io.OutputStream;

/**
 * A {@code OutputStream} that drop every written bit.
 * 
 * @author Torsten Krause (tk at markenwerk dot net)
 * @since 1.0.0
 */
class NullOutputStream extends OutputStream {

	@Override
	public void write(int b) throws IOException {
	}

}
