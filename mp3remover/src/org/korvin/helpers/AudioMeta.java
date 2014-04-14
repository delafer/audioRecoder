package org.korvin.helpers;

import java.io.File;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.red5.io.m4a.impl.AACReader;

public class AudioMeta {

	public static int minRecommendedBitrate(int sampleRate, int channels) {
		double stereo2mono = 1.70d; //stereo to mono koeff
		int sample_rate_base = 44100;
		double bitrate_default = 48.5d;
		double korr = 1d;
		if (sample_rate_base != sampleRate) korr = Math.pow(((double)sample_rate_base/(double)sampleRate)*1.2 , 1d/(1.5d))/1.2;
		double t = 2.5d;
		double r = bitrate_default  / Math.pow(korr, t / (t+1d));
		double pre = (channels == 1 ? (r / stereo2mono) : r);
		double max_deviation = pre > 30 ? 0.25d : 0.15d; //0.35d; //35%
		return (int)Math.round( pre * (1d - max_deviation) );
	}

	AudioHeader audioData;
	File file;
	int channels = 0;

	private AudioMeta(File file) {
		this.file = file;
		try {
			AudioFile af = AudioFileIO.read(file);
			this.audioData = af.getAudioHeader();

			this.channels = asNum(audioData.getChannels());
			if ((channels < 1 || channels > 7 ) && ("AAC".equalsIgnoreCase(audioData.getEncodingType()))) {
				AACReader r = new AACReader(file, true);
				System.out.print(">>>"+channels);
				channels = r.getAudioChannels();
				System.out.println("-"+channels);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static AudioMeta instance(File ff) {
		return new AudioMeta(ff);
	}

	private int asNum(String s) {
		if (s == null || s.isEmpty()) return 0;

		try {
			Double d = Double.valueOf(s);
			return d.intValue();
		} catch (Exception e) {
		}
		return 0;
	}

	public int getMinimumBitrate() {
		return minRecommendedBitrate(audioData.getSampleRateAsNumber(), channels);
	}

	public boolean isLowQuality() {
		int min = getMinimumBitrate();
		int current = (int)audioData.getBitRateAsNumber();
		boolean lowQuality = (current < min);
		if (lowQuality) {
			System.out.println(Args.fill("Low quality [%1]: %2 < %3" , file.getName(), current, min));
		}
		return lowQuality;
	}

	public AudioHeader getAudioHeader() {
		return audioData;
	}

	public int getChannels() {
		return channels;
	}

	public String getChannelsText() {
		return channels == 1 ? "mono" : channels == 2 ? "stereo" : ""+channels;
	}


}
