package org.korvin.mp3remover;

public class Testttt {

	static double rates[] = new double[] {44100, 32000, 22000, 16000, 11000};
	static double size[] = new double[] {8270, 7858, 5854, 4458, 3090};

	static double size2[] = new double[] {6832, 6879, 4868, 3459, 2863};

	static int[][] vars = new int[][] {{1,2},{1,3},{1,4},{1,5},{2,3},{2,4},{2,5},{3,4},{3,5},{4,5}};

	public static void main(String[] args) {
		int c = 0;
		double total = 0d;
		for (int i = 0; i < vars.length; i++) {
			c++;
			int[] xy = vars[i];
			int x = xy[0]-1;
			int y = xy[1]-1;
			double r = rates[x] / rates[y];
			double s = size[x] / size[y];

//			double se1=  Math.pow(r / 1.05d, 1d/1.6d);
			double se1=  Math.pow(r*1.2 , 1d/(1.5d))/1.2;

			double deviation = s/se1;
			total += deviation;
			System.out.println(se1);
			System.out.println("R: "+r+"\r\nS: "+s+"\r\nDev: "+deviation+"\r\n");
		}

		System.out.println("Final ratio: "+(total/((double)c)));
//		double x1 = 1.2185305845012258;
//		double x2 = 1.2494348635416497;
//		System.out.println((x1+x2)*0.5d);

	}

}
