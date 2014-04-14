/**
 *
 */
package org.delafer.recoder.gui.pages.base;

import org.eclipse.swt.widgets.Composite;

/**
 * @author Alexander Tavrovsky
 *
 */
public abstract class AbstractPageUI extends Composite {

	/**
	 * @param parent
	 * @param style
	 */
	public AbstractPageUI(Composite parent, int style) {
		super(parent, style);
	}

	public void onEnter() {
	}

	public void onExit() {
	}

	public void beforeExit() {

	}

}
