package org.delafer.recoder.model;

import java.io.File;
import java.io.Serializable;

import org.delafer.recoder.helpers.utils.UtilsCommon;
import org.delafer.recoder.model.enums.AudioType;
import org.delafer.recoder.model.enums.SampleRate;
import org.delafer.recoder.model.enums.SampleSize;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.audio.mp3.MP3AudioHeader;
import org.jaudiotagger.audio.mp3.Mp3HeaderWrapper;
import org.jaudiotagger.tag.Tag;
import org.red5.io.m4a.impl.AACReader;

public class AudioFile {

	IFile file;
	AudioData data;


	public AudioFile(String fileName) {
		this(FilePersistent.valueOf(fileName), true);
	}

	public AudioFile(File file) {
		this(FilePersistent.valueOf(file), true);
	}

	public AudioFile(IFile file, boolean reinit) {
		this.file = file;
		data();
		if (reinit) reinitMetaData();
	}

	public static AudioFile getTempAudio(String ext) {
		return new AudioFile(FileFresh.getTempFile(ext), false);
	}

	public static AudioFile getDestAudio(String path, String name) {
		return new AudioFile(FileFresh.getDestinationFile(path, name), false);
	}

	public void close() {
		file.close();
	}


	public void reinitMetaData() {
		AudioData adTmp= new AudioData(this.data);
		try {
			File aFile = file.getFile();
			org.jaudiotagger.audio.AudioFile af = AudioFileIO.read(aFile);
			AudioHeader header = af.getAudioHeader();

			data.audioType = AudioType.getByExt(this.file.getExtension());

			adTmp.channels = readChannels(aFile, header);
			adTmp.bitRate = (int)header.getBitRateAsNumber();
			adTmp.length = header.getTrackLength();
			adTmp.size = aFile.length();
			adTmp.sampleRate = SampleRate.getByRate(header.getSampleRateAsNumber(), SampleRate.SR_44100);
			adTmp.sampleSize = SampleSize.getBySize(header.getBitsPerSample(), SampleSize.BPS_16);
			adTmp.tag = af.getTag();

		} catch (Throwable t) {
			t.printStackTrace();
		}
		this.data = adTmp;
	}

	private synchronized final AudioData data() {

		if (this.data == null) {
				data = new AudioData();
				data.audioType = AudioType.getByExt(this.file.getExtension());
				data.channels = 2;
				data.bitRate = 0;
				data.length = 0;
				data.size = file.getFile().length();
				data.sampleRate =  SampleRate.SR_44100;
				data.sampleSize = SampleSize.BPS_16;
		}
		return this.data;
	}

	private int readChannels(File file, AudioHeader header) {

		int tmpChannels = UtilsCommon.asInt(header.getChannels());
		if ((tmpChannels < 1 || tmpChannels > 7 )) {

			if ("AAC".equalsIgnoreCase(header.getEncodingType())) {
				try {
					AACReader r = new AACReader(file, true);
					tmpChannels = r.getAudioChannels();
				} catch (Exception e) {
					if (-15==tmpChannels)
						tmpChannels = 1;
					else
						tmpChannels = 2;
				}

			} else
			if (header instanceof org.jaudiotagger.audio.mp3.MP3AudioHeader) {
				MP3AudioHeader mp3Header = (MP3AudioHeader) header;
				Mp3HeaderWrapper hw = new Mp3HeaderWrapper(mp3Header);
				tmpChannels = hw.getChannels();
			}
			else {
				tmpChannels = 2;
			}
		}
		return tmpChannels;
	}

	public SampleSize getSampleSize() {
		return data().sampleSize;
	}
	public void setSampleSize(SampleSize sampleSize) {
		data().sampleSize = sampleSize;
	}
	public SampleRate getSampleRate() {
		return data().sampleRate;
	}
	public void setSampleRate(SampleRate sampleRate) {
		data().sampleRate = sampleRate;
	}
	public AudioType getAudioType() {
		return data().audioType;
	}
	public void setAudioType(AudioType audioType) {
		data().audioType = audioType;
	}
	public int getBitRate() {
		return data().bitRate;
	}
	public void setBitRate(int bitRate) {
		data().bitRate = bitRate;
	}
	public int getChannels() {
		return data().channels;
	}
	public void setChannels(int channels) {
		data().channels = channels;
	}
	public int getLength() {
		return data().length;
	}
	public void setLength(int length) {
		data().length = length;
	}
	public long getSize() {
		return data().size;
	}
	public void setSize(long size) {
		data().size = size;
	}

	public IFile getFileData() {
		return file;
	}

	public void setFileData(IFile filePersistent) {
		this.file = filePersistent;
	}


	public static class AudioData implements Serializable {

		private static final long serialVersionUID = -7124614529038948112L;

		public SampleSize sampleSize;
		public SampleRate sampleRate;
		public AudioType audioType;
		public Tag tag;

		public int		bitRate;
		public int		channels;
		public int		length; //sec
		public long	size;

		public AudioData() {
		}

		public AudioData(AudioData data) {
			if (null == data) return ;
			this.sampleSize = data.sampleSize;
			this.sampleRate = data.sampleRate;
			this.audioType = data.audioType;
			this.bitRate = data.bitRate;
			this.channels = data.channels;
			this.length = data.length;
			this.size = data.size;
			this.tag = data.tag;
		}

		@Override
		public String toString() {
			return String
					.format("a-Data [SR: %s, Type: %s, BPS: %s, Chnls: %s]", sampleRate, audioType, bitRate, channels);
		}



	}


	public AudioData getData() {
		return data();
	}

	@Override
	public String toString() {
		return String.format("AudioFile [file=%s, data=%s]", file, data);
	}

}
