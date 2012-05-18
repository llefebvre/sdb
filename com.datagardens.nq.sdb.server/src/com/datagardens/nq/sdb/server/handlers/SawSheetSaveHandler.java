package com.datagardens.nq.sdb.server.handlers;

import org.apache.mina.core.session.IoSession;

import com.datagardens.nq.sdb.commons.model.IndividualCut;
import com.datagardens.nq.sdb.commons.model.SawSheet;
import com.datagardens.nq.sdb.commons.protocol.Message;
import com.datagardens.nq.sdb.commons.protocol.MessageHeader.Command;
import com.datagardens.nq.sdb.server.jdbc.Row;
import com.datagardens.nq.sdb.server.jdbc.Table;

public class SawSheetSaveHandler extends CommandHandler {

	public SawSheetSaveHandler() {
		super(Command.saw_sheet_save);
	}

	@Override
	public void handle(Message message, IoSession session)
	{
		/*
		 * Updating the and saw sheet happens  in two steps
		 * first we have to update the SawSheet Table with the header information
		 * then we have to update the IndividualLine Table with the individual
		 * cuts information.
		 */
		
		try 
		{
			SawSheet sheet = (SawSheet) message.getBody().getContent().getFirstElement();
			Table sawSheetTable = database.getTable("SawSheet");
			
			Row updateSheetRow = new Row();
			updateSheetRow.put("qty_blank", sheet.getQuantityBlanks() + "");
			updateSheetRow.put("blank_length", sheet.getBlankLength() + "");
			updateSheetRow.put("yield_per_blank", sheet.getYieldPerBlank() + "");
			updateSheetRow.put("planned_net_yield", sheet.getPlannedNetYield() + "");
			updateSheetRow.put("nbr_of_shifts", sheet.getNumberOfShifts() + "");
			updateSheetRow.put("total_actual_yield", sheet.getTotalActualCutYield() + "");
			
			sawSheetTable.putRow(updateSheetRow, "saw_sheet_no='" + sheet.getSawSheetNumber() + "'");
			
			Table individualCutTable = database.getTable("IndividualLine");
			
			
			for(IndividualCut cut : sheet.getCuts().values())
			{				
				Row updateCutRow = new Row();
				updateCutRow.put("job_no", sheet.getJobId());
				updateCutRow.put("saw_sheet_no", sheet.getSawSheetNumber() + "");
				updateCutRow.put("shift_no",  + cut.getId() + "");
				updateCutRow.put("s_n_first_cut", cut.getSerialNumberFirstCut() + "");
				updateCutRow.put("s_n_last_cut",  cut.getSerialNumberLastCut()+ "");
				updateCutRow.put("cut_length", cut.getCutLength() + "");
				updateCutRow.put("emp_no", cut.getEmpNumber() + "");
				updateCutRow.put("yield_cut", cut.getYieldCut() + "");
				updateCutRow.put("cut_date",  cut.getDate() + "");
				updateCutRow.put("heat_no", cut.getHeatNumber() + "");
				updateCutRow.put("ni_no", cut.getnI() + "");
				updateCutRow.put("signed", cut.isSigned()? "1":"0");
				
				individualCutTable.putRow(updateCutRow, "saw_sheet_no='" + sheet.getSawSheetNumber() 
						+ "' AND shift_no='" + cut.getId() + "' AND s_n_first_cut='" + cut.getSerialNumberFirstCut() + "'");
			}
			
			sendOkResponse(session, message.getHeader().getThreadId(), null);
		} 
		catch (Exception e) 
		{
			sendErrorMessage(session, message.getHeader().getThreadId(), e.getMessage());
			e.printStackTrace();
		}		
	}

	

}
