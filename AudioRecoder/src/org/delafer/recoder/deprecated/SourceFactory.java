package org.delafer.recoder.deprecated;



public class SourceFactory {
	
	private transient static boolean initialized = false;
	
	
	public synchronized static boolean isInitialized() {
		return initialized;
	}



	public synchronized static void setInitialized(final boolean flag) {
		initialized = flag;
	}



	private transient static IFileAbstract uncompressed;
	private transient static IFileAbstract compressed;
	
	
	public synchronized  static void setData(IFileAbstract uncompressedD,  IFileAbstract compressedD) {
		uncompressed = uncompressedD;
		compressed = compressedD;
		initialized = true;
	}
	
	
	public static synchronized void resetData() {
		initialized = false;
		compressed = null;
		uncompressed = null;
	}


	public static  IFileAbstract getUncompressedFile() {
		return uncompressed;
	}



	public static IFileAbstract getCompressedFile() {
		return compressed;
	}
	
	
	
}
