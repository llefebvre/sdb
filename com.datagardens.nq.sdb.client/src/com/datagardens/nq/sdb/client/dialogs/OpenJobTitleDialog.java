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

import com.datagardens.nq.sdb.client.ImageKey;
import com.datagardens.nq.sdb.client.SWTResourceManager;

public class OpenJobTitleDialog 
extends TitleAreaDialog {

	private String jobId;
	private Text idText;
	private Button okButton;
	
	public OpenJobTitleDialog(Shell parentShell) {
		super(parentShell);
		setTitleImage(SWTResourceManager.getImage(ImageKey.TITLE_JOB));
	}
	
	@Override
	protected Control createDialogArea(Composite parent) 
	{
		setTitle("Open Job");
		setMessage("Open, review and edit an existing job from the M2M database.");
		
		Composite area = (Composite) super.createDialogArea(parent);
		
		Composite controls = new Composite(area, SWT.NONE);
		controls.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		GridLayout layout = new GridLayout(1, false);
		layout.marginHeight = 10;
		layout.marginWidth = 10;
		layout.verticalSpacing = 2;
		controls.setLayout(layout);
		
		new Label(controls, SWT.NONE).setText("Enter the job ID:");
		
		idText = new Text(controls, SWT.BORDER);
		idText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		
		
		idText.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				jobId = idText.getText();
				okButton.setEnabled(!idText.getText().isEmpty());
			}
		});
		
		idText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		
		
		
		return area;
	}
	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		okButton = createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		okButton.setEnabled(false);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}
	
	public String getJobId() {
		return jobId;
	}
	
	@Override
	protected Point getInitialSize() 
	{
		return new Point(300, 210);
	}
}
