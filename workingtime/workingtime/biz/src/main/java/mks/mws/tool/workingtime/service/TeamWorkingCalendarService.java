package mks.mws.tool.workingtime.service;

import java.util.Date;
import java.util.List;

import mks.mws.tool.workingtime.entity.TeamWorkingCalendar;

public interface TeamWorkingCalendarService {
	void saveTeamWorkingCalendarData(Date fromDate, Date toDate, List<Object[]> teamWorkingCalendarData);

	List<Object[]> getAllTeamWorkingCalendarData(String fromDate, String toDate);

	List<Object[]> getRegisterWorkingCalendarData(String name, String fromDate, String toDate);

	void saveRegisterWorkingCalendarData(String name, Date fromDate, Date toDate,
			List<Object[]> teamWorkingCalendarData, String note);

	List<Object[]> getNotesByAccountAndDateRange(String account, String fromDate, String toDate);

	List<Long> saveOrUpdateCalendars(List<TeamWorkingCalendar> calendars);
	
}
