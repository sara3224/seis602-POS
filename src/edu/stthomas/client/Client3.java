package edu.stthomas.client;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;
import java.io.*;
import java.util.HashMap;
import org.eclipse.swt.graphics.Point;
//import org.eclipse.swt.graphics.Image;


public class Client3 {

	protected Shell shlPosLogIn;
	private Text UserTextField;
	private Text PasswordTextField;
	
	private static String userName = "";
	private static String password ="";
	private static HashMap<String, String> users = new HashMap<>();

	/**
	 * Launch the application.
	 * @param args
	 */
	public void login()
	{
		
		try {
			fileInput();

			this.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void fileInput() throws IOException {
		
		
		FileInputStream in = null;
		
		try {
			int count = 0;
			int nLCount = 0;
			
			in = new FileInputStream("UserNames.txt");
			
			int c;
			while ((c = in.read()) != -1) {
				if(nLCount ==1)
				{
					users.put(userName, password);
					userName = "";
					password = "";
					count = 0;
					nLCount = 0;
				}
				if (c == ' ') {
					count ++;
					continue;
				}
				if(c == '\n') {
					nLCount ++;
					continue;
				}
				
				if(count == 1)
					password += (char)c;
				else
					userName += (char)c;
			}
			users.put(userName, password);

			
		} finally {
			if (in != null) {
				in.close();
			}
		}
		
	
	}
	
	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
//		shlPosLogIn.pack();
		shlPosLogIn.open();
		shlPosLogIn.layout();
		while (!shlPosLogIn.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shlPosLogIn = new Shell();
		shlPosLogIn.setMinimumSize(new Point(500, 500));
		shlPosLogIn.setBackground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
		shlPosLogIn.setSize(1500, 1000);
		shlPosLogIn.setText("POS Login");
		
		UserTextField = new Text(shlPosLogIn, SWT.BORDER | SWT.CENTER);
		UserTextField.setText("User Name");
		UserTextField.setBounds(774, 406, 152, 19);
		
		PasswordTextField = new Text(shlPosLogIn,SWT.PASSWORD | SWT.BORDER |SWT.CENTER);
		PasswordTextField.setText("Password");
		PasswordTextField.setBounds(774, 469, 152, 19);
		
		Label lblNewLabel = new Label(shlPosLogIn, SWT.NONE);
		lblNewLabel.setBounds(673, 409, 59, 14);
		lblNewLabel.setText("UserName");
		
		Button btnNewButton = new Button(shlPosLogIn, SWT.NONE);
		btnNewButton.setBounds(774, 520, 94, 27);
		btnNewButton.setText("Login");
		
		Label lblNewLabel_1 = new Label(shlPosLogIn, SWT.NONE);
		lblNewLabel_1.setBounds(673, 472, 59, 14);
		lblNewLabel_1.setText("Password");
		
		CLabel lblNewLabel_2 = new CLabel(shlPosLogIn, SWT.BORDER |SWT.RESIZE);
		lblNewLabel_2.setBackground(SWTResourceManager.getImage(Client3.class,"./MPops.png"));
		lblNewLabel_2.setBounds(673, 91, 250, 250);
	//	lblNewLabel_2.setText("Mom and Pops Grocery Store");
		
		btnNewButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				String userName = UserTextField.getText();
				String password = PasswordTextField.getText();
				String errorMsg = null;
				MessageBox messageBox = new MessageBox(shlPosLogIn, SWT.OK | SWT.ICON_ERROR);
	//			Text text;
				
				if (userName == null  || userName.isEmpty() || password == null || password.isEmpty()) {
										
					messageBox.setText("Alert");
					if (userName == null || userName.isEmpty()) {
						errorMsg = "Please enter Username";
					}
					else if (password == null || password.isEmpty()) {
						errorMsg = "Please enter Password";
					}
					if (errorMsg != null) {
						messageBox.setMessage(errorMsg);
						messageBox.open();
					}
				}
				
				else if(users.get(userName) == null) {
				
					messageBox.setText("Alert");
					errorMsg = "Incorrect Username";
					
					if (errorMsg != null) {
						messageBox.setMessage(errorMsg);
						messageBox.open();
					}
				}
				
				else if(password.compareTo(users.get(userName)) != 0) {
					
					messageBox.setText("Alert");
					errorMsg = "Incorrect Password";
	//				System.out.println(Integer.valueOf(password) == Integer.valueOf(users.get(userName)));
//					System.out.println(password.length());
//					System.out.println(users.get(userName).length());
//					System.out.println(password.compareTo(users.get(userName)));
					
					if (errorMsg != null) {
						messageBox.setMessage(errorMsg);
						messageBox.open();
					}
				}
				else {
					messageBox.setText("Info");
					messageBox.setMessage("Login Successful\n Welcome User");
					messageBox.open();
					shlPosLogIn.close();
					POSWindow pos = new POSWindow();
					//TODO uncomment to run Main
					pos.openWindow();
					
//					System.out.println("Reached");
					}
				//TODO add POS Window
			}
		});
		
	}
}
					
							
				
			
		

	


