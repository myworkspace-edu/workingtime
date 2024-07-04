package mks.mws.tool.workingtime.service.impl;

import mks.mws.tool.workingtime.model.WorkingCalendar;
import mks.mws.tool.workingtime.repository.WorkingCalendarRepository;
import mks.mws.tool.workingtime.service.WorkingCalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkingCalendarServiceImpl implements WorkingCalendarService {

    @Autowired
    private WorkingCalendarRepository workingCalendarRepository;

    @Override
    public List<WorkingCalendar> getAllWorkingCalendars() {
        return workingCalendarRepository.findAll();
    }

    @Override
    public void saveWorkingCalendar(WorkingCalendar workingCalendar) {
        workingCalendarRepository.save(workingCalendar);
    }
}
