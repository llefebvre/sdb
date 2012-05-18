package com.datagardens.nq.sdb.client.dialogs;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class AddUserDialog extends TitleAreaDialog {

	private String username;
	private String password;
	
	private Text userText;
	private Text passwordText;
	private Text confirmPasswordText;
	
	private Button okButton;
	
	public AddUserDialog(Shell parentShell) {
		super(parentShell);
//		setTitleImage(SWTResourceManager.getImage(ImageKey.TITLE_JOB));
	}
	
	@Override
	protected Control createDialogArea(Composite parent) 
	{
		setTitle("Add user");
		setMessage("New user information");
		
		Composite area = (Composite) super.createDialogArea(parent);
		
		Composite controls = new Composite(area, SWT.NONE);
		GridLayout controlsLayout = new GridLayout(2, false);
		controlsLayout.marginHeight = 15;
		controlsLayout.marginWidth = 10;
		controlsLayout.verticalSpacing = 4;
		controls.setLayout(controlsLayout);
		controls.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		
		new Label(controls, SWT.NONE).setText("Username:");
		userText = new Text(controls, SWT.BORDER);
		userText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		
		new Label(controls, SWT.NONE).setText("Password: ");
		passwordText = new Text(controls, SWT.BORDER | SWT.PASSWORD);
		passwordText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		
		new Label(controls, SWT.NONE).setText("Confirm Password: ");
		confirmPasswordText = new Text(controls, SWT.BORDER | SWT.PASSWORD);
		confirmPasswordText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		
		setupEventHandlers();
		
		return area;
	}
	
	private boolean allInputValid()
	{
		String errorMessage = getErrorMessage();
		setErrorMessage(errorMessage);
		return errorMessage == null;
	}
	@Override
	public String getErrorMessage() 
	{
		if(userText.getText().isEmpty())
		{
			return "Username connot be empty";
		}
		
		if(passwordText.getText().isEmpty())
		{
			return "Password cannot be empty";
		}
		
		if(!passwordText.getText().equals(confirmPasswordText.getText()))
		{
			return "Failed to confirm password";
		}
		
		/*if(WidgetUtilities.getSingleSelectionFromViewer(roleViewer) == null)
		{
			return "You have to select a role for the user, If you dont want at assign a Role at this moment, select \"Not Assigned\".";
		}
		*/
		
		return null;
	}

	private void setDialogComplete(boolean complete)
	{
		okButton.setEnabled(complete);
	}
	
	private void setupEventHandlers() 
	{
		userText.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) 
			{
				username = userText.getText();
				setDialogComplete(allInputValid());
			}
		});
		
		passwordText.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				password = passwordText.getText();
				setDialogComplete(allInputValid());
			}
		});
		
		confirmPasswordText.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				setDialogComplete(allInputValid());
			}
		});
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		okButton = createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, false);
		okButton.setEnabled(false);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, true);
	}
	
	@Override
	protected Point getInitialSize() {
		return new Point(300, 240);
	}

	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
}
