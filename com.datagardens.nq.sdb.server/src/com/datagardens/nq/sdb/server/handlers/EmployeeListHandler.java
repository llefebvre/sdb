package com.datagardens.nq.sdb.server.handlers;

import org.apache.mina.core.session.IoSession;

import com.datagardens.nq.sdb.commons.model.Employee;
import com.datagardens.nq.sdb.commons.protocol.Message;
import com.datagardens.nq.sdb.commons.protocol.MessageHeader.Command;
import com.datagardens.nq.sdb.commons.protocol.MessageParameters;
import com.datagardens.nq.sdb.server.agentclient.AgentSession;
import com.datagardens.nq.sdb.server.jdbc.Row;
import com.datagardens.nq.sdb.server.jdbc.Table;

public class EmployeeListHandler extends CommandHandler {

	public EmployeeListHandler() {
		super(Command.employee_list);
	}

	@Override
	public void handle(Message message, IoSession session) 
	{
		try
		{
			AgentSession agentSession = AgentSession.open();
			MessageParameters parameters = new MessageParameters(Employee.class);

			Table empTable = database.getTable("Employee");
			
			for(Employee emp : agentSession.getEmployeeList())
			{
				/* Add/update database record*/
				Row employeeRow = new Row(); 
				employeeRow.put("emp_no", emp.getEmployeeNumber());
				employeeRow.put("first_name", emp.getName());
				employeeRow.put("last_name", emp.getLastName().replaceAll("'", "''"));
				empTable.putRow(employeeRow, "emp_no='"+emp.getEmployeeNumber()+"'");
				
				parameters.add(emp);
			}
			
			sendOkResponse(session,
					message.getHeader().getThreadId(), parameters);
		
		}
		catch(Exception e)
		{
			sendErrorMessage(session,
					message.getHeader().getThreadId(), "[SERVER]" + e.getMessage());
		}
		

		
		
		
	}

}
