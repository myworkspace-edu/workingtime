package mks.mws.tool.workingtime.controller;

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

import lombok.extern.slf4j.Slf4j;
import mks.mws.tool.workingtime.common.model.TableStructure;
import mks.mws.tool.workingtime.controller.model.RegisterCalendarModel;
import mks.mws.tool.workingtime.service.TeamWorkingCalendarService;
import mks.mws.tool.workingtime.validate.RegisterCalendarValidator;

/**
 * Controller to handle requests related to Handsontable data.
 */
@Controller
@RequestMapping("/handsontableData")
@Slf4j
public class HandsontableDataController extends BaseController {

	@Value("${registerCalendarList.colHeaders}")
	private String[] calendarListColHeaders;

	@Value("${registerCalendarList.colWidths}")
	private int[] calendarListColWidths;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private TeamWorkingCalendarService teamWorkingCalendarService;

	@Autowired
	private RegisterCalendarValidator registerCalendarValidator;

	/**
	 * Retrieves the calendar table data.
	 *
	 * @param name        the name of the calendar
	 * @param fromDate    the start date
	 * @param toDate      the end date
	 * @param httpSession the HTTP session
	 * @return the table structure containing the calendar data
	 */
	@GetMapping(value = { "/loaddata" }, produces = "application/json")
	@ResponseBody
	public TableStructure getProductTableData(@RequestParam String name, @RequestParam String fromDate,
			@RequestParam String toDate, HttpSession httpSession, Locale locale) {
		
		httpSession.setAttribute("fromDate", fromDate);
		httpSession.setAttribute("toDate", toDate);
		
		List<Object[]> lstCalendars = teamWorkingCalendarService.getRegisterWorkingCalendarData(name, fromDate, toDate);

		// Fetch localized column headers and widths
		String[] calendarListColHeaders = messageSource.getMessage("registerCalendarList.colHeaders", null, locale).split(",");

		TableStructure productTable = new TableStructure(calendarListColWidths, calendarListColHeaders, lstCalendars);
		
		return productTable;
	}

	/**
	 * Retrieves the note data for a specified calendar within a date range.
	 *
	 * @param name        the name of the calendar
	 * @param fromDate    the start date of the range
	 * @param toDate      the end date of the range
	 * @param httpSession the HTTP session to store the date range attributes
	 * @return a list of note data objects
	 */

	@GetMapping(value = { "/notedata" }, produces = "application/json")
	@ResponseBody
	public List<Object[]> getNoteData(@RequestParam String name, @RequestParam String fromDate,
			@RequestParam String toDate, HttpSession httpSession) {

		httpSession.setAttribute("fromDate", fromDate);
		httpSession.setAttribute("toDate", toDate);

		return teamWorkingCalendarService.getNotesByAccountAndDateRange(name, fromDate, toDate);
	}

	/**
	 * Saves the calendar data.
	 *
	 * @param registerCalendarModel the model containing the calendar data
	 * @return the response entity with the result of the save operation
	 */
	@PostMapping(value = "/savedata", consumes = "application/json", produces = "application/json")
	@ResponseBody
	public ResponseEntity<Map<String, String>> saveCalendarData(
			@RequestBody RegisterCalendarModel registerCalendarModel) {
		log.debug("Received RegisterCalendarModel: {}", registerCalendarModel);

		if (registerCalendarModel == null || !registerCalendarValidator.validate(registerCalendarModel)) {
			log.error("Invalid input data. Data not saved.");
			Map<String, String> errorResponse = new HashMap<>();
			errorResponse.put("response", "Error: Invalid input data. Data not saved.");
			return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
		}

		try {
			teamWorkingCalendarService.saveRegisterWorkingCalendarData(registerCalendarModel.getName(),
					registerCalendarModel.getFromDate(), registerCalendarModel.getToDate(),
					registerCalendarModel.getData(), registerCalendarModel.getNote());

			Map<String, String> successResponse = new HashMap<>();
			successResponse.put("response", "Data saved successfully");
			return new ResponseEntity<>(successResponse, HttpStatus.OK);
		} catch (Exception ex) {
			log.error("Could not save calendar data.", ex);
			Map<String, String> errorResponse = new HashMap<>();
			errorResponse.put("response", "An error occurred while saving the data.");
			return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
//	@PostMapping(value = "/savedata", consumes = "application/json", produces = "application/json")
//	@ResponseBody
//	public ResponseEntity<Map<String, String>> saveCalendarData(@RequestBody RegisterCalendarModel registerCalendarModel) {
//	    log.debug("Received RegisterCalendarModel: {}", registerCalendarModel);
//
//	    // Check if the received model is valid
//	    if (registerCalendarModel == null || !registerCalendarValidator.validate(registerCalendarModel)) {
//	        log.error("Invalid input data. Data not saved.");
//	        Map<String, String> errorResponse = new HashMap<>();
//	        errorResponse.put("response", "Error: Invalid input data. Data not saved.");
//	        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
//	    }
//
//	    try {
//	        // Process and save data
//	        teamWorkingCalendarService.saveRegisterWorkingCalendarData(
//	            registerCalendarModel.getName(),
//	            registerCalendarModel.getFromDate(),
//	            registerCalendarModel.getToDate(),
//	            registerCalendarModel.getData(),
//	            registerCalendarModel.getNote()
//	        );
//
//	        Map<String, String> successResponse = new HashMap<>();
//	        successResponse.put("response", "Data saved successfully");
//	        return new ResponseEntity<>(successResponse, HttpStatus.OK);
//	    } catch (Exception ex) {
//	        log.error("Could not save calendar data.", ex);
//	        Map<String, String> errorResponse = new HashMap<>();
//	        errorResponse.put("response", "An error occurred while saving the data.");
//	        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
//	    }
//	}
//}
