package org.delafer.recoder.test;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.wb.swt.SWTResourceManager;

public class Snippet166 {
	private static Table table;

public static void main(String[] args) {
	Display display = new Display();
	Image image1 = display.getSystemImage(SWT.ICON_WORKING);
	Image image2 = display.getSystemImage(SWT.ICON_QUESTION);
	Image image3 = display.getSystemImage(SWT.ICON_ERROR);

	Shell shell = new Shell(display);
	shell.setSize(600, 500);
	shell.setLayout(new FillLayout());

	final ScrolledComposite sc = new ScrolledComposite(shell, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);

	final Composite parent = new Composite(sc, SWT.NONE);
//	parent.setBackground(SWTResourceManager.getColor(SWT.COLOR_DARK_MAGENTA));
	RowLayout ltRow = new RowLayout(SWT.VERTICAL);
	ltRow.spacing = 0;
	ltRow.marginBottom = 0;
	ltRow.marginTop = 0;
	ltRow.marginLeft = 0;
	ltRow.marginRight = 0;
	parent.setLayout(ltRow);

//	TaskProgress[] tasks = new TaskProgress[5];
//	for (int i = 0; i < 5; i++) {
		TaskProgress a = new TaskProgress(parent, SWT.NONE);

		ProgressBar progressBar = new ProgressBar(parent, SWT.HORIZONTAL  | SWT.INDETERMINATE);
		progressBar.setSelection(0);

		table = new Table(parent, SWT.BORDER | SWT.FULL_SELECTION);
		table.setLayoutData(new RowData(174, 161));
		table.setForeground(SWTResourceManager.getColor(100, 149, 237));
		table.setBackground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		table.setFont(SWTResourceManager.getFont("Lucida Console", 8, SWT.NORMAL));
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
//	}
//	tasks[2].setState(2, 1);
	 sc.setMinSize(320, 175);
	 sc.setExpandHorizontal(true);
	 sc.setExpandVertical(true);
	 sc.addControlListener(new ControlAdapter()
		{
			public void controlResized(ControlEvent e)
			{
				parent.layout();
			}
		});
	 sc.setContent(parent);
	shell.open();
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch()) {
			display.sleep();
		}
	}
	display.dispose();
}
}