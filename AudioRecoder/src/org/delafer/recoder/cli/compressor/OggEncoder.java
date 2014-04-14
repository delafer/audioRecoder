package org.delafer.recoder.cli.compressor;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Iterator;
import java.util.Locale;

import org.delafer.recoder.cli.base.ExecCmd;
import org.delafer.recoder.config.Config;
import org.delafer.recoder.helpers.Args;
import org.delafer.recoder.helpers.utils.UtilsCommon;
import org.delafer.recoder.helpers.utils.UtilsFile;
import org.delafer.recoder.helpers.utils.UtilsString;
import org.delafer.recoder.model.AudioFile;
import org.delafer.recoder.model.AudioFile.AudioData;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagField;

public class OggEncoder extends ExecCmd {


	public static final DecimalFormat FRM_INT_2F = new DecimalFormat("#.##", new DecimalFormatSymbols(Locale.US));

	public OggEncoder() {}

	static final String cliPath = UtilsFile.correctPath(Config.BASEDIR_PLUGINS+"ogg", false);

	@Override
	public String getCliPath() {
		return cliPath;
	}

	public String getCliExec() {
		return "oggenc2.exe";
	}



	public String getCliArgs(AudioData input) {

		StringBuilder arg = new StringBuilder();



		arg.append(" -Q --ignorelength --advanced-encode-option impulse_noisetune=-5");

		String factor = format(getCompressionFactor(input));
//		System.out.println("###****** "+factor);
		arg.append(Args.fill(" -q %1",factor ));
		//arg.append(input.channels >= 2 ? " -hev2" : " -he");

		String comments = debugInfo();
		if (UtilsString.isNotEmpty(comments)) arg.append(comments);

		arg.append(" -o %o %i");

		return arg.toString();
	}


	private void add(StringBuilder src, MetaDataExtractor tag,  String arg, FieldKey key) {

		try {

			String res = tag.get(key);
			if (UtilsString.isEmpty(res)) return ;

			if (src.length() == 0 || src.charAt(src.length()-1)!=' ') src.append(' ');

			src.append(arg).append(" \"").append(res).append("\"");

		} catch (Throwable e) {
			e.printStackTrace();
		}


	}

	private String debugInfo() {
		AudioFile af = this.getSource();
		if (null == af) return "";
		Tag tag = af.getData().tag;
		if (null == tag) return "";

		StringBuilder sb = new StringBuilder();

		MetaDataExtractor mde = new MetaDataExtractor(tag);

		add(sb, mde, "-c", FieldKey.COMMENT);
		add(sb, mde, "-d", FieldKey.YEAR);
		add(sb, mde, "-N", FieldKey.TRACK);
		add(sb, mde, "-t", FieldKey.TITLE);
		add(sb, mde, "-l", FieldKey.ALBUM);
		add(sb, mde, "-a", FieldKey.ARTIST);
		add(sb, mde, "-G", FieldKey.GENRE);

//		try {
//			Iterator<TagField> it = tag.getFields();
//			while (it.hasNext()) {
//				TagField tf = it.next();
//				System.out.println(">>>"+tf.getId());
//			}
//
//
//			System.out.println(tag.getFirst(FieldKey.ARTIST));
//			System.out.println(tag.getFirst(FieldKey.ALBUM));
//			System.out.println(tag.getFirst(FieldKey.ALBUM_ARTIST));
//			System.out.println(tag.getFirst(FieldKey.TITLE));
//			System.out.println(tag.getFirst(FieldKey.COMMENT));
//			System.out.println(tag.getFirst(FieldKey.YEAR));
//			System.out.println(tag.getFirst(FieldKey.TRACK));
//			System.out.println(tag.getFirst(FieldKey.DISC_NO));
//			System.out.println(tag.getFirst(FieldKey.COMPOSER));
//			System.out.println(tag.getFirst(FieldKey.ARTIST_SORT));
//			System.out.println(tag.getFirst(FieldKey.GENRE));
//			System.out.println(tag.getFirst(FieldKey.ENCODER));
//			System.out.println(tag.getFirst(FieldKey.TRACK_TOTAL));
//			System.out.println(tag.getFirst("\u00a9day"));
//
//		} catch (Throwable e) {
//			e.printStackTrace();
//		}


		return sb.toString();
	}

	private static double convert(double i) {
		double res = (i *  6d ) - 2d;
		if (res<-2d) res = -2d;
		if (res > 10d) res = 10d;
		return res;

	}

	private double getCompressionFactor(AudioData input) {
		double base = 0.165d;
		if (input.channels == 1) base *= 0.96d;
		double factor =getFactorBySR(input.sampleRate.getSampleRate());
		double rFactor = (1d + factor) / 2d;
		base *= rFactor;

		double ret =  convert(base);
		System.out.println("used compression factor: "+ret);
		return ret;
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
		return "ogg";
	}

	public double getFactorBySR(int sr) {
		switch (sr) {
		case 6000:
			return 2.7d;
		case 8000:
			return 2.5d;
		case 110250:
			return 2d;
		case 12000:
			return 1.85d;///
		case 16000:
			return 1.65d;
		case 22050:
			return 1.46d;
		case 24000:
			return 1.23d;///
		case 32000:
			return 1d;
		case 48000:
			return 1.015d;
		case 44100:
		default:
			return 1d;
		}
	}
}
