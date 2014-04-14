package org.delafer.recoder.gui.threads.controller;

import java.util.LinkedList;
import java.util.List;

import org.delafer.recoder.config.Config;
import org.delafer.recoder.gui.pages.MainTaskPageUI;
import org.delafer.recoder.gui.pages.base.AbstractPageUI;
import org.delafer.recoder.gui.pages.deprecated.ProgressPage;
import org.delafer.recoder.gui.threads.ConvertorThread;
import org.delafer.recoder.gui.threads.ShowProgressThread;
import org.delafer.recoder.model.ProgressModel;

public class ThreadController {


	int threads;
	boolean alive;
	List<ConvertorThread> th = new LinkedList<ConvertorThread>();
	AbstractPageUI page;
	private ShowProgressThread guiUpdater;

	public ThreadController(AbstractPageUI page) {
		super();
		threads  = Config.instance().threads;
		alive = false;
		this.page = page;
	}

	public void checkAlive() {
		if (alive) return ;

		for (int i = 0; i < threads; i++) {
			ConvertorThread thread = new ConvertorThread(i+1);
			th.add(thread);
		}

		alive = true;
	}


	public synchronized void start() {
		checkAlive();

		startGuiUpdater();


		for (ConvertorThread thread : th) {
			thread.runJob();
		}
	}

	private void startGuiUpdater() {
		guiUpdater = new ShowProgressThread( (MainTaskPageUI) page);
	};

	private void stopGuiUpdater() {
		if (null != guiUpdater) {
		guiUpdater.cancel();
		guiUpdater.purge();
		guiUpdater = null;
		}
	};

	private void updateGUI() {
		guiUpdater.updateGUI();
	};

	public synchronized void pause() {
		if (!alive) return ;

		stopGuiUpdater();


		for (ConvertorThread thread : th) {
			thread.pauseJob();
		}
	}




	public synchronized void stop() {
		if (!alive) return ;

		for (ConvertorThread thread : th) {
			thread.stopJob();
		}

		try {
			Thread.sleep(450);
		} catch (InterruptedException e) {}

		ProgressModel.reset();
		updateGUI();
		stopGuiUpdater();

//		try {
//			Thread.sleep(450);
//		} catch (InterruptedException e) {}


		alive = false;
	};



}
