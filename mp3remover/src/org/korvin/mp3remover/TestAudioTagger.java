package org.korvin.mp3remover;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.korvin.helpers.Args;
import org.korvin.helpers.AudioMeta;
import org.korvin.mp3remover.libs.AbstractFileProcessor;
import org.red5.io.m4a.impl.AACReader;

public class TestAudioTagger {



	static Set<String> audios;

	static Set<String> rates = new TreeSet<String>();

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
	}

	public static void main(String[] args) {

		if (args == null || args.length == 0 || args[0]==null || args[0].trim().length() == 0 ) {
			System.out.println("no args");
			System.exit(-1);
		}

		AbstractFileProcessor abs = new AbstractFileProcessor(args[0]) {

			public boolean isAudio(FileInfo fi) {
				return audios.contains(fi.getExtension().toLowerCase());
			}

			@Override
			public boolean accept(File entry, FileInfo fileData) {
				if (isAudio(fileData)) return true;
				return false;
			}

			@Override
			public void processFile(File file, FileInfo fileInfo) throws Exception {
//				System.out.println(fileInfo.getFileName());
				if (isAudio(fileInfo)) processAudio(file, fileInfo);
			}

			private int asNum(String s) {
				if (s == null || s.isEmpty()) return 0;

				try {
					Double d = Double.valueOf(s);
					return d.intValue();
				} catch (Exception e) {
				}
				return 0;
			}


			private void processAudio(File file, FileInfo fileInfo) {
				if (!file.isFile()) return ;
				if (!fileInfo.getExtension().equalsIgnoreCase("m4a")) return ;
				try {
					AudioMeta meta = AudioMeta.instance(file);
					AudioHeader h = meta.getAudioHeader();
					if (true==true) {
						System.out.println(Args.fill("### %1: bitrate: %2(%3), bps: %4, chanels: %5(%6), s.rate: %7(%8), encoding:%9(%10) ###",
								fileInfo.getFileName(), //1
								h.getBitRateAsNumber(), //2
								h.getBitRate(), //3
								h.getBitsPerSample(), //4
								meta.getChannelsText(),
								meta.getChannels(),
								h.getSampleRate(),
								h.getSampleRateAsNumber(),
								h.getFormat(),
								h.getEncodingType()));
						System.out.println("Length:"+h.getTrackLength());
					}



				} catch (Throwable e) {
					System.out.println(Args.fill("Can't read file: %1", fileInfo.getNameWithPath()));
				}
//				System.out.println();
//				System.out.println();

			}
		};
		abs.start();
	}
}
