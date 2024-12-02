/**
 * Processing events of search OKR by emails.
 */

$(document).ready(function() {
	loadTableData();

	$('#saveDataBtn').on('click', function() {
		saveTableData();

	});
	//    $('#loadDataBtn').on('click', function() {
	//        loadTableData();
	//    });
	$('#fromDate, #toDate').on('change', function() {
		loadTableData();
	});
});

/**
 * Load column width, header, initTable()
 */
function loadTableData() {
	var fromDate = $('#fromDate').val();
	var toDate = $('#toDate').val();
	$.ajax({
		url: _ctx + 'teamcalendar/loaddata',
		type: 'GET',
		dataType: 'json',
		data: {
			fromDate: fromDate,
			toDate: toDate
		},
		contentType: 'application/json',
		success: function(res) {
			console.log("res=" + JSON.stringify(res));
			if (res) {
				tblCalendarData = res;
				tblCalendarColHeaders = res.colHeaders;
				tblCalendarColWidths = res.colWidths;
				okrData = res.data;
				initTable();
			}
		},
		error: function(e) {
			console.log("Error: " + e);
		}
	});
}

function initTable() {
	var container = document.getElementById('tblCalendar');
	var mergeCells = [];

	// Kiểm tra và gộp các ô liên tiếp có cùng dữ liệu
	for (var row = 0; row < tblCalendarData.data.length - 1; row++) {
		if (tblCalendarData.data[row][0] === tblCalendarData.data[row + 1][0]) {
			mergeCells.push({
				row: row,
				col: 0,
				rowspan: 2,
				colspan: 1
			});
		}
	}

	hotCalendar = new Handsontable(container, {
		data: tblCalendarData.data,
		colHeaders: tblCalendarData.colHeaders,
		colWidths: tblCalendarData.colWidths,
		height: 290,
		rowHeaders: true,
		minRows: 10,
		currentRowClassName: 'currentRow',
		currentColClassName: 'currentCol',
		manualColumnResize: true,
		manualRowResize: true,
		minSpareRows: 1,
		contextMenu: true,
		mergeCells: mergeCells,
		licenseKey: 'non-commercial-and-evaluation',
		hiddenColumns: {
	        columns: [12], 
	        indicators: false 
	    },
		plugins: ['HiddenColumns'],
		cells: function(row, col, prop) {
			var cellProperties = {};

			if (col === 1) { // Cột section (col = 1)
				cellProperties.readOnly = true; // Khóa cột section lại
				cellProperties.renderer = function(instance, td, row, col, prop, value, cellProperties) {
					Handsontable.renderers.TextRenderer.apply(this, arguments);
					td.style.textAlign = 'center';
					td.style.backgroundColor = '#66CDAA';
					td.style.color = 'black';
				};
			} else {
				cellProperties.renderer = function(instance, td, row, col, prop, value, cellProperties) {
					Handsontable.renderers.TextRenderer.apply(this, arguments);
					td.style.textAlign = 'center';
					if (value === 'Y' || value === 'y') {
						td.style.backgroundColor = '#d1e7dd';
						td.style.color = 'black'; // Màu chữ đen
					} else if (value === 'N' || value === 'n') {
						td.style.backgroundColor = '#ffb3b3';
						td.style.color = 'black';
					} else {
						td.style.backgroundColor = '#FFFFFF';
						td.style.color = 'black';
					}
				};
			}

			return cellProperties;
		}
	});
}

function saveTableData() {
	var fromDate = $('#fromDate').val();
	var toDate = $('#toDate').val();
	var data = hotCalendar.getData();


	if (!fromDate || !toDate) {
		if (!fromDate) {
			displayError(errorMessages.FROM_DATE_REQUIRED);
		} else if (!toDate) {
			displayError(errorMessages.TO_DATE_REQUIRED);
		}
		return;
	}

	for (var row = 1; row < data.length; row += 2) {
		data[row][0] = data[row - 1][0];
	}

	if (new Date(toDate) <= new Date(fromDate)) {
		displayError(errorMessages.TO_DATE_AFTER_FROM_DATE);
		return;
	}

	var hasN = false;
	// Check for empty cells and 'N' in the table, and validate cell values
	for (var i = 0; i < data.length; i++) {
		if (data[i][0] !== null && data[i][0] !== "") {
			for (var j = 0; j < data[i].length - 1; j++) {
				var cellValue = data[i][j];
				// bỏ qua cột note, from date, to date, id
				if (j !== data[i].length - 1 && j !== data[i].length - 2 && j !== data[i].length - 3 && j !== data[i].length - 4) {
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
		}
	}

	// Tạo cấu trúc dữ liệu tương tự `TableStructure`
	var colHeaders = hotCalendar.getColHeader();
	var colWidths = [];
	for (let i = 0; i < colHeaders.length; i++) {
		let width = hotCalendar.getColWidth(i);
		colWidths.push(width);
	}

	var calendarModel = {
		fromDate: fromDate,
		toDate: toDate,
		colWidths: colWidths,
		colHeaders: colHeaders,
		data: data,
		colWidths: colWidths,   
        colHeaders: colHeaders,
		countRow: countRowsWithDataInColumn0()
	};

	$.ajax({
		url: _ctx + 'teamcalendar/savedata',
		type: 'POST',
		contentType: 'application/json',
		dataType: 'json',
		data: JSON.stringify(calendarModel),
		success: function(res) {
			displaySuccessMessage('Save successfully');
		},
		error: function(xhr, status, error) {
			console.error("Status: " + status);
			console.error("Error: " + error);
			console.error("Response: " + xhr.responseText);
			displayError('Save Failed');
		}
	});
}

function isSectionColumn(columnIndex) {
	// Determine AM, PM columns
	return columnIndex === 0 || columnIndex === 1;
}

function countRowsWithDataInColumn0() {
    var data = hotCalendar.getData(); 
    var count = 0;

    for (var i = 0; i < data.length; i++) {
        if (data[i][0] !== null && data[i][0].trim() !== "") {
            count++;
        }
    }

    console.log("Row count validate: ", count);
    return parseInt(count)*2;
}

function exportToExcel() {
    var data = hotCalendar.getData();
    var colHeaders = hotCalendar.getColHeader();
    var hiddenColumns = [12];

    // Loại bỏ cột id
    var filteredHeaders = colHeaders.filter((header, index) => !hiddenColumns.includes(index));
    var filteredData = data.map(row => row.filter((_, index) => !hiddenColumns.includes(index)));
	
	// Đảm bảo chiều rộng cột được đặt tự động dựa trên độ dài nội dung
    var colWidths = filteredHeaders.map((header, index) => {
        var maxLength = filteredData.reduce((max, row) => {
            var cellLength = row[index] ? row[index].toString().length : 0;
            return Math.max(max, cellLength);
        }, header.length);
        return Math.min(maxLength + 2, 30);
    });	

    // Chuẩn bị dữ liệu dạng mảng cho Excel
    var worksheetData = [filteredHeaders];
    worksheetData = worksheetData.concat(filteredData);

    // Tạo một workbook và worksheet bằng SheetJS
    var worksheet = XLSX.utils.aoa_to_sheet(worksheetData);
    var workbook = XLSX.utils.book_new();
    XLSX.utils.book_append_sheet(workbook, worksheet, "Calendar Data");
	
	// Áp dụng chiều rộng cột cho worksheet
    worksheet['!cols'] = colWidths.map(width => ({ wch: width }));

    // Áp dụng định dạng tự động bọc chữ cho tất cả các ô
    for (var row = 0; row < worksheetData.length; row++) {
        for (var col = 0; col < worksheetData[row].length; col++) {
            var cell = worksheet[XLSX.utils.encode_cell({ r: row, c: col })];
            if (!cell) continue;
            cell.s = {
                alignment: {
                    wrapText: true,
                    horizontal: 'center',
                    vertical: 'center'
                }
            };
        }
    }
		
    // Lưu file Excel
    XLSX.writeFile(workbook, "TeamCalendar.xlsx");
}

$('#exportExcelBtn').on('click', function() {
    exportToExcel();
});

