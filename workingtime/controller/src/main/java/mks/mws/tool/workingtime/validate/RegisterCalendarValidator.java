package mks.mws.tool.workingtime.validate;

import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;
import mks.mws.tool.workingtime.controller.model.RegisterCalendarModel;

import java.util.regex.Pattern;

@Component
@Slf4j
public class RegisterCalendarValidator {

    // Regex to check for special characters
    private static final Pattern SPECIAL_CHAR_PATTERN = Pattern.compile("[!@#$%^&*(),.?\":{}|<>]");

    public boolean validate(RegisterCalendarModel registerCalendarModel) {
        // Check for null or empty required fields
        if (registerCalendarModel.getName() == null || registerCalendarModel.getName().trim().isEmpty() ||
            registerCalendarModel.getFromDate() == null ||
            registerCalendarModel.getToDate() == null ||
            registerCalendarModel.getData() == null || registerCalendarModel.getData().isEmpty()) {

            log.error("validation.error.required.fields");
            return false;
        }

        // Check for special characters in the name
        if (SPECIAL_CHAR_PATTERN.matcher(registerCalendarModel.getName()).find()) {
            log.error("validation.error.special.characters.in.name");
            return false;
        }

        // Check if toDate is after fromDate
        if (registerCalendarModel.getToDate().before(registerCalendarModel.getFromDate())) {
            log.error("validation.error.toDate.before.fromDate");
            return false;
        }

        // Check for invalid values in the data array
        boolean hasN = false;
        for (int rowIndex = 0; rowIndex < registerCalendarModel.getData().size(); rowIndex++) {
            Object[] row = registerCalendarModel.getData().get(rowIndex);
            for (int colIndex = 0; colIndex < row.length; colIndex++) {
                Object cell = row[colIndex];
                String cellValue = cell == null ? "" : cell.toString().trim();

                // Skip checking Section columns (e.g., AM, PM)
                if (isSectionColumn(colIndex)) {
                    continue;
                }

                // Check for empty cells
                if (cellValue.isEmpty()) {
                    log.error("validation.error.empty.cell");
                    return false;
                }

                // Check for valid 'Y' or 'N' values
                if (!cellValue.equalsIgnoreCase("Y") && !cellValue.equalsIgnoreCase("N")) {
                    log.error("validation.error.invalid.cell.value");
                    return false;
                }

                // Set flag if 'N' is found
                if (cellValue.equalsIgnoreCase("N")) {
                    hasN = true;
                }
            }
        }

        // Check for note if any 'N' is present
        if (hasN && (registerCalendarModel.getNote() == null || registerCalendarModel.getNote().trim().isEmpty())) {
            log.error("validation.error.note.required");
            return false;
        }

        return true;
    }

    // Method to identify Section columns (AM, PM)
    private boolean isSectionColumn(int columnIndex) {
        // Identify columns for AM, PM
        return columnIndex == 0 || columnIndex == 1;
    }
}
