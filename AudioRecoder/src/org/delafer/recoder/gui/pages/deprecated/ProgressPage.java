package org.delafer.recoder.gui.pages.deprecated;

import org.delafer.recoder.config.Config;
import org.delafer.recoder.gui.pages.base.AbstractPageUI;
import org.delafer.recoder.helpers.Convertors;
import org.delafer.recoder.helpers.utils.UtilsCommon;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ProgressBar;

public class ProgressPage extends AbstractPageUI {
	public ProgressBar memoryBar;
	public Text txtCompressedBytes;
	public Text txtReqCounter;
	public Text txtDecompressedBytes;
	public Text txtTotalBytes;
	public Text txtComprSpeed;
	public Text txtDecompSpeed;
	public Text txtAverageSpeed;
	public ProgressBar compressionBar;
	public Text txtRatio;
	public Text txtMemSaved;
	public Text txtMemTotal;
	public Text txtMemAvailable;
	public Text txtMemUsed;
	public Text txtMemMaxUsed;
	public Text txtReqProSec;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public ProgressPage(Composite parent, int style) {
		super(parent, style);
		parent.setSize(700, 500);
		this.setSize(680, 395);
		setLayout(new GridLayout(2, false));

		createBenchGroup();
		createTrafficGroup();
		createReqGroup();
		createMemoryGroup();
		createCompGroup();

	}

	private void createCompGroup() {
		Group grpMemory = new Group(this, SWT.NONE);
		grpMemory.setLayout(new GridLayout(1, false));
		grpMemory.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		grpMemory.setText("Compression");

		compressionBar = new ProgressBar(grpMemory, SWT.NONE);
		memoryBar.setSelection(0);
		compressionBar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Composite composite = new Composite(grpMemory, SWT.NONE);
		composite.setLayout(new GridLayout(4, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));

		txtRatio = createTextLabel(composite, "Ratio (%)");
		txtMemSaved = createTextLabel(composite, "Memory saved");
	}

	private void createMemoryGroup() {
		Group grpMemory = new Group(this, SWT.NONE);
		grpMemory.setLayout(new GridLayout(1, false));
		grpMemory.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		grpMemory.setText("Memory");

		memoryBar = new ProgressBar(grpMemory, SWT.NONE);
//		memoryBar.setSelection(50);
		memoryBar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Composite composite = new Composite(grpMemory, SWT.NONE);
		composite.setLayout(new GridLayout(4, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));

		txtMemTotal = createTextLabel(composite, "Total memory");
		txtMemAvailable = createTextLabel(composite, "Available memory");
		txtMemUsed = createTextLabel(composite, "Memory used");
		txtMemMaxUsed = createTextLabel(composite, "Max used");
	}

	public static Text createTextLabel(Composite composite, String label) {
		Label lblNewLabel = new Label(composite, SWT.NONE);
		lblNewLabel.setText(label);

		Text text = new Text(composite, SWT.BORDER);
		text.setEditable(false);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		return text;
	}

	private void createBenchGroup() {
		Group grpBenchmark = new Group(this, SWT.NONE);
		grpBenchmark.setText(" Benchmark ");
		grpBenchmark.setLayout(new GridLayout(2, false));
		grpBenchmark.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));

		Label lblNewLabel = new Label(grpBenchmark, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setText("Average");

		txtAverageSpeed = new Text(grpBenchmark, SWT.BORDER);
		txtAverageSpeed.setEditable(false);
		txtAverageSpeed.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		lblNewLabel = new Label(grpBenchmark, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setText("Compression");

		txtComprSpeed = new Text(grpBenchmark, SWT.BORDER);
		txtComprSpeed.setEditable(false);
		txtComprSpeed.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		lblNewLabel = new Label(grpBenchmark, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setText("Decompression");

		txtDecompSpeed = new Text(grpBenchmark, SWT.BORDER);
		txtDecompSpeed.setEditable(false);
		txtDecompSpeed.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
	}

	private void createTrafficGroup() {
		Group grpTraffic = new Group(this, SWT.NONE);
		grpTraffic.setText(" Traffic ");
		grpTraffic.setLayout(new GridLayout(2, false));
		grpTraffic.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));

		Label lblNewLabel = new Label(grpTraffic, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setText("Total");

		txtTotalBytes = new Text(grpTraffic, SWT.BORDER);
		txtTotalBytes.setEditable(false);
		txtTotalBytes.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		lblNewLabel = new Label(grpTraffic, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setText("Compressed");

		txtCompressedBytes = new Text(grpTraffic, SWT.BORDER);
		txtCompressedBytes.setEditable(false);
		txtCompressedBytes.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		lblNewLabel = new Label(grpTraffic, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setText("Decompressed");

		txtDecompressedBytes = new Text(grpTraffic, SWT.BORDER);
		txtDecompressedBytes.setEditable(false);
		txtDecompressedBytes.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

//		lblNewLabel = new Label(grpTraffic, SWT.NONE);
//		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
//		lblNewLabel.setText("Saved");
//
//		text = new Text(grpTraffic, SWT.BORDER);
//		text.setEditable(false);
//		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
	}

	private void createReqGroup() {
		Group grpTraffic = new Group(this, SWT.NONE);
		grpTraffic.setText(" Processed requests ");
		grpTraffic.setLayout(new GridLayout(2, false));
		grpTraffic.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 2, 1));

		Label lblNewLabel = new Label(grpTraffic, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setText("Total / Compress / Decompress");

		txtReqCounter = new Text(grpTraffic, SWT.BORDER);
		txtReqCounter.setEditable(false);
		txtReqCounter.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));


		lblNewLabel = new Label(grpTraffic, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setText("Request Pro Sec (Requests / Sec)");

		txtReqProSec = new Text(grpTraffic, SWT.BORDER);
		txtReqProSec.setEditable(false);
		txtReqProSec.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
	}

	@Override
	public void onEnter() {
		UtilsCommon.freeMemory();
		Runtime rt = Runtime.getRuntime();
		txtMemTotal.setText(Convertors.autoSize(rt.maxMemory()));
		txtMemAvailable.setText(Convertors.autoSize(rt.totalMemory()));

		memoryBar.setMaximum(100);
		compressionBar.setMaximum(100);
		compressionBar.setSelection((int)Math.round(Config.instance().ratioCompresion));
		txtRatio.setText(Convertors.formatValue(Config.instance().ratioCompresion)+" %");

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
