package mks.mws.tool.workingtime.controller;

import mks.mws.tool.workingtime.model.WorkingCalendar;
import mks.mws.tool.workingtime.service.WorkingCalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/workingcalendar")
public class WorkingCalendarController {

    @Autowired
    private WorkingCalendarService workingCalendarService;

    @PostMapping("/registerWorkingCalendar")
    public ResponseEntity<?> registerWorkingCalendar(@RequestBody WorkingCalendar calendar) {
        try {
            workingCalendarService.saveWorkingCalendar(calendar);
            return ResponseEntity.ok().body("{\"success\": true}");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("{\"success\": false, \"message\": \"" + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<WorkingCalendar>> getAllWorkingCalendars() {
        List<WorkingCalendar> calendars = workingCalendarService.getAllWorkingCalendars();
        return ResponseEntity.ok(calendars);
    }
}
