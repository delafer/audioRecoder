package org.delafer.recoder.gui.pages;

import org.delafer.recoder.gui.pages.base.AbstractPageUI;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ProgressBar;

public class TaskCollectorPageUI extends AbstractPageUI {

	public ProgressBar scanProgress;


	public Text txtFileName;
	public Text txtUncompSize;
	public Text txtCompSize;
	public Text txtComprName;
//	public Text txtRatio;
//	public Text txtSizeSaved;


	private Group grpMemory;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public TaskCollectorPageUI(Composite parent, int style) {
		super(parent, style);
		parent.setSize(700, 500);
		this.setSize(680, 395);
		setLayout(new GridLayout(1, false));

		createBenchGroup();
		createCompGroup();

	}

	public void setInDeterminated(boolean value) {

		if (scanProgress == null || scanProgress.isDisposed()) {
			createProgressBar(value, false);
		} else {
			boolean indt = (scanProgress.getStyle() & SWT.INDETERMINATE) ==SWT.INDETERMINATE;
			if (value == indt) return ;
			scanProgress.dispose();
			scanProgress = null;
			createProgressBar(value, true);
		}
	}

	private void createProgressBar(boolean value, boolean redraw) {
		scanProgress = new ProgressBar (grpMemory,  SWT.HORIZONTAL | (value ? SWT.INDETERMINATE : 0));
		scanProgress.setMaximum(100);
		scanProgress.setSelection(0);
		scanProgress.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		if (redraw) grpMemory.layout(true);
	}

	private void createCompGroup() {
		grpMemory = new Group(this, SWT.NONE);
		grpMemory.setLayout(new GridLayout(1, false));
		grpMemory.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		grpMemory.setText("Total progress:");

		setInDeterminated(true);

//		Composite composite = new Composite(grpMemory, SWT.NONE);
//		composite.setLayout(new GridLayout(4, false));
//		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));

//		txtRatio = createTextLabel(composite, "Ratio (%)");
//		txtSizeSaved = createTextLabel(composite, "Size saved");

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
		grpBenchmark.setText("Current file");
		grpBenchmark.setLayout(new GridLayout(2, false));
		grpBenchmark.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));

		Label lblNewLabel = new Label(grpBenchmark, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setText("Count: ");

		txtComprName = new Text(grpBenchmark, SWT.BORDER);
		txtComprName.setEditable(false);
		txtComprName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));


		lblNewLabel = new Label(grpBenchmark, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setText("File name");

		txtFileName = new Text(grpBenchmark, SWT.BORDER);
		txtFileName.setEditable(false);
		txtFileName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		lblNewLabel = new Label(grpBenchmark, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setText("Original (size)");

		txtUncompSize = new Text(grpBenchmark, SWT.BORDER);
		txtUncompSize.setEditable(false);
		txtUncompSize.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

//		lblNewLabel = new Label(grpBenchmark, SWT.NONE);
//		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
//		lblNewLabel.setText("Compressed (size)");

//		txtCompSize = new Text(grpBenchmark, SWT.BORDER);
//		txtCompSize.setEditable(false);
//		txtCompSize.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
	}


	@Override
	public void onEnter() {


		scanProgress.setMaximum(100);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
