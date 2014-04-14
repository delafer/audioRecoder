package org.delafer.recoder.gui.pages.base;

import org.delafer.recoder.gui.buttons.PageButtonData;
import org.delafer.recoder.gui.wizard.SmartWizard;
import org.eclipse.swt.widgets.Composite;

public abstract class AbstractPageController {
	
	
	private SmartWizard wizard;
	protected AbstractPageUI cmp;	
	
	public AbstractPageController(SmartWizard wizard) {
		this.wizard = wizard;
	}


	
	public abstract void drawComposite(Composite composite);
	
	public void setBasePage(AbstractPageUI page) {
		this.cmp = page;
	}
	
	protected AbstractPageUI getBasePage() {
		return this.cmp;
	}
	
	public void beforeExit() {
		getBasePage().beforeExit();
	}
	
	public void onEnter() {
		getBasePage().onEnter();
		addButtons();
	}
	
	public void onExit() {
		getBasePage().onExit();
		removeButtons();
	}
	
	public boolean nextAllowed() {
		return true;
	}
	
	
	public void addButtons() {
	}
	
	protected void addButton(PageButtonData button) {
		if (button == null) return ;
		wizard.addButton(button);
	}
	
	private void removeButtons() {
		wizard.removeButtons();
	}
	
	
	
}
