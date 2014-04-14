/**
 *
 */
package org.delafer.recoder.gui.pages;

import java.util.Collections;
import java.util.Queue;

import org.delafer.recoder.gui.buttonsTasks.AsyncButtonTask;
import org.delafer.recoder.gui.buttonsTasks.IButtonTask;
import org.delafer.recoder.gui.pages.base.AbstractPageController;
import org.delafer.recoder.gui.threads.controller.ThreadController;
import org.delafer.recoder.gui.wizard.SmartWizard;
import org.delafer.recoder.helpers.Convertors;
import org.delafer.recoder.model.AudioFile;
import org.delafer.recoder.tasks.BatchTask;
import org.delafer.recoder.tasks.TaskQueue;
import org.delafer.recoder.tasks.TasksQueueCollector;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

/**
 * @author Alexander Tavrovsky
 *
 */
public class TaskCollectorPageController extends AbstractPageController{

	private ThreadController thr;
	/**
	 *
	 */
	public TaskCollectorPageController(SmartWizard wizard) {
		super(wizard);
	}

	@Override
	public void drawComposite(Composite composite) {
		composite.setLayout(new FillLayout());
		TaskCollectorPageUI cmp = new TaskCollectorPageUI(composite, SWT.NONE);
		this.setBasePage(cmp);
	}

	private static final int BUF_SIZE = 2048;

	@Override
	public void onEnter() {
		super.onEnter();
		final Display d = Display.getDefault();
		IButtonTask task = new IButtonTask() {

			public boolean runTask() throws Exception {

				TasksQueueCollector collector = new TasksQueueCollector() {
					long ct,st = System.currentTimeMillis();
					@Override
					public void progress(final int count, final AudioFile audioFile) {
						ct = System.currentTimeMillis();
						if (ct-st<250) return ;
						st = ct;
						d.asyncExec(new Runnable() {
				            public void run() {

				            	TaskCollectorPageUI page = (TaskCollectorPageUI)getBasePage();

				            	page.txtComprName.setText(String.valueOf(count));
				            	page.txtFileName.setText(audioFile.getFileData().getFullName());
				            	page.txtUncompSize.setText(Convertors.autoSize(audioFile.getSize()));

				            }
				         });

					}
				};
				collector.initialize();
				Queue<BatchTask> tasks = collector.getTasks();
				TaskQueue.setTasks(tasks);

				Display.getDefault().asyncExec(new Runnable() {
		            public void run() {

		            	TaskCollectorPageUI page = (TaskCollectorPageUI)getBasePage();

		            	if (page == null || page.isDisposed()) return ;

		            	page.setInDeterminated(false);
		            	page.scanProgress.setSelection(100);
		            }
		         });



				return true;
			}
		};

		AsyncButtonTask.doTask(task, AsyncButtonTask.NEXT_BUTTON_ID);
	}






}
