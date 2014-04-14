package org.delafer.recoder.deprecated.compressors;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

public class Exificient implements ICompressor {
	
	public static final int UID = 3;

	
	/* (non-Javadoc)
	 * @see org.delafer.xmlbench.compressors.ICompressor#getName()
	 */
	public String getName() {
		return "EXIficient / EXI Compressor 0.8 Copyright (C) 2007-2011 Siemens AG";
	}
	
	/* (non-Javadoc)
	 * @see org.delafer.xmlbench.compressors.ICompressor#getDescription()
	 */
	public String getDescription() {
		return getName();
	}
	
	public static int BUFFER_SIZE = 2048;
	
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
		return is;
//		InflaterInputStream iis = new InflaterInputStream(is, new Inflater(), BUFFER_SIZE);
//		return iis;
	}
	
	/* (non-Javadoc)
	 * @see org.delafer.xmlbench.compressors.ICompressor#compressData(java.io.OutputStream)
	 */

	public OutputStream compressor(OutputStream inData) {
		return inData;
//		DeflaterOutputStream	deflaterStream = new DeflaterOutputStream(inData, new Deflater(Deflater.BEST_SPEED), BUFFER_SIZE);
//		return deflaterStream;

	}
	
}
