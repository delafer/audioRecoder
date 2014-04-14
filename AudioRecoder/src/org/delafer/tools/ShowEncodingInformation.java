package org.delafer.tools;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.delafer.recoder.cli.base.ExecCmd;
import org.delafer.recoder.helpers.AbstractFileProcessor;
import org.delafer.recoder.model.AudioFile;
import org.delafer.recoder.model.FilePersistent;
import org.delafer.recoder.model.AudioFile.AudioData;

public class ShowEncodingInformation {

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
	}

	public static void main(String[] args) {

		AbstractFileProcessor abs = new AbstractFileProcessor("F:\\m4a\\") {

			@Override
			public void processFile(File file, FilePersistent f) throws Exception {

				AudioFile af = new AudioFile(file);
				System.out.println(f.getFullName()+" -> bitrate: "+af.getBitRate());


			}

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub

			}

			@Override
			public boolean accept(File entry, FilePersistent fileData) {
				String ext = fileData.getExtension().toLowerCase();
				if (!audios.contains(ext)) return false;
				return !"m4a".equals(fileData.getExtension());
			}
		};
		abs.start();
	}

}
