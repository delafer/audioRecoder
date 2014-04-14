package org.delafer.recoder.cli.compressor;

public class CompandingFilter {


	public static int[] dbi = new int[] { -70, -55, -45, -30, -20, -10, -1 };
	public static int[] dbn = new int[] { -70, -30, -20, -15, -14, -8, -14 };

	
	public static int getHighValue(int at) {
		return dbi[at];
	}
	
	public static int getLowValue(int at) {
		return dbi[at];
	}
	
	public static void main(String[] args) {
		
		

	}

}
