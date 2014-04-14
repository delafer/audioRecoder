package org.delafer.recoder.deprecated.compressors;

import java.io.InputStream;
import java.io.OutputStream;

public interface ICompressor {

//	public abstract int getUID();

	public abstract String getName();

	public abstract String getDescription();

	public abstract String getURL();

	/** 
	 * Method to decode / decompress data
	 * @param InputStream inData, OutputStream outData
	 * @return return code
	 * @see impl.AbstractDataProcessor#decodeData(java.io.InputStream, java.io.OutputStream)
	 */
	public abstract InputStream decompressor(InputStream is) throws Exception;

	/**
	 *  Method to encode / compress data
	 * @param InputStream inData, OutputStream outData
	 * @return return code
	 * @see impl.AbstractDataProcessor#encodeData(java.io.InputStream, java.io.OutputStream)
	 */

	public abstract OutputStream compressor(OutputStream inData) throws Exception;;

}