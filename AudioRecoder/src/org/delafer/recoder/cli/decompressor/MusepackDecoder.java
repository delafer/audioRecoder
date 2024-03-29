package org.delafer.recoder.cli.decompressor;

import org.delafer.recoder.cli.base.ExecCmd;
import org.delafer.recoder.config.Config;
import org.delafer.recoder.helpers.utils.UtilsFile;
import org.delafer.recoder.model.AudioFile.AudioData;

public class MusepackDecoder extends ExecCmd {

	public MusepackDecoder() {}

	static final String cliPath = UtilsFile.correctPath(Config.BASEDIR_PLUGINS+"mpc", false);

	@Override
	public String getCliPath() {
		return cliPath;
	}

	public String getCliExec() {
		return "mpcdec.exe";
	}

	public String getCliArgs(AudioData input) {
		return "--silent %i %o";
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
