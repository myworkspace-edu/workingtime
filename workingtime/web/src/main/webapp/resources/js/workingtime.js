$(document).ready(function() {
    loadTableData();
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
                initTable(tblProductData, tblProductColHeaders, tblProductColWidths);
            }
        },
        error: function(e) {
            console.error("Error: " + e);
        }
    });
}

function initTable(data, colHeaders, colWidths) {
    const container = document.getElementById('handsontable');
    
    // Add the new columns for From and To dates
    colHeaders.push("From", "To");
    colWidths.push(100, 100);

    const columns = [
        { data: 0, type: "text" }, // Account column
        { data: 1, type: "dropdown", source: ["AM", "PM"] }, // Section column
        { data: 2, type: "checkbox", checkedTemplate: "Y", uncheckedTemplate: "N" },
        { data: 3, type: "checkbox", checkedTemplate: "Y", uncheckedTemplate: "N" },
        { data: 4, type: "checkbox", checkedTemplate: "Y", uncheckedTemplate: "N" },
        { data: 5, type: "checkbox", checkedTemplate: "Y", uncheckedTemplate: "N" },
        { data: 6, type: "checkbox", checkedTemplate: "Y", uncheckedTemplate: "N" },
        { data: 7, type: "checkbox", checkedTemplate: "Y", uncheckedTemplate: "N" },
        { data: 8, type: "checkbox", checkedTemplate: "Y", uncheckedTemplate: "N" },
    ];

    new Handsontable(container, {
        data: data,
        colHeaders: colHeaders,
        colWidths: colWidths,
        columns: columns,
        height: 450,
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
}

document.addEventListener("DOMContentLoaded", function() {
    const data = [
        ["", "", "N", "N", "N", "N", "N", "N", "N", "", ""],
        ["", "", "N", "N", "N", "N", "N", "N", "N", "", ""],
        ["", "", "N", "N", "N", "N", "N", "N", "N", "", ""],
        ["", "", "N", "N", "N", "N", "N", "N", "N", "", ""],
        ["", "", "N", "N", "N", "N", "N", "N", "N", "", ""],
        ["", "", "N", "N", "N", "N", "N", "N", "N", "", ""],
        ["", "", "N", "N", "N", "N", "N", "N", "N", "", ""],
        ["", "", "N", "N", "N", "N", "N", "N", "N", "", ""],
        ["", "", "N", "N", "N", "N", "N", "N", "N", "", ""],
        ["", "", "N", "N", "N", "N", "N", "N", "N", "", ""],
        ["", "", "N", "N", "N", "N", "N", "N", "N", "", ""],
        ["", "", "N", "N", "N", "N", "N", "N", "N", "", ""],
        ["", "", "N", "N", "N", "N", "N", "N", "N", "", ""],
        ["", "", "N", "N", "N", "N", "N", "N", "N", "", ""],
    ];

    const colHeaders = [
        "Account",
        "Section",
        "Mon",
        "Tue",
        "Wed",
        "Thu",
        "Fri",
        "Sat",
        "Sun",
        "From",
        "To",
    ];

    const colWidths = [170, 156, 80, 80, 80, 80, 80, 80, 80, 100, 100];

    const columns = [
        { data: 0, type: "text" }, 
        { data: 1, type: "dropdown", source: ["AM", "PM"] },
        { data: 2, type: "checkbox", checkedTemplate: "Y", uncheckedTemplate: "N" },
        { data: 3, type: "checkbox", checkedTemplate: "Y", uncheckedTemplate: "N" },
        { data: 4, type: "checkbox", checkedTemplate: "Y", uncheckedTemplate: "N" },
        { data: 5, type: "checkbox", checkedTemplate: "Y", uncheckedTemplate: "N" },
        { data: 6, type: "checkbox", checkedTemplate: "Y", uncheckedTemplate: "N" },
        { data: 7, type: "checkbox", checkedTemplate: "Y", uncheckedTemplate: "N" },
        { data: 8, type: "checkbox", checkedTemplate: "Y", uncheckedTemplate: "N" },
    ];

    const container = document.getElementById('handsontable');

    new Handsontable(container, {
        data: data,
        colHeaders: colHeaders,
        colWidths: colWidths,
        columns: columns,
        height: 450,
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
    
    // Initialize datepickers for From and To inputs
    $("#fromDate, #toDate").datepicker({ dateFormat: 'dd/mm/yy' });

    // Register Calendar button click event
    document.getElementById('registerCalendar').addEventListener('click', function() {
        const fromDate = document.getElementById('fromDate').value;
        const toDate = document.getElementById('toDate').value;
        
        if (fromDate && toDate) {
            // Perform registration logic here
            console.log("Registering calendar from", fromDate, "to", toDate);
        } else {
            alert('Please enter both From and To dates.');
        }
    });
});
