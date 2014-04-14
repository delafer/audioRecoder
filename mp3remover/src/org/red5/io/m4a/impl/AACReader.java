/*
 * RED5 Open Source Flash Server - http://code.google.com/p/red5/
 *
 * Copyright 2006-2013 by respective authors (see below). All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.red5.io.m4a.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.mina.core.buffer.IoBuffer;
import org.red5.io.IStreamableFile;
import org.red5.io.ITag;
import org.red5.io.ITagReader;
import org.red5.io.IoConstants;
import org.red5.io.amf.Output;
import org.red5.io.flv.impl.Tag;
import org.red5.io.mp4.MP4Frame;
import org.red5.io.mp4.impl.MP4Reader;
import org.red5.io.utils.HexDump;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.coremedia.iso.IsoFile;
import com.coremedia.iso.boxes.AbstractMediaHeaderBox;
import com.coremedia.iso.boxes.ChunkOffset64BitBox;
import com.coremedia.iso.boxes.ChunkOffsetBox;
import com.coremedia.iso.boxes.HandlerBox;
import com.coremedia.iso.boxes.MediaBox;
import com.coremedia.iso.boxes.MediaHeaderBox;
import com.coremedia.iso.boxes.MediaInformationBox;
import com.coremedia.iso.boxes.MovieBox;
import com.coremedia.iso.boxes.MovieHeaderBox;
import com.coremedia.iso.boxes.SampleDependencyTypeBox;
import com.coremedia.iso.boxes.SampleDescriptionBox;
import com.coremedia.iso.boxes.SampleSizeBox;
import com.coremedia.iso.boxes.SampleTableBox;
import com.coremedia.iso.boxes.SampleToChunkBox;
import com.coremedia.iso.boxes.SampleToChunkBox.Entry;
import com.coremedia.iso.boxes.SoundMediaHeaderBox;
import com.coremedia.iso.boxes.TimeToSampleBox;
import com.coremedia.iso.boxes.TrackBox;
import com.coremedia.iso.boxes.TrackHeaderBox;
import com.coremedia.iso.boxes.apple.AppleWaveBox;
import com.coremedia.iso.boxes.mdat.MediaDataBox;
import com.coremedia.iso.boxes.sampleentry.AudioSampleEntry;
import com.coremedia.iso.boxes.sampleentry.SampleEntry;
import com.googlecode.mp4parser.boxes.mp4.ESDescriptorBox;
import com.googlecode.mp4parser.boxes.mp4.objectdescriptors.AudioSpecificConfig;
import com.googlecode.mp4parser.boxes.mp4.objectdescriptors.DecoderConfigDescriptor;
import com.googlecode.mp4parser.boxes.mp4.objectdescriptors.DecoderSpecificInfo;
import com.googlecode.mp4parser.boxes.mp4.objectdescriptors.ESDescriptor;

/**
 * A Reader is used to read the contents of a M4A file.
 * NOTE: This class is not implemented as threading-safe. The caller
 * should make sure the threading-safety.
 *
 * @author The Red5 Project
 * @author Paul Gregoire, (mondain@gmail.com)
 */
public class AACReader implements IoConstants, ITagReader {

	/**
	 * Logger
	 */
	private static Logger log = LoggerFactory.getLogger(M4AReader.class);

	/**
	 * Input stream
	 */
	private FileInputStream fis;

	/**
	 * File channel
	 */
	private FileChannel channel;

	/**
	 * Provider of boxes
	 */
	private IsoFile isoFile;

	private String audioCodecId = "mp4a";

	//decoder bytes / configs
	private byte[] audioDecoderBytes;

	/** Duration in milliseconds. */
	private long duration;

	private long timeScale;

	//audio sample rate kHz
	private double audioTimeScale;

	private int audioChannels;

	//default to aac lc
	private int audioCodecType = 1;

	private String formattedDuration;

	private long mdatOffset;

	//samples to chunk mappings
	private List<Entry> audioSamplesToChunks;

	//samples
	private long[] audioSamples;

	private long audioSampleSize;

	//chunk offsets
	private long[] audioChunkOffsets;

	//sample duration
	private long audioSampleDuration = 1024;

	//keep track of current sample
	private int currentFrame = 1;

	private int prevFrameSize = 0;

	private List<MP4Frame> frames = new ArrayList<MP4Frame>();

	/**
	 * Container for metadata and any other tags that should
	 * be sent prior to media data.
	 */
	private LinkedList<ITag> firstTags = new LinkedList<ITag>();

	private final Semaphore lock = new Semaphore(1, true);

	/** Constructs a new M4AReader. */
	AACReader() {
	}

	/**
	 * Creates M4A reader from file input stream, sets up metadata generation flag.
	 *
	 * @param f                    File input stream
	 */
	public AACReader(File f, boolean onlyHeader) throws IOException {
		if (null == f) {
			log.warn("Reader was passed a null file");
			log.debug("{}", ToStringBuilder.reflectionToString(this));
		}
		this.fis = new FileInputStream(f);
		// create a channel
		channel = fis.getChannel();
		// instance an iso file from mp4parser
		isoFile = new IsoFile(channel);
		//decode all the info that we want from the atoms
		decodeHeader();
		if (onlyHeader) return ;
		//analyze the samples/chunks and build the keyframe meta data
		analyzeFrames();
		//add meta data
		firstTags.add(createFileMeta());
		//create / add the pre-streaming (decoder config) tags
		createPreStreamingTags();
	}

	/**
	 * This handles the moov atom being at the beginning or end of the file, so the mdat may also
	 * be before or after the moov atom.
	 */
	public void decodeHeader() {
		try {
			// we want a moov and an mdat, anything else will throw the invalid file type error
			MovieBox moov = isoFile.getBoxes(MovieBox.class).get(0);
			if (log.isDebugEnabled()) {
				log.debug("moov children: {}", moov.getBoxes().size());
				MP4Reader.dumpBox(moov);
			}
			// get the movie header
			MovieHeaderBox mvhd = moov.getMovieHeaderBox();
			// get the timescale and duration
			timeScale = mvhd.getTimescale();
			duration = mvhd.getDuration();
			log.debug("Time scale {} Duration {}", timeScale, duration);
			double lengthInSeconds = (double) duration / timeScale;
			log.debug("Seconds {}", lengthInSeconds);
			// look at the tracks
			log.debug("Tracks: {}", moov.getTrackCount());
			List<TrackBox> tracks = moov.getBoxes(TrackBox.class); // trak
			for (TrackBox trak : tracks) {
				if (log.isDebugEnabled()) {
					log.debug("trak children: {}", trak.getBoxes().size());
					MP4Reader.dumpBox(trak);
				}
				TrackHeaderBox tkhd = trak.getTrackHeaderBox(); // tkhd
				log.debug("Track id: {}", tkhd.getTrackId());
				MediaBox mdia = trak.getMediaBox(); // mdia
				long scale = 0;
				if (mdia != null) {
					if (log.isDebugEnabled()) {
						log.debug("mdia children: {}", mdia.getBoxes().size());
						MP4Reader.dumpBox(mdia);
					}
					MediaHeaderBox mdhd = mdia.getMediaHeaderBox(); // mdhd
					if (mdhd != null) {
						log.debug("Media data header atom found");
						// this will be for either video or audio depending media info
						scale = mdhd.getTimescale();
						log.debug("Time scale {}", scale);
					}
					HandlerBox hdlr = mdia.getHandlerBox(); // hdlr
					if (hdlr != null) {
						String hdlrType = hdlr.getHandlerType();
						if ("soun".equals(hdlrType)) {
							if (scale > 0) {
								audioTimeScale = scale * 1.0;
								log.debug("Audio time scale: {}", audioTimeScale);
							}
						} else {
							log.debug("Unhandled handler type: {}", hdlrType);
						}
					}
					MediaInformationBox minf = mdia.getMediaInformationBox();
					if (minf != null) {
						if (log.isDebugEnabled()) {
							log.debug("minf children: {}", minf.getBoxes().size());
							MP4Reader.dumpBox(minf);
						}
						AbstractMediaHeaderBox abs = minf.getMediaHeaderBox();
						if (abs instanceof SoundMediaHeaderBox) { // smhd
							//SoundMediaHeaderBox smhd = (SoundMediaHeaderBox) abs;
							log.debug("Sound header atom found");
						} else {
							log.debug("Unhandled media header box: {}", abs.getType());
						}
					}
				}
				SampleTableBox stbl = trak.getSampleTableBox(); // mdia/minf/stbl
				if (stbl != null) {
					if (log.isDebugEnabled()) {
						log.debug("stbl children: {}", stbl.getBoxes().size());
						MP4Reader.dumpBox(stbl);
					}
					SampleDescriptionBox stsd = stbl.getSampleDescriptionBox(); // stsd
					if (stsd != null) {
						//stsd: mp4a, avc1, mp4v
						//String type = stsd.getType();
						if (log.isDebugEnabled()) {
							log.debug("stsd children: {}", stsd.getBoxes().size());
							MP4Reader.dumpBox(stsd);
						}
						SampleEntry entry = stsd.getSampleEntry();
						log.debug("Sample entry type: {}", entry.getType());
						// determine if audio or video and process from there
						if (entry instanceof AudioSampleEntry) {
							processAudioBox(stbl, (AudioSampleEntry) entry, scale);
						}
					}
				}
			}
			//real duration
			StringBuilder sb = new StringBuilder();
			double videoTime = ((double) duration / (double) timeScale);
			log.debug("Video time: {}", videoTime);
			int minutes = (int) (videoTime / 60);
			if (minutes > 0) {
				sb.append(minutes);
				sb.append('.');
			}
			//formatter for seconds / millis
			NumberFormat df = DecimalFormat.getInstance();
			df.setMaximumFractionDigits(2);
			sb.append(df.format((videoTime % 60)));
			formattedDuration = sb.toString();
			log.debug("Time: {}", formattedDuration);

			List<MediaDataBox> mdats = isoFile.getBoxes(MediaDataBox.class);
			if (mdats != null && !mdats.isEmpty()) {
				log.debug("mdat count: {}", mdats.size());
				MediaDataBox mdat = mdats.get(0);
				if (mdat != null) {
					mdatOffset = mdat.getDataStartPosition();
				}
			}
			log.debug("Offset - mdat: {}", mdatOffset);
		} catch (Exception e) {
			log.error("Exception decoding header / atoms", e);
		}
	}

	/**
	 * Process the audio information contained in the atoms.
	 *
	 * @param stbl
	 * @param ase AudioSampleEntry
	 * @param scale timescale
	 */
	private void processAudioBox(SampleTableBox stbl, AudioSampleEntry ase, long scale) {
		// get codec
		String codecName = ase.getType();
		// set the audio codec here - may be mp4a or...
		setAudioCodecId(codecName);
		log.debug("Sample size: {}", ase.getSampleSize());
		long ats = ase.getSampleRate();
		// skip invalid audio time scale
		if (ats > 0) {
			audioTimeScale = ats * 1.0;
		}
		log.debug("Sample rate (audio time scale): {}", audioTimeScale);
		audioChannels = ase.getChannelCount();
		log.debug("Channels: {}", audioChannels);
		if (ase.getBoxes(ESDescriptorBox.class).size() > 0) {
			// look for esds
			ESDescriptorBox esds = ase.getBoxes(ESDescriptorBox.class).get(0);
			if (esds == null) {
				log.debug("esds not found in default path");
				// check for decompression param atom
				AppleWaveBox wave = ase.getBoxes(AppleWaveBox.class).get(0);
				if (wave != null) {
					log.debug("wave atom found");
					// wave/esds
					esds = wave.getBoxes(ESDescriptorBox.class).get(0);
					if (esds == null) {
						log.debug("esds not found in wave");
						// mp4a/esds
						//AC3SpecificBox mp4a = wave.getBoxes(AC3SpecificBox.class).get(0);
						//esds = mp4a.getBoxes(ESDescriptorBox.class).get(0);
					}
				}
			}
			//mp4a: esds
			if (esds != null) {
				// http://stackoverflow.com/questions/3987850/mp4-atom-how-to-discriminate-the-audio-codec-is-it-aac-or-mp3
				ESDescriptor descriptor = esds.getEsDescriptor();
				if (descriptor != null) {
					DecoderConfigDescriptor configDescriptor = descriptor.getDecoderConfigDescriptor();
					AudioSpecificConfig audioInfo = configDescriptor.getAudioSpecificInfo();
					if (audioInfo != null) {
						audioDecoderBytes = audioInfo.getConfigBytes();
						/* the first 5 (0-4) bits tell us about the coder used for aacaot/aottype
						 * http://wiki.multimedia.cx/index.php?title=MPEG-4_Audio
						 0 - NULL
						 1 - AAC Main (a deprecated AAC profile from MPEG-2)
						 2 - AAC LC or backwards compatible HE-AAC
						 3 - AAC Scalable Sample Rate
						 4 - AAC LTP (a replacement for AAC Main, rarely used)
						 5 - HE-AAC explicitly signaled (Non-backward compatible)
						23 - Low Delay AAC
						29 - HE-AACv2 explicitly signaled
						32 - MP3on4 Layer 1
						33 - MP3on4 Layer 2
						34 - MP3on4 Layer 3
						*/
						byte audioCoderType = audioDecoderBytes[0];
						//match first byte
						switch (audioCoderType) {
							case 0x02:
								log.debug("Audio type AAC LC");
							case 0x11: //ER (Error Resilient) AAC LC
								log.debug("Audio type ER AAC LC");
							default:
								audioCodecType = 1; //AAC LC
								break;
							case 0x01:
								log.debug("Audio type AAC Main");
								audioCodecType = 0; //AAC Main
								break;
							case 0x03:
								log.debug("Audio type AAC SBR");
								audioCodecType = 2; //AAC LC SBR
								break;
							case 0x05:
							case 0x1d:
								log.debug("Audio type AAC HE");
								audioCodecType = 3; //AAC HE
								break;
							case 0x20:
							case 0x21:
							case 0x22:
								log.debug("Audio type MP3");
								audioCodecType = 33; //MP3
								audioCodecId = "mp3";
								break;
						}
						log.debug("Audio coder type: {} {} id: {}", new Object[] { audioCoderType, Integer.toBinaryString(audioCoderType), audioCodecId });
					} else {
						log.debug("Audio specific config was not found");
						DecoderSpecificInfo info = configDescriptor.getDecoderSpecificInfo();
						if (info != null) {
							log.debug("Decoder info found: {}", info.getTag());
							// qcelp == 5
						}
					}
				} else {
					log.debug("No ES descriptor found");
				}
			}
		} else {
			log.debug("Audio sample entry had no descriptor");
		}
		//stsc - has Records
		SampleToChunkBox stsc = stbl.getSampleToChunkBox(); // stsc
		if (stsc != null) {
			log.debug("Sample to chunk atom found");
			audioSamplesToChunks = stsc.getEntries();
			log.debug("Audio samples to chunks: {}", audioSamplesToChunks.size());
			// handle instance where there are no actual records (bad f4v?)
		}
		//stsz - has Samples
		SampleSizeBox stsz = stbl.getSampleSizeBox(); // stsz
		if (stsz != null) {
			log.debug("Sample size atom found");
			audioSamples = stsz.getSampleSizes();
			log.debug("Samples: {}", audioSamples.length);
			// if sample size is 0 then the table must be checked due to variable sample sizes
			audioSampleSize = stsz.getSampleSize();
			log.debug("Sample size: {}", audioSampleSize);
			long audioSampleCount = stsz.getSampleCount();
			log.debug("Sample count: {}", audioSampleCount);
		}
		//stco - has Chunks
		ChunkOffsetBox stco = stbl.getChunkOffsetBox(); // stco / co64
		if (stco != null) {
			log.debug("Chunk offset atom found");
			audioChunkOffsets = stco.getChunkOffsets();
			log.debug("Chunk count: {}", audioChunkOffsets.length);
		} else {
			//co64 - has Chunks
			ChunkOffset64BitBox co64 = stbl.getBoxes(ChunkOffset64BitBox.class).get(0);
			if (co64 != null) {
				log.debug("Chunk offset (64) atom found");
				audioChunkOffsets = co64.getChunkOffsets();
				log.debug("Chunk count: {}", audioChunkOffsets.length);
			}
		}
		//stts - has TimeSampleRecords
		TimeToSampleBox stts = stbl.getTimeToSampleBox(); // stts
		if (stts != null) {
			log.debug("Time to sample atom found");
			List<TimeToSampleBox.Entry> records = stts.getEntries();
			log.debug("Audio time to samples: {}", records.size());
			// handle instance where there are no actual records (bad f4v?)
			if (records.size() > 0) {
				TimeToSampleBox.Entry rec = records.get(0);
				log.debug("Samples = {} delta = {}", rec.getCount(), rec.getDelta());
				//if we have 1 record it means all samples have the same duration
				audioSampleDuration = rec.getDelta();
			}
		}
		// sdtp - sample dependency type
		SampleDependencyTypeBox sdtp = stbl.getSampleDependencyTypeBox(); // sdtp
		if (sdtp != null) {
			log.debug("Independent and disposable samples atom found");
			List<SampleDependencyTypeBox.Entry> recs = sdtp.getEntries();
			for (SampleDependencyTypeBox.Entry rec : recs) {
				log.debug("{}", rec);
			}
		}
	}

	public long getTotalBytes() {
		try {
			return channel.size();
		} catch (Exception e) {
			log.error("Error getTotalBytes", e);
			return 0;
		}
	}

	/**
	 * Get the current position in a file or IoBuffer.
	 *
	 * @return           Current position in a file
	 */
	private long getCurrentPosition() {
		try {
			//if we are at the end of the file drop back to mdat offset
			if (channel.position() == channel.size()) {
				log.debug("Reached end of file, going back to data offset");
				channel.position(mdatOffset);
			}
			return channel.position();
		} catch (Exception e) {
			log.error("Error getCurrentPosition", e);
			return 0;
		}
	}

	/** {@inheritDoc} */
	public boolean hasVideo() {
		return false;
	}

	/**
	 * Returns the file buffer.
	 *
	 * @return  File contents as byte buffer
	 */
	public IoBuffer getFileData() {
		return null;
	}

	/** {@inheritDoc}
	 */
	public IStreamableFile getFile() {
		// TODO wondering if we need to have a reference
		return null;
	}

	/** {@inheritDoc}
	 */
	public int getOffset() {
		// XXX what's the difference from getBytesRead
		return 0;
	}

	/** {@inheritDoc}
	 */
	public long getBytesRead() {
		return getCurrentPosition();
	}

	/** {@inheritDoc} */
	public long getDuration() {
		return duration;
	}

	public String getAudioCodecId() {
		return audioCodecId;
	}

	/** {@inheritDoc}
	 */
	public boolean hasMoreTags() {
		return currentFrame < frames.size();
	}

	/**
	 * Create tag for metadata event.
	 *
	 * @return         Metadata event tag
	 */
	ITag createFileMeta() {
		log.debug("Creating onMetaData");
		// Create tag for onMetaData event
		IoBuffer buf = IoBuffer.allocate(1024);
		buf.setAutoExpand(true);
		Output out = new Output(buf);
		out.writeString("onMetaData");
		Map<Object, Object> props = new HashMap<Object, Object>();
		// Duration property
		props.put("duration", ((double) duration / (double) timeScale));

		// Audio codec id - watch for mp3 instead of aac
		props.put("audiocodecid", audioCodecId);
		props.put("aacaot", audioCodecType);
		props.put("audiosamplerate", audioTimeScale);
		props.put("audiochannels", audioChannels);
		props.put("canSeekToEnd", false);
		out.writeMap(props);
		buf.flip();

		//now that all the meta properties are done, update the duration
		duration = Math.round(duration * 1000d);

		ITag result = new Tag(IoConstants.TYPE_METADATA, 0, buf.limit(), null, 0);
		result.setBody(buf);
		return result;
	}

	/**
	 * Tag sequence
	 * MetaData, Audio config, remaining audio
	 *
	 * Packet prefixes:
	 * af 00 ...   06 = Audio extra data (first audio packet)
	 * af 01          = Audio frame
	 *
	 * Audio extra data(s):
	 * af 00                = Prefix
	 * 11 90 4f 14          = AAC Main   = aottype 0
	 * 12 10                = AAC LC     = aottype 1
	 * 13 90 56 e5 a5 48 00 = HE-AAC SBR = aottype 2
	 * 06                   = Suffix
	 *
	 * Still not absolutely certain about this order or the bytes - need to verify later
	 */
	private void createPreStreamingTags() {
		log.debug("Creating pre-streaming tags");
		if (audioDecoderBytes != null) {
			IoBuffer body = IoBuffer.allocate(audioDecoderBytes.length + 3);
			body.put(new byte[] { (byte) 0xaf, (byte) 0 }); //prefix
			if (log.isDebugEnabled()) {
				log.debug("Audio decoder bytes: {}", HexDump.byteArrayToHexString(audioDecoderBytes));
			}
			body.put(audioDecoderBytes);
			body.put((byte) 0x06); //suffix
			ITag tag = new Tag(IoConstants.TYPE_AUDIO, 0, body.position(), null, prevFrameSize);
			body.flip();
			tag.setBody(body);
			//add tag
			firstTags.add(tag);
		} else {
			//default to aac-lc when the esds doesnt contain descripter bytes
			log.warn("Audio decoder bytes were not available");
		}
	}

	/**
	 * Packages media data for return to providers.
	 *
	 */
	public ITag readTag() {
		//log.debug("Read tag");
		ITag tag = null;
		try {
			lock.acquire();
			//empty-out the pre-streaming tags first
			if (!firstTags.isEmpty()) {
				log.debug("Returning pre-tag");
				// Return first tags before media data
				return firstTags.removeFirst();
			}
			//log.debug("Read tag - sample {} prevFrameSize {} audio: {} video: {}", new Object[]{currentSample, prevFrameSize, audioCount, videoCount});

			//get the current frame
			MP4Frame frame = frames.get(currentFrame);
			log.debug("Playback {}", frame);

			int sampleSize = frame.getSize();

			int time = (int) Math.round(frame.getTime() * 1000.0);
			//log.debug("Read tag - dst: {} base: {} time: {}", new Object[]{frameTs, baseTs, time});

			long samplePos = frame.getOffset();
			//log.debug("Read tag - samplePos {}", samplePos);

			//determine frame type and packet body padding
			byte type = frame.getType();

			//create a byte buffer of the size of the sample
			ByteBuffer data = ByteBuffer.allocate(sampleSize + 2);
			try {
				//log.debug("Writing audio prefix");
				data.put(MP4Reader.PREFIX_AUDIO_FRAME);
				//do we need to add the mdat offset to the sample position?
				channel.position(samplePos);
				channel.read(data);
			} catch (IOException e) {
				log.error("Error on channel position / read", e);
			}

			//chunk the data
			IoBuffer payload = IoBuffer.wrap(data.array());

			//create the tag
			tag = new Tag(type, time, payload.limit(), payload, prevFrameSize);
			//log.debug("Read tag - type: {} body size: {}", (type == TYPE_AUDIO ? "Audio" : "Video"), tag.getBodySize());

			//increment the sample number
			currentFrame++;
			//set the frame / tag size
			prevFrameSize = tag.getBodySize();
		} catch (InterruptedException e) {
			log.warn("Exception acquiring lock", e);
		} finally {
			lock.release();
		}
		//log.debug("Tag: {}", tag);
		return tag;
	}

	/**
	 * Performs frame analysis and generates metadata for use in seeking. All the frames
	 * are analyzed and sorted together based on time and offset.
	 */
	public void analyzeFrames() {
		log.debug("Analyzing frames");
		if (audioSamplesToChunks != null) {
			// tag == sample
			int sample = 1;
			// position
			Long pos = null;
			//add the audio frames / samples / chunks
			for (int i = 0; i < audioSamplesToChunks.size(); i++) {
				Entry record = audioSamplesToChunks.get(i);
				long firstChunk = record.getFirstChunk();
				long lastChunk = audioChunkOffsets.length;
				if (i < audioSamplesToChunks.size() - 1) {
					Entry nextRecord = audioSamplesToChunks.get(i + 1);
					lastChunk = nextRecord.getFirstChunk() - 1;
				}
				for (long chunk = firstChunk; chunk <= lastChunk; chunk++) {
					long sampleCount = record.getSamplesPerChunk();
					pos = audioChunkOffsets[(int) (chunk - 1)];
					while (sampleCount > 0) {
						//calculate ts
						double ts = (audioSampleDuration * (sample - 1)) / audioTimeScale;
						// sample size
						int size = 0;
						// if we have no samples, skip size check as its probably not aac
						if (audioSamples.length > 0) {
							//update sample size
							size = (int) audioSamples[sample - 1];
							// skip empty AAC data which is 6 bytes long
							log.trace("Audio sample - size: {} pos: {}", size, pos);
							if (size == 6) {
								try {
									// get current pos
									long position = channel.position();
									// jump to data position
									channel.position(pos);
									// create buffer to store bytes so we can check them
									ByteBuffer dst = ByteBuffer.allocate(6);
									// read the data
									channel.read(dst);
									// flip it
									dst.flip();
									// reset the position
									channel.position(position);
									byte[] tmp = dst.array();
									log.trace("Audio bytes: {} equal: {}", HexDump.byteArrayToHexString(tmp), Arrays.equals(MP4Reader.EMPTY_AAC, tmp));
									if (Arrays.equals(MP4Reader.EMPTY_AAC, tmp)) {
										log.trace("Skipping empty AAC data frame");
										// update counts
										pos += size;
										sampleCount--;
										sample++;
										// read next
										continue;
									}
								} catch (IOException e) {
									log.warn("Exception during audio analysis", e);
								}
							}
						}
						// set audio sample size
						size = (int) (size != 0 ? size : audioSampleSize);
						// exclude data that is not within the mdat box
						if (pos >= mdatOffset) {
							//create a frame
							MP4Frame frame = new MP4Frame();
							frame.setOffset(pos);
							frame.setSize(size);
							frame.setTime(ts);
							frame.setType(TYPE_AUDIO);
							frames.add(frame);
							//log.debug("Sample #{} {}", sample, frame);
						} else {
							log.warn("Skipping audio frame with invalid position");
						}
						// update counts
						pos += size;
						sampleCount--;
						sample++;
					}
				}
			}
		}
		//sort the frames
		Collections.sort(frames);
		log.debug("Frames count: {}", frames.size());
		//log.debug("Frames: {}", frames);
		//release some memory
		if (audioSamplesToChunks != null) {
			audioChunkOffsets = null;
			audioSamplesToChunks.clear();
			audioSamplesToChunks = null;
		}
	}

	/**
	 * Put the current position to pos. The caller must ensure the pos is a valid one.
	 *
	 * @param pos position to move to in file / channel
	 */
	public void position(long pos) {
		log.debug("position: {}", pos);
		currentFrame = getFrame(pos);
		log.debug("Setting current sample: {}", currentFrame);
	}

	/**
	 * Search through the frames by offset / position to find the sample.
	 *
	 * @param pos
	 * @return
	 */
	private int getFrame(long pos) {
		int sample = 1;
		int len = frames.size();
		MP4Frame frame = null;
		for (int f = 0; f < len; f++) {
			frame = frames.get(f);
			if (pos == frame.getOffset()) {
				sample = f;
				break;
			}
		}
		return sample;
	}

	/** {@inheritDoc}
	 */
	public void close() {
		log.debug("Close");
		if (channel != null) {
			try {
				channel.close();
				fis.close();
				fis = null;
			} catch (IOException e) {
				log.error("Channel close {}", e);
			} finally {
				if (frames != null) {
					frames.clear();
					frames = null;
				}
			}
		}
	}

	public void setAudioCodecId(String audioCodecId) {
		this.audioCodecId = audioCodecId;
	}

	public ITag readTagHeader() {
		return null;
	}

	public int getAudioChannels() {
		return audioChannels;
	}

}
