/**
 * Processing events of search OKR by emails.
 */
$(document).ready(function() {
    loadTableData();
    setupRegisterForm(); // Call the setupRegisterForm function
});

/**
 * Load column width, header, initTable()
 */
function loadTableData() {
    $.ajax({
        url: _ctx + 'handsontable/loaddata',
        type: 'GET',
        dataType: 'json',
        contentType: 'application/json',
        success: function(res) {
            console.log("res=" + JSON.stringify(res));

            if (res) {
                tblProductData = res.data;
                tblProductColHeaders = res.colHeaders;
                tblProductColWidths = res.colWidths;
                okrData = res.data;
                initTable();
            }
        },
        error: function(e) {
            console.error("Error: " + e);
        }
    });
}

function initTable() {
    var container = document.getElementById('tblProduct');

    hotProduct = new Handsontable(container, {
        data: tblProductData,
        colHeaders: tblProductColHeaders,
        colWidths: tblProductColWidths,
        height: 800,
        rowHeaders: true,
        minRows: 14,
        currentRowClassName: 'currentRow',
        currentColClassName: 'currentCol',
        manualColumnResize: true,
        manualRowResize: true,
        minSpareRows: 1,
        contextMenu: true,
        licenseKey: 'non-commercial-and-evaluation'
    });
}

const container = document.querySelector('#handsontable');
document.addEventListener("DOMContentLoaded", function() {
    const data = [
        ["", "", "N", "N", "N", "N", "N", "N", "N"],
        // ...
    ];

    const example = document.getElementById("handsontable");

    new Handsontable(example, {
        data,
        height: 450,
        colWidths: [170, 156, 80, 80, 80, 80, 80, 80, 80],
        colHeaders: [
            "Account",
            {
                type: "dropdown",
                source: ["AM", "PM"],
                title: "Section",
            },
            "Mon",
            "Tue",
            "Wed",
            "Thu",
            "Fri",
            "Sat",
            "Sun",
        ],
        columns: [
            { data: 0, type: "text" },
            {
                data: 1,
                type: "dropdown",
                source: ["AM", "PM"],
            },
            { data: 2, type: "checkbox", checkedTemplate: "Y", uncheckedTemplate: "N" },
            { data: 3, type: "checkbox", checkedTemplate: "Y", uncheckedTemplate: "N" },
            { data: 4, type: "checkbox", checkedTemplate: "Y", uncheckedTemplate: "N" },
            { data: 5, type: "checkbox", checkedTemplate: "Y", uncheckedTemplate: "N" },
            { data: 6, type: "checkbox", checkedTemplate: "Y", uncheckedTemplate: "N" },
            { data: 7, type: "checkbox", checkedTemplate: "Y", uncheckedTemplate: "N" },
            { data: 8, type: "checkbox", checkedTemplate: "Y", uncheckedTemplate: "N" },
        ],
        dropdownMenu: true,
        hiddenColumns: {
            indicators: true,
        },
        contextMenu: true,
        filters: true,
        rowHeaders: true,
        manualRowMove: true,
        autoWrapCol: true,
        autoWrapRow: true,
        licenseKey: "non-commercial-and-evaluation",
    });
});

function setupRegisterForm() {
    $('#registrationForm').on('submit', function(event) {
        event.preventDefault();

        var fromDate = $('#fromDate').val();
        var toDate = $('#toDate').val();
        var status = 'active'; // You can modify this according to your needs

        $.ajax({
            url: _ctx + 'handsontable/register',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({ fromDate: fromDate, toDate: toDate, status: status }),
            success: function(response) {
                console.log("Response: " + JSON.stringify(response));
                if (response.success) {
                    var newData = [fromDate, toDate, status];
                    hotProduct.getSourceData().push(newData);
                    hotProduct.render(); // Re-render the table to reflect the new data
                    $('#registerForm').modal('hide');
                } else {
                    alert("Failed to register working calendar: " + response.message);
                }
            },
            error: function(error) {
                console.error("Error: " + JSON.stringify(error));
                alert("Failed to register working calendar. Please try again later.");
            }
        });
    });
}

$(document).ready(function() {
    loadTableData();
    setupRegisterForm();
});
