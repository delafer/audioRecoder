package org.delafer.recoder.cli.dsp;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

import org.delafer.recoder.config.Config;
import org.delafer.recoder.helpers.utils.UtilsCommon;
import org.delafer.recoder.helpers.utils.UtilsFile;
import org.delafer.recoder.helpers.utils.UtilsString;
import org.delafer.recoder.model.AudioFile;
import org.delafer.recoder.model.AudioFile.AudioData;
import org.delafer.recoder.model.ProgressModel;

public class GainAnalyzer {

	public static double targetLevel = -17.0d;

    private enum Signature {Average("RMS lev dB"), Max("RMS Pk dB"), Min("RMS Tr dB"), Offset("DC offset");

    private String signChars;

    Signature(String value) {
        this.signChars = value;
    }

    public String getSignChars() {
        return signChars;
    }
    }

    private Double volumeAvr;
    private Double volumeMin;
    private Double volumeMax;
    private Double offset;

    static final String cliPath = UtilsFile.correctPath(Config.BASEDIR_PLUGINS+"sox", false);

    private AudioFile inputFile;


    private static File workDir = new File(Config.instance().workDirectory.toString());

    public  String getCliExec() {
        return "sox.exe";
    }
    public  String getCliArgs(AudioData input) {
        return "%i -n stats";
    }


    public String getCliPath() {
        return cliPath;
    }

    public AudioFile getInputFile() {
        return inputFile;
    }
    public void setInputFile(AudioFile inputFile) {
        this.inputFile = inputFile;
    }


    public String getCMDLine() {
        StringBuilder sb = new StringBuilder();
        sb.append(UtilsFile.correctPath(getCliPath(), false));
        sb.append(getCliExec());
        sb.append(' ');

        AudioFile i = getInputFile();

        String args = getCliArgs(i.getData());

        String inputName = getNameForCLI(i);

        args = UtilsString.replace(args, "%i", inputName);

        sb.append(args);

        return sb.toString();
    }
    private String getNameForCLI(AudioFile i) {
        String str =   i.getFileData().getFullNameWithPath();
        StringBuilder sb = new StringBuilder(str.length()+2);
        sb.append('"');
        sb.append(str);
        sb.append('"');
        return sb.toString();
    }


    public int run()  {
        int exitVal = 0;
        try {

            String cmdLine = getCMDLine();

            final Process prc = Runtime.getRuntime().exec(cmdLine, null, workDir);

            readCLIStream(prc.getInputStream());
            readCLIStream(prc.getErrorStream());


            prc.waitFor(16, TimeUnit.MINUTES);
            exitVal = prc.exitValue();

            sleep();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return exitVal;
    }
    private void readCLIStream(InputStream stream) {
        try
          {
             BufferedReader in = new BufferedReader(new InputStreamReader(stream));
             String line;
             while((line = in.readLine()) != null) {
                 if (line.isEmpty()) continue ;
                 decodeLine(line.trim());
             }
             in.close();
          }
          catch (Exception e)
          {
              e.printStackTrace();
          }
    }


    private void decodeLine(String line) {
        for (Signature next : Signature.values()) {
            if (line.startsWith(next.getSignChars())) {
                String pLine = line.substring(next.getSignChars().length()).trim();
                String[] aLine = pLine.split(" ");
               	if (aLine.length>0) {
               		Double value = parse(aLine[0]);

               		switch (next) {
					case Average:
						this.volumeAvr = value;
						break;
					case Max:
						this.volumeMax = value;
						break;
					case Min:
						this.volumeMin = value;
						break;
					case Offset:
						this.offset = value;
						break;
					default:
						break;
					}

               	}

            }
        }

    }


    private Double parse(String val) {
		try {
			return Double.parseDouble(val);
		} catch (Exception e) {
			return null;
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
          if (istrm_ == null) return ;
          try
          {
             BufferedReader in = new BufferedReader(new InputStreamReader(istrm_));
             String line;
             while((line = in.readLine()) != null) {
                 if (line.isEmpty()) continue ;
                 ProgressModel.toConsole(line);
             }
             in.close();
          }
          catch (Exception e)
          {
              e.printStackTrace();
          }
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
	public Double getVolumeAvr() {
		return volumeAvr;
	}
	public Double getVolumeMin() {
		return volumeMin;
	}
	public Double getVolumeMax() {
		return volumeMax;
	}

	public Double averageVolume() {
		if (volumeAvr==null || volumeMin == null || volumeMax == null) return volumeAvr;

		double rAvg = volumeAvr.doubleValue();
		double min = volumeMin.doubleValue();
		if (min<-45d) min = -45d;
		double max = volumeMax.doubleValue();
		double cAvg = (min+ max) * 0.5d;

		double nAvg = (rAvg * 9d + cAvg) / 10.0d;
		return UtilsCommon.round(nAvg,  2);

	}

	public Double correctionLevel() {
		Double avrVol = averageVolume();
		if (null == avrVol) return null;
		return UtilsCommon.round(targetLevel - avrVol.doubleValue(), 1);
	}
	public Double getOffset() {
		return offset != null ? Double.valueOf(-offset.doubleValue()) : null;
	}


}
