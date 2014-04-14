/**
 * 
 */
package org.delafer.recoder.gui.pages.deprecated;

import org.delafer.recoder.gui.buttons.ButtonManager;
import org.delafer.recoder.gui.buttons.PageButtonData;
import org.delafer.recoder.gui.pages.base.AbstractPageController;
import org.delafer.recoder.gui.threads.controller.ThreadController;
import org.delafer.recoder.gui.wizard.SmartWizard;
import org.delafer.recoder.helpers.utils.UtilsCommon;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;

/**
 * @author Alexander Tavrovsky
 *
 */
public class ProgressPageWP extends AbstractPageController{

	private ThreadController thr;

	/**
	 * 
	 */
	public ProgressPageWP(SmartWizard wizard) {
		super(wizard);
	}

	@Override
	public void drawComposite(Composite composite) {
		composite.setLayout(new FillLayout());
		ProgressPage cmp = new ProgressPage(composite, SWT.NONE);
		this.setBasePage(cmp);
	}
	
	

	@Override
	public void onExit() {
		if (thr != null) thr.stop();
		super.onExit();
	}

	@Override
	public void addButtons() {
		
		thr = new ThreadController((ProgressPage)cmp);
		
		PageButtonData startBtn = new PageButtonData(ButtonManager.BTN_PRG_START) {

			public void onKeyPress(Event event) {
				//System.out.println("Pressed Start");
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
