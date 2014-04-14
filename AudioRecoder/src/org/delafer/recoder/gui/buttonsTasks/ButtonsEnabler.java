package org.delafer.recoder.gui.buttonsTasks;

import org.delafer.recoder.gui.buttons.ButtonManager;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;

public class ButtonsEnabler {
	
	private transient int[] BUTTONS_ID;
	private boolean taskDone = false;
	private IButtonTask task;
	
	public ButtonsEnabler(IButtonTask task, int[] bUTTONS_ID) {
		this.BUTTONS_ID = bUTTONS_ID;
		this.task = task;
	}
	
	
	public boolean doTask() {
		if (!this.taskDone) {
			
		boolean success;
		try {
			success = task != null ? task.runTask() : true;
		} catch (Exception e) {
			e.printStackTrace();
			success =false;
		}
		
		this.taskDone = success;
		}
		
		return this.taskDone;
	}



	public void onTaskStart() {
		if (null != BUTTONS_ID) {
			Display.getDefault().asyncExec(new Runnable() {
	            public void run() {
	            	for (int uid : BUTTONS_ID) {
	        			setState(uid, false);
	        		};
	            }
	         });
		}
	}
	
	public void onTaskSuccess() {
		if (null != BUTTONS_ID) {
			Display.getDefault().asyncExec(new Runnable() {
	            public void run() {
	            	for (int uid : BUTTONS_ID) {
	        			setState(uid, true);
	        		};
	            }
	         });
		}
	}
	
	private final void setState(int buttonUID, boolean state) {
		final Button btn = ButtonManager.instance().getButtonById(buttonUID);
 	 	if (btn != null && !btn.isDisposed()) btn.setEnabled(state);
	}
	
}
