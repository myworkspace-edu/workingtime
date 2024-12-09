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
		updateToDateEndOfMonth()
	});
	
	$('#loadPreviousMonthBtn').on('click', function () {
			loadPreviousMonthData();
	});
});

// Hàm load dữ liệu tháng trước
function loadPreviousMonthData() {
    var fromDate = $('#fromDate').val();

    if (!fromDate) {
        displayError("Vui lòng chọn ngày bắt đầu trước khi lấy dữ liệu tháng trước.");
        return;
    }

    var currentFromDate = new Date(fromDate);
    // Tính toán khoảng thời gian tháng trước
    var previousMonthStart = new Date(currentFromDate.getFullYear(), currentFromDate.getMonth() - 1, 1); // Ngày đầu tháng trước
    var previousMonthEnd = new Date(currentFromDate.getFullYear(), currentFromDate.getMonth(), 0); // Ngày cuối tháng trước

    // Lấy ngày cuối tháng của tháng hiện tại
    var lastDayOfMonth = new Date(currentFromDate.getFullYear(), currentFromDate.getMonth() + 1, 0);
    var formattedStartDate = formatDate(previousMonthStart);
    var formattedEndDate = formatDate(previousMonthEnd);
    var formattedLastDay = formatDate(lastDayOfMonth);

    if (hotCalendar && hotCalendar.getData().length > 0) {
        swal({
            title: "Bạn có chắc không?",
            text: "Nếu bạn tiếp tục, dữ liệu hiện tại sẽ bị thay thế.",
            icon: "warning",
            buttons: true,
            dangerMode: true,
        })
        .then((willLoad) => {
            if (willLoad) {
                $.ajax({
                    url: _ctx + 'teamcalendar/loaddata',
                    type: 'GET',
                    dataType: 'json',
                    data: {
                        fromDate: formattedStartDate,
                        toDate: formattedEndDate
                    },
                    success: function (res) {
                        console.log("Dữ liệu tháng trước:", res);
                        if (res) {
                            hotCalendar.loadData(res.data);
                            for (var i = 0; i < res.data.length; i++) {
                                res.data[i][10] = formatDate2(res.data[i][10]);
                                res.data[i][11] = formatDate2(res.data[i][11]);
                            }
                            $('#fromDate').val(fromDate); // Giữ nguyên ngày fromDate đã chọn
                            $('#toDate').val(formattedLastDay); // Cập nhật toDate là ngày cuối của tháng hiện tại
                        }
                    },
                    error: function (e) {
                        console.log("Error: ", e);
                        displayError("Không thể tải dữ liệu tháng trước.");
                    }
                });
            } else {
                // Nếu người dùng không đồng ý, không làm gì
                swal("Dữ liệu hiện tại vẫn được giữ lại!");
            }
        });
    }
}

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
				rowspan: 3,
				colspan: 1
			});
		}
	}
	
	// chuyển đổi ngày cho cột from và to
    for (var i = 0; i < tblCalendarData.data.length; i++) {
        tblCalendarData.data[i][10] = formatDate(tblCalendarData.data[i][10]);
        tblCalendarData.data[i][11] = formatDate(tblCalendarData.data[i][11]);
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
		
		beforeChange: function (changes) {
           // `changes` is an array of [row, column, oldValue, newValue]
           changes.forEach(function (change) {
               const [row, col, oldValue, newValue] = change;

               // Nếu cột nằm trong phạm vi cần in hoa (ví dụ: cột 1 đến cột 7)
               if (col >= 1 && col <= 8 && newValue !== null && newValue !== undefined) {
                   change[3] = newValue.toString().toUpperCase(); // Chuyển sang chữ in hoa
               }
           });
       },
		
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

    // loại bỏ các hàng am và pm dư thừa
    filteredData = filteredData.filter(row => {
        if ((row[1] === "AM" || row[1] === "PM") && row.slice(2).every(cell => !cell)) {
            return false;
        }
        return true;
    });

    // Tạo workbook và worksheet
    var workbook = new ExcelJS.Workbook();
    var worksheet = workbook.addWorksheet("Calendar Data");

    // chiều rộng cột được đặt tự động dựa trên độ dài nội dung
    filteredHeaders.forEach((header, index) => {
        var maxLength = filteredData.reduce((max, row) => {
            var cellLength = row[index] ? row[index].toString().length : 0;
            return Math.max(max, cellLength);
        }, header.length);
        worksheet.getColumn(index + 1).width = Math.min(maxLength + 2, 30);
    });

    // Thêm tiêu đề vào worksheet
    worksheet.addRow(filteredHeaders);

    // Thêm dữ liệu vào worksheet
    filteredData.forEach(row => {
        worksheet.addRow(row);
    });

    // Gộp các ô trong cột Account
    let currentMergeStart = null;
    worksheet.eachRow((row, rowIndex) => {
        if (rowIndex > 1) { // Bỏ qua hàng tiêu đề
            let accountCell = row.getCell(1); // Cột Account
            if (!accountCell.value) {
                // Nếu ô trống, thuộc cùng nhóm với hàng trước
                if (!currentMergeStart) {
                    currentMergeStart = rowIndex - 1;
                }
            } else {
                // Nếu ô không trống, kiểm tra có cần gộp hàng trước đó không
                if (currentMergeStart !== null) {
                    worksheet.mergeCells(currentMergeStart, 1, rowIndex - 1, 1);
                    currentMergeStart = null;
                }
                currentMergeStart = rowIndex; // Bắt đầu nhóm mới
            }
        }
    });
	// Gộp nhóm cuối nếu cần và nếu nhóm đó hợp lệ
	if (currentMergeStart !== null && currentMergeStart + 1 <= worksheet.rowCount) {
	    worksheet.mergeCells(currentMergeStart, 1, currentMergeStart + 1, 1);
	}

    // Áp dụng định dạng cho tất cả các ô
    worksheet.eachRow((row, rowIndex) => {
        row.eachCell((cell, colIndex) => {
            // Căn giữa
            cell.alignment = { vertical: 'middle', horizontal: 'center', wrapText: true };
            cell.border = {
                top: { style: 'thin' },
                left: { style: 'thin' },
                bottom: { style: 'thin' },
                right: { style: 'thin' }
            };
			
			cell.font = { size: 12 };

            if (rowIndex === 1) {
                cell.font = { size: 12, bold: true };
            }
        });
    });

    // Lưu file Excel
    workbook.xlsx.writeBuffer().then(function(buffer) {
        var blob = new Blob([buffer], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' });
        var link = document.createElement('a');
        link.href = URL.createObjectURL(blob);
        link.download = "TeamCalendar.xlsx";
        link.click();
    });
}


$('#exportExcelBtn').on('click', function() {
    exportToExcel();
});


function updateToDateEndOfMonth() {
    // Lấy giá trị từ fromDate
    var date = new Date($('#fromDate').val());

    // Xác định ngày cuối cùng của tháng
    var endOfMonth = new Date(date.getFullYear(), date.getMonth() + 1, 1);

    // Format ngày theo định dạng YYYY-MM-DD
    var formatDate = (d) => d.toISOString().split("T")[0];

    // Cập nhật giá trị của toDate
    document.getElementById("toDate").value = formatDate(endOfMonth);
}


// Hàm formatDate để định dạng ngày theo MM/DD/YYYY
function formatDate(date) {
    if (!date || isNaN(new Date(date))) {
        return '';  // Trả về chuỗi rỗng nếu ngày không hợp lệ
    }
    const d = new Date(date);
    const month = String(d.getMonth() + 1).padStart(2, '0');
    const day = String(d.getDate()).padStart(2, '0');
    const year = d.getFullYear();
    return `${month}/${day}/${year}`;
}
