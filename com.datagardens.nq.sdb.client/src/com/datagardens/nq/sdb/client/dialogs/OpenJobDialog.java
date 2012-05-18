package com.datagardens.nq.sdb.client.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class OpenJobDialog extends Dialog {

	private String jobId;
	
	private Text idText;
	private Button okButton;
	
	public OpenJobDialog(Shell parentShell) {
		super(parentShell);
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		
		Composite area = (Composite) super.createDialogArea(parent);
		GridLayout layout = new GridLayout(2, false);
		
		layout.marginWidth  = 10;
		layout.marginHeight = 10;
		
		area.setLayout(layout);
		
		Label inst = new Label(area, SWT.NONE);
		inst.setText("Enter the ID of the Job that you want to open");
		inst.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
		
		
		Label label = new Label(area, SWT.NONE);
		label.setText("Job ID: ");
		
		idText = new Text(area, SWT.BORDER);
		idText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		
		
		idText.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				jobId = idText.getText();
				okButton.setEnabled(!idText.getText().isEmpty());
			}
		});
		
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
}
