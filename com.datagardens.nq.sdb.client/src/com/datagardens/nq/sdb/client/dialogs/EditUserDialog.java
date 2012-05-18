package com.datagardens.nq.sdb.client.dialogs;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.datagardens.nq.sdb.client.views.WidgetUtilities;
import com.datagardens.nq.sdb.client.views.adapters.ModelContentProvider;
import com.datagardens.nq.sdb.client.views.adapters.ModelLabelProvider;
import com.datagardens.nq.sdb.commons.model.nio.rbac.SDBUser;
import com.datagardens.nq.sdb.commons.model.nio.rbac.SDBUser.SDBRole;

public class EditUserDialog extends TitleAreaDialog {

	private Text userText;
	private Text passwordText;
	private Text confirmPasswordText;
	private ComboViewer roleViewer;
	private Button okButton;
	
	private SDBUser user;
	
	public EditUserDialog(Shell parentShell, SDBUser user)
	{
		super(parentShell);
		this.user = new SDBUser(user.getUsername(), user.getPassword());
		this.user.setRole(user.getRole());
	}
	
	@Override
	protected Control createDialogArea(Composite parent) 
	{
		setTitle("Edit user");
		setMessage("Select the user name, password and role for the user.");
		
		Composite area = (Composite) super.createDialogArea(parent);
		
		Composite controls = new Composite(area, SWT.NONE);
		controls.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		FillLayout controlsLayout = new FillLayout(SWT.VERTICAL);
		controlsLayout.marginHeight = 10;
		controlsLayout.marginWidth= 10;
		controlsLayout.spacing = 8;
		controls.setLayout(controlsLayout);
		
		GridLayout grpsLayout = new GridLayout(2, false);
		grpsLayout.marginWidth = 5;
		grpsLayout.marginHeight = 5;
		
		GridData grpsCtlsData = new GridData(SWT.FILL, SWT.FILL, true, false);
		
		Group usernameGrp = new Group(controls, SWT.NONE);
		usernameGrp.setText("Username");
		usernameGrp.setLayout(grpsLayout);
		new Label(usernameGrp, SWT.NONE).setText("Username:");
		userText = new Text(usernameGrp, SWT.BORDER);
		userText.setLayoutData(grpsCtlsData);
		
		Group passwordGrp = new Group(controls, SWT.NONE);
		passwordGrp.setText("Password");
		passwordGrp.setLayout(grpsLayout);
		new Label(passwordGrp, SWT.NONE).setText("Password:");
		passwordText = new Text(passwordGrp, SWT.BORDER | SWT.PASSWORD);
		passwordText.setLayoutData(grpsCtlsData);
		new Label(passwordGrp, SWT.NONE).setText("Confirm Password:");
		confirmPasswordText= new Text(passwordGrp, SWT.BORDER | SWT.PASSWORD);
		confirmPasswordText.setLayoutData(grpsCtlsData);
		
		Group roleGrp = new Group(controls, SWT.NONE);
		roleGrp.setText("Role");
		roleGrp.setLayout(grpsLayout);
		new Label(roleGrp, SWT.NONE).setText("Role:");
		roleViewer = new ComboViewer(new Combo(roleGrp, SWT.BORDER | SWT.READ_ONLY));
		roleViewer.getCombo().setLayoutData(grpsCtlsData);
		
		initiateDialog();
		setupEventsHandlers();
		
		return area;
	}

	private void initiateDialog()
	{
		userText.setText(user.getUsername());
		
		roleViewer.setContentProvider(new ModelContentProvider());
		roleViewer.setLabelProvider(new ModelLabelProvider());
		roleViewer.setInput(SDBRole.values());
		
		WidgetUtilities.selectCComboItemWithLabel(roleViewer.getCombo(), user.getRole().getId());
	}

	private void setupEventsHandlers()
	{
		userText.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) 
			{
				user.setUsername(userText.getText());
				setDialogComplete(allInputValid());
			}
		});
		
		passwordText.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) 
			{
				user.setPassword(passwordText.getText());
				setDialogComplete(allInputValid());
			}
		});
		
		confirmPasswordText.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) 
			{
				user.setPassword(confirmPasswordText.getText());
				setDialogComplete(allInputValid());
			}
		});
		
		roleViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				
				SDBRole selectedRole = (SDBRole) WidgetUtilities.getSingleSelectionFromViewer(roleViewer);
				
				if(selectedRole != null)
				{
					user.setRole(selectedRole);
				}
				
				setDialogComplete(allInputValid());
			}
		});
	}
	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		okButton = createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, false);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, true);
		setDialogComplete(false);
	}
	
	private void setDialogComplete(boolean complete)
	{
		okButton.setEnabled(complete);
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
		
		if(WidgetUtilities.getSingleSelectionFromViewer(roleViewer) == null)
		{
			return "You have to select a role for the user, If you dont want at assign a Role at this moment, select \"Not Assigned\".";
		}
		
		return null;
	}
	
	public SDBUser getUser() {
		return user;
	}
}
