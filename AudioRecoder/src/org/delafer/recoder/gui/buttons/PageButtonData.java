package org.delafer.recoder.gui.buttons;

import org.eclipse.swt.widgets.Event;



public abstract class PageButtonData extends DynamicBtnData {
	
	
	public PageButtonData(IButtonData btn) {
		super(btn.getName(), btn.getUID());
	}
	
	public abstract void onKeyPress(Event event);
	
//	{
//		// TODO implement user reaction
//	}

}
