/**
 * 
 */
package org.delafer.recoder.gui.pages.deprecated;

import org.delafer.recoder.gui.buttonsTasks.AsyncButtonTask;
import org.delafer.recoder.gui.buttonsTasks.IButtonTask;
import org.delafer.recoder.gui.pages.base.AbstractPageController;
import org.delafer.recoder.gui.wizard.SmartWizard;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Alexander Tavrovsky
 *
 */
public class CachePageWP extends AbstractPageController{

	/**
	 * 
	 */
	public CachePageWP(SmartWizard wizard) {
		super(wizard);
	}

	@Override
	public void drawComposite(Composite composite) {
		composite.setLayout(new FillLayout());
		CachePage cmp = new CachePage(composite, SWT.NONE);
		this.setBasePage(cmp);
		
	}

	@Override
	public void onEnter() {
		super.onEnter();
	}

}
