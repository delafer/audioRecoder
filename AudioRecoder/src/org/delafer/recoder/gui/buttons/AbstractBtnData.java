/**
 * 
 */
package org.delafer.recoder.gui.buttons;

import org.eclipse.swt.graphics.Image;

/**
 * @author Alexander Tavrovsky
 *
 */
public abstract class AbstractBtnData implements IButtonData {
	
	private String name;
	private Image img;
	private int uid;
	
	public AbstractBtnData(String name, int uid) {
		this.name = name;
		this.uid = uid;
	}
	
	public AbstractBtnData(Image img, int uid) {
		this.img = img;
		this.uid = uid;
	}
	
	public AbstractBtnData(int uid) {
		this("",uid);
	}
	
	public void setName(String name) {
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see org.delafer.xmlbench.wizard.buttons.IButton#getName()
	 */
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see org.delafer.xmlbench.wizard.buttons.IButton#getUID()
	 */
	public int getUID() {
		return uid;
	}

	public Image getIcon() {
		return img;
	}
	
	public void setIcon(Image ico) {
		this.img = ico;
	}

}
