package org.korvin.mp3remover;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.korvin.mp3remover.libs.AbstractFileProcessor;

public class CleanM4A {

	static Set<String> audios;

	static {
		audios = new HashSet<>();
		audios.add("m4a");
		audios.add("aac");
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
				System.out.println(fileInfo.getFileName());
				if (isAudio(fileInfo)) processAudio(file, fileInfo);
			}


			private void processAudio(File file, FileInfo fileInfo) {
				if (!file.isFile()) return ;
				String toCheck = fileInfo.getPath()+'\\'+fileInfo.getFileNameBase()+".mp3";
				File tcf = new File(toCheck);
				if (!tcf.exists()) {
					System.out.println("Could NOT be deleted: "+fileInfo.getNameWithPath());
					return ;
				}

				file.delete();

			}
		};
		abs.start();
		System.out.println("well done!");
	}
}
