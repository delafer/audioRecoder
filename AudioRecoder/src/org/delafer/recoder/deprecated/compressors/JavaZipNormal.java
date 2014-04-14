package org.delafer.recoder.deprecated.compressors;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

public class JavaZipNormal implements ICompressor {
	
	public static final int UID = 2;

	
	/* (non-Javadoc)
	 * @see org.delafer.xmlbench.compressors.ICompressor#getName()
	 */
	public String getName() {
		return "Build-in Java ZIP Deflater (normal mode)";
	}
	
	/* (non-Javadoc)
	 * @see org.delafer.xmlbench.compressors.ICompressor#getDescription()
	 */
	public String getDescription() {
		return getName();
	}
	
	private static int BUFFER_SIZE = 512;
	
	/* (non-Javadoc)
	 * @see org.delafer.xmlbench.compressors.ICompressor#getURL()
	 */
	public String getURL() {
		return "";
	}

	/* (non-Javadoc)
	 * @see org.delafer.xmlbench.compressors.ICompressor#decompressData(java.io.InputStream)
	 */
	public InputStream decompressor(InputStream is) {
		
		InflaterInputStream iis = new InflaterInputStream(is);
		return iis;
	}
	
	/* (non-Javadoc)
	 * @see org.delafer.xmlbench.compressors.ICompressor#compressData(java.io.OutputStream)
	 */

	public OutputStream compressor(OutputStream inData) {
		DeflaterOutputStream	deflaterStream = new FixedDeflaterOutputStream(inData, Deflater.DEFAULT_COMPRESSION);
		return deflaterStream;

	}
	
}
