package org.delafer.recoder.gui.threads;

import java.util.Timer;
import java.util.TimerTask;

import org.delafer.recoder.config.Config;
import org.delafer.recoder.gui.pages.MainTaskPageUI;
import org.delafer.recoder.helpers.utils.UtilsTime;
import org.delafer.recoder.model.ProgressModel;
import org.delafer.recoder.test.TaskProgress;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;

public class ShowProgressThread extends Timer {

	private RepeatedTask task;

	public ShowProgressThread(MainTaskPageUI page) {
		super(true);
		task = new RepeatedTask(page);
		this.schedule(task, (long)(UtilsTime.SECOND * 1), (long)(UtilsTime.SECOND / 3));
	}

	public void updateGUI() {
		task.runTask(true);
	}

	public static class RepeatedTask extends TimerTask {

		protected final MainTaskPageUI page;


		public RepeatedTask(MainTaskPageUI page) {
			this.page = page;
		}

		private volatile static transient int runs = 0;


		public void normalUpdate() {
			Table tbl  = this.page.table;
			tbl.setRedraw(false);
			tbl.clearAll();
			tbl.setRedraw(true);
			tbl.update();

			this.page.txtProcessedFiles.setText(ProgressModel.getProcessedCnt());
			this.page.txtRemainingFiles.setText(ProgressModel.getRemainingCnt());
			this.page.txtSizeOriginal.setText(ProgressModel.getSizeOriginal());
			this.page.txtSizeNew.setText(ProgressModel.getSizeRepacked());
			this.page.txtTimeElapsed.setText(ProgressModel.getElapssedTime());
			this.page.txtTimeLeft.setText(ProgressModel.getRemainingTime());

			this.page.ratioBar.setSelection(ProgressModel.getProgress());


			for (int i = Config.instance().threads-1; i >= 0; i--) {
				TaskProgress tp = this.page.tasks[i];
				tp.update(ProgressModel.states[i]);
				tp.updateName(ProgressModel.names[i]);
			}


		}




		@Override
		public void run() {

			runTask(false);
		}

		public void runTask(final boolean updateAll) {
			Display.getDefault().asyncExec(new Runnable() {
	            public void run() {
	            	if (RepeatedTask.this.page == null || RepeatedTask.this.page.isDisposed()) return ;
	            	normalUpdate();
	            }
	         });
			runs++;
			Thread.yield();
		}

	}


}
