package org.delafer.recoder.gui.buttons;

import org.eclipse.swt.graphics.Image;

public interface IButtonData {
	
	public enum Type {DYNAMIC, STATIC};
	
	public  String getName();
	public Image getIcon();
	public int getUID();
	public Type getType();
	
}
