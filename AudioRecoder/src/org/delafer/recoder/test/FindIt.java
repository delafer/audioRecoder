package org.delafer.recoder.test;

public class FindIt {
	
	
	public static class ThreadRatio {
		
		public ThreadRatio(int packers, int depackers) {
			this.packers = packers;
			this.depackers = depackers;
		}
		
		public int packers;
		public int depackers;
	}
	
	public static ThreadRatio findRatio(int rate) {
	
	if (rate==100) return new ThreadRatio(1, 0);
	if (rate==0) return new ThreadRatio(0, 1);
		
	int c1 = 0;
	int c2 = 1;
	int c3;
	int z1 = 1;
	int z2 = 0;
	int z3;
	double xx;
	double rz;
	double err;
	double pi_x = Math.PI;
	pi_x = 0.01d * (double)rate;
	
	  c1 =  (int)Math.floor(pi_x);
	  int iter = 0;
	  do
	  {
		 iter++;
		 c3 = c1+c2;
		 z3 = z1+z2;
		 rz = (double)c3/z3;
		 err = rz-pi_x;
		 //System.out.printf("%d/%d = %.12f error %.5e\r\n",c3,z3,rz,err);
		 xx = (double)pi_x * z3;
		 if (xx<c3)
		 {
		   c2 = c3;
		   z2 = z3;
		 }
		 else
		 {
		   c1 = c3;
		   z1 = z3;
		 }
		 
		 //System.out.println(err);
	  } while (((Math.abs(err)>0.01d)&&(z3<1000)) && (iter<64));
	  
//	  System.out.println("AT="+c3);
//	  System.out.println("BT="+(z3-c3));
	  
	  return new ThreadRatio(c3, (z3-c3));
	}
	
	public static void main(String[] args) {
		ThreadRatio tr = findRatio(25);
		System.out.println(tr.depackers);
		System.out.println(tr.packers);
	}
	
	
}
