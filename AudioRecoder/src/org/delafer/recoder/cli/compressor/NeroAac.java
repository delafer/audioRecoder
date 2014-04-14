package org.delafer.recoder.cli.compressor;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import org.delafer.recoder.cli.base.ExecCmd;
import org.delafer.recoder.config.Config;
import org.delafer.recoder.helpers.Args;
import org.delafer.recoder.helpers.utils.UtilsCommon;
import org.delafer.recoder.helpers.utils.UtilsFile;
import org.delafer.recoder.model.AudioFile.AudioData;

import com.sun.corba.se.spi.orbutil.fsm.Input;

public class NeroAac extends ExecCmd {


	public static final DecimalFormat FRM_INT_2F = new DecimalFormat("#.##", new DecimalFormatSymbols(Locale.US));

	public NeroAac() {}

	static final String cliPath = UtilsFile.correctPath(Config.BASEDIR_PLUGINS+"nero", false);

	@Override
	public String getCliPath() {
		return cliPath;
	}

	public String getCliExec() {
		return "neroAacEnc.exe";
	}



	public String getCliArgs(AudioData input) {

		StringBuilder arg = new StringBuilder();

		arg.append(" -ignorelength");
		arg.append(Args.fill(" -q %1", format(getCompressionFactor(input))));
		arg.append(input.channels >= 2 ? " -hev2" : " -he");
		arg.append(" -he");
		arg.append(" -if %i -of %o");

		return arg.toString();
	}


	private double getCompressionFactor(AudioData input) {
		double base = 0.20;
		if (input.channels == 1) base *= 1.30d;
		base *= getFactorBySR(input.sampleRate.getSampleRate());

		return base;
	}


	private String format(double d) {
		return FRM_INT_2F.format(UtilsCommon.round(d, 2));
	}

	public boolean isInputWithExtension() {
		return true;
	}

	public boolean isOutputWithExtension() {
		return true;
	}

	public String getDestExtension() {
		return "m4a";
	}

	public double getFactorBySR(int sr) {
		switch (sr) {
		case 6000:
			return 1.58d;
		case 8000:
			return 1.55d;
		case 110250:
			return 1.53d;
		case 12000:
			return 1.50d;
		case 16000:
			return 1.48d;
		case 22050:
			return 1.46d;
		case 24000:
			return 1.45d;
		case 32000:
			return 1.4d;
		case 44100:
		case 48000:
		default:
			return 1d;
		}
	}
}
