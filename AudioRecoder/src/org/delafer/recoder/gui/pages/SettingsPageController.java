/**
 * 
 */
package org.delafer.recoder.gui.pages;

import org.delafer.recoder.gui.pages.base.AbstractPageController;
import org.delafer.recoder.gui.wizard.SmartWizard;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Alexander Tavrovsky
 *
 */
public class SettingsPageController extends AbstractPageController{

	/**
	 * 
	 */
	public SettingsPageController(SmartWizard wizard) {
		super(wizard);
	}

	@Override
	public void drawComposite(Composite composite) {
		composite.setLayout(new FillLayout());
		SettingsPageUI cmp = new SettingsPageUI(composite, SWT.NONE);
		this.setBasePage(cmp);
	}

}
