package org.delafer.recoder.test;

import org.delafer.recoder.model.ProgressModel.State;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

public class TaskProgress extends Composite {

	private static final String[] tasks = {"Decoding","Processing","Encoding","Checking"};

	private CLabel[] elements;
	private State[] states;

	private static Color[] colors = {
		SWTResourceManager.getColor(SWT.COLOR_RED),
		SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND),
		SWTResourceManager.getColor(SWT.COLOR_GREEN),
		SWTResourceManager.getColor(100, 149, 237)
		};

	private Text txt;

	private Color getColor(State state) {

		if (state==null) state = State.New;

		switch (state) {
		case Fail:
			return colors[0];
		case Success:
			return colors[2];
		case InProgress:
			return colors[3];
		case New:
		default:
			return colors[1];
		}
	}

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public TaskProgress(Composite parent, int style) {
		super(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout(9, true);
		gridLayout.verticalSpacing = 3;
		gridLayout.marginHeight = 2;
		setLayout(gridLayout);
//		this.setBackground(SWTResourceManager.getColor(SWT.COLOR_DARK_MAGENTA));
		states = new State[tasks.length];

		elements = new CLabel[tasks.length];
		for (int i = 0; i < tasks.length; i++) {
			elements[i] = createTaskBtn(tasks[i]);
			setState(i, State.New);
		}
		txt = new Text(this, SWT.BORDER | SWT.NONE);
		txt.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 5, 1));
		txt.setSize(200, 24);
		txt.setEditable(false);
	}

	public void setState(int btnNum, State state) {
		states[btnNum] = state;
		updateUI(btnNum);
	}

	private void updateUI(int btnNum) {
		elements[btnNum].setBackground(getColor(states[btnNum]));
	}

	private CLabel createTaskBtn(String name) {
//		Composite cmpTask1 = new Composite(this, SWT.BORDER | SWT.EMBEDDED);
//		cmpTask1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
//		GridLayout glTask1 = new GridLayout(1, false);
//		glTask1.horizontalSpacing = 2;
//		glTask1.verticalSpacing = 2;
//		glTask1.marginWidth = 0;
//		glTask1.marginHeight = 0;
//		cmpTask1.setLayout(glTask1);

		CLabel task1 = new CLabel(this, SWT.BORDER | SWT.SHADOW_IN | SWT.CENTER);
		task1.setMargins(2, 5, 3, 5);
		GridData gd = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd.verticalSpan = 0;
		task1.setLayoutData(gd);
		task1.setFont(SWTResourceManager.getFont("Microsoft Sans Serif", 8, SWT.BOLD));
		task1.setText(name);
		return task1;
	}

	public void updateName(String name) {
		this.txt.setText(name != null ? name : "");
	}

	public void update(State[] statesNew) {
		for (int i = 0; i < statesNew.length; i++) {
			if (states[i] != statesNew[i]) {
				setState(i, statesNew[i]);
			}
		}
	}

}
