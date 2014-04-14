package org.delafer.recoder.gui.pages;

import org.delafer.recoder.config.Config;
import org.delafer.recoder.deprecated.ExternalFile;
import org.delafer.recoder.deprecated.IFileAbstract;
import org.delafer.recoder.gui.dialogs.DirectoryOpenDialog;
import org.delafer.recoder.gui.pages.base.AbstractPageUI;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class SelectSourcePageUI extends AbstractPageUI {
	private Text txtInputDir;
	private Text txtOutputDir;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public SelectSourcePageUI(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(1, false));


		Label lblSelectedXmlFile = new Label(this, SWT.NONE);
		lblSelectedXmlFile.setText("Selected Source directory");

		txtInputDir = new Text(this, SWT.BORDER);
		txtInputDir.setEditable(false);
		txtInputDir.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblPath = new Label(this, SWT.NONE);
		lblPath.setText("Path");

		txtOutputDir = new Text(this, SWT.BORDER);
		txtOutputDir.setEditable(false);
		txtOutputDir.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Group group = new Group(this, SWT.NONE);
		group.setLayout(new GridLayout(1, false));
		group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));


//		SelectionAdapter selection = new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				Button btn = (Button)e.widget;
//				IFileAbstract fileInf = (IFileAbstract) btn.getData();
//				txtInputDir.setText(fileInf.getName());
//				txtOutputDir.setText(fileInf.getPath());
//			}
//		};

//		for (int i = 0; i < radioBtn.length; i++) {
//			radioBtn[i].addSelectionListener(selection);
//		}

		Button btnNewButton = new Button(group, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String path = DirectoryOpenDialog.open();
				Config.instance().sourceDirectory.setString(path);
				onEnter();

			}
		});
		btnNewButton.setText("Select Input directory");

	}

	@Override
	public void onEnter() {
		txtInputDir.setText(Config.instance().sourceDirectory.getString());
		txtOutputDir.setText(txtInputDir.getText());
	}



	@Override
	public void onExit() {
	}

	@Override
	public void beforeExit() {
	}


	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
