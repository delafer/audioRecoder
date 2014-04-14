package org.delafer.recoder.cli.dsp;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

import org.delafer.recoder.helpers.utils.UtilsCommon;


public class Equalizer {

	public static final double AMPLIFICATION = 2d;

//	double[][] equalizer =
//		{
//			{   85,1.5d,-1.0d },
//			{  190,1.0d, 0.9d },
//			{  400,1.0d,-0.7d },
//			{ 1300,1.2d, 0.7d },
//			{ 2500,0.5d, 0.4d },
//			{ 4000,2.0d, 0.4d },
//			{11000,1.5d,-2.0d },
//		};

	double[][] equalizer =
	{
		{   70,1.5d,-1.0d },
		{  150,0.7d, 0.7d },
		{  291,3.0d,-0.4d },
//		{  400,1.0d, 0.2d },
		{ 1300,1.2d, 0.7d },
		{ 2500,0.5d, 0.4d },
		{ 4000,2.0d, 0.4d },
		{11000,1.5d,-2.0d },
	};


	public static final DecimalFormatSymbols dcs = new DecimalFormatSymbols(Locale.US);
	public static final NumberFormat floatFormat = new DecimalFormat("0.##", dcs);

    /** demand holder idiom Lazy-loaded Singleton */
    private static class Holder {
        private final static Equalizer	INSTANCE	= new Equalizer();
    }

    /**
     * Gets the single instance of LockCore.
     * @return single instance of LockCore
     */
    public static Equalizer instance() {
        return Holder.INSTANCE;
    }


    private String eqValues;

	private Equalizer() {
		this. eqValues = getEqualizer();
	}

	private String getEqualizer() {
		StringBuilder sb = new StringBuilder();
		for (double[] next : equalizer) {
			sb.append(" equalizer ");
			sb.append(Math.round(next[0]));
			sb.append(' ');
			sb.append(UtilsCommon.round(next[1], 1));
			sb.append("q ");
			if (next[2]>0) sb.append('+');
			sb.append(floatFormat.format(UtilsCommon.round((AMPLIFICATION*next[2]), 1)));

		}
		sb.append(' ');
		return sb.toString();
	}

	public String getEqualizerSettings() {
		return eqValues;
	}




	public static void main(String[] args) {
			String a = Equalizer.instance().getEqualizerSettings();
			System.out.println("["+a+"]");
			System.out.println(floatFormat.format(1000.2d));

	}
}
