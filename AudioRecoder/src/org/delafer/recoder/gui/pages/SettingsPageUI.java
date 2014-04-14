package org.delafer.recoder.gui.pages;

import java.util.Map;

import org.delafer.recoder.config.Config;
import org.delafer.recoder.deprecated.compressors.CompressionFactory;
import org.delafer.recoder.deprecated.compressors.ICompressor;
import org.delafer.recoder.deprecated.compressors.JavaZipFast;
import org.delafer.recoder.deprecated.compressors.JavaZipNormal;
import org.delafer.recoder.gui.pages.base.AbstractPageUI;
import org.delafer.recoder.helpers.utils.UtilsCommon;
import org.delafer.recoder.test.FindIt;
import org.delafer.recoder.test.FindIt.ThreadRatio;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Text;

public class SettingsPageUI extends AbstractPageUI {
	private Text txtThreads;
	private Text text_1;
	private Scale scale;
	private Text scaleRate;
	private Combo cmpCompressor;
	private Text txtDepackers;
	private Text txtPackers;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public SettingsPageUI(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(1, false));

//		Label lblNewLabel = new Label(this, SWT.NONE);
//		lblNewLabel.setText("Compression alghoritm");
//
//		cmpCompressor = new Combo(this, SWT.READ_ONLY);
//		cmpCompressor.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Composite composite = new Composite(this, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		composite.setLayout(new GridLayout(2, false));

		Label lblNewLabel_1 = new Label(composite, SWT.NONE);
		lblNewLabel_1.setText("Requests parallel");

		txtThreads = new Text(composite, SWT.BORDER);
		txtThreads.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

//		Label lblCompressionDecompression = new Label(this, SWT.NONE);
//		lblCompressionDecompression.setText("Compression / decompression threads ratio");
//
//		Composite ratioComp = new Composite(this, SWT.NONE);
//		ratioComp.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
//		GridLayout gl_ratioComp = new GridLayout(2, false);
//		gl_ratioComp.verticalSpacing = 2;
//		gl_ratioComp.marginWidth = 0;
//		gl_ratioComp.marginHeight = 0;
//		gl_ratioComp.horizontalSpacing = 0;
//		ratioComp.setLayout(gl_ratioComp);
//
//		scaleRate = new Text(ratioComp, SWT.BORDER);
//
//		setGridData(scaleRate);
//		scaleRate.setEditable(false);
//
//		scale = new Scale(ratioComp, SWT.NONE);
//		scale.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				updateScaleRate();
//			}
//		});
//		scale.setPageIncrement(1);
//		scale.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
//
//
//		txtDepackers = new Text(ratioComp, SWT.BORDER);
//		txtDepackers.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
//		txtDepackers.setEditable(false);
//		setGridData(txtDepackers);
//		Label lblThreads1 = new Label(ratioComp, SWT.NONE);
//		lblThreads1.setText("  Decompressor threads");
//
//		txtPackers = new Text(ratioComp, SWT.BORDER);
//		txtPackers.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
//		txtPackers.setEditable(false);
//		setGridData(txtPackers);
//		Label lblThreads2 = new Label(ratioComp, SWT.NONE);
//		lblThreads2.setText("  Compressor threads");

	}

//	private static void setGridData(Text txt) {
//		GridData newDataGrid = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
//		newDataGrid.widthHint = 48;
//		txt.setLayoutData(newDataGrid);
//	}

//	public void updateScaleRate()  {
//		int sel = scale.getSelection();
//		scaleRate.setText(String.valueOf(sel)+" %");
//
//		ThreadRatio ratio = FindIt.findRatio(sel);
//		txtDepackers.setText(String.valueOf(ratio.depackers));
//		txtPackers.setText(String.valueOf(ratio.packers));
//
//
//	}

	@Override
	public void onEnter() {

//		Map<Integer, ICompressor> cmps = CompressionFactory.getCompressors();
//		int at = 0;
//		int selected = -1;
//		int defCmp  = Config.instance().defaultCompressor;
//		for (Map.Entry<Integer, ICompressor> obj : cmps.entrySet()) {
//			cmpCompressor.add(obj.getValue().getName());
//			cmpCompressor.setData(String.valueOf(at), obj.getKey());
//			if (obj.getKey().equals(Integer.valueOf(defCmp))) selected = at;
//			at++;
//		}
//
//		if (selected != -1) cmpCompressor.select(selected);
//
//		scale.setSelection(Config.instance().threadsRatio);
		txtThreads.setText(String.valueOf(Config.instance().threads));
//		updateScaleRate();
	}

//	private int getSelectionCompressor() {
//		int idx = cmpCompressor.getSelectionIndex();
//		Integer val = (Integer)cmpCompressor.getData(String.valueOf(idx));
//		return val != null ? val.intValue() : -1;
//	}

	@Override
	public void onExit() {


			if (Config.instance().threads > 24) {

				MessageBox messageBox = new MessageBox(Display.getCurrent().getActiveShell(), SWT.ICON_WARNING);
				String msg = "Warning: BUILD-IN java ZIP deflater not intended to be\r\n used in high-concurrency multithreaded environments\r\n";
				//msg += "\r\nDue to some bugs in JVM\r\n(Native memory allocation (malloc) problem)\r\nmultithreaded usage of Infalter/Deflater streams\r\ncan cause OS Freeze and Instability";
				msg += "\r\n\r\nTo prevent data loss -> please save all important data first\r\n and exit existing apps";
			    messageBox.setMessage(msg);
				messageBox.open();

				messageBox = new MessageBox(Display.getCurrent().getActiveShell(), SWT.ICON_WARNING);
				msg = "You have selected "+Config.instance().threads+" parallel requests";
				msg += "\r\n\r\nIt's highly recommended to reduce\r\nthis value for Build-In Java Zip archiver\r\n\r\nRecommended value: 8 or less";
			    messageBox.setMessage(msg);
				messageBox.open();
			}

	}


	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
