package mks.mws.tool.workingtime.service;

import java.util.List;

import mks.mws.tool.workingtime.model.WorkingCalendar;

public interface WorkingCalendarService {
    List<WorkingCalendar> getAllWorkingCalendars();
    void saveWorkingCalendar(WorkingCalendar workingCalendar);
}
