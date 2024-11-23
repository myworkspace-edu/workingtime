package mks.mws.tool.workingtime.controller.model;

import java.util.Date;

import java.util.List;

import lombok.Data;

@Data
public class RegisterCalendarModel {
	private String name;
	private Date fromDate;
	private Date toDate;
	private List<Object[]> data;
	private String note;
	int[] colWidths;   // Add this field for column widths
    String[] colHeaders; // Add this field for column headers
}
