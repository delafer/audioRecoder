/**
 *
 */
package org.delafer.recoder.gui.threads;

import org.delafer.recoder.model.ProgressModel;
import org.delafer.recoder.model.ProgressModel.Context;
import org.delafer.recoder.tasks.BatchTask;
import org.delafer.recoder.tasks.TaskQueue;

/**
 * @author Alexander Tavrovsky
 *
 */
public final class ConvertorThread extends Thread {

	private volatile boolean paused = false; /** flag stopped. */
	private volatile boolean stopped = false; /** flag stopped. */

	public static volatile transient int currD = 0;

	private int threadNr;

	public ConvertorThread(int number) {
		super();
		this.threadNr = number;
		this.setDaemon(true);
		setPriority(Thread.NORM_PRIORITY-1);
		paused = true;
		start();
	}

	@Override
	public synchronized void start() {
		super.start();
	}

	@Override
	public void run() {

		try {

		initContext();
		ProgressModel.setTotalTasks(TaskQueue.size());

		while (!stopped) {

			synchronized (this) {
				while (paused) wait();
			}

			BatchTask task = TaskQueue.getNextTask();
			if (task != null) {
				task.doConversation();
				ProgressModel.incrementProcessedTasks();
			}
			else {
				System.err.println("!!!!DONE!!!!");
				System.err.println("!!!!DONE!!!!");
				System.err.println("!!!!DONE!!!!");
				System.err.println("!!!!DONE!!!!");
				for (int i = 0; i < 5; i++) {
					ProgressModel.toConsole("!!!DONE!!!");
				}

				paused = false;
				stopped = true;
			}

            Thread.yield();
            Thread.sleep(75);
            Thread.yield();


		}


		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initContext() {
		ProgressModel.getContextStore().set(new Context(threadNr));
	}

	public synchronized void stopJob() {
		if (stopped || isInterrupted()) return ;
		paused = false;
		stopped = true;
		notify();
		interrupt();

	}

	public synchronized void pauseJob() {
		if (paused || stopped || isInterrupted()) return ;
		paused = true;
		System.out.println("Pause");
		notify();
	}

	public synchronized void runJob() {
		if (!paused || stopped || isInterrupted()) return ;
		///System.out.println("Run");
		paused = false;
		notify();
	}

	public synchronized void updateJob() {
		notify();
	}



	@Override
	public void interrupt() {
		super.interrupt();
	}

	@Override
	public boolean isInterrupted() {
		return super.isInterrupted();
	}


}
