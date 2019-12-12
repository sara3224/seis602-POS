package edu.stthomas.client;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.custom.SashForm;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.*;
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
import edu.stthomas.exceptions.POSException;
import edu.stthomas.model.Item;
import edu.stthomas.model.SalesLineItem;
import edu.stthomas.model.SalesTransaction;
import edu.stthomas.service.PointOfSale;
import edu.stthomas.service.User;
import org.eclipse.swt.widgets.Label;

public class POSWindow {

	protected Shell shell;
	String user; //TODO level 1 can only do 11 i.e POS and returns. rest all i.e add inventory, remove and report level 2
    int registerId;
    String shift; //TODO: Change to String
    static ArrayList<Item> itemInv = new ArrayList<>();
	PointOfSale pos = null;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main (String[] args)
	
//	public void openWindow()
	{
		try {
			fileInput();
			POSWindow window = new POSWindow();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void openWindow()
	{
		try {
			fileInput();
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
	
	public static void fileInput() throws IOException {
		
		
		BufferedReader in = null;
		
		try {

			in = new BufferedReader(new InputStreamReader(new FileInputStream("data/inventory.tsv"), "UTF-8"));
			
			String str = in.readLine();

			while ((str = in.readLine()) != null) {
				String[] itemAtt = str.split("\t");
				Item item = new Item(Integer.parseInt(itemAtt[0]), itemAtt[1], Integer.parseInt(itemAtt[2]), 
						Double.parseDouble(itemAtt[3]), Double.parseDouble(itemAtt[4]), Integer.parseInt(itemAtt[5]), 
						Integer.parseInt(itemAtt[6]), Integer.parseInt(itemAtt[7]), Integer.parseInt(itemAtt[8]));
				itemInv.add(item);
				
			}
			System.out.println(itemInv);

			
		} finally {
			if (in != null) {
				in.close();
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
		combo.setItems(new String[] {"DAY", "NIGHT"});
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
				registerId = combo_2.getSelectionIndex() + 1;
//				System.out.println(e);
//				System.out.println(combo_2.getText());
			}
		});
		combo_2.setItems(new String[] {"Register 1", "Register 2", "Register 3"});
		combo_2.setBounds(700, 10, 100, 22);
		combo_2.setText("Register");
		
		
		
		List list = new List(composite_1, SWT.BORDER);
		list.setBackground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
		list.setBounds(100, 80, 700, 406);
		
		Composite composite_2 = new Composite(sashForm, SWT.NONE);
		sashForm.setWeights(new int[] {1, 1});	
		Button SubmitBtn= new Button(composite_1, SWT.NONE);
		SubmitBtn.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
                pos = new PointOfSale(user, Shift.valueOf(shift), registerId);
                System.out.println(pos.getItemsAndQuantity());
				generateBtn(composite_2, list, pos);

			}
		});
		SubmitBtn.setBounds(700, 47, 94, 27);
		SubmitBtn.setText("Submit");
		
		
		
		List list_1 = new List(composite_1, SWT.BORDER);
		list_1.setBounds(100, 527, 700, 149);
		
		Button SalesBtn = new Button(composite_1, SWT.NONE);
		SalesBtn.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				try {
					salesReport(pos.complete(), list_1);
				} catch (POSException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		SalesBtn.setBounds(600, 700, 109, 27);
		SalesBtn.setText("Finalize Sale");
		
		Button ReturnBtn = new Button(composite_1, SWT.NONE);
		ReturnBtn.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				System.out.println(list.getSelectionIndex());
				list.remove(list.getSelectionIndex(), list.getSelectionIndex());
//				try {
//					salesReport(pos.complete(), list_1);
//				} catch (POSException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
			}
		});
		ReturnBtn.setBounds(400, 700, 109, 27);
		ReturnBtn.setText("Return");
		
		Label lblNewLabel = new Label(composite_1, SWT.NONE);
		lblNewLabel.setAlignment(SWT.CENTER);
		lblNewLabel.setBounds(388, 492, 128, 22);
		lblNewLabel.setText("Sales Report");
		
	}
	
	public void salesReport(SalesTransaction salesRecord, List list_1) {
        String str = "sales id: "+salesRecord.getId() + " cashier id: " +salesRecord.getCashier().getId()+ " shift: "+salesRecord.getShift()+" level: "+salesRecord.getCashier().getLevel()+ " Register: "
                +salesRecord.getRegister().getRegisterId();
        String str_2 = "sales amt: " + salesRecord.getTotalAmtBeforeTax() + " sales tax: " +salesRecord.getTotalTaxAmt()
                +" total amt: " +salesRecord.getTotalAmt() +" sales time: " +salesRecord.getTransactionTime();
        list_1.add(str);
        list_1.add(str_2);
        java.util.List<SalesLineItem> salesLineItems = salesRecord.getSalesLineItems();
        for(SalesLineItem lineItem: salesLineItems) {
            //4.	Registers will record the register number, the user (cashier), the dates and times of sale, sale items, and the amount of sales.
            String str_1 = "item id:"+lineItem.getItemId()+" quantity:"+lineItem.getQuantity() + " price: "+lineItem.getPrice()
                    +" tax:" +lineItem.getTax() +" sale amt: "+lineItem.getLineItemAmtBeforeTax() +" sale tax: "+lineItem.getLineItemTax()
                    +" total amt: "+lineItem.getLineItemAmt();
            list_1.add(str_1);
        }
       
    }
	public void generateBtn(Composite composite_2, List list, PointOfSale pos)
	{
		int maxCol = 5;
		int maxRow = 8;
		int size = itemInv.size();
			
		int rows = (itemInv.size() + maxCol -1) / maxCol;


		
		
		for(int j = 0; j < rows; j++) 
		{
			int cols = maxCol;
			if(size > maxCol)
			{
				size -= maxCol;
				
			}
			else
				cols = size;

			for(int i=0; i <cols; i++)
			{
				Item itm = itemInv.get(((j * maxCol) + (i)));
				Button btn = new Button(composite_2, SWT.NONE);
				btn.setBounds((104 * i) + 10, (104*j) + 10, 94, 94);
				btn.setText(itm.getName());
//				btn.setText("item #" + ((j * 5) + (i + 1)));
				btn.addListener(SWT.Selection, new Listener() {
					public void handleEvent(Event e) {
						list.add("\tAdded: " + itm.getName() + "\tQuantity: 1\t\t\t\tPrice: " + itm.getPrice());
						pos.addItem(itm.getItemId(), 1);
		                System.out.println(pos.getItemsAndQuantity());

					}
				});
			}
		}
	}
}
