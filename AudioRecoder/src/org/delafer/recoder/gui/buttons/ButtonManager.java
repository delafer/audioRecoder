package org.delafer.recoder.gui.buttons;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;


public class ButtonManager {
	
	public static final int UID_NEXT_BTN = -1;
	public static final int UID_PREV_BTN = -2;
	
	public static final int UID_START_BTN = 20;
	public static final int UID_STOP_BTN = 21;
	public static final int UID_PAUSE_BTN = 22;
	public static final int UID_GC_BTN = 23;
	
	public static IButtonData BTN_NEXT 			= new StaticBtnData(" >>> ", UID_NEXT_BTN);
	public static IButtonData BTN_PREV 			= new StaticBtnData(" <<< ", UID_PREV_BTN);
	
	public static IButtonData BTN_PRG_START 	= new DynamicBtnData("       start       ", UID_START_BTN);
	public static IButtonData BTN_PRG_STOP 		= new DynamicBtnData("       stop        ", UID_STOP_BTN);
	public static IButtonData BTN_PRG_PAUSE 	= new DynamicBtnData("       pause       ", UID_PAUSE_BTN);
	public static IButtonData BTN_PRG_GC 	= new DynamicBtnData("       Free RAM       ", UID_GC_BTN);
	
	
	
    /** demand holder idiom Lazy-loaded Singleton */
    private static class Holder {
        private final static ButtonManager	INSTANCE	= new ButtonManager();
    }
    
    
    private transient HashMap<Integer, Button> btnsStatic = new HashMap<Integer, Button>(3);
    private transient HashMap<Integer, Button> btnsDynamic = new HashMap<Integer, Button>(3);
    
    /**
     * Gets the single instance of LockCore.
     * @return single instance of LockCore
     */
    public static ButtonManager instance() {
        return Holder.INSTANCE;
    }
    
    private synchronized void registerBtn(IButtonData btn, Button btnSwt) {
    	switch (btn.getType()) {
		case DYNAMIC:
			btnsDynamic.put(btn.getUID(), btnSwt);
			break;
		case STATIC:
			btnsStatic.put(btn.getUID(), btnSwt);
			break;
		default:
			break;
		}
    }
    
    public synchronized Button createButton(Composite cmp, IButtonData ibutton) {
    	Button btn = new Button(cmp, SWT.NONE);
    	btn.setText(ibutton.getName());
    	
    	registerBtn(ibutton, btn);
    	
    	return btn;
    	
    }
    
    public synchronized Button getButtonById(int id) {
    	final Integer key = Integer.valueOf(id);
    	Button btn  = btnsStatic.get(key);
    	return btn != null ? btn : btnsDynamic.get(key);
    }
    
	public synchronized void removeDynamicButtons() {
		if (btnsDynamic.isEmpty()) return ;
		Button btn;
		for (Map.Entry<Integer, Button> elm : btnsDynamic.entrySet()) {
			btn = elm.getValue();
			if (!btn.isDisposed()) btn.dispose();
		}
		btnsDynamic.clear();		
	}
		
    
}
