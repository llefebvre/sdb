package com.datagardens.nq.sdb.server;

import java.io.IOException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

public class ControlView extends ViewPart {

	public static final String ID = "com.datagardens.nq.sdb.server.views.control";
	
	private Button startButton;
	private Button stopButton;
	private Label label;
	
	private Server server;
	
	public ControlView() 
	{
		
	}

	@Override
	public void createPartControl(Composite parent) 
	{
		parent.setLayout(new GridLayout(2, true));
		startButton = new Button(parent, SWT.PUSH);
		startButton.setText("  Start  ");
		startButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				server = new Server();
				
				try 
				{
					if(server.start(false))
					{
						updateLabel("System ready listening port " + Server.PORT);
					}
				}
				catch (IOException ioe) 
				{
					ioe.printStackTrace();
				}
			}
		});
		
		stopButton = new Button(parent, SWT.PUSH);
		stopButton.setText("  Stop  ");
		stopButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(server.stop())
				{
					updateLabel("Server has been stopped");
				}
			}
		});
		
		label = new Label(parent, SWT.NONE);
		label.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2,1));
			
	}
	
	private void updateLabel(final String string) 
	{
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
			
			@Override
			public void run() {
				label.setText(string);
			}
		});
	}

	@Override
	public void setFocus() {
	}
}
