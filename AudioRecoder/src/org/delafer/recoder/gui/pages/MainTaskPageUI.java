package org.delafer.recoder.gui.pages;

import org.delafer.recoder.config.Config;
import org.delafer.recoder.gui.pages.base.AbstractPageUI;
import org.delafer.recoder.helpers.VTStack;
import org.delafer.recoder.model.ProgressModel;
import org.delafer.recoder.test.TaskProgress;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

public class MainTaskPageUI extends AbstractPageUI {

	public ProgressBar ratioBar;


	public Text txtProcessedFiles;
	public Text txtRemainingFiles;
	public Text txtSizeOriginal;
	public Text txtSizeNew;


	public TaskProgress[] tasks;


	public Table table;


	public Text txtTimeElapsed;


	public Text txtTimeLeft;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public MainTaskPageUI(Composite parent, int style) {
		super(parent, style);
		parent.setSize(700, 500);
		this.setSize(680, 395);
		setLayout(new GridLayout(1, false));

		createCompGroup();
		createProgressTaskGroup();
		createConsoleGroup();

	}

	private void createConsoleGroup() {
			 table = new Table(this, SWT.BORDER | SWT.VIRTUAL);
			 table.setFont(SWTResourceManager.getFont("Lucida Console", 8, SWT.NORMAL));
			 table.setForeground(SWTResourceManager.getColor(127, 255, 0));
			 table.setBackground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
			 table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
			 table.addListener(SWT.SetData, new Listener() {
			    public void handleEvent(Event event) {
			       TableItem item = (TableItem)event.item;
			       String txt = ProgressModel.console.get(event.index);
			       item.setText(txt != null ? txt : "");
			    }
			 });
			 table.setItemCount(VTStack.LOG_LINES);

	}

	private void createProgressTaskGroup() {
		final ScrolledComposite sc = new ScrolledComposite(this, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		sc.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
//		sc.setBackground(SWTResourceManager.getColor(SWT.COLOR_DARK_MAGENTA));

		final Composite parent = new Composite(sc, SWT.NONE) {

			@Override
			public Point computeSize(int wHint, int hHint, boolean changed) {
				Point p =  super.computeSize(wHint, hHint, changed);
				p.y = p.y-15;
				return p;
			}

		};
		GridLayout gl = new GridLayout(1, false);
		gl.verticalSpacing = 0;
		parent.setLayout(gl);
//		parent.setBackground(SWTResourceManager.getColor(SWT.COLOR_DARK_MAGENTA));
		tasks = new TaskProgress[5];
		for (int i = 0; i < Config.instance().threads; i++) {
			tasks[i] = new TaskProgress(parent, SWT.NONE);
			tasks[i].setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
//			tasks[i].setBackground(SWTResourceManager.getColor(SWT.COLOR_CYAN));
		}
		 sc.setMinSize(520, 0);
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
	}

	private void createCompGroup() {
		Group grpMemory = new Group(this, SWT.NONE);
		grpMemory.setLayout(new GridLayout(1, false));
		grpMemory.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		grpMemory.setText("Progress");

		ratioBar = new ProgressBar(grpMemory, SWT.NONE);
		ratioBar.setMaximum(100);
		ratioBar.setSelection(0);
		ratioBar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Composite composite = new Composite(grpMemory, SWT.NONE);
		composite.setLayout(new GridLayout(4, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));

		txtProcessedFiles = createTextLabel(composite, "Processed");
		txtRemainingFiles = createTextLabel(composite, "Remaining");
		txtSizeOriginal = createTextLabel(composite, "Original (size)");
		txtSizeNew = createTextLabel(composite, "Repacked (size)");

		txtTimeElapsed = createTextLabel(composite, "Elapsed:");
		txtTimeLeft = createTextLabel(composite, "Time left:");

	}


	public static Text createTextLabel(Composite composite, String label) {
		Label lblNewLabel = new Label(composite, SWT.NONE);
		lblNewLabel.setText(label);

		Text text = new Text(composite, SWT.BORDER);
		text.setEditable(false);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		return text;
	}


	@Override
	public void onEnter() {


		ratioBar.setMaximum(100);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
