package org.delafer.recoder.tasks;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import org.delafer.recoder.cli.base.ExecCmd;
import org.delafer.recoder.cli.dsp.GainAnalyzer;
import org.delafer.recoder.cli.dsp.SoxProcessor;
import org.delafer.recoder.cli.dsp.SoxProcessor2;
import org.delafer.recoder.factory.FactoryCLI;
import org.delafer.recoder.model.AudioFile;
import org.delafer.recoder.model.IFile;
import org.delafer.recoder.model.ProgressModel;
import org.delafer.recoder.model.ProgressModel.State;
import org.delafer.recoder.model.ProgressModel.TaskType;
import org.delafer.recoder.model.enums.AudioType;

public class BatchTask {

	private static final int NORMAL_FILE_SIZE = 2*1024*1024;
	private AudioFile source;
	private transient int threadNr;

	private final static boolean  MODE_SAFE = false;

	public BatchTask(AudioFile source) {
		this.source = source;
	}


	public AudioFile getSourceAudio() {

		if (null != source) {
			ProgressModel.addSizeOriginal(source.getFileData().size());
			ProgressModel.setTaskName(threadNr, source.getFileData().getFullName());
		}

		return source;
	}

	private boolean checkResult(AudioFile af) {
		return af != null
		   && af.getFileData() != null
		   //&& af.getFileData().size() > 127l
		   && af.getFileData().exists();
	}

	private State getState(AudioFile ret) {
		return checkResult(ret) ? ProgressModel.State.Success :  ProgressModel.State.Fail;
	}

	private int runTask(ExecCmd cmd, TaskType type) {
		ProgressModel.setTaskState(threadNr, type, ProgressModel.State.InProgress);
		return cmd.run();
	}


	public AudioFile getUncompressedAudio() {

		AudioFile ret = getUncompressedAudioIntr();

		ProgressModel.setTaskState(threadNr, ProgressModel.TaskType.Decoding, getState(ret));

		return ret;
	}


	public AudioFile getUncompressedAudioIntr() {

		AudioFile source = getSourceAudio();
		if (null == source) return null;

		AudioType aType = source.getAudioType();

		if (AudioType.wav.equals(aType)) return source;

		ExecCmd cmd = FactoryCLI.getDecompressor(source);

		if (null == cmd) {
			System.out.println("Can't find decompressor for: "+source.getFileData().getBaseName());
			return null;
		}

		AudioFile tmp = AudioFile.getTempAudio("wav");
		cmd.setInputFile(source);
		cmd.setOutputFile(tmp);

		int exitCode = runTask(cmd, ProgressModel.TaskType.Decoding);
		if (exitCode != 0) return null;

		sleep();
		return tmp;

	}

	public AudioFile getDSPAudio() {

		AudioFile ret = getDSPAudioIntr();

		ProgressModel.setTaskState(threadNr, ProgressModel.TaskType.Processing, getState(ret));

		return ret;
	}

	/*
	public AudioFile getDSPAudioIntr222() {

		AudioFile source = getUncompressedAudio();
		if (null == source) return null;

		source.reinitMetaData();


		SoxProcessor2 cmd = new SoxProcessor2();

		AudioFile destination =  AudioFile.getTempAudio("wav");

		cmd.setInputFile(source);
		cmd.setOutputFile(destination);

		int exitCode= runTask(cmd, ProgressModel.TaskType.Processing);
		if (exitCode != 0) return null;

		sleep();
		return destination;

	}*/

	public AudioFile getDSPAudioIntr() {

		AudioFile source = getUncompressedAudio();
		if (null == source) return null;

		source.reinitMetaData();

		GainAnalyzer volumeAvg = new GainAnalyzer();
		volumeAvg.setInputFile(source);
		volumeAvg.run();


		SoxProcessor cmd = new SoxProcessor();
		cmd.setGainCorrection(volumeAvg.correctionLevel());
		cmd.setOffset(volumeAvg.getOffset());

		AudioFile destination =  AudioFile.getTempAudio("wav");

		cmd.setInputFile(source);
		cmd.setOutputFile(destination);

		int exitCode= runTask(cmd, ProgressModel.TaskType.Processing);
		if (exitCode != 0) return null;

		sleep();
		return destination;

	}

	public AudioFile getDestinationAudio() {

		AudioFile ret = getDestinationAudioIntr();

		ProgressModel.setTaskState(threadNr, ProgressModel.TaskType.Encoding, getState(ret));
		ProgressModel.addSizeRepacked(source.getFileData().size());

		return ret;
	}

	public AudioFile getDestinationAudioIntr() {

		AudioFile srcAudioFile = getSourceAudio();

		IFile scrInf = srcAudioFile.getFileData();


		AudioFile input = getDSPAudio();
//		AudioFile input = getUncompressedAudio();
		if (null == input) return null;


		input.reinitMetaData();

		ExecCmd cmd = FactoryCLI.getCompressor(input);
		cmd.setInputFile(input);
		cmd.setSource(srcAudioFile);

		AudioFile output = AudioFile.getDestAudio(scrInf.getPath(), scrInf.getBaseName() + '.' + cmd.getDestExtension());

		cmd.setOutputFile(output);


		int exitCode = runTask(cmd, ProgressModel.TaskType.Encoding);
		if (exitCode != 0) return null;

		sleep();
		return output;
	}


	public void doConversation() {

		this.threadNr = ProgressModel.getContextStore().get().getThreadNr();
		for (ProgressModel.TaskType next : ProgressModel.TaskType.values()) {
			ProgressModel.setTaskState(threadNr, next, ProgressModel.State.New);
		}

		AudioFile res = getDestinationAudio();

		checkIsTaskSuccess(this.source, res);

		if (null == res) {
			System.out.println("Conversation failed: "+getSourceAudio());
			return ;
		}
		System.err.println("Converted: "+res.getFileData().getFullNameWithPath());
	}

	private void checkIsTaskSuccess(AudioFile source, AudioFile destination) {
		ProgressModel.setTaskState(threadNr, TaskType.Checking, ProgressModel.State.InProgress);

		try {
			if (destination == null || destination.getFileData() == null || !destination.getFileData().exists())
				throw new IOException("Error recoding file");

			long originalSize = source.getFileData().size();
			long newSize = destination.getFileData().size();

			if ((newSize > originalSize) || (newSize < 3500 && newSize * 5 < originalSize) || ((newSize * 11 < originalSize) && newSize < NORMAL_FILE_SIZE) /*|| (newSize * 50 < originalSize)*/) {
				deleteFile(destination);
				throw new IOException("Error recoding file");
			}


			deleteFile(source);

			ProgressModel.setTaskState(threadNr, TaskType.Checking, ProgressModel.State.Success);

		} catch (Throwable e) {
			ProgressModel.setTaskState(threadNr, TaskType.Checking, ProgressModel.State.Fail);
		}


	}

	private void deleteFile(AudioFile auf) {
		if (auf == null || auf.getFileData() == null || auf.getFileData().getFile() == null) return ;
		File aFile = auf.getFileData().getFile();
		if (!aFile.exists() || !aFile.isFile()) return ;
		if (MODE_SAFE) {
			try {
				aFile.renameTo(new File(aFile.toURI().toURL().getFile()+".bak"));
			} catch (MalformedURLException e) {
				aFile.renameTo(new File(auf.getFileData().getFullNameWithPath()+".bak"));
			}
		} else {
			if (aFile.canWrite())
				aFile.delete();
			else
				aFile.deleteOnExit();
		}
	}

	private final void sleep() {
        try {
        	for (int i = 4; i >= 0; i--) {
        		Thread.yield();
        		Thread.sleep(20);
			}
		} catch (InterruptedException e) {}
	}

}
