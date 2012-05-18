package com.datagardens.nq.sdb.client;

import java.text.MessageFormat;

import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

import com.datagardens.nq.sdb.commons.model.NQDataModel;

public class ClientWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

    public ClientWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
        super(configurer);
    }

    public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer) {
        return new ClientActionAdvisor(configurer);
    }
    
    public void preWindowOpen() {
        IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
        configurer.setInitialSize(new Point(1050, 700));
        configurer.setShowCoolBar(false);
        configurer.setShowStatusLine(false);
        configurer.setTitle(MessageFormat.format("Serialization Database Client [{0}, {1}]", 
        		NQDataModel.getClientUser().getUsername(), 
        		NQDataModel.getClientUser().getRole()));
    }
    
    @Override
    public boolean preWindowShellClose() {
    	NQDataModel.disconnect();
    	return true;
    }
}
