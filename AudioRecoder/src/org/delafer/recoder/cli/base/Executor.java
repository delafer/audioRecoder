package org.delafer.recoder.cli.base;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

public class Executor {

	private static final File WORKING_DIR = new File("b:/");


	public Executor() {
	}

	public static void main(String[] args) {
		Executor e = new Executor();
		e.run();
	}

	private static int PRIO_LOW = (int)Math.round(0.5d * (double)(Thread.MIN_PRIORITY + Thread.NORM_PRIORITY));


	public void run()  {
		try {
			long t1 = System.currentTimeMillis();
//			final Process prc = Runtime.getRuntime().exec("D:\\audio\\lame399.64\\lame.exe --silent --decode D:\\audio\\decode.mp3 c:\\wav\\decode.wav", null, WORKING_DIR);
			final Process prc = Runtime.getRuntime().exec("D:\\audio\\mpg123\\mpg123.exe -q  --skip-id3v2  --ignore-streamlength -w b:\\decoded.wav D:\\audio\\decode.mp3", null, WORKING_DIR);

			Thread t = new Thread() {

				@Override
				public void run() {
				    try
				    {
				        BufferedReader brN = new BufferedReader(new InputStreamReader(prc.getInputStream()));
//				        BufferedReader brE = new BufferedReader(new InputStreamReader(prc.getErrorStream()));
				        String n, e=null;
				        while (((n = brN.readLine()) != null))
				        {
				           if (null != n) System.out.println(n);
//				           if (null != e) System.err.println(e);
				        }

				        brN.close();
//				        brE.close();
				    }
				    catch (IOException e)
				    {
				        e.printStackTrace();
				    }

				}

			};
			t.setDaemon(true);
			t.setPriority(Thread.NORM_PRIORITY);
			t.start();


			prc.waitFor(16, TimeUnit.MINUTES);
			int exitVal = prc.exitValue();

			t.interrupt();
			long t2 = System.currentTimeMillis();
			System.out.println(t2-t1);
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

}
