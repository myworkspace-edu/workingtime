package mks.mws.tool.workingtime.validate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mks.mws.tool.workingtime.common.AppConst;
import mks.mws.tool.workingtime.entity.Task;
import mksgroup.java.common.CommonUtil;

public class TaskValidator {

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	public static List<Task> validateAndCleasing(List<Object[]> data) {
	    List<Task> paramList = new ArrayList<>();

	    for (Object[] rowData : data) {
	        if (CommonUtil.isNNNE(rowData)) {
	            Long taskId = parseTaskId(rowData[0]);

	            Date date = parseDate(rowData[1]);      // Date
	            Date deadline = parseDate(rowData[9]);  // Deadline

	            Integer statusIndex = AppConst.STATUS_MAP.get((String) rowData[7]);

	            Task task = new Task(
	                taskId,
	                (String) rowData[3],   // Category
	                date,                  // Date
	                (String) rowData[2],   // Name
	                (String) rowData[4],   // Task name
	                (String) rowData[5],   // Output product
	                (String) rowData[6],   // PIC
	                statusIndex,           // Status
	                (String) rowData[10],  // Note
	                deadline,              // Deadline
	                (String) rowData[8]    // Project
	            );

	            paramList.add(task);
	        }
	    }

	    return paramList;
	}

	private static Long parseTaskId(Object id) {
		if (id == null) {
			return null;
		} else if (id instanceof Double) {
			return ((Double) id).longValue();
		} else if (id instanceof Long) {
			return (Long) id;
		} else if (id instanceof Integer) {
			return Long.valueOf((Integer) id);
		} else if (id instanceof String) {
			String strId = (String) id;
			return (strId.length() > 0) ? Long.valueOf(strId) : null;
		} else {
			throw new RuntimeException("Unknown data " + id.getClass());
		}
	}

	private static Date parseDate(Object dateObj) {
		if (dateObj == null || ((String) dateObj).trim().isEmpty()) {
			return null;
		}
		try {
			return dateFormat.parse((String) dateObj);
		} catch (ParseException e) {
			e.printStackTrace();
			throw new RuntimeException("Could not parse date: " + e.getMessage());
		}
	}
}

//public class TaskValidator {
//
//	public static List<Task> validateAndCleasing(List<Object[]> data) {
//  	List<Task> paramList = new ArrayList<Task>();
//
//  	Task task;
//  	Long taskId;
//  	String strId;
//  	for (Object[] rowData : data) {
//  		if (CommonUtil.isNNNE(rowData)) {
//  			Object id = rowData[0];
//  			// On Handsontable, a integer cell 1 can submitted as 1.0; 
//  			if (id == null) {
//  				taskId = null;
//  			} else if (id instanceof Double) {
//  				taskId = ((Double) id).longValue();
//  			} else if (id instanceof Long) {
//  				taskId = (Long) id;
//  			} else if (id instanceof Integer) {
//  				taskId = Long.valueOf((Integer) id);
//  			} else if (id instanceof String) {
//  				strId = (String) id;
//  				taskId = (strId.length() > 0) ? Long.valueOf(strId) : null;
//  			} else {
//  				throw new RuntimeException("Unknown data " + id.getClass());
//  			}
//
//  			Integer statusIndex = AppConst.STATUS_MAP.get((String) rowData[5]);
//  			task = new Task(taskId,
//  					(String) rowData[1],
//  					(String) rowData[2],
//						(String) rowData[3],
//						(String) rowData[4],
//						statusIndex,
//						(String) rowData[6]);
//  			paramList.add(task);
//  		}
//		}
//  	
//  	return paramList;
//	}
//
//}
