package edu.stthomas.client;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.List;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Event;

import edu.stthomas.enums.Shift;
import edu.stthomas.service.User;

public class POSWindow {

	protected Shell shell;
	String user; //TODO level 1 can only do 11 i.e POS and returns. rest all i.e add inventory, remove and report level 2
    String registerId;
    String shift; //TODO: Change to String

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			POSWindow window = new POSWindow();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
	

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setMinimumSize(new Point(1800, 950));
		shell.setSize(450, 300);
		shell.setText("SWT Application");
		shell.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Composite composite = new Composite(shell, SWT.NONE);
		composite.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		SashForm sashForm = new SashForm(composite, SWT.NONE);
		
		Composite composite_1 = new Composite(sashForm, SWT.NONE);
		
		Combo combo = new Combo(composite_1, SWT.NONE);
		combo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shift = combo.getText();
			}
		});
		combo.setItems(new String[] {"Day", "Night"});
		combo.setLocation(100, 10);
		combo.setSize(100, 22);
		combo.setText("Shift");
		
		Combo combo_1 = new Combo(composite_1, SWT.NONE);
		combo_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				user = combo_1.getText();
			}
		});
		
		User users = new User();
		String[] userName = new String[4];
		int counter = 0;
		for (String key : users.getUsers().keySet())
		{
			userName[counter] = key;
			counter++;
		}
		combo_1.setItems(userName);
		combo_1.setBounds(400, 10, 100, 22);
		combo_1.setText("Cashier");
		
		
		Combo combo_2 = new Combo(composite_1, SWT.NONE);
		combo_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				registerId = combo_2.getText();
//				System.out.println(e);
//				System.out.println(combo_2.getText());
			}
		});
		combo_2.setItems(new String[] {"Register 1", "Register 2", "Register 3"});
		combo_2.setBounds(700, 10, 100, 22);
		combo_2.setText("Register");
		
		
		List list = new List(composite_1, SWT.BORDER);
		list.setBackground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
		list.setBounds(100, 80, 700, 564);
		
		Composite composite_2 = new Composite(sashForm, SWT.NONE);
		sashForm.setWeights(new int[] {1, 1});
		
		for(int j = 0; j < 5; j++) 
		{
			for(int i=0; i <8; i++)
			{
				Button btn = new Button(composite_2, SWT.NONE);
				btn.setBounds((104 * i) + 10, (37*j) + 10, 94, 27);
				btn.setText("item #" + ((j * 8) + (i + 1)));
				btn.addListener(SWT.Selection, new Listener() {
					public void handleEvent(Event e) {
						System.out.println(btn.getText());
						
					}
				});
			}
		}
		
		
//		Button btnNewButton_1 = new Button(composite_2, SWT.NONE);
//		btnNewButton_1.setBounds(121, 10, 94, 27);
//		btnNewButton_1.setText("New Button");
//		sashForm.setWeights(new int[] {1, 1});

	}
}
