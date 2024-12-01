package mks.mws.tool.workingtime.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import mks.mws.tool.workingtime.entity.TeamWorkingCalendar;
import mks.mws.tool.workingtime.repository.TeamWorkingCalendarRepository;
import mks.mws.tool.workingtime.repository.WorkingCalendarRepository;
import mks.mws.tool.workingtime.service.TeamWorkingCalendarService;

@Service
@Slf4j
public class TeamWorkingCalendarServiceImpl implements TeamWorkingCalendarService {

    @Autowired
    private TeamWorkingCalendarRepository teamWorkingCalendarRepository;
    @Autowired
    private WorkingCalendarRepository workingCalendarRepository;

    @Override
    public void saveTeamWorkingCalendarData(Date fromDate, Date toDate, List<Object[]> teamWorkingCalendarData) {
        List<TeamWorkingCalendar> calendars = convertToTeamWorkingCalendars(fromDate, toDate, teamWorkingCalendarData);
        saveOrUpdateCalendars(calendars); // Use saveOrUpdate for saving or updating the calendars
    }

    @Override
    public List<Long> saveOrUpdateCalendars(List<TeamWorkingCalendar> calendars) {
        // If the method in repository returns Longs, adapt this method to handle it
        return workingCalendarRepository.saveOrUpdate(calendars); // Ensure saveOrUpdate method exists in repository
    }

    @Override
    public List<Object[]> getAllTeamWorkingCalendarData(String fromDate, String toDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<TeamWorkingCalendar> dataList;
        List<Object[]> result = new ArrayList<>();
        try {
            Date fromParsedDate = (fromDate != null && !fromDate.isEmpty()) ? sdf.parse(fromDate) : null;
            Date toParsedDate = (toDate != null && !toDate.isEmpty()) ? sdf.parse(toDate) : null;

            if (fromParsedDate == null && toParsedDate == null) {
                dataList = teamWorkingCalendarRepository.findAll();
            } else if (fromParsedDate == null) {
                dataList = teamWorkingCalendarRepository.findAllByToDateLessThanEqual(toParsedDate);
            } else if (toParsedDate == null) {
                dataList = teamWorkingCalendarRepository.findAllByFromDateGreaterThanEqual(fromParsedDate);
            } else {
                dataList = teamWorkingCalendarRepository.findAllByFromDateGreaterThanEqualAndToDateLessThanEqual(fromParsedDate, toParsedDate);
            }

            for (TeamWorkingCalendar data : dataList) {
                result.add(convertToDataArray(data));
            }
        } catch (Exception e) {
            log.error("Error retrieving team working calendar data", e);
        }

        return result;
    }

    @Override
    public List<Object[]> getRegisterWorkingCalendarData(String name, String fromDate, String toDate) {
        Date from = convertToDate(fromDate);
        Date to = convertToDate(toDate);
        
        if (from == null || to == null) {
            return new ArrayList<>(); // Return empty list if date conversion failed
        }

        List<TeamWorkingCalendar> dataList = teamWorkingCalendarRepository.findByAccountAndFromDateBetween(name, from, to);
        List<Object[]> result = new ArrayList<>();

        for (TeamWorkingCalendar data : dataList) {
            result.add(convertToRegisterDataArray(data));
        }

        result.add(new Object[] { "AM", "", "", "", "", "", "", "" });
        result.add(new Object[] { "PM", "", "", "", "", "", "", "" });

        return result;
    }

    private Object[] convertToRegisterDataArray(TeamWorkingCalendar data) {
        return new Object[] { data.getId(), data.getSection(), data.getMon(), data.getTue(), data.getWed(), data.getThur(),
                data.getFri(), data.getSat(), data.getSun() };
    }

    @Override
    public List<Object[]> getNotesByAccountAndDateRange(String account, String fromDate, String toDate) {
        Date from = convertToDate(fromDate);
        Date to = convertToDate(toDate);

        if (from == null || to == null) {
            return new ArrayList<>(); // Return empty list if date conversion failed
        }

        List<TeamWorkingCalendar> dataList = teamWorkingCalendarRepository.findByAccountAndFromDateBetween(account, from, to);
        List<Object[]> noteData = new ArrayList<>();
        for (TeamWorkingCalendar data : dataList) {
            noteData.add(new Object[] { data.getNote() });
        }
        return noteData;
    }

    private Date convertToDate(String dateString) {
        if (dateString == null || dateString.trim().isEmpty()) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return sdf.parse(dateString);
        } catch (ParseException e) {
            log.error("Could not parse date: {}", dateString, e);
            return null;
        }
    }

    private Object[] convertToDataArray(TeamWorkingCalendar data) {
        return new Object[] {data.getAccount(), data.getSection(), data.getMon(), data.getTue(), data.getWed(),
                data.getThur(), data.getFri(), data.getSat(), data.getSun(), data.getId()};
    }

    private List<TeamWorkingCalendar> convertToTeamWorkingCalendars(Date from, Date to, List<Object[]> data) {
        teamWorkingCalendarRepository.deleteByFromDateAndToDate(from, to);
        List<TeamWorkingCalendar> calendars = new ArrayList<>();
        String account;
        TeamWorkingCalendar calendar;
        for (Object[] row : data) {
            account = (String) row[0];
            if (account != null && !account.trim().isEmpty() && row.length >= 8) {
                calendar = new TeamWorkingCalendar();
                calendar.setFromDate(from);
                calendar.setToDate(to);
                calendar.setAccount((String) row[0]);
                calendar.setSection((String) row[1]);
                calendar.setMon((String) row[2]);
                calendar.setTue((String) row[3]);
                calendar.setWed((String) row[4]);
                calendar.setThur((String) row[5]);
                calendar.setFri((String) row[6]);
                calendar.setSat((String) row[7]);
                calendar.setSun((String) row[8]);
                Object idObject = (row.length == 10) ? row[9] : null; 
                if (idObject != null) {
                	calendar.setId(((Double) idObject).longValue());
                } else {
                    calendar.setId(null); 
                }
                calendars.add(calendar);
            } else {
                log.warn("Data row has insufficient length: {}", row.length);
            }
        }
        return calendars;
    }

    @Override
    public void saveRegisterWorkingCalendarData(String name, Date fromDate, Date toDate, List<Object[]> teamWorkingCalendarData, String note) {
    	System.out.println(1);
        List<TeamWorkingCalendar> calendars = new ArrayList<>();
        for (Object[] row : teamWorkingCalendarData) {
            TeamWorkingCalendar data = new TeamWorkingCalendar();
            data.setFromDate(fromDate);
            data.setToDate(toDate);
            data.setAccount(name);
            data.setSection((String) row[0]);
            data.setMon((String) row[1]);
            data.setTue((String) row[2]);
            data.setWed((String) row[3]);
            data.setThur((String) row[4]);
            data.setFri((String) row[5]);
            data.setSat((String) row[6]);
            data.setSun((String) row[7]);
            data.setNote(note);
            calendars.add(data);
        }
        saveOrUpdateCalendars(calendars); // Use saveAll if you don't have saveOrUpdate method
    }
}


//public class TeamWorkingCalendarServiceImpl implements TeamWorkingCalendarService {
//
//	@Autowired
//	private TeamWorkingCalendarRepository teamWorkingCalendarRepository;
//	@Autowired
//	private WorkingCalendarRepository workingCalendarRepository;
//
//	@Override
//	public void saveTeamWorkingCalendarData(Date fromDate, Date toDate, List<Object[]> teamWorkingCalendarData) {
//		List<TeamWorkingCalendar> calendars = convertToTeamWorkingCalendars(fromDate, toDate, teamWorkingCalendarData);
//		teamWorkingCalendarRepository.saveAll(calendars); // Use saveAll for JPA repository
//	}
//
//	public List<Long> saveOrUpdateCalendars(List<TeamWorkingCalendar> calendars) {		
//		return workingCalendarRepository.saveOrUpdate(calendars);
//	}
//
//	@Override
//	public List<Object[]> getAllTeamWorkingCalendarData(String fromDate, String toDate) {
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		List<TeamWorkingCalendar> dataList;
//		List<Object[]> result = new ArrayList<>();
//		try {
//			Date fromParsedDate = fromDate != null && !fromDate.isEmpty() ? sdf.parse(fromDate) : null;
//			Date toParsedDate = toDate != null && !toDate.isEmpty() ? sdf.parse(toDate) : null;
//
//			if (fromParsedDate == null && toParsedDate == null) {
//				// Get all data if both fromDate and toDate are null or empty
//				dataList = teamWorkingCalendarRepository.findAll();
//			} else if (fromParsedDate == null) {
//				// If only fromDate is null or empty
//				dataList = teamWorkingCalendarRepository.findAllByToDateLessThanEqual(toParsedDate);
//			} else if (toParsedDate == null) {
//				// If only toDate is null or empty
//				dataList = teamWorkingCalendarRepository.findAllByFromDateGreaterThanEqual(fromParsedDate);
//			} else {
//				// If both fromDate and toDate have values
//				dataList = teamWorkingCalendarRepository
//						.findAllByFromDateGreaterThanEqualAndToDateLessThanEqual(fromParsedDate, toParsedDate);
//			}
//
//			for (TeamWorkingCalendar data : dataList) {
//				result.add(convertToDataArray(data));
//			}
//		} catch (Exception e) {
//			log.error("Error retrieving team working calendar data", e);
//		}
//
//		return result;
//	}
//
//	@Override
//	public List<Object[]> getRegisterWorkingCalendarData(String name, String fromDate, String toDate) {
//		Date from = convertToDate(fromDate);
//		Date to = convertToDate(toDate);
//
//		if (from == null || to == null) {
//			return new ArrayList<>(); // Return empty list if date conversion failed
//		}
//
//		List<TeamWorkingCalendar> dataList = teamWorkingCalendarRepository.findByAccountAndFromDateBetween(name, from,
//				to);
//		List<Object[]> result = new ArrayList<>();
//
//		// Convert each data item to the desired format
//		for (TeamWorkingCalendar data : dataList) {
//			result.add(convertToRegisterDataArray(data));
//		}
//
//		// Add the two specific rows with "AM" and "PM"
//		result.add(new Object[] { "AM", "", "", "", "", "", "", "" });
//		result.add(new Object[] { "PM", "", "", "", "", "", "", "" });
//
//		return result;
//	}
//
//	// Custom converter method for register calendar data
//	private Object[] convertToRegisterDataArray(TeamWorkingCalendar data) {
//		return new Object[] { data.getSection(), data.getMon(), data.getTue(), data.getWed(), data.getThur(),
//				data.getFri(), data.getSat(), data.getSun() };
//	}
//
//	@Override
//	public List<Object[]> getNotesByAccountAndDateRange(String account, String fromDate, String toDate) {
//		Date from = convertToDate(fromDate);
//		Date to = convertToDate(toDate);
//
//		if (from == null || to == null) {
//			return new ArrayList<>(); // Return empty list if date conversion failed
//		}
//
//		List<TeamWorkingCalendar> dataList = teamWorkingCalendarRepository.findByAccountAndFromDateBetween(account,
//				from, to);
//		List<Object[]> noteData = new ArrayList<>();
//		for (TeamWorkingCalendar data : dataList) {
//			noteData.add(new Object[] { data.getNote() });
//		}
//		return noteData;
//	}
//
//	private Date convertToDate(String dateString) {
//		if (dateString == null || dateString.trim().isEmpty()) {
//			return null; // Return null if dateString is null or empty
//		}
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		try {
//			return sdf.parse(dateString);
//		} catch (ParseException e) {
//			log.error("Could not parse date: {}", dateString, e);
//			return null; // Handle the error as needed
//		}
//	}
//
//	private Object[] convertToDataArray(TeamWorkingCalendar data) {
////		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // Định dạng thời gian
//		return new Object[] { data.getAccount(), data.getSection(), data.getMon(), data.getTue(), data.getWed(),
//				data.getThur(), data.getFri(), data.getSat(), data.getSun()
////				, data.getNote(),
////				sdf.format(data.getFromDate()), sdf.format(data.getToDate())
//
//		};
//	}
//
//	private List<TeamWorkingCalendar> convertToTeamWorkingCalendars(Date from, Date to, List<Object[]> data) {
//		teamWorkingCalendarRepository.deleteByFromDateAndToDate(from, to);
//		List<TeamWorkingCalendar> calendars = new ArrayList<>();
//		/*
//		 * khai báo biến ngoài vòng lặp
//		 */
//		String account;
//		TeamWorkingCalendar calendar;
//		for (Object[] row : data) {
//			account = (String) row[0];
//			if (account != null && !account.trim().isEmpty() && row.length >= 9) { // Đảm bảo mảng có ít nhất 9 phần tử
//				calendar = new TeamWorkingCalendar();
//				calendar.setFromDate(from);
//				calendar.setToDate(to);
//				calendar.setAccount((String) row[0]);
//				calendar.setSection((String) row[1]);
//				calendar.setMon((String) row[2]);
//				calendar.setTue((String) row[3]);
//				calendar.setWed((String) row[4]);
//				calendar.setThur((String) row[5]);
//				calendar.setFri((String) row[6]);
//				calendar.setSat((String) row[7]);
//				calendar.setSun((String) row[8]);
//				calendars.add(calendar);
//			} else {
//				// Log lỗi hoặc xử lý mảng không đầy đủ theo cách phù hợp
//				log.warn("Data row has insufficient length: {}", row.length);
//			}
//		}
//
//		return calendars;
//	}
//
//	@Override
//	public void saveRegisterWorkingCalendarData(String name, Date fromDate, Date toDate,
//			List<Object[]> teamWorkingCalendarData, String note) {
//		teamWorkingCalendarRepository.deleteByAccountAndFromDateAndToDate(name, fromDate, toDate);
//		List<TeamWorkingCalendar> entities = new ArrayList<>();
//		for (Object[] row : teamWorkingCalendarData) {
//			TeamWorkingCalendar data = new TeamWorkingCalendar();
//			data.setFromDate(fromDate);
//			data.setToDate(toDate);
//			data.setAccount(name);
//			data.setSection((String) row[0]);
//			data.setMon((String) row[1]);
//			data.setTue((String) row[2]);
//			data.setWed((String) row[3]);
//			data.setThur((String) row[4]);
//			data.setFri((String) row[5]);
//			data.setSat((String) row[6]);
//			data.setSun((String) row[7]);
//			data.setNote(note);
//			entities.add(data);
//		}
//		teamWorkingCalendarRepository.saveAll(entities);
//	}
//}
