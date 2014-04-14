package org.jaudiotagger.audio.mp3;

public class Mp3HeaderWrapper {

	org.jaudiotagger.audio.mp3.MP3AudioHeader header;
	int channelMode;

	public Mp3HeaderWrapper(MP3AudioHeader header) {
		this.header = header;
		channelMode = header.mp3FrameHeader != null ? header.mp3FrameHeader.getChannelMode() : -1;
	}


	public int getChannels() {
		switch (channelMode) {
		case MPEGFrameHeader.MODE_DUAL_CHANNEL:
		case MPEGFrameHeader.MODE_JOINT_STEREO:
		case MPEGFrameHeader.MODE_STEREO:
			return 2;
		case MPEGFrameHeader.MODE_MONO:
			return 1;
		default:
			return 1;
		}
	}


}
