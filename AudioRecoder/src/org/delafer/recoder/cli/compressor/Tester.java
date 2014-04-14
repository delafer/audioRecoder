package org.delafer.recoder.cli.compressor;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import org.delafer.recoder.helpers.utils.UtilsCommon;

public class Tester {

	public static final DecimalFormat FRM_INT_2F = new DecimalFormat("#.##", new DecimalFormatSymbols(Locale.US));
	private static String format(double d) {
		return FRM_INT_2F.format(UtilsCommon.round(d, 2));
	}

	private static double convert(double i) {
		double res = (i *  6d ) - 2d;
		if (res<-2d) res = -2d;
		if (res > 10d) res = 10d;
		return res;

	}


	public static void main(String[] args) {

//		System.out.println(Math.sq);
		double f = 0.01d;

		do {
			if (f>2d) f = 2d;
			System.out.println(format(f)+ " > "+format(convert(f)));
			f = f + 0.05;

		} while (f < 2.05d);



//		while (f <= 2d) {
//			System.out.println(format(f));
//			f = f + 0.05;
//			if (f>2d) f = 2d;
//
//		}


	}

}
