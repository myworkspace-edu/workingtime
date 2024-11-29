package mks.mws.tool.workingtime.controller.model;

import java.util.Date;

import java.util.List;

import lombok.Data;

@Data
public class CalendarModel {
	
	private Date fromDate;
	private Date toDate;
	private String note;
	private List<Object[]> data;
	int[] colWidths;   // Add this field for column widths
    String[] colHeaders;  // Add this field for column headers
    int countRow;
}
