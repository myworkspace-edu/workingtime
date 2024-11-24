package mks.mws.tool.workingtime.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;
import mks.mws.tool.workingtime.common.model.TableStructure;
import mks.mws.tool.workingtime.controller.model.CalendarModel;
import mks.mws.tool.workingtime.service.TeamWorkingCalendarService;
import mks.mws.tool.workingtime.validate.TeamCalendarValidator;

/**
 * Controller for managing team working calendar data.
 *
 * <p>
 * This controller provides endpoints for displaying the team calendar page,
 * loading calendar data, and saving calendar data.
 * </p>
 *
 * <ul>
 * <li>{@code GET /teamcalendar}: Displays the team calendar page with default
 * dates if not set in the session.</li>
 * <li>{@code GET /teamcalendar/loaddata}: Retrieves the calendar data for a
 * specified date range.</li>
 * <li>{@code POST /teamcalendar/savedata}: Saves the calendar data.</li>
 * </ul>
 */
@Controller
@RequestMapping("/teamcalendar")
@Slf4j
public class TeamWorkingCalendarController extends BaseController {

	@Value("${calendarList.colHeaders}")
	private String[] calendarListColHeaders;

	@Value("${calendarList.colWidths}")
	private int[] calendarListColWidths;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private TeamWorkingCalendarService teamWorkingCalendarService;

	@Autowired
	private TeamCalendarValidator teamCalendarValidator;

	@GetMapping(value = "")
	public ModelAndView displayHome(HttpSession httpSession) {
		String fromDate = (String) httpSession.getAttribute("fromDate");
		String toDate = (String) httpSession.getAttribute("toDate");

		if (fromDate == null || toDate == null) {
			LocalDate today = LocalDate.now();
			fromDate = today.toString();
			toDate = today.toString();

			httpSession.setAttribute("fromDate", fromDate);
			httpSession.setAttribute("toDate", toDate);
		}

		ModelAndView mav = new ModelAndView("teamcalendar");
		mav.addObject("fromDate", fromDate);
		mav.addObject("toDate", toDate);
		return mav;
	}

	@GetMapping(value = { "/loaddata" }, produces = "application/json")
	@ResponseBody
	public TableStructure getProductTableData(@RequestParam String fromDate, @RequestParam String toDate,
			HttpSession httpSession, Locale locale) {

		httpSession.setAttribute("fromDate", fromDate);
		httpSession.setAttribute("toDate", toDate);

		List<Object[]> lstCalendars = teamWorkingCalendarService.getAllTeamWorkingCalendarData(fromDate, toDate);
		for (int i = 0; i < 6; i++) {
			lstCalendars.add(new Object[] { "", "AM", "", "", "", "", "", "", "" });
			lstCalendars.add(new Object[] { "", "PM", "", "", "", "", "", "", "" });
		}

		// Lấy headers theo ngôn ngữ hiện tại
		String[] calendarListColHeaders = messageSource.getMessage("calendarList.colHeaders", null, locale).split(",");

		TableStructure productTable = new TableStructure(calendarListColWidths, calendarListColHeaders, lstCalendars);

		return productTable;
	}

	@PostMapping(value = "/savedata", consumes = "application/json", produces = "application/json")
	@ResponseBody
	public ResponseEntity<Map<String, String>> saveCalendarData(@RequestBody CalendarModel calendarModel) {
		log.debug("Received CalendarModel: {}", calendarModel);

		if (!teamCalendarValidator.validate(calendarModel)) {
			log.error("Invalid input data. Data not saved.");
			Map<String, String> errorResponse = new HashMap<>();
			errorResponse.put("message", "Error: Invalid input data. Data not saved.");
			return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
		}

		try {
			teamWorkingCalendarService.saveTeamWorkingCalendarData(calendarModel.getFromDate(),
					calendarModel.getToDate(), calendarModel.getData());

			Map<String, String> successResponse = new HashMap<>();
			successResponse.put("message", "Data saved successfully");
			return new ResponseEntity<>(successResponse, HttpStatus.OK);
		} catch (Exception ex) {
			log.error("Could not save calendar data.", ex);
			Map<String, String> errorResponse = new HashMap<>();
			errorResponse.put("message", "An error occurred while saving the data.");
			return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}

//	@PostMapping(value = "/savedata", consumes = "application/json", produces = "application/json")
//	@ResponseBody
//	public ResponseEntity<Map<String, String>> saveCalendarData(@RequestBody CalendarModel calendarModel) {
//		// Log the values of the CalendarModel
//	    log.debug("Received CalendarModel: {}", calendarModel);
//
//		if (!teamCalendarValidator.validate(calendarModel)) {
//			log.error("Invalid input data. Data not saved.");
//			Map<String, String> errorResponse = new HashMap<>();
//			errorResponse.put("message", "Error: Invalid input data. Data not saved.");
//			return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
//		}
//
//		teamWorkingCalendarService.saveTeamWorkingCalendarData(calendarModel.getFromDate(), calendarModel.getToDate(),
//				calendarModel.getData());
//
//		Map<String, String> successResponse = new HashMap<>();
//		successResponse.put("message", "Data saved successfully");
//		return new ResponseEntity<>(successResponse, HttpStatus.OK);
//	}
//}
