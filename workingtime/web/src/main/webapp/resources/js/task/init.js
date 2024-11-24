var htTask;

/**
 * Processing after the webpage are loaded in the browser.
 */
$(document).ready(function() {
	loadTableData();
});


/**
 * Load column width, header, initTable()
 */
function loadTableData() {

	$.ajax({
		url: _ctx + 'task/load',
		type: 'GET',
		dataType: 'json',
		contentType: 'application/json',
		success: function(res) {
			console.log("res=" + JSON.stringify(res));

			if (res) {
				initTable(res.colHeaders, res.colWidths, res.data, res.columnTypes);
			}
		},
		error: function(e) {
			console.log("Error: " + e);
		}
	});
}

function initTable(colHeaders, colWidths, data) {
    var container = document.getElementById('tblTask');
    
    htTask = new Handsontable(container, {
        data: data,
        colHeaders: colHeaders,
        colWidths: colWidths,
        columns: [
            { type: 'text' }, // ID
            { type: 'date', dateFormat: 'YYYY-MM-DD', correctFormat: true }, // Date
            { type: 'text' }, // Name
            { type: 'text' }, // Category
            { type: 'text' }, // Task name
            { type: 'text' }, // Output product
            { type: 'text' }, // PIC
            { type: 'dropdown', source: ['Open', 'Doing', 'Done'] }, // Status
            { type: 'text' }, // Project
            { type: 'date', dateFormat: 'YYYY-MM-DD', correctFormat: true }, // Deadline
            { type: 'text' }  // Note
        ],
        rowHeaders: true,
        minRows: 8,
        currentRowClassName: 'currentRow',
        currentColClassName: 'currentCol',
        manualColumnResize: true,
        manualRowResize: true,
        minSpareRows: 1,
        contextMenu: true,
        licenseKey: 'non-commercial-and-evaluation'
    });
}
//var htTask;
//
///**
// * Processing after the webpage are loaded in the browser.
// */
//$(document).ready(function() {
//    loadTableData();
//});
//
///**
// * Load column width, header, initTable()
// */
//function loadTableData() {
//    $.ajax({
//        url : _ctx + 'task/load',
//        type : 'GET',
//        dataType : 'json',
//        contentType : 'application/json',
//        success : function(res) {
//            console.log("res=" + JSON.stringify(res));
//
//            if (res) {
//                initTable(res.colHeaders, res.colWidths, res.data, res.columnTypes);
//            }                
//        },
//        error : function (e) {
//            console.log("Error: " + e);
//        }
//    });
//}
//
//function initTable(colHeaders, colWidths, data, columnTypes) {
//    var container = document.getElementById('tblTask');
//    
//    htTask = new Handsontable(container, {
//        data: data,
//        colHeaders: colHeaders,
//        colWidths: colWidths,
//        columns: columnTypes,
//        rowHeaders: true,
//        minRows: 8,
//        currentRowClassName: 'currentRow',
//        currentColClassName: 'currentCol',
//        manualColumnResize: true,
//        manualRowResize: true,
//        minSpareRows : 1,
//        contextMenu: true,
//        licenseKey: 'non-commercial-and-evaluation'
//    });
//}
