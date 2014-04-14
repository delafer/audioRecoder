package org.delafer.tools;

import java.io.File;

import org.delafer.recoder.cli.base.ExecCmd;
import org.delafer.recoder.helpers.AbstractFileProcessor;
import org.delafer.recoder.model.FilePersistent;
import org.delafer.recoder.model.AudioFile.AudioData;

public class CheckUndeletedSources {

	public static void main(String[] args) {

		AbstractFileProcessor abs = new AbstractFileProcessor("F:\\m4a\\") {

			@Override
			public void processFile(File file, FilePersistent f) throws Exception {

				String newName = f.getBaseNameWithPath() + ".m4a";
				File nf = new File(newName);
				if (nf.exists()) {
					System.out.println("Delete: "+f.getFullNameWithPath());
				}


			}

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub

			}

			@Override
			public boolean accept(File entry, FilePersistent fileData) {
				return !"m4a".equalsIgnoreCase(fileData.getExtension());
			}
		};
		abs.start();
	}

}
