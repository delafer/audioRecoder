package org.delafer.recoder.gui.buttons;

public class DynamicBtnData extends AbstractBtnData {

	public DynamicBtnData(String name, int uid) {
		super(name, uid);
	}

	public DynamicBtnData(int uid) {
		super(uid);
	}

	public Type getType() {
		return Type.DYNAMIC;
	}

}
