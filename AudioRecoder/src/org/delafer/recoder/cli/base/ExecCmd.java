package org.delafer.recoder.cli.base;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import org.delafer.recoder.config.Config;
import org.delafer.recoder.helpers.utils.UtilsFile;
import org.delafer.recoder.helpers.utils.UtilsString;
import org.delafer.recoder.model.AudioFile;
import org.delafer.recoder.model.AudioFile.AudioData;
import org.delafer.recoder.model.ProgressModel;


public abstract class ExecCmd {


	private String cliPath;

	private AudioFile inputFile;
	private AudioFile outputFile;
	private AudioFile source;

	private static File workDir = new File(Config.instance().workDirectory.toString());

	public abstract boolean isInputWithExtension();
	public abstract boolean isOutputWithExtension();

	public abstract String getCliExec();
	public abstract String getCliArgs(AudioData input);

	public abstract String getDestExtension();

	public String getCliPath() {
		return cliPath;
	}
	public void setCliPath(String cliPath) {
		this.cliPath = cliPath;
	}
	public AudioFile getInputFile() {
		return inputFile;
	}
	public void setInputFile(AudioFile inputFile) {
		this.inputFile = inputFile;
	}
	public AudioFile getOutputFile() {
		return outputFile;
	}
	public void setOutputFile(AudioFile outputFile) {
		this.outputFile = outputFile;
	}


	public AudioFile getSource() {
		return source;
	}
	public void setSource(AudioFile source) {
		this.source = source;
	}


	public String getCMDLine() {
		StringBuilder sb = new StringBuilder();
		sb.append(UtilsFile.correctPath(getCliPath(), false));
		sb.append(getCliExec());
		sb.append(' ');

		AudioFile i = getInputFile();
		AudioFile o = getOutputFile();

		String args = getCliArgs(i.getData());

		String inputName = getNameForCLI(i, isInputWithExtension());
		String outputName = getNameForCLI(o, isOutputWithExtension());

		args = UtilsString.replace(args, "%i", inputName);
		args = UtilsString.replace(args, "%o", outputName);

		sb.append(args);

		return sb.toString();
	}
	private String getNameForCLI(AudioFile i, boolean withExt) {
		//System.out.println(">>>>>>>>>"+i.getFileData().getFullNameWithPath());
		String str =  withExt ? i.getFileData().getFullNameWithPath() : i.getFileData().getBaseNameWithPath();
		StringBuilder sb = new StringBuilder(str.length()+2);
		sb.append("\"");

		sb.append(str);
		sb.append("\"");
		return sb.toString();
	}


	 static boolean needsQuoting(String s) {
	        int len = s.length();
	        if (len == 0) // empty string have to be quoted
	            return true;
		for (int i = 0; i < len; i++) {
		    switch (s.charAt(i)) {
		    case ' ': case '\t': case '\\': case '"':
			return true;
		    }
		}
		return false;
	    }

	    static String winQuote(String s) {
		if (! needsQuoting(s))
		    return s;
		s = s.replaceAll("([\\\\]*)\"", "$1$1\\\\\"");
		s = s.replaceAll("([\\\\]*)\\z", "$1$1");
		return "\"" + s + "\"";
	    }

	public int run()  {
		int exitVal = 0;
		try {

			String cmdLine = getCMDLine();

			System.out.println("LINE:"+cmdLine);
			String[] aaa = tokenizer(cmdLine);

//			int j =0;
//			for (String string : aaa) {
//				System.out.println((++j)+"}}} "+string);
//			}
			final Process prc = Runtime.getRuntime().exec(tokenizer(cmdLine), null, workDir);

			SyncPipe sp1 = new SyncPipe(prc.getInputStream());
			SyncPipe sp2 = new SyncPipe(prc.getErrorStream());

			sp1.start();
			sp2.start();


			prc.waitFor(36, TimeUnit.MINUTES);
			exitVal = prc.exitValue();


			sp1.interrupt();
			sp2.interrupt();

			sleep();
			getInputFile().close();

		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		return exitVal;
	}


	public static String[] tokenizer(String cmdLine) {
		ArrayList<String> al = new ArrayList<String>();
		char ch;
		StringBuilder sb = new StringBuilder();
		boolean quotes = false;
		for (int i = 0; i < cmdLine.length(); i++) {
			ch = cmdLine.charAt(i);
			switch (ch) {
			case ' ':  //32 - space
			case '\t': //9 - tab
			case '\n': //10 - newline
			case '\r': //13 - carriage return
			case '\f': //12 - page break / form feed char
				if (quotes)
					sb.append(ch);
				else
					flushSb(al, sb);
				break;
			case '"':
				quotes = !quotes;
			default:
				sb.append(ch);
				break;
			}
		}
		flushSb(al, sb);
		String[] res = new String[al.size()];
		al.toArray(res);
		return res;
	}


	private static void flushSb(ArrayList<String> al, StringBuilder sb) {
		if (sb.length()>0) {
			al.add(sb.toString());
			sb.setLength(0);
		}

	}


	private static final class SyncPipe extends Thread
	{

	private static int PRIO_LOW = (int)Math.round(0.55d * (double)(Thread.MIN_PRIORITY + Thread.NORM_PRIORITY));

	public SyncPipe(InputStream istrm) {
		  this.setPriority(PRIO_LOW);
		  this.setDaemon(true);
	      istrm_ = istrm;
	  }
	  public void run() {
	      try
	      {
	    	 BufferedReader in = new BufferedReader(new InputStreamReader(istrm_));
	    	 String line;
	    	 while((line = in.readLine()) != null) {
	    		 if (couldBeSkipped(line)) continue ;
	    		 ProgressModel.toConsole(line);
//	    		 System.out.println(line);
	    	 }
	    	 in.close();
	      }
	      catch (Exception e)
	      {
	          e.printStackTrace();
	      }
	  }
	private boolean couldBeSkipped(String line) {
		if (line.isEmpty()) return true;
		if (line.startsWith("Processed") && line.endsWith("seconds...")) return true;
		if ("*                                                           *".equals(line)) return true;
		return false;
	}
	  private final InputStream istrm_;
	}


	private final void sleep() {
        try {
        	for (int a = 9; a >= 0; a--) {
        		Thread.yield();
        		Thread.sleep(50);
			}
		} catch (final InterruptedException a) {}
	}




}
