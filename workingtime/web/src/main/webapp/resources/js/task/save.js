/**
 * Process event click on button "Save" in screen "Task".
 */
$(document).ready(function() {
    $('#frmTask').submit(function(e) {
        e.preventDefault();
        
        var colHeaders = htTask.getColHeader();
        var tableData = htTask.getData();
        
        // Build array of column width
        var colWidths=[]
        for (let i = 0; i < colHeaders.length; i++) {
            let w = htTask.getColWidth(i);
            colWidths.push(w);
        }

        var formDataJson = JSON.stringify({"colWidths": colWidths, "colHeaders": colHeaders, "data": tableData});
        
        $.ajax({	
            url : _ctx + 'task/save',
            type : 'POST',
            data : formDataJson,
            dataType: "json",
            contentType: 'application/json',
            success : function(result) {
                console.log("Result:" + JSON.stringify(result));
                updateData(result);
            },
            error : function() {
                console.log("Error!");
            }
        });
    });
});

/**
 * Update the Handsontable with new data
 */
function updateData(result) {
    htTask.loadData(result.data);
    
    
    // So the result message
    $("#success-alert").fadeTo(2000, 500).slideUp(500, function() {
        $("#success-alert").slideUp(500);
    });
}


///**
// * Process event click on button "Save" in screen "Task".
// */
//$(document).ready(function() {
//
//	$('#frmTask').submit(function(e) {
//		e.preventDefault();
//
//		// Kiểm tra xem htTask đã được khởi tạo hay chưa
//		if (!htTask) {
//			console.log("Handsontable instance not found.");
//			return;
//		}
//
//		var colHeaders = htTask.getSettings().colHeaders;
//		var tableData = htTask.getData();
//
//		// Build array of column width
//		var colWidths = []
//		for (let i = 0; i < colHeaders.length; i++) {
//			let w = htTask.getColWidth(i);
//			colWidths.push(w);
//		}
//
//
//		var formDataJson = JSON.stringify({ "colWidths": colWidths, "colHeaders": colHeaders, "data": tableData });
//
//
//		$.ajax({
//			url: _ctx + 'task/save',
//			type: 'POST',
//			data: formDataJson,
//			dataType: "json",
//			contentType: 'application/json',
//			success: function(result) {
//				// result = JSON.parse(result);
//				console.log("Result:" + JSON.stringify(result));
//				updateData(result);
//			},
//			error: function(xhr, status, error) {
//				console.log("Error:", status, error);
//			}
//		});
//	});
//});
//
///**
// * 
// */
//function updateData(result) {
//	// Kiểm tra dữ liệu trước khi tải vào Handsontable
//	if (result && result.data) {
//		htTask.loadData(result.data);
//	}
//	// So the result message
//	$("#success-alert").fadeTo(2000, 500).slideUp(500, function() {
//		$("#success-alert").slideUp(500);
//	});
//}
