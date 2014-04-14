package org.delafer.recoder.factory;

import org.delafer.recoder.cli.base.ExecCmd;
import org.delafer.recoder.cli.compressor.NeroAac;
import org.delafer.recoder.cli.compressor.OggEncoder;
import org.delafer.recoder.cli.decompressor.AacDecoder;
import org.delafer.recoder.cli.decompressor.DecompressorWrapper;
import org.delafer.recoder.cli.decompressor.FFMpegDecoder;
import org.delafer.recoder.cli.decompressor.FlacDecoder;
import org.delafer.recoder.cli.decompressor.LameMp3Dec;
import org.delafer.recoder.cli.decompressor.MonkeyAudioDecoder;
import org.delafer.recoder.cli.decompressor.Mp4Decoder;
import org.delafer.recoder.cli.decompressor.MusepackDecoder;
import org.delafer.recoder.cli.decompressor.OggDecoder;
import org.delafer.recoder.cli.decompressor.OptimFrogDecoder;
import org.delafer.recoder.cli.decompressor.OpusDecoder;
import org.delafer.recoder.cli.decompressor.SpeexDecoder;
import org.delafer.recoder.cli.decompressor.TTADecoder;
import org.delafer.recoder.cli.decompressor.WmaDecoder;
import org.delafer.recoder.cli.decompressor.WvDecoder;
import org.delafer.recoder.model.AudioFile;
import org.delafer.recoder.model.enums.AudioType;

public class FactoryCLI {


	public static ExecCmd getDecompressor(AudioFile au) {
		AudioType at = au.getAudioType();
		switch (at) {
		case mp3:
//			return new MpegMpg123();
			return new LameMp3Dec();
		case wma:
			return new WmaDecoder();
		case wav:
			return new DecompressorWrapper();
		case m4a:
		case m4b:
			return new Mp4Decoder();
		case aac:
			return new AacDecoder();
		case ogg:
			return new OggDecoder();
		case flac:
			return new FlacDecoder();
		case ape:
			return new MonkeyAudioDecoder();
		case ofr:
			return new OptimFrogDecoder();
		case shn:
			return new FFMpegDecoder();
		case wv:
			return new WvDecoder();
		case mpc:
			return new MusepackDecoder();
		case opus:
			return new OpusDecoder();
		case spx:
			return new SpeexDecoder();
		case tta:
			return new TTADecoder();
		case ra:
		case rm:
			return new FFMpegDecoder();
		default:
			return null;
		}
	}


	public static ExecCmd getCompressor(AudioFile au) {
		switch (au.getAudioType()) {
		default:
			return new NeroAac();
			//return new OggEncoder();
		}
	}


}
