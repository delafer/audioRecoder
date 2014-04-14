/**
 * 
 */
package org.delafer.recoder.deprecated.compressors;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Alexander Tavrovsky
 *
 */
public class DummyCompressionOutputStream extends OutputStream {
	
	
	private transient int size;
	/**
	 * 
	 */
	public DummyCompressionOutputStream() {
		this.size = 0;
	}

	public int getSize() {
		return size;
	}

	/* (non-Javadoc)
	 * @see java.io.OutputStream#write(int)
	 */
	@Override
	public void write(int b) throws IOException {
		this.size++;
	}


	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		this.size += (len - off);
	}

	@Override
	public void flush() throws IOException {
	}

	@Override
	public void close() throws IOException {
		this.size = 0;
	}
	

}
