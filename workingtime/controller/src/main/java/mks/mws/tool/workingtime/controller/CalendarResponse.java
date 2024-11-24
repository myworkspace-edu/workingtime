package mks.mws.tool.workingtime.controller;

import mks.mws.tool.workingtime.entity.TeamWorkingCalendar;
import java.util.List;

public class CalendarResponse {
    private String[] colHeaders;
    private int[] colWidths;
    private List<TeamWorkingCalendar> data;

    // Getters and Setters
    public String[] getColHeaders() {
        return colHeaders;
    }

    public void setColHeaders(String[] colHeaders) {
        this.colHeaders = colHeaders;
    }

    public int[] getColWidths() {
        return colWidths;
    }

    public void setColWidths(int[] colWidths) {
        this.colWidths = colWidths;
    }

    public List<TeamWorkingCalendar> getData() {
        return data;
    }

    public void setData(List<TeamWorkingCalendar> data) {
        this.data = data;
    }
}
