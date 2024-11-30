$(document).ready(function() {
	loadTableData();
	loadNoteData();

	$('#saveDataBtn').on('click', function() {
		saveTableData();
	});
	//    $('#loadDataBtn').on('click', function() {
	//        loadTableData();
	//        loadNoteData() 
	//    });
	
	$('#fromDate').on('change', function() {
		fitStartWeek()
	})
	
	$('#toDate').on('change', function() {
			fitEndWeek()
	})
	
	$('#fromDate, #toDate,#name').on('change', function() {
		loadTableData();
		loadNoteData();
	});

	$(document).ready(function() {
		$("#name").on("keydown", function(e) {
			console.log("Key pressed:", e.key);
			if (e.key === 'Enter') {
				e.preventDefault();
			}
		});
	});

});
/**
 * Load column width, header, initTable()
 */

function loadNoteData() {
	var fromDate = $('#fromDate').val();
	var toDate = $('#toDate').val();
	var name = $('#name').val();
	$.ajax({
		url: _ctx + 'handsontableData/notedata',
		type: 'GET',
		dataType: 'json',
		data: {
			fromDate: fromDate,
			toDate: toDate,
			name: name,
		},
		contentType: 'application/json',
		success: function(res) {
			console.log("res=" + JSON.stringify(res));
			if (res && Array.isArray(res) && res.length > 0) {
				var firstNote = res[0];
				var changeToString = firstNote.toString();
				$('#note').val(changeToString);
			} else {
				$('#note').val("");
			}
		},
		error: function(e) {
			console.log("Error: " + e);
		}
	});
}

function loadTableData() {
	var fromDate = $('#fromDate').val();
	var toDate = $('#toDate').val();
	var name = $('#name').val();
	$.ajax({
		url: _ctx + 'handsontableData/loaddata',
		type: 'GET',
		dataType: 'json',
		data: {
			fromDate: fromDate,
			toDate: toDate,
			name: name,
		},
		contentType: 'application/json',
		success: function(res) {
			console.log("res=" + JSON.stringify(res));
			if (res && res.data && res.data.length > 0) {
				tblCalendarData = res;
				tblCalendarColHeaders = res.colHeaders;
				tblCalendarColWidths = res.colWidths;
				okrData = res.data;
			} else {
				// Set default data when response data is empty
				tblCalendarData = {
					colHeaders: res.colHeaders,
					colWidths: res.colWidths,
					data: [
						["AM", "", "", "", "", "", "", ""],
						["PM", "", "", "", "", "", "", ""]
					]
				};
				tblCalendarColHeaders = tblCalendarData.colHeaders;
				tblCalendarColWidths = tblCalendarData.colWidths;
				okrData = tblCalendarData.data;
			}
			initTable();
		},
		error: function(e) {
			console.log("Error: " + e);
		}
	});
}

function initTable() {
	var container = document.getElementById('handsontable');
	hotProduct = new Handsontable(container, {
		data: tblCalendarData.data,
		colHeaders: tblCalendarData.colHeaders,
		colWidths: tblCalendarData.colWidths,
		height: 90,
		rowHeaders: true,
		minRows: 1,
		maxRows: 2,
		currentRowClassName: 'currentRow',
		currentColClassName: 'currentCol',
		manualColumnResize: true,
		manualRowResize: true,
		minSpareRows: 1,
		contextMenu: true,
		licenseKey: 'non-commercial-and-evaluation',
		cells: function(row, col, prop) {
			var cellProperties = {};

			// set read-only cho cột section
			if (col === 0 && (row === 0 || row === 1)) {
				cellProperties.readOnly = true;

				// Apply custom renderer for styling
				cellProperties.renderer = function(instance, td, row, col, prop, value, cellProperties) {
					Handsontable.renderers.TextRenderer.apply(this, arguments);
					td.style.textAlign = 'center';
					td.style.backgroundColor = '#66CDAA';  // Custom background color
					td.style.color = 'black';  // Text color
				};
			} else {
				// Handle other columns
				cellProperties.renderer = function(instance, td, row, col, prop, value, cellProperties) {
					Handsontable.renderers.TextRenderer.apply(this, arguments);
					td.style.textAlign = 'center';

					if (value === 'Y' || value === 'y') {
						td.style.backgroundColor = '#d1e7dd';  // Background for 'Y'
						td.style.color = 'black';
					} else if (value === 'N' || value === 'n') {
						td.style.backgroundColor = '#ffb3b3';  // Background for 'N'
						td.style.color = 'black';
					} else {
						td.style.backgroundColor = '#FFFFFF';  // Default background
						td.style.color = 'black';
					}
				};
			}

			return cellProperties;
		}
	});
}


function saveTableData() {
	var id = $('#id').val();
	var fromDate = $('#fromDate').val();
	var toDate = $('#toDate').val();
	var name = $('#name').val();
	var note = $('#note').val();
	var updatedData = hotProduct.getData();

	// Regex để kiểm tra ký tự đặc biệt
	var specialCharPattern = /[!@#$%^&*(),.?":{}|<>]/g;

	// Kiểm tra ký tự đặc biệt trong tên
	if (specialCharPattern.test(name)) {
		displayError(errorMessages.SPECIAL_CHAR_NOT_ALLOWED);
		return;
	}

	// Kiểm tra độ dài tên
	if (name.length > 30) {
		displayError(errorMessages.NAME_TOO_LONG);
		return;
	} else if (name.length < 8) {
		displayError(errorMessages.NAME_TOO_SHORT);
		return;
	}

	// Kiểm tra các trường bắt buộc
	if (!fromDate || !toDate || !name) {
		if (!fromDate) {
			displayError(errorMessages.FROM_DATE_REQUIRED);
		} else if (!toDate) {
			displayError(errorMessages.TO_DATE_REQUIRED);
		} else if (!name) {
			displayError(errorMessages.NAME_REQUIRED);
		}
		return;
	}

	// Kiểm tra 'toDate' phải sau 'fromDate'
	if (new Date(toDate) <= new Date(fromDate)) {
		displayError(errorMessages.TO_DATE_AFTER_FROM_DATE);
		return;
	}

	// Kiểm tra giá trị của các ô trong bảng
	var hasN = false;
	for (var i = 0; i < updatedData.length; i++) {
		for (var j = 0; j < updatedData[i].length; j++) {
			var cellValue = updatedData[i][j];
			if (!cellValue) {
				displayError(errorMessages.ALL_CELLS_FILLED);
				return;
			}
			if (!isSectionColumn(j) && cellValue.toString().toUpperCase() !== 'Y' && cellValue.toString().toUpperCase() !== 'N') {
				displayError(errorMessages.INVALID_CELL_VALUE);
				return;
			}
			if (cellValue.toString().toLowerCase() === 'n') {
				hasN = true;
			}
		}
	}

	// Nếu có 'N' trong dữ liệu thì phải có ghi chú
	if (hasN && (!note || note.trim() === "")) {
		displayError(errorMessages.MUST_REGISTER_NOTE);
		return;
	}

	// Tạo đối tượng `TableStructure` tương tự như trong Task
	var colHeaders = hotProduct.getColHeader();
	var colWidths = [];
	for (let i = 0; i < colHeaders.length; i++) {
		let width = hotProduct.getColWidth(i);
		colWidths.push(width);
	}

    // Tạo đối tượng `registerCalendarModel`
    var registerCalendarModel = {
		id: id,
        name: name,
        fromDate: fromDate,
        toDate: toDate,
        data: updatedData, // Directly use the updatedData array
        note: note,
        colWidths: colWidths,   
        colHeaders: colHeaders 
    };


	$.ajax({
		url: _ctx + 'handsontableData/savedata',
		type: 'POST',
		data: JSON.stringify(registerCalendarModel),
		dataType: 'json',
		contentType: 'application/json',
		success: function(res) {
			displaySuccessMessage('Save successfully');
		},
		error: function(xhr, status, error) {
			console.error("Status: " + status);
			console.error("Error: " + error);
			console.error("Response: " + xhr.responseText);
			// Thêm thông tin chi tiết từ response nếu có
			var response = JSON.parse(xhr.responseText);
			console.error("Response Detail: " + response.detail);
			displayError('Save Failed');
		}
	});
}


// Method to determine section columns
function isSectionColumn(columnIndex) {
	return columnIndex === 0 || columnIndex === 1;
}

function fitStartWeek() {
    var date = new Date($('#fromDate').val()); 

    var startOfWeek = new Date(date);
    var endOfWeek = new Date(date);

    // Check if the day is Sunday (getDay() == 0)
    if (date.getDay() === 0) {
        // Sunday: endOfWeek is the date itself
        endOfWeek = new Date(date);
        startOfWeek.setDate(date.getDate() - 6); // Go back 6 days
    } else {
        startOfWeek.setDate(date.getDate() - date.getDay() + 1); // Move to Monday
        endOfWeek.setDate(startOfWeek.getDate() + 6); // Add 6 days to get Sunday
    }

    // Format dates as YYYY-MM-DD for the input fields
    var formatDate = (d) => d.toISOString().split("T")[0];

    // Set the values in the input fields
    document.getElementById("fromDate").value = formatDate(startOfWeek);
    document.getElementById("toDate").value = formatDate(endOfWeek);
}


function fitEndWeek() {
    var date = new Date($('#toDate').val());

    var startOfWeek = new Date(date);
    var endOfWeek = new Date(date);

    if (date.getDay() === 0) {
        // If it's Sunday, startOfWeek is 6 days before
        startOfWeek.setDate(date.getDate() - 6);
    } else {
        startOfWeek.setDate(date.getDate() - date.getDay() + 1); // Move to Monday
        endOfWeek.setDate(startOfWeek.getDate() + 6); // Move to Sunday
    }

    // Format dates as YYYY-MM-DD
    var formatDate = (d) => d.toISOString().split("T")[0];

    // Update the input fields
    document.getElementById("fromDate").value = formatDate(startOfWeek);
    document.getElementById("toDate").value = formatDate(endOfWeek);
}
