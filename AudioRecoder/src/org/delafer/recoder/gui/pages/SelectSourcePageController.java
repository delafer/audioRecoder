/**
 *
 */
package org.delafer.recoder.gui.pages;

import org.delafer.recoder.config.Config;
import org.delafer.recoder.gui.pages.base.AbstractPageController;
import org.delafer.recoder.gui.wizard.SmartWizard;
import org.delafer.recoder.helpers.utils.UtilsFile;
import org.delafer.recoder.model.common.MString;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;


/**
 * @author Alexander Tavrovsky
 *
 */
public class SelectSourcePageController extends AbstractPageController{

	/**
	 *
	 */
	public SelectSourcePageController(SmartWizard wizard) {
		super(wizard);
	}

	@Override
	public void drawComposite(Composite composite) {
		composite.setLayout(new FillLayout());
		SelectSourcePageUI cmp = new SelectSourcePageUI(composite, SWT.NONE);
		this.setBasePage(cmp);
	}


	@Override
	public boolean nextAllowed() {
		MString dir = Config.instance().sourceDirectory;

		boolean ok = UtilsFile.isRealDirectory(dir.getValue());

		if (!ok) {
			MessageBox messageBox = new MessageBox(Display.getCurrent().getActiveShell(), SWT.ICON_WARNING);
		    messageBox.setMessage("To continue -> Please select a valid non empty directory");
			messageBox.open();
		}

		return ok;
	}

}
