package org.delafer.recoder.deprecated.compressors;

import java.util.Map;
import java.util.TreeMap;

import org.delafer.recoder.config.Config;

public class CompressionFactory {

	private static TreeMap<Integer, ICompressor> compressors;

	static {
		compressors = new TreeMap<Integer, ICompressor>();

		compressors.put(JavaZipFast.UID, new JavaZipFast());

		compressors.put(JavaZipNormal.UID, new JavaZipNormal());

//		compressors.put(Exificient.UID, new Exificient());
//		compressors.put(SnappyJS.UID, new SnappyJS());

	}

	public static Map<Integer, ICompressor> getCompressors() {
		return compressors;
	}

	public static ICompressor getSelectedCompressor() {
		return compressors.get(Config.instance().defaultCompressor);
	}

}
