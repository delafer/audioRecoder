package org.delafer.recoder.cli.decompressor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import org.delafer.recoder.cli.base.ExecCmd;
import org.delafer.recoder.model.AudioFile.AudioData;

public class DecompressorWrapper extends ExecCmd {

	public DecompressorWrapper() {}


	public boolean isInputWithExtension() {
		return true;
	}

	public boolean isOutputWithExtension() {
		return true;
	}

	public String getDestExtension() {
		return "wav";
	}


	public String getCliExec() {
		return null;
	}


	@Override
	public String getCliArgs(AudioData input) {
		return null;
	}


	@Override
	public int run() {
		try {
			copyFile(getInputFile().getFileData().getFile(), getOutputFile().getFileData().getFile());
			return 0;
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Can't copy file: "+getInputFile());
			return -1;
		}
	}


	@SuppressWarnings("resource")
	private static void copyFile(File sourceFile, File destFile) throws IOException {
		if (!sourceFile.exists()) return;

		ensureTargetDirectoryExists(destFile.getParentFile());

		if (!destFile.exists()) {
			destFile.createNewFile();
		}
		FileChannel source = null;
		FileChannel destination = null;
		source = new FileInputStream(sourceFile).getChannel();
		destination = new FileOutputStream(destFile, false).getChannel();
		if (destination != null && source != null) {

			long bytesTransferred = 0;
	        while(bytesTransferred < source.size()){
	          bytesTransferred += source.transferTo(0, source.size(), destination);
	        }

//			destination.transferFrom(source, 0, source.size());
		}
		if (source != null) {
			source.close();
		}
		if (destination != null) {
			destination.close();
		}

	}

	  private static void ensureTargetDirectoryExists(File aTargetDir){
		    if(!aTargetDir.exists()){
		      aTargetDir.mkdirs();
		    }
		  }
}
