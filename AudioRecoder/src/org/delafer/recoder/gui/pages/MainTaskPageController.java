/**
 *
 */
package org.delafer.recoder.gui.pages;

import org.delafer.recoder.gui.buttons.ButtonManager;
import org.delafer.recoder.gui.buttons.PageButtonData;
import org.delafer.recoder.gui.pages.base.AbstractPageController;
import org.delafer.recoder.gui.pages.base.AbstractPageUI;
import org.delafer.recoder.gui.threads.controller.ThreadController;
import org.delafer.recoder.gui.wizard.SmartWizard;
import org.delafer.recoder.helpers.utils.UtilsCommon;
import org.delafer.recoder.model.ProgressModel;
import org.delafer.recoder.model.ProgressModel.Context;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;

/**
 * @author Alexander Tavrovsky
 *
 */
public class MainTaskPageController extends AbstractPageController{

	private ThreadController thr;
	/**
	 *
	 */
	public MainTaskPageController(SmartWizard wizard) {
		super(wizard);
	}

	@Override
	public void drawComposite(Composite composite) {
		composite.setLayout(new FillLayout());
		MainTaskPageUI cmp = new MainTaskPageUI(composite, SWT.NONE);
		this.setBasePage(cmp);
	}

	private static final int BUF_SIZE = 2048;

	@Override
	public void onEnter() {
		super.onEnter();

		ProgressModel.getContextStore().set(new Context(-1));

//		IButtonTask task = new IButtonTask() {
//
//			public boolean runTask() throws Exception {
//
//				Display.getDefault().asyncExec(new Runnable() {
//		            public void run() {
//
//		            	MainTaskPageUI page = (MainTaskPageUI)getBasePage();
//
//		            	if (page == null || page.isDisposed()) return ;
//
//		            	page.ratioBar.setSelection((int) 0);
//		            }
//		         });
//
//
//
//				return SourceFactory.isInitialized();
//			}
//		};
//
//		AsyncButtonTask.doTask(task, AsyncButtonTask.NEXT_BUTTON_ID);
	}


	@Override
	public void addButtons() {

		thr = new ThreadController((AbstractPageUI)cmp);

		PageButtonData startBtn = new PageButtonData(ButtonManager.BTN_PRG_START) {

			public void onKeyPress(Event event) {

				thr.start();

			}
		};

		PageButtonData pauseBtn = new PageButtonData(ButtonManager.BTN_PRG_PAUSE) {

			public void onKeyPress(Event event) {
				//System.out.println("Pressed pause");
				thr.pause();
			}
		};


		PageButtonData stopBtn = new PageButtonData(ButtonManager.BTN_PRG_STOP) {

			public void onKeyPress(Event event) {
				//System.out.println("Pressed Stop");
				thr.stop();
			}
		};

		PageButtonData gcBtn = new PageButtonData(ButtonManager.BTN_PRG_GC) {

			public void onKeyPress(Event event) {
				UtilsCommon.freeMemory();
			}
		};

		addButton(startBtn);
		addButton(pauseBtn);
		addButton(stopBtn);
		addButton(gcBtn);
	}



}
