package org.delafer.recoder.tasks;

import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Set;

import org.delafer.recoder.config.Config;
import org.delafer.recoder.helpers.AbstractFileProcessor;
import org.delafer.recoder.helpers.Args;
import org.delafer.recoder.model.AudioFile;
import org.delafer.recoder.model.FilePersistent;
import org.delafer.recoder.model.ProgressModel;

public abstract class TasksQueueCollector {

	private static final int MIN_ALLOWED_BITRATE = 48;
//	private static final int MIN_ALLOWED_BITRATE = 0;
	private static final int PROGRESS_EVERY = 6;
	private LinkedList<BatchTask> tasks;
	private int count  = 0;

	public TasksQueueCollector() {
		tasks = new LinkedList<>();
	}

	public abstract void progress(int count, AudioFile fileInfo);

	public void initialize() {

		final Set<String> audios = Config.audios;

		AbstractFileProcessor abs = new AbstractFileProcessor(Config.instance().sourceDirectory.getString()) {

			private long totalSize = 0L;

			public boolean isAudio(FilePersistent fi) {
				return audios.contains(fi.getExtension().toLowerCase());
			}

			@Override
			public boolean accept(File entry, FilePersistent fileData) {
				return (isAudio(fileData));
			}

			@Override
			public void processFile(File file, FilePersistent filePersistent) throws Exception {
				processAudio(file, filePersistent);
			}

			private void processAudio(File file, FilePersistent filePersistent) {
				if (!file.isFile()) return ;

				try {
					AudioFile auFile = new AudioFile(file);
					boolean isOk = isRightConversationTask(filePersistent, auFile);
					if (!isOk) {
						System.out.println("Skipping: "+filePersistent.getFullName()+" bitRate: "+auFile.getBitRate());
						return ;
					}

					totalSize += auFile.getFileData().size();
					BatchTask task = new BatchTask(auFile);

					tasks.add(task);

					if ((++count) % PROGRESS_EVERY == 0) {
						progress(count, task.getSourceAudio());
					}

//					if (meta.isLowQuality()) {
//						System.out.println(Args.fill("### %1: bitrate: %2(%3), bps: %4, chanels: %5, s.rate: %6, encoding:%7(%8) ###", fileInfo.getFileName(), h.getBitRateAsNumber(), h.getBitRate(), h.getBitsPerSample(), meta.getChannelsText(), h.getSampleRate(), h.getFormat(), h.getEncodingType()));
//						System.out.println();
//					}

				} catch (RuntimeException rt) {
					System.out.println(Args.fill("Can't read file: %1", filePersistent.getFullNameWithPath()));
				}catch (Throwable e) {
					System.out.println(Args.fill("Can't read file: %1", filePersistent.getFullNameWithPath()));
				}
			}

			@Override
			public void onFinish() {

				ProgressModel.setSizeTotal(totalSize);

				BatchTask task = null;
				try {
					task = tasks.getLast();
				} catch (java.util.NoSuchElementException e) {}

				if (null == task) return ;
				progress(count, task.getSourceAudio());
				Collections.shuffle(tasks, new Random(System.currentTimeMillis() + Runtime.getRuntime().freeMemory()));
				Collections.shuffle(tasks);
			}
		};
		abs.start();
	}

	public Queue<BatchTask> getTasks() {
		return this.tasks;
	}

	private boolean isRightConversationTask(FilePersistent filePersistent, AudioFile auFile) {

		int bitRate =auFile.getBitRate();

		if ((bitRate <= MIN_ALLOWED_BITRATE)&&(bitRate>0)) {
			return false;
		}
		return true;
	}

	}
