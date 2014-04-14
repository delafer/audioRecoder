package org.delafer.recoder.cli.decompressor;

import org.delafer.recoder.cli.base.ExecCmd;
import org.delafer.recoder.config.Config;
import org.delafer.recoder.helpers.utils.UtilsFile;
import org.delafer.recoder.model.AudioFile.AudioData;

public class LameMp3Dec extends ExecCmd {

	public LameMp3Dec() {}

	static final String cliPath = UtilsFile.correctPath(Config.BASEDIR_PLUGINS+"lame", false);

	@Override
	public String getCliPath() {
		return cliPath;
	}

	public String getCliExec() {
		return "lame.exe";
	}

	public String getCliArgs(AudioData input) {
		return "--silent --ignore-tag-errors --priority 2 --decode %i %o";
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
