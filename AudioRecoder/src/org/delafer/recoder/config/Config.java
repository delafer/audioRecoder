package org.delafer.recoder.config;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import org.delafer.recoder.deprecated.compressors.JavaZipFast;
import org.delafer.recoder.helpers.utils.UtilsFile;
import org.delafer.recoder.model.common.MString;


public class Config {

    /** demand holder idiom Lazy-loaded Singleton */
    private static class Holder {
        private final static Config	INSTANCE	= new Config();
    }

    /**
     * Gets the single instance of LockCore.
     * @return single instance of LockCore
     */
    public static Config instance() {
        return Holder.INSTANCE;
    }

    public MString sourceDirectory = new MString(UtilsFile.correctPath("F:\\tests\\test3",false));
//    public MString sourceDirectory = new MString(UtilsFile.correctPath("F:\\torrent\\finished.books\\",false));
//    public MString tempDirectory = new MString(UtilsFile.correctPath("C:\\tmp\\",false));
//    public MString workDirectory = new MString(UtilsFile.correctPath("C:\\tmp\\work\\",false));
    public MString tempDirectory = new MString(UtilsFile.correctPath("c:\\tmp\\",false));
    public MString workDirectory = new MString(UtilsFile.correctPath("c:\\tmp\\work\\",false));


    public int threads = Runtime.getRuntime().availableProcessors()+1;

    public static final String PATH_SEPARATOR = System.getProperty("file.separator");

    public static final String BASEDIR_PLUGINS = UtilsFile.correctPath("e:\\plugins\\",false);

	public static Set<String> audios;

	static {
		audios = new HashSet<>();
		audios.add("mp3");
		audios.add("wav");
		audios.add("pcm");
		audios.add("raw");
		audios.add("ape");
		audios.add("flac");
		audios.add("fla");
		audios.add("wv");
		audios.add("aud");
		audios.add("aif");
		audios.add("ra");
		audios.add("rm");
		audios.add("wma");
		audios.add("mp+");
		audios.add("mpc");
		audios.add("opus");
		audios.add("m4a");
		audios.add("m4b");
		audios.add("aac");
		audios.add("aac");
		audios.add("ofs");
		audios.add("ofr");
		audios.add("spx");
		audios.add("mac");
		audios.add("ogg");
	}











  public int threadsRatio = 25;
  public int cacheRatio = 50;
  public int defaultCompressor = JavaZipFast.UID;
  public boolean cacheOn = false;
  public int cacheEntries = 500;

  public int selectedFile = 2;


  public long sizeCompressed = 0l;
  public double ratioCompresion = 100d;
  public long sizedSaved = 0l;

}
