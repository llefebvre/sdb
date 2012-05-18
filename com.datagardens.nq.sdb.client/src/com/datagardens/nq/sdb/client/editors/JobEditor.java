package com.datagardens.nq.sdb.client.editors;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

import com.datagardens.nq.sdb.client.SWTResourceManager;

public class JobEditor extends EditorPart {

	public static final String ID = "com.datagardens.nq.sdb.client.editors.JobEditor";
	
	public JobEditor() {
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
	}

	@Override
	public void doSaveAs() {
	}

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isDirty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void createPartControl(Composite parent) {
		Composite comp = new Composite(parent, SWT.BORDER);
		comp.setBackground(SWTResourceManager.getColor(SWT.COLOR_DARK_MAGENTA));

	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
