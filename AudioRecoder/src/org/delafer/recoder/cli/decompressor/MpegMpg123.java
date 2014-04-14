package org.delafer.recoder.cli.decompressor;

import org.delafer.recoder.cli.base.ExecCmd;
import org.delafer.recoder.config.Config;
import org.delafer.recoder.helpers.utils.UtilsFile;
import org.delafer.recoder.model.AudioFile.AudioData;

public class MpegMpg123 extends ExecCmd {

	public MpegMpg123() {}

	static final String cliPath = UtilsFile.correctPath(Config.BASEDIR_PLUGINS+"mpg123", false);

	@Override
	public String getCliPath() {
		return cliPath;
	}

	public String getCliExec() {
		return "mpg123.exe";
	}

	public String getCliArgs(AudioData input) {
		return "--float --skip-id3v2  --ignore-streamlength -w %o %i";
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
