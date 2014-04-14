package org.delafer.recoder.gui.dialogs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;

public class FileOpenDialog {

	public static String open() {

		FileDialog dialog = new FileDialog(Display.getCurrent().getActiveShell(), SWT.OPEN);
		String [] filterNames = new String [] {"XML Files", "All Files (*)"};
		String [] filterExtensions = new String [] {"*.xml", "*"};
		String filterPath = "/";
		String platform = SWT.getPlatform();
		if (platform.equals("win32") || platform.equals("wpf")) {
			filterNames = new String [] {"XML Files", "All Files (*.*)"};
			filterExtensions = new String [] {"*.xml", "*.*"};
			filterPath = "c:\\";
		}

		dialog.setFilterNames (filterNames);
		dialog.setFilterExtensions (filterExtensions);
		dialog.setFilterPath (filterPath);
		String path = dialog.open ();
		System.out.println ("Open from: " + path);
		return path;
	}

}
