package org.delafer.recoder.cli.decompressor;

import org.delafer.recoder.cli.base.ExecCmd;
import org.delafer.recoder.config.Config;
import org.delafer.recoder.helpers.utils.UtilsFile;
import org.delafer.recoder.model.AudioFile.AudioData;

/**
 * @author korvin
 * could be used to decode wma
 *
 */
public class FFMpegDecoder extends ExecCmd {

	public FFMpegDecoder() {}

	static final String cliPath = UtilsFile.correctPath(Config.BASEDIR_PLUGINS+"ffmpeg", false);

	@Override
	public String getCliPath() {
		return cliPath;
	}

	public String getCliExec() {
		return "ffmpeg.exe";
	}

	public String getCliArgs(AudioData input) {
		return "-loglevel panic -y -i %i -vn -f wav %o";
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
