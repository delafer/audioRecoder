package org.delafer.recoder.gui.buttons;

public class StaticBtnData extends AbstractBtnData {

	public StaticBtnData(String name, int uid) {
		super(name, uid);
	}

	public StaticBtnData(int uid) {
		super(uid);
	}

	public Type getType() {
		return Type.STATIC;
	}

}
