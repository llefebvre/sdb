package com.datagardens.nq.sdb.client;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

import com.datagardens.nq.sdb.client.commands.adminstration.AddUserAction;
import com.datagardens.nq.sdb.client.commands.adminstration.DeleteUserAction;
import com.datagardens.nq.sdb.client.commands.adminstration.EditUserAction;
import com.datagardens.nq.sdb.client.commands.adminstration.UserManagementAction;
import com.datagardens.nq.sdb.client.commands.production.AddShiftToOperationAction;
import com.datagardens.nq.sdb.client.commands.production.AnalysisAction;
import com.datagardens.nq.sdb.client.commands.production.CloseJobAction;
import com.datagardens.nq.sdb.client.commands.production.DataEntryAction;
import com.datagardens.nq.sdb.client.commands.production.OpenJobAction;
import com.datagardens.nq.sdb.client.commands.production.PrintJobAction;
import com.datagardens.nq.sdb.client.commands.production.RemoveShiftFromOperationAction;
import com.datagardens.nq.sdb.client.commands.production.SaveSawSheetAction;
import com.datagardens.nq.sdb.client.commands.reporting.EfficiencyReportAction;
import com.datagardens.nq.sdb.client.commands.reporting.IORReportAction;
import com.datagardens.nq.sdb.client.commands.tracking.TrackingByJobNumber;
import com.datagardens.nq.sdb.client.commands.tracking.TrackingByNINumber;
import com.datagardens.nq.sdb.client.commands.tracking.TrackingByPartNumberAction;
import com.datagardens.nq.sdb.client.commands.tracking.TrackingBySerialNumberAction;
import com.datagardens.nq.sdb.commons.model.NQDataModel;
import com.datagardens.nq.sdb.commons.model.nio.rbac.SDBUser.SDBRole;

public class ClientActionAdvisor extends ActionBarAdvisor {

	public static ClientActionAdvisor instnace;
	
	
	//tracking
	private MenuManager trackignMenu;
	private IWorkbenchAction trackingBySerialNumberAction;
	private IWorkbenchAction trackingByJobNumberAction;
	private IWorkbenchAction trackingByPartNumberAction;
	private IWorkbenchAction trackingbyNINumberAction;
	//production
	private MenuManager prodMenu;
	private IWorkbenchAction addShiftAction;
	private IWorkbenchAction removeShiftAction;
	private IWorkbenchAction openJobAction;
	private IWorkbenchAction printJobAction;
	private IWorkbenchAction dataEntryAction;
	private IWorkbenchAction analysisAction;
	private IWorkbenchAction closeJobAction;
	private IWorkbenchAction saveSawSheetAction;
	//reporting
	private IWorkbenchAction efficiencyReportAction;
	private IWorkbenchAction iorReportAction;
	//administration
	private IWorkbenchAction userManagementAction;
	private IWorkbenchAction addUserAction;
	private IWorkbenchAction editUserAction;
	private IWorkbenchAction deleteserAction;
	//help
	private IWorkbenchAction helpAction;
	private IWorkbenchAction aboutAction;
	//file
	private IWorkbenchAction exitAction;
	
	private Map<String, IAction> actions;
	
    public ClientActionAdvisor(IActionBarConfigurer configurer) {
        super(configurer);
        actions = new HashMap<String, IAction>();
        instnace = this;
    }
    
    public MenuManager getProdMenu() {
		return prodMenu;
	}
    
    public MenuManager getTrackignMenu() {
		return trackignMenu;
	}
    
    public IWorkbenchAction getOpenJobAction() {
		return openJobAction;
	}

    protected void makeActions(IWorkbenchWindow window) 
    {
    	/* Tracking Actions */
    	trackingBySerialNumberAction = new TrackingBySerialNumberAction(window);
    	trackingByJobNumberAction = new TrackingByJobNumber(window);
    	trackingByPartNumberAction = new TrackingByPartNumberAction(window);
    	trackingbyNINumberAction = new TrackingByNINumber(window);
    	register(trackingBySerialNumberAction);
    	register(trackingByJobNumberAction);
    	register(trackingByPartNumberAction);
    	register(trackingbyNINumberAction);
    	
    	/* Production Actions */
    	openJobAction = new OpenJobAction(window);
    	printJobAction = PrintJobAction.generateAction(window);
    	dataEntryAction = new DataEntryAction(window);
    	analysisAction = AnalysisAction.generateAction(window);
    	closeJobAction = CloseJobAction.generateAction(window);
    	saveSawSheetAction = SaveSawSheetAction.generateAction(window);
    	addShiftAction  = AddShiftToOperationAction.generateAction(window);
    	removeShiftAction = RemoveShiftFromOperationAction.generateAction(window);
    	register(openJobAction);
    	register(printJobAction);
    	register(dataEntryAction);
    	register(analysisAction);
    	register(closeJobAction);
    	register(saveSawSheetAction);
    	register(addShiftAction);
    	register(removeShiftAction);
    	
    	/* Reporting Actions*/
    	efficiencyReportAction = new EfficiencyReportAction(window);
    	iorReportAction = IORReportAction.generateAction(window);
    	register(efficiencyReportAction);
    	register(iorReportAction);
    	
    	/* Administration Actions */
    	userManagementAction = new UserManagementAction(window);
    	addUserAction = AddUserAction.generateAction(window);
    	editUserAction = EditUserAction.generateAction(window);
    	deleteserAction = DeleteUserAction.generateAction(window);
    	register(addUserAction);
    	register(editUserAction);
    	register(deleteserAction);
    	
    	
    	/* Help Actions */
    	helpAction = ActionFactory.HELP_CONTENTS.create(window);
    	register(helpAction);
    	aboutAction = ActionFactory.ABOUT.create(window);
    	register(aboutAction);
    	
    	exitAction = ActionFactory.QUIT.create(window);
    	register(exitAction);
    }

    protected void fillMenuBar(IMenuManager menuBar)
    {
    	trackignMenu = new MenuManager("&Tracking");
    	trackignMenu.add(trackingBySerialNumberAction);
    	trackignMenu.add(trackingByJobNumberAction);
    	trackignMenu.add(trackingByPartNumberAction);
    	trackignMenu.add(trackingbyNINumberAction);
    	menuBar.add(trackignMenu);
    	
    	prodMenu = new MenuManager("&Production");
    	prodMenu.add(openJobAction);
    	prodMenu.add(printJobAction);
    	prodMenu.add(dataEntryAction);
    	prodMenu.add(analysisAction);
    	menuBar.add(prodMenu);
    	
    	MenuManager reportMenu = new MenuManager("&Reporting");
    	
    	if(NQDataModel.getClientUser().hasLevel(SDBRole.ADMINISTRATOR))
    	{
    		reportMenu.add(efficiencyReportAction);
    	}
    	
    	reportMenu.add(iorReportAction);
    	
    	menuBar.add(reportMenu);
    	
    	MenuManager adminMenu = new MenuManager("&Adminstration");
    	
    	if(NQDataModel.getClientUser().hasLevel(SDBRole.ROOT))
		{
    		adminMenu.add(userManagementAction);
		}
    	
    	menuBar.add(adminMenu);
    	
    	MenuManager helpMenu = new MenuManager("&Help");
    	helpMenu.add(helpAction);    
    	helpMenu.add(aboutAction);
    	menuBar.add(helpMenu);
    	
    }
    
    @Override
    protected void register(IAction action) 
    {
    	Assert.isNotNull(action, "Action must not be null");
    	String id = action.getId();
    	Assert.isNotNull(id, "Action must not have null id");
    	getActionBarConfigurer().registerGlobalAction(action);
    	actions.put(id, action);
    }
    
}
