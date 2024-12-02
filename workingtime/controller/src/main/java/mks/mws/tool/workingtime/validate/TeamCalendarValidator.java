package mks.mws.tool.workingtime.validate;

import mks.mws.tool.workingtime.controller.model.CalendarModel;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import java.util.Arrays;
import java.util.Date;

@Component
@Slf4j
public class TeamCalendarValidator {

	public boolean validate(CalendarModel calendarModel) {
		// Validate fromDate and toDate
		Date fromDate = calendarModel.getFromDate();
		Date toDate = calendarModel.getToDate();
		
		if (fromDate == null) {
			log.error("Validation Error: fromDate is null.");
			return false;
		}

		if (toDate == null) {
			log.error("Validation Error: toDate is null.");
			return false;
		}

		if (toDate.before(fromDate)) {
			log.error("Validation Error: toDate is earlier than fromDate.");
			return false;
		}

		// Validate table data
		List<Object[]> data = calendarModel.getData();
		if (data == null || data.isEmpty()) {
			log.error("Validation Error: No data available in the table.");
			return false;
		}

		boolean atLeastOneValidPair = false;

		// Process rows in pairs
		for (int rowIndex = 0; rowIndex < calendarModel.getCountRow(); rowIndex += 2) {
			if (rowIndex + 1 >= data.size()) {
				log.debug("Incomplete pair detected at row index: " + rowIndex);
				continue; // Skip validation for incomplete pair
			}

			Object[] row1 = data.get(rowIndex);
			Object[] row2 = data.get(rowIndex + 1);

			// Log rows for debugging
			log.debug("Row " + (rowIndex + 1) + ": " + Arrays.toString(row1));
			log.debug("Row " + (rowIndex + 2) + ": " + Arrays.toString(row2));

			boolean validPair = true;

			// Validate each row in the pair
			if (!validateRow(row1, rowIndex + 1)) {
				validPair = false;
			}

			if (!validateRow(row2, rowIndex + 2)) {
				validPair = false;
			}

			if (validPair) {
				atLeastOneValidPair = true;
			}
		}

		if (!atLeastOneValidPair) {
			log.error("Validation Error: No valid row pairs found.");
			return false;
		}

		log.info("Validation successful: At least one valid row pair is found.");
		return true;
	}

	private boolean validateRow(Object[] row, int rowIndex) {
		// Validate that all cells except the 'Section' column (index 1) are non-empty
		for (int colIndex = 2; colIndex < row.length - 4; colIndex++) {
			Object cell = row[colIndex];
			if (cell == null || cell.toString().trim().isEmpty()) {
				log.error("Validation Error: Empty cell found in row " + rowIndex + ", column " + (colIndex + 1));
				return false;
			}

			// Validate only 'Y/y/N/n' values are allowed
			String cellValue = cell.toString().trim().toLowerCase();
			if (!"y".equals(cellValue) && !"n".equals(cellValue)) {
				log.error("Validation Error: Invalid value '" + cell + "' found in row " + rowIndex + ", column "
						+ (colIndex + 1) + ". Expected 'Y/y/N/n'.");
				return false;
			}
		}
		return true;
	}
}
