package org.korvin.mp3remover;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.korvin.mp3remover.libs.AbstractFileProcessor;

public class MainEntryPoint {

	static Set<String> audios;

	static {
		audios = new HashSet<>();
		audios.add("mp3");
		audios.add("wav");
		audios.add("pcm");
		audios.add("raw");
		audios.add("ape");
		audios.add("flac");
		audios.add("wv");
		audios.add("aud");
		audios.add("aif");
	}

	public static void main(String[] args) {
		AbstractFileProcessor abs = new AbstractFileProcessor(args[0]) {

			public boolean isAudio(FileInfo fi) {
				return audios.contains(fi.getExtension().toLowerCase());
			}

			@Override
			public boolean accept(File entry, FileInfo fileData) {

				if (isAudio(fileData)) return true;
				String ext = fileData.getExtension().toLowerCase();
				if ("dat".equals(ext)) return true;
				if ("m4a".equals(ext)|| "aac".equals(ext)) return false;
				System.out.println("Ignored: "+fileData.getFileName());
				return false;
			}

			@Override
			public void processFile(File file, FileInfo fileInfo) throws Exception {
				if (isAudio(fileInfo)) processAudio(file, fileInfo);
				if (isTempFile(file, fileInfo)) deleteTemp(file);
			}

			private void deleteTemp(File file) {
				file.delete();
			}

			private boolean isTempFile(File file, FileInfo fileInfo) {
				return (file.isFile() && fileInfo.getFileNameBase().toLowerCase().contains("partfile"));
			}

			private void processAudio(File file, FileInfo fileInfo) {
				if (!file.isFile()) return ;
				String toCheck = fileInfo.getPath()+'\\'+fileInfo.getFileNameBase()+".m4a";
				File tcf = new File(toCheck);
				if (!tcf.exists()) {
					System.out.println("Could NOT be deleted: "+fileInfo.getNameWithPath());
					return ;
				}

				double size = (double)file.length();
				long min = (long)Math.round(size / 8d);
				if (tcf.length()<min || tcf.length() > file.length()) {
					System.out.println("Strange file size: "+tcf.getName());
					return ;
				}
				file.delete();

			}
		};
		abs.start();
		System.out.println("well done!");
	}
}
