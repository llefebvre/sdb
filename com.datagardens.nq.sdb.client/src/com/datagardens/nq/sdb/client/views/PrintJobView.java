package com.datagardens.nq.sdb.client.views;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.MediaPrintableArea;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;

import com.datagardens.nq.sdb.client.ImageKey;
import com.datagardens.nq.sdb.client.SWTResourceManager;
import com.datagardens.nq.sdb.client.SheetPrinter;
import com.datagardens.nq.sdb.client.views.nav.ModelNavigationAction;
import com.datagardens.nq.sdb.client.views.nav.ModelNavigationObject.NavigationCategory;
import com.datagardens.nq.sdb.commons.model.IndividualCut;
import com.datagardens.nq.sdb.commons.model.Job;
import com.datagardens.nq.sdb.commons.model.Operation;
import com.datagardens.nq.sdb.commons.model.SawSheet;
import com.datagardens.nq.sdb.commons.model.ShiftLogSheet;

public class PrintJobView 
extends Composite
implements ICustomComposite, ISelectionListener
{
	
	private Composite printComposite;
	
	private Job selectedJob;
	
	private Button printAllPacketBtn;
	private Button selectIndividualPagesBtn;
	private Button snAdjusmentBtn;
	
	private Spinner individualCutSheets;
	private Spinner multimeCutSheets;
	
	private Spinner operation20Sheets;
	private Spinner operation30Sheets;
	private Spinner operation40Sheets;
	private Spinner operation50Sheets;
	private Spinner operation60Sheets;
	private Spinner operation70Sheets;
	private Spinner operation80Sheets;
	private Spinner operation90Sheets;
	
	private Button cancelBtn;
	private Button printPreviewBtn;
	private Button printBtn;
	
	private IWorkbenchWindow window;
	
	
	public PrintJobView(Composite parent, int style, IWorkbenchWindow window) 
	{
		super(parent, style);
		window.getSelectionService().addSelectionListener(this);
		createContents();
	}
	
	@Override
	public void createContents() 
	{
		setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		
		GridLayout layout = new GridLayout();
		layout.marginHeight = 10;
		layout.marginWidth = 10;
		layout.verticalSpacing = 10;
		setLayout(layout);
		
		CLabel label = new CLabel(this, SWT.WRAP);
		label.setText("This command will print the pages requiered to log all the activites for the job.\nPlease hand these pages to the machine operators.");
		label.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		label.setBackground(this.getBackground());
		label.setImage(SWTResourceManager.getImage(ImageKey.ICON_32_PRODUCTION_PRINT));
		
		printComposite = new Composite(this, SWT.BORDER);
		printComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		fillPrintComposite();
	}

	private void fillPrintComposite() 
	{
		printComposite.setLayout(new GridLayout(1, false));
		
		Group packetGrp = new Group(printComposite, SWT.NONE);
		packetGrp.setLayout(new GridLayout(1, false));
		packetGrp.setText("Print");
		packetGrp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		printAllPacketBtn = new Button(packetGrp, SWT.RADIO);
		printAllPacketBtn.setText("Select what sheets to print");
		printAllPacketBtn.setSelection(true);
		selectIndividualPagesBtn =  new Button(packetGrp, SWT.RADIO);
		selectIndividualPagesBtn.setText("Print the entire job packet");
		selectIndividualPagesBtn.setEnabled(false);
		snAdjusmentBtn = new Button(packetGrp, SWT.RADIO);
		snAdjusmentBtn.setText("Print S/N Adjusment sheets");
		snAdjusmentBtn.setEnabled(false);
		
		SelectionAdapter listener = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				evaluateEnabled();
			}
		};
		
		printAllPacketBtn.addSelectionListener(listener);
		selectIndividualPagesBtn.addSelectionListener(listener);
		snAdjusmentBtn.addSelectionListener(listener);
		
		Group sawSheetGrp = new Group(printComposite, SWT.NONE);
		sawSheetGrp.setText("Saw Sheets");
		sawSheetGrp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		sawSheetGrp.setLayout(new GridLayout(2, false));
		new Label(sawSheetGrp, SWT.NONE).setText("Individual Cut:");
		individualCutSheets = new Spinner(sawSheetGrp, SWT.BORDER);
		new Label(sawSheetGrp, SWT.NONE).setText("Multiple Cut:");
		multimeCutSheets = new Spinner(sawSheetGrp, SWT.BORDER);
		
		
		Group shiftLogGrp = new Group(printComposite, SWT.NONE);
		shiftLogGrp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		shiftLogGrp.setText("Shit Log Sheets");
		GridLayout shetLogLayout = new GridLayout(5, true);
		shetLogLayout.horizontalSpacing = 10;
		shetLogLayout.verticalSpacing = 3;
		shiftLogGrp.setLayout(shetLogLayout);
		
		Label opt1Label = new Label(shiftLogGrp, SWT.NONE);
		opt1Label.setText("Operation");
		opt1Label.setFont(SWTResourceManager.getBoldFont(getFont()));
		
		Label cps1 = new Label(shiftLogGrp, SWT.NONE);
		cps1.setText("Copies");
		cps1.setFont(SWTResourceManager.getBoldFont(getFont()));
		
		new Label(shiftLogGrp, SWT.NONE);
		
		Label opt2Label = new Label(shiftLogGrp, SWT.NONE);
		opt2Label.setText("Operation");
		opt2Label.setFont(SWTResourceManager.getBoldFont(getFont()));
		
		Label cps2 = new Label(shiftLogGrp, SWT.NONE);
		cps2.setText("Copies");
		cps2.setFont(SWTResourceManager.getBoldFont(getFont()));
		
		GridData labelsData = new GridData(SWT.CENTER, SWT.FILL, false , false);
		
		Label lab1 = new Label(shiftLogGrp, SWT.NONE);
		lab1.setText("20");lab1.setLayoutData(labelsData);
		operation20Sheets = new Spinner(shiftLogGrp, SWT.BORDER);
		new Label(shiftLogGrp, SWT.NONE);
		Label lab2 = new Label(shiftLogGrp, SWT.NONE);
		lab2.setText("60");lab2.setLayoutData(labelsData);
		operation60Sheets = new Spinner(shiftLogGrp, SWT.BORDER);
		
		Label lab3 = new Label(shiftLogGrp, SWT.NONE);
		lab3.setText("30");lab3.setLayoutData(labelsData);
		operation30Sheets = new Spinner(shiftLogGrp, SWT.BORDER);
		new Label(shiftLogGrp, SWT.NONE);
		Label lab4 = new Label(shiftLogGrp, SWT.NONE);
		lab4.setText("70");lab4.setLayoutData(labelsData);
		operation70Sheets = new Spinner(shiftLogGrp, SWT.BORDER);
		
		Label lab5 = new Label(shiftLogGrp, SWT.NONE);
		lab5.setText("40");lab5.setLayoutData(labelsData);
		operation40Sheets = new Spinner(shiftLogGrp, SWT.BORDER);
		new Label(shiftLogGrp, SWT.NONE);
		Label lab6 = new Label(shiftLogGrp, SWT.NONE);
		lab6.setText("80");lab6.setLayoutData(labelsData);
		operation80Sheets = new Spinner(shiftLogGrp, SWT.BORDER);
		
		Label lab7 = new Label(shiftLogGrp, SWT.NONE);
		lab7.setText("50");lab7.setLayoutData(labelsData);
		operation50Sheets = new Spinner(shiftLogGrp, SWT.BORDER);
		new Label(shiftLogGrp, SWT.NONE);
		Label lab8 = new Label(shiftLogGrp, SWT.NONE);
		lab8.setText("90");lab8.setLayoutData(labelsData);
		operation90Sheets = new Spinner(shiftLogGrp, SWT.BORDER);
		
		Composite btnsOuter = new Composite(printComposite, SWT.NONE);
		btnsOuter.setLayout(new GridLayout(2, false));
		btnsOuter.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		
		Label space = new Label(btnsOuter, SWT.NONE);
		space.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		
		Composite btnsInner = new Composite(btnsOuter, SWT.NONE);
		btnsInner.setLayout(new GridLayout(3, true));
		btnsInner.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		
		cancelBtn = new Button(btnsInner, SWT.PUSH);
		cancelBtn.setText("Cancel");
		cancelBtn.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		printPreviewBtn = new Button(btnsInner, SWT.PUSH);
		printPreviewBtn.setText("Print Preview");
		printPreviewBtn.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		printPreviewBtn.setEnabled(false);
		printBtn = new Button(btnsInner, SWT.PUSH);
		printBtn.setText("Print");
		printBtn.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		printBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				printSawSheet();
			}
		});
		
	}

	public void fillWithJob(Job job) 
	{
		
		selectedJob = job;
		resetControls();
		
		individualCutSheets.setSelection(job.getSawSheet().getNumberOfShifts());
		
		for(ShiftLogSheet sheet : selectedJob.getShiftLogSheets().values())
		{
			switch(sheet.getOperationNumber())
			{
			case 20:
				operation20Sheets.setSelection(sheet.getNumberOfShifts());
				break;
			case 30:
				operation30Sheets.setSelection(sheet.getNumberOfShifts());
				break;
			case 40:
				operation40Sheets.setSelection(sheet.getNumberOfShifts());
				break;
			case 50:
				operation50Sheets.setSelection(sheet.getNumberOfShifts());
				break;
			case 60:
				operation60Sheets.setSelection(sheet.getNumberOfShifts());
				break;
			case 70:
				operation70Sheets.setSelection(sheet.getNumberOfShifts());
				break;
			case 80:
				operation80Sheets.setSelection(sheet.getNumberOfShifts());
				break;
			case 90:
				operation90Sheets.setSelection(sheet.getNumberOfShifts());
				break;
			/*case 100:
				operation20Sheets.setSelection(sheet.getNumberOfShifts());
				break;
			case 120:
				operation20Sheets.setSelection(sheet.getNumberOfShifts());
				break;*/
			}
		}
		
		evaluateEnabled();
	}

	private void resetControls() 
	{
		operation20Sheets.setSelection(0);
		operation30Sheets.setSelection(0);
		operation40Sheets.setSelection(0);
		operation50Sheets.setSelection(0);
		operation60Sheets.setSelection(0);
		operation70Sheets.setSelection(0);
		operation80Sheets.setSelection(0);
		operation90Sheets.setSelection(0);
	}

	private void evaluateEnabled() 
	{
		if(selectedJob == null)
		{
			return;
		}
		
		individualCutSheets.setEnabled(printAllPacketBtn.getSelection());
		multimeCutSheets.setEnabled(false);
		
		operation20Sheets.setEnabled(printAllPacketBtn.getSelection() && operation20Sheets.getSelection()>0);
		operation30Sheets.setEnabled(printAllPacketBtn.getSelection() && operation30Sheets.getSelection()>0);
		operation40Sheets.setEnabled(printAllPacketBtn.getSelection() && operation40Sheets.getSelection()>0);
		operation50Sheets.setEnabled(printAllPacketBtn.getSelection() && operation50Sheets.getSelection()>0);
		operation60Sheets.setEnabled(printAllPacketBtn.getSelection() && operation60Sheets.getSelection()>0);
		operation70Sheets.setEnabled(printAllPacketBtn.getSelection() && operation70Sheets.getSelection()>0);
		operation80Sheets.setEnabled(printAllPacketBtn.getSelection() && operation80Sheets.getSelection()>0);
		operation90Sheets.setEnabled(printAllPacketBtn.getSelection() && operation90Sheets.getSelection()>0);
		
		
		printBtn.setEnabled(printAllPacketBtn.getSelection());
	}
	
	private void printSawSheet() {
		ProgressMonitorDialog dialog = new ProgressMonitorDialog(getShell());
		
		try
		{
			dialog.run(true, false, new IRunnableWithProgress() {
				
				@Override
				public void run(IProgressMonitor monitor)
				throws InvocationTargetException, InterruptedException 
				{
					monitor.beginTask("Printing Job", 100);
					monitor.worked(20);
					
					try
					{
						monitor.subTask("Printing Blank Saw Sheets...");
						SawSheet sawSheet = selectedJob.getSawSheet();
						for(int i=0; i<sawSheet.getNumberOfShifts(); i++)
						{
							SheetPrinter printer = new SheetPrinter(selectedJob, 
									selectedJob.getSawSheet(), true, i);
							printer.print();
							
							monitor.worked(80/sawSheet.getNumberOfShifts());
						}
					}
					catch(Exception e)
					{
						throw new InvocationTargetException(e);
					}
					finally
					{
						monitor.done();
					}
					
				
				}				
			});
		}
		catch(InvocationTargetException ite)
		{
			MessageDialog.openError(getShell(),
					"Sending Job to printer...", ite.getCause().getMessage());
			ite.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		
		IStructuredSelection structuredSelection = (IStructuredSelection) selection;
		Object selected = structuredSelection.getFirstElement();
		
		if(selected instanceof ModelNavigationAction)
		{
			ModelNavigationAction action = (ModelNavigationAction) selected;
			if(action.getSubCategory() != null && 
					action.getSubCategory() == NavigationCategory.PRINT_JOB)
			{
				
			}
		}
	}
}
