package mks.mws.tool.workingtime.transformer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mks.mws.tool.workingtime.common.AppConst;
import mks.mws.tool.workingtime.entity.Task;

public class JpaTransformer {

    public static List<Object[]> convert2D(List<Task> lstTasks) {
        if (lstTasks == null) {
            return null;
        }

        List<Object[]> lstObject = new ArrayList<>();

        for (Task task : lstTasks) {
            Object[] rowData = new Object[11];
            rowData[0] = task.getId();
            rowData[1] = formatDate(task.getDate());
            rowData[2] = task.getName();
            rowData[3] = task.getCat();
            rowData[4] = task.getTaskname();
            rowData[5] = task.getProduct();
            rowData[6] = task.getPic();
            rowData[7] = (task.getStatus() != null) ? AppConst.STATUS_NAMES[task.getStatus()] : null;
            rowData[8] = task.getProject();
            rowData[9] = formatDate(task.getDeadline());
            rowData[10]=task.getNote();

            lstObject.add(rowData);
        }

        return lstObject;
    }

    private static String formatDate(Date date) {
        if (date == null) {
            return null;
        }
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }
}



//public class JpaTransformer {
//
//	public static List<Object[]> convert2D(List<Task> lstTasks) {
//		if (lstTasks == null) {
//			return null;
//		}
//
//		List<Object[]> lstObject = new ArrayList<Object[]>();
//
//		Object[] rowData;
//		Integer statusCode;
//		for (Task task : lstTasks) {
//			rowData = new Object[7];
//			rowData[0] = task.getId();
//			rowData[1] = task.getCat();
//			rowData[2] = task.getName();
//			rowData[3] = task.getProduct();
//			rowData[4] = task.getPic();
//
//			statusCode = task.getStatus();
//
//			rowData[5] = (statusCode != null) ? AppConst.STATUS_NAMES[statusCode] : null;
//			rowData[6] = task.getNote();
//
//			lstObject.add(rowData);
//		}
//
//		return lstObject;
//	}
//
//}
