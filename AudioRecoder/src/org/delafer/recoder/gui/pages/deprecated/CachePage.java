package org.delafer.recoder.gui.pages.deprecated;

import org.delafer.recoder.config.Config;
import org.delafer.recoder.gui.pages.base.AbstractPageUI;
import org.delafer.recoder.helpers.Convertors;
import org.delafer.recoder.helpers.utils.UtilsCommon;
import org.delafer.recoder.model.ProgressModel;
import org.delafer.recoder.test.FindIt;
import org.delafer.recoder.test.FindIt.ThreadRatio;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.ModifyEvent;

public class CachePage extends AbstractPageUI {
	private static final String CACHE_HITS_UND_MISSES_RATIO = "Cache efficiency / Hits und Misses Ratio";
	private Text txtCacheEntries;
	private Text text_1;
	protected Scale scale;
	protected Label lblHitsMisses;
	protected Text txtHits;
	protected Text txtMisses;
	private Button btnCacheOn;


	public void updateScaleRate()  {
		int sel = scale.getSelection();

		StringBuilder sb = new StringBuilder(36);
		sb.append(CACHE_HITS_UND_MISSES_RATIO);
		sb.append(' ');
		sb.append(scale.getSelection());
		sb.append(" %");
		lblHitsMisses.setText(sb.toString());

		ThreadRatio ratio = FindIt.findRatio(sel);
		txtHits.setText(String.valueOf(ratio.packers));
		txtMisses.setText(String.valueOf(ratio.depackers));


	}


	private static void setGridData(Text txt) {
		GridData newDataGrid = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
		newDataGrid.widthHint = 48;
		txt.setLayoutData(newDataGrid);
	}

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public CachePage(Composite parent, int style) {
		super(parent, style);
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.marginBottom = 30;
		gridLayout.marginTop = 30;
		gridLayout.marginRight = 15;
		gridLayout.marginLeft = 15;
		setLayout(gridLayout);

		btnCacheOn = new Button(this, SWT.CHECK);
		btnCacheOn.setSelection(false);
		btnCacheOn.setText("Emulate caching");

		lblHitsMisses = new Label(this, SWT.NONE);
		lblHitsMisses.setText(CACHE_HITS_UND_MISSES_RATIO);
		lblHitsMisses.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		scale = new Scale(this, SWT.NONE);
		scale.setMaximum(100);
		scale.setMinimum(0);
		scale.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				updateScaleRate();
			}
		});
		scale.setPageIncrement(1);
		scale.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));


		Composite ratioComp = new Composite(this, SWT.NONE);
		ratioComp.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		GridLayout gl_ratioComp = new GridLayout(2, false);
		gl_ratioComp.verticalSpacing = 2;
		gl_ratioComp.marginWidth = 0;
		gl_ratioComp.marginHeight = 0;
		gl_ratioComp.horizontalSpacing = 0;
		ratioComp.setLayout(gl_ratioComp);

		txtHits = new Text(ratioComp, SWT.BORDER);
		txtHits.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		txtHits.setEditable(false);
		setGridData(txtHits);
		Label lblThreads1 = new Label(ratioComp, SWT.NONE);
		lblThreads1.setText("  Cache hits");

		txtMisses = new Text(ratioComp, SWT.BORDER);
		txtMisses.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		txtMisses.setEditable(false);
		setGridData(txtMisses);
		Label lblThreads2 = new Label(ratioComp, SWT.NONE);
		lblThreads2.setText("  Cache misses");

		Composite composite = new Composite(this, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		composite.setLayout(new GridLayout(2, true));

		Label lblNewLabel = new Label(composite, SWT.NONE);
		lblNewLabel.setText("Cache Entries:");

		txtCacheEntries = new Text(composite, SWT.BORDER);
		txtCacheEntries.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent paramModifyEvent) {
				int val = UtilsCommon.textAsInt(txtCacheEntries.getText(), prevVal);
				text_1.setText(Convertors.autoSize(val * Config.instance().sizeCompressed));
				prevVal = val;
			}
		});
//		txtCacheEntries.setText("500");
		txtCacheEntries.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblAllocatedMemory = new Label(composite, SWT.NONE);
		lblAllocatedMemory.setText("Allocated Memory:");

		text_1 = new Text(composite, SWT.BORDER);
		text_1.setEditable(false);
		text_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

	}

	private transient static int prevVal = 0;

	@Override
	public void onEnter() {
		scale.setSelection(Config.instance().cacheRatio);
		btnCacheOn.setSelection(Config.instance().cacheOn);
		txtCacheEntries.setText(String.valueOf(Config.instance().cacheEntries));
		updateScaleRate();
	}

	@Override
	public void onExit() {
		Config.instance().cacheRatio = scale.getSelection();
		Config.instance().cacheOn = btnCacheOn.getSelection();
		Config.instance().cacheEntries = UtilsCommon.textAsInt(txtCacheEntries.getText(), 0);

//		ProgressModel.setCacheEfficiency(Config.instance().cacheRatio);
//		ProgressModel.setCacheOn(Config.instance().cacheOn);
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
