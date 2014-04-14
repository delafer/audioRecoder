package org.delafer.recoder.gui.dialogs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;

public class DirectoryOpenDialog {

	public static String open() {
		DirectoryDialog dialog = new DirectoryDialog (Display.getCurrent().getActiveShell(), SWT.OPEN);
		String filterPath = "/";
		String platform = SWT.getPlatform();
//		if (platform.equals("win32") || platform.equals("wpf")) {
//			filterNames = new String [] {"XML Files", "All Files (*.*)"};
//			filterExtensions = new String [] {"*.xml", "*.*"};
//			filterPath = "c:\\";
//		}
//		dialog.setFilterNames (filterNames);
//		dialog.setFilterExtensions (filterExtensions);
		dialog.setFilterPath (filterPath);
		String path = dialog.open ();
		System.out.println ("Open from: " + path);
		return path;
	}

}
