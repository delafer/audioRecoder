package org.delafer.recoder.cli.decompressor;

import org.delafer.recoder.cli.base.ExecCmd;
import org.delafer.recoder.config.Config;
import org.delafer.recoder.helpers.utils.UtilsFile;
import org.delafer.recoder.model.AudioFile.AudioData;

public class FlacDecoder extends ExecCmd {

	public FlacDecoder() {}

	static final String cliPath = UtilsFile.correctPath(Config.BASEDIR_PLUGINS+"flac", false);

	@Override
	public String getCliPath() {
		return cliPath;
	}

	public String getCliExec() {
		return "flac.exe";
	}

	public String getCliArgs(AudioData input) {
		return "-d -s -o %o %i";
	}


	public boolean isInputWithExtension() {
		return true;
	}

	public boolean isOutputWithExtension() {
		return true;
	}

	public String getDestExtension() {
		return "wav";
	}

}
