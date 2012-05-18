package com.datagardens.nq.sdb.client.views;

import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.datagardens.nq.sdb.client.ImageKey;
import com.datagardens.nq.sdb.client.SWTResourceManager;
import com.datagardens.nq.sdb.client.commands.adminstration.AddUserAction;
import com.datagardens.nq.sdb.client.commands.adminstration.DeleteUserAction;
import com.datagardens.nq.sdb.client.commands.adminstration.EditUserAction;
import com.datagardens.nq.sdb.client.views.adapters.ITableEditorListener;
import com.datagardens.nq.sdb.client.views.adapters.ModelContentProvider;
import com.datagardens.nq.sdb.client.views.adapters.ModelLabelProvider;
import com.datagardens.nq.sdb.client.views.nav.ModelNavigationFolder;
import com.datagardens.nq.sdb.client.views.nav.ModelNavigationObject.NavigationCategory;
import com.datagardens.nq.sdb.commons.model.ModelException;
import com.datagardens.nq.sdb.commons.model.NQDataModel;
import com.datagardens.nq.sdb.commons.model.nio.rbac.SDBUser;
import com.datagardens.nq.sdb.commons.model.nio.rbac.SDBUser.SDBRole;

public class AdministrationView extends Composite {

	public static AdministrationView instance;
	
	private IWorkbenchPartSite site;
	private IWorkbenchWindow window;
	private TableViewer viewer;
	private SDBRole [] availableRoles;
	
	private Button addButton;
	private Button editButton;
	private Button deleteButton;
	
	public AdministrationView(Composite parent, int style, IWorkbenchPartSite site)
	{
		super(parent, style);
		
		instance = this;
		 
		GridLayout layout = new GridLayout(1, false);
		layout.marginWidth = 15;
		layout.marginHeight = 15;
		layout.verticalSpacing = 10;
		setLayout(layout);
		
		this.window = site.getWorkbenchWindow();
		this.site = site;
		
		createHeader();
		createBody();
	}

	private void createHeader() 
	{
		Composite headerComposite = new Composite(this, SWT.BORDER);
		headerComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		GridLayout headerLayout = new GridLayout(3, false);
		headerLayout.verticalSpacing = 1;
		headerComposite.setLayout(headerLayout);
		
		Label icon = new Label(headerComposite, SWT.NONE);
		icon.setImage(SWTResourceManager.getImage(ImageKey.ICON_32_ADMINISTRATOR));
		icon.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, false, false, 1, 2));
		
		Label title = new Label(headerComposite, SWT.NONE);
		title.setText("Administration Management");
		title.setFont(SWTResourceManager.getBoldFont(getFont()));
		title.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, true));
		
		Composite menuComposite = new Composite(headerComposite, SWT.NONE);
		menuComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 2));
		menuComposite.setLayout(new RowLayout());
		
		addButton = WidgetUtilities.createActionButton(AddUserAction.getActionReference(), 
				menuComposite, SWT.PUSH);
		
		editButton = WidgetUtilities.createActionButton(EditUserAction.getActionReference(), 
				menuComposite, SWT.PUSH);
		
		deleteButton = WidgetUtilities.createActionButton(DeleteUserAction.getActionReference(), 
				menuComposite, SWT.PUSH);
		
		Label subTitle = new Label(headerComposite, SWT.NONE);
		subTitle.setText("Create, Delete and Edit SDB users.");
		subTitle.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		
		
	}

	private void createBody()
	{
		Composite bodyComposite = new Composite(this, SWT.NONE);
		bodyComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		bodyComposite.setLayout(new FillLayout());
		
		viewer = new TableViewer(bodyComposite, SWT.FULL_SELECTION | SWT.BORDER);
		viewer.getTable().setHeaderVisible(true);
		
		site.setSelectionProvider(viewer);
		
		TableViewerColumn userNameColum = new TableViewerColumn(viewer, SWT.NONE);
		userNameColum.getColumn().setText("User");
		userNameColum.getColumn().setWidth(250);
		userNameColum.setLabelProvider(new ModelLabelProvider());
		
		TableViewerColumn roleColumn = new TableViewerColumn(viewer, SWT.NONE);
		roleColumn.getColumn().setText("Role");
		roleColumn.getColumn().setWidth(180);
		roleColumn.setLabelProvider(new CellLabelProvider() {
			
			@Override
			public void update(ViewerCell cell) {
				
				SDBUser user = (SDBUser) cell.getElement();
				if(user != null)
				{
					cell.setText(user.getRole().getId());
				}
			}
		});
		
		viewer.setContentProvider(new ModelContentProvider());
		
		WidgetUtilities.addTableEditorListener(viewer, new ITableEditorListener() {
			
			@Override
			public void onItemFound(Item item, int columnIndex) 
			{
				final TableItem selectedItem = (TableItem) item;
				
				if(selectedItem.getData() instanceof SDBUser)
				{
					final SDBUser user = (SDBUser) selectedItem.getData();
					
					if(user.hasLevel(SDBRole.ROOT))
					{
						return;
					}
					
					final ComboViewer comboViewer = new ComboViewer(new CCombo(viewer.getTable(), 
							SWT.BORDER | SWT.READ_ONLY));
					TableEditor editor = (TableEditor) viewer.getData("editor");
					
					//fill combo editor
					comboViewer.setContentProvider(new ModelContentProvider());
					comboViewer.setLabelProvider(new ModelLabelProvider());
					comboViewer.setInput(availableRoles);
					
					editor.setEditor(comboViewer.getControl(), selectedItem, 1);
					
					WidgetUtilities.selectCComboItemWithLabel(comboViewer.getCCombo(),
							user.getRole().getId());
					
					comboViewer.addSelectionChangedListener(new ISelectionChangedListener() {
						
						@Override
						public void selectionChanged(SelectionChangedEvent event) 
						{
							IStructuredSelection selection = 
								(IStructuredSelection) event.getSelection();
							
							if(selection != null)
							{
								SDBRole oldRole = user.getRole();
								
								try
								{
									user.setRole((SDBRole) selection.getFirstElement());
									NQDataModel.editUser(user);
								}
								catch(ModelException e)
								{
									//TODO: pop-up error
																
									//restore the old role
									user.setRole(oldRole);
									e.printStackTrace();
								
								}
								finally
								{
									comboViewer.getCCombo().dispose();
									refreshView();
								}
							}
						}
					});
				}
			}
		});
		
		window.getSelectionService().addSelectionListener(new ISelectionListener() {
			
			@Override
			public void selectionChanged(IWorkbenchPart part, ISelection selection) {
				IStructuredSelection iss = (IStructuredSelection) selection;
				Object selected = iss.getFirstElement();
				if(selected instanceof ModelNavigationFolder)
				{
					ModelNavigationFolder folder = (ModelNavigationFolder) selected;
					if(folder.getMainCategory() == NavigationCategory.ADMINISTRATION)
					{
						BusyIndicator.showWhile(getDisplay(), new Runnable() {
							
							@Override
							public void run() {
								fillViewer();
							}
						});
					}
				}
			}
		});
	}
	
	private void refreshView() {
		viewer.refresh();
	}

	public void fillViewer() 
	{
		try
		{
			viewer.setInput(NQDataModel.getUsersList());
			availableRoles = new SDBRole[3];
			availableRoles[0] = SDBRole.DATA_ENTRY;
			availableRoles[1] = SDBRole.SALES_COORDINATOR;
			availableRoles[2] = SDBRole.ADMINISTRATOR;
			
			viewer.setSelection(null);
		}
		catch(final ModelException e)
		{
			PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
				
				@Override
				public void run() {
					//TODO: Pop-up error window
					e.printStackTrace();
				}
			});
		}
	}
}
