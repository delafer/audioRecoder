package org.delafer.recoder.cli.dsp;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

import org.delafer.recoder.cli.base.ExecCmd;
import org.delafer.recoder.config.Config;
import org.delafer.recoder.helpers.utils.UtilsFile;
import org.delafer.recoder.model.AudioFile.AudioData;
import org.delafer.recoder.model.enums.SampleRate;
import org.delafer.recoder.model.enums.SampleSize;

public class SoxProcessor extends ExecCmd {

	public SoxProcessor() {}

	static final String cliPath = UtilsFile.correctPath(Config.BASEDIR_PLUGINS+"sox", false);


	private Double gainCorrection;
	private Double offset;

	public static final DecimalFormatSymbols dcs = new DecimalFormatSymbols(Locale.US);
	public static final NumberFormat offsetFormat = new DecimalFormat("0.00####",dcs);

	@Override
	public String getCliPath() {
		return cliPath;
	}

	public String getCliExec() {
		return "sox.exe";
	}



	public static String equalizer = Equalizer.instance().getEqualizerSettings(); //" equalizer 120 0.5q -2 equalizer 750 0.3q +2 equalizer 2000 0.4q +4 equalizer 3500 0.5q +3 equalizer 6000 0.5q +2 equalizer 9000 0.5q -8";

	public String getCliArgs(AudioData input) {

		StringBuilder s = new StringBuilder(160);

		s.append(" %i");
		s.append(getOutputFormat());
		s.append(" %o");
		s.append(getOutputFormatHR());
		s.append(getOffsetArg());
		s.append(getPreGain());
		s.append(getCompand());
		s.append(getHighPass());
		s.append(getEqualizer());
		s.append(getPostGain());
		s.append(" dither -s");
		return s.toString();
	}

	private String getOutputFormatHR() {
		int rate = getInputFile().getSampleRate().getSampleRate();
		if (rate < 22050) {
			return " rate "+SampleRate.SR_22050.getSampleRate();
		} else
		if (rate > SampleRate.SR_44100.getSampleRate()) {
			return " rate "+SampleRate.SR_44100.getSampleRate();
		}
		return "";
	}

	private String getOutputFormat() {
		StringBuilder sb = new StringBuilder();
		if (getInputFile().getSampleSize().getBps() != 16) {
			sb.append(" -b "+SampleSize.BPS_16.getBps());
		}
		return sb.toString();
	}

	private String getOffsetArg() {
		if (offset == null || Math.abs(offset.doubleValue()) < 0.000005d ) return "";
		return " dcshift "+offsetFormat.format(offset)+" 0.02";
	}

	private Object getPostGain() {
		return " gain -n 0";
	}

	private String getEqualizer() {
		return equalizer;
	}


	private Object getHighPass() {
		return " highpass 10";
	}

	private String getPreGain() {
		Double gainCorr = getGainCorrection();

		if (null == gainCorr)
			return " --norm=-1";

		System.out.println("$$$$$$:::gain correction:::"+gainCorr);
		return " gain -b "+gainCorr;
	}

	private String getCompand() {
		return " compand 0.2,1.6 8:-80,-90,-60,-75,-50,-50,-28,-23,-6,-6,0,-5 -3.5 -90 0.1";
		//return " compand 0.2,1.5 6:-80,-90,-60,-75,-50,-50,-47,-40,-36,-23,-8,-8,0,-7 -3 -90 0.1";
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

	public Double getGainCorrection() {
		return gainCorrection;
	}

	public void setGainCorrection(Double gainCorrection) {
		this.gainCorrection = gainCorrection;
	}

	public Double getOffset() {
		return offset;
	}

	public void setOffset(Double offset) {
		this.offset = offset;
	}

}
