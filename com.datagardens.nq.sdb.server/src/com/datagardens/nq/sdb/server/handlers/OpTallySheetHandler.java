package com.datagardens.nq.sdb.server.handlers;

import java.util.HashMap;
import java.util.Map;

import org.apache.mina.core.session.IoSession;

import com.datagardens.nq.sdb.commons.model.JobStatsOperation;
import com.datagardens.nq.sdb.commons.model.JobStatsShiftLogLine;
import com.datagardens.nq.sdb.commons.model.OpTallySheet;
import com.datagardens.nq.sdb.commons.model.OperatorStats;
import com.datagardens.nq.sdb.commons.protocol.Message;
import com.datagardens.nq.sdb.commons.protocol.MessageBody;
import com.datagardens.nq.sdb.commons.protocol.MessageHeader.Command;
import com.datagardens.nq.sdb.commons.protocol.MessageParameters;
import com.datagardens.nq.sdb.server.agentclient.SQLQuery;
import com.datagardens.nq.sdb.server.jdbc.RowSet;

public class OpTallySheetHandler extends CommandHandler {

	public OpTallySheetHandler() {
		super(Command.operation_tally_sheet);
	}

	@Override
	public void handle(Message message, IoSession session)
	{
		System.out.println("RUNNING TALLY HANDLER");
		MessageBody body = message.getBody();
		MessageParameters parameters = body.getContent();
		
		String jobNumber = (String) parameters.getAll().get(0);
		String operationNumber = (String) parameters.getAll().get(1);
		
		System.out.print("op_tally_sheet_handler for Job=" + jobNumber + ", Operation= " + operationNumber);
		
		SQLQuery queryJobStatsOperation    = new SQLQuery(buildJobStatsOperationQuery(jobNumber, operationNumber));
		SQLQuery queryJobStatsShiftLogLine = new SQLQuery(buildJobStatsShiftLogLineQuery(jobNumber,operationNumber));
		SQLQuery queryOperatorStats        = new SQLQuery(buildOperatorStatsQuery(jobNumber,operationNumber));

		/* query which returns ShiftLogLine marked with dup,gap etc. 
		SQLQuery queryShiftLog = new SQLQuery(buildDetailQuery(jobNumber,operationNumber));*/

		try
		{
			MessageParameters responseParams = new MessageParameters(OpTallySheet.class);
			Map<Integer, OperatorStats> operatorStats = new HashMap<Integer, OperatorStats>();
			JobStatsOperation jobStatsOperation = null;
			JobStatsShiftLogLine jobStatsSLL = null;
								
			RowSet operatorStatsRows =  database.runQuery(queryOperatorStats);
			RowSet jobStatsOperationRows = database.runQuery(queryJobStatsOperation);
			RowSet jobStatsSLLRows = database.runQuery(queryJobStatsShiftLogLine);
			
			if(operatorStatsRows.length() > 0)
			{
				for (Integer i=0; i < operatorStatsRows.length(); i++) {				
					OperatorStats opStat = parseOperatorStats(operatorStatsRows.get(i));
					operatorStats.put(opStat.getEmployeeNumber(),opStat);
				}
			}
			if(jobStatsOperationRows.length() > 0)
			{
				jobStatsOperation = parseJobStatsOperation(jobStatsOperationRows.get(0));
			}
			if(jobStatsSLLRows.length() > 0)
			{
				jobStatsSLL = parseJobStatsShiftLogLine(jobStatsSLLRows.get(0));
			}


			OpTallySheet opTallySheet = new OpTallySheet(jobNumber,Integer.parseInt(operationNumber),
					operatorStats,jobStatsOperation,jobStatsSLL);
			responseParams.add(opTallySheet);
			sendOkResponse(session, message.getHeader().getThreadId(), responseParams);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			sendErrorMessage(session, message.getHeader().getThreadId(), e.getMessage());
		}

	}
	

	/* ShiftLogLine marked with dup, gap etc.
	 * 
 duplicate serial_no   started_emp_no       workpiece_status     job_no               operation_no
 --------- ----------- -------------------- -------------------- -------------------- ------------
                     1 411                  Complete             25612-0000                     20
 dup                 2 496                  Complete             25612-0000                     20
 dup                 2 496                  Reworked             25612-0000                     20
                     3 399                  Complete             25612-0000                     20
                     4 496                  Complete             25612-0000                     20
 gap                 6 411                  Scrapped             25612-0000                     20
                     7 411                  Scrapped             25612-0000                     20
 */
	private String buildDetailQuery(String jobNumber, String operationNumber) {
		String query = "SELECT CASE (sn_gap.gap)" +
	            " WHEN 'gap' THEN sn_gap.gap " +
				" ELSE CASE (sn_count.dup_count) " +
	            "      WHEN 1 THEN '' ELSE 'dup' END " +
				" END AS duplicate,"+
	       "shiftlogline.serial_no," +
	       "started_emp_no," +
	       "workpieceStatus.description AS workpiece_status," +
	       "job_no," +
	       "operation_no" +
	  "FROM shiftlogline LEFT OUTER JOIN " +
	  "(SELECT 'gap' as gap,serial_no " +
	   "FROM shiftlogline sll3 " +
	   " WHERE sll3.job_no       = '<JOB_NO>' " +
	      "AND sll3.operation_no = <OPERATION_NO> "+
	      "AND (sll3.serial_no-1 >0) " +
	      "AND (sll3.serial_no-1) NOT IN (SELECT serial_no " +
	                              "FROM shiftlogline sll " +
	                              "WHERE sll.job_no       = '<JOB_NO>' " +
	                                "AND sll.operation_no = <OPERATION_NO>)) sn_gap ON (shiftlogline.serial_no = sn_gap.serial_no) " +
	      "LEFT OUTER JOIN   (SELECT count(*) AS dup_count, " +
	      "    sll2.serial_no " +
	     "FROM shiftlogline sll2 " +
	    "WHERE sll2.job_no       = '<JOB_NO>' " +
	    "  AND sll2.operation_no = <OPERATION_NO> " +
	"GROUP BY sll2.serial_no) sn_count ON (shiftlogline.serial_no = sn_count.serial_no) " +
	"LEFT OUTER JOIN workpieceStatus ON (shiftlogline.workpiece_status = workpieceStatus.status_code) " +
	" WHERE job_no       = '<JOB_NO>' " +
	"   AND operation_no = <OPERATION_NO> " +
	" ORDER BY line_no";
		
		query = query.replaceAll("<JOB_NO>",jobNumber);
		query = query.replaceAll("<OPERATION_NO>",operationNumber);
					
		return query;

	}
	
	/* Operator Statistics
 emp_no               Name                 Complete    Scrapped    Reworked    operator_time  mean_cycle     Routed         to_peer
 -------------------- -------------------- ----------- ----------- ----------- -------------- -------------- -------------- --------------
 399                  Tien (Tim) Banh                3           0           1      5.0999999      1.6999999     -17.241371     -4.0816288
 331                  Alexei Hartung                 1           0           0            1.6            1.6     -10.344826      2.0408144
 496                  Robert Huberdeau               5           0           1            7.0            1.4      3.4482808      14.285715
 411                  Ryan MacDonald                 6           2           1           10.8      1.8000001     -24.137932     -10.204086
   */
	private String buildOperatorStatsQuery(String jobNumber, String operationNumber) {
		String query = "SELECT started_emp_no AS emp_no, " +
				"emp.first_name AS first_name, " +
				"emp.last_name  AS last_name, " +
				"SUM(CASE workpiece_Status WHEN 1 THEN 1 ELSE 0 END) AS Complete, " +
				"SUM(CASE workpiece_Status WHEN 3 THEN 1 ELSE 0 END) AS Scrapped, " +
				"SUM(CASE workpiece_Status WHEN 4 THEN 1 ELSE 0 END) AS Reworked, " +
				"ot.operator_time, " +
				"(ot.operator_time/SUM(CASE workpiece_Status WHEN 1 THEN 1 ELSE 0 END)) AS mean_cycle, " +
				"((o.net_cycle_time-(ot.operator_time/SUM(CASE workpiece_Status WHEN 1 THEN 1 ELSE 0 END)))/o.net_cycle_time)*100 AS Routed, " +
				"((o.mean_cycle_time-(ot.operator_time/SUM(CASE workpiece_Status WHEN 1 THEN 1 ELSE 0 END)))/o.mean_cycle_time)*100 AS to_peer " +
				"FROM shiftlogline sll " +
				"LEFT OUTER JOIN Employee emp    ON (sll.started_emp_no = emp.emp_no) " +
				"LEFT OUTER JOIN OperatorTime ot ON (sll.job_no = ot.job_no AND sll.operation_no = ot.operation_no AND sll.started_emp_no  = ot.emp_no) " +
				"LEFT OUTER JOIN Operation o     ON (sll.job_no = o.job_no AND sll.operation_no = o.operation_no) " +
				"WHERE sll.job_no       = 'JOB_NO' " +
				"  AND sll.operation_no = OPERATION_NO " +
				"GROUP BY sll.started_emp_no,emp.last_name,emp.first_name,ot.operator_time,o.net_cycle_time,o.mean_cycle_time " +
				"ORDER BY emp.last_name, sll.started_emp_no";
		
		query = query.replaceAll("JOB_NO",jobNumber);
		query= query.replaceAll("OPERATION_NO",operationNumber);

		return query;
	}
	/*
	 * Job Statistics from the Operation table:

set_up         cycle          net_cycle_time mean_cycle_time
-------------- -------------- -------------- ---------------
           4.0           1.25           1.45            1.63
           
	 */
	private String buildJobStatsOperationQuery(String jobNumber, String operationNumber) {
		String query = "SELECT job_no," +
				    "operation_no," +
				    "set_up, " +
					"cycle, " +
					"net_cycle_time  AS net_cycle_time, " +
					"mean_cycle_time AS mean_cycle_time " +
				" FROM Operation " +
				"WHERE job_no = '<JOB_NO>' AND operation_no = <OPERATION_NO> ";
				
		query = query.replaceAll("<JOB_NO>",jobNumber);
		query = query.replaceAll("<OPERATION_NO>",operationNumber);

		return query;
	}
	
	/*
	 * Job Statistics from the ShiftLogLine table:

pieces_cut  Complete    Scrapped    Reworked    Good  Scrap Rework
----------- ----------- ----------- ----------- ----- ----- ------
         20          15           2           3  75.0  10.0   15.0
*/
	private String buildJobStatsShiftLogLineQuery(String jobNumber, String operationNumber) {
		
		String query = "SELECT " +
				"MAX(s_n_last_cut) AS pieces_cut, " +
				"sll.Complete, " +
				"sll.Scrapped, " +
				"sll.Reworked, " +
				"((CONVERT(real,sll.Complete)/MAX(s_n_last_cut))*100) AS Good, " +
				"((CONVERT(real,sll.Scrapped)/MAX(s_n_last_cut))*100) AS Scrap, " +
				"((CONVERT(real,sll.Reworked)/MAX(s_n_last_cut))*100) AS Rework " +
				"FROM IndividualLine il LEFT OUTER JOIN ( " +
						"SELECT " +
						"sll2.job_no, " +
						"sll2.operation_no, " +
						"SUM(CASE workpiece_status WHEN 1 THEN 1 ELSE 0 END) AS Complete, " +
						"SUM(CASE workpiece_status WHEN 3 THEN 1 ELSE 0 END) AS Scrapped, " +
						"SUM(CASE workpiece_status WHEN 4 THEN 1 ELSE 0 END) AS Reworked  " +
						"FROM shiftlogline sll2 " +
						"WHERE sll2.job_no      = '<JOB_NO>' " +
						"  AND sll2.operation_no = <OPERATION_NO> " +
						"GROUP BY sll2.job_no, sll2.operation_no) sll ON (il.job_no = sll.job_no) " +
				"WHERE il.job_no = '<JOB_NO>' " +
				"GROUP BY sll.Complete,sll.Scrapped,sll.Reworked";
				
		query = query.replaceAll("<JOB_NO>",jobNumber);
		query = query.replaceAll("<OPERATION_NO>",operationNumber);

		return query;
	}
	
}
