
$(document).ready(function () {
	
	$('#cDate').datepicker({
		autoclose: true,
		format:'yyyy-mm',
		startDate: new Date("2018-07"),
		startView: "months", 
		minViewMode: "months",
		language:'ko'
	});
	
	$('#cDate').datepicker().on('changeDate', function (ev) {
		$("#mainForm").attr("action", "/dataroom/work-scheduling");
		$("#mainForm").attr("method", "GET");
	    $("#mainForm").submit();
	});
	
		
	$("#btnExport").click(function (e) {
		var filename = '근무표.xls';
	    var downloadLink;
	    var dataType = 'application/vnd.ms-excel';
	    var tableSelect = document.getElementById('divContents');
	    var tableHTML = tableSelect.outerHTML.replace(/ /g, '%20');
	    
	    // Create download link element
	    downloadLink = document.createElement("a");
	    
	    document.body.appendChild(downloadLink);
	    
	    if(navigator.msSaveOrOpenBlob) {
	        var blob = new Blob(['\ufeff', tableHTML], {
	            type: dataType
	        });
	        navigator.msSaveOrOpenBlob( blob, filename);
	    } else {
	        // Create a link to the file
	        downloadLink.href = 'data:' + dataType + ', ' + tableHTML;
	        // Setting the file name
	        downloadLink.download = filename;
	        //triggering the function
	        downloadLink.click();
	    }
	});
	
	$("#btnPrint").click(function () {
		var workTitle = $("#workTitle").text();
		var headerTitle = $(".card-title span").text();
		
		$("#divContents").printThis({
			debug: false,               // show the iframe for debugging
			importCSS: true,            // import page CSS
			importStyle: false,         // import style tags
			printContainer: true,       // grab outer container as well as the contents of the selector
			loadCSS: "",  // path to additional css file - us an array [] for multiple
			pageTitle: workTitle,              // add title to print page
			removeInline: false,        // remove all inline styles from print elements
			printDelay: 333,            // variable print delay
			header: headerTitle,               // prefix to html
			footer: null,               // postfix to html
			base: false,                // preserve the BASE tag, or accept a string for the URL
			formValues: true,           // preserve input/form values
			canvas: false,              // copy canvas elements (experimental)
			doctypeString: '',       // enter a different doctype for older markup
			removeScripts: false,       // remove script tags from print content
			copyTagClasses: false       // copy classes from the html & body tag
		});
	});
	
	$("#btnWorkOrder").click(function () {
		location.href = "/dataroom/work-order?cDate=" +  $("#cDate").val();
	});
	
	$("#btnPopupSave").click(function () {
		$("#popupForm").attr("action", "/dataroom/work-scheduling/" + $("#workId").val() + "/" + $("#cDate").val());
		$("#popupForm").attr("method", "POST");
		$("#popupForm").submit();
	});
	
	$("#btnNewCreate").click(function () {
		$.ajax({
			url : "/dataroom/work-scheduling/new",
			data: { cDate : $("#cDate").val() },
			method : "post",
			success : function(data) {
				if(data.result) {
					location.reload();
				} else {
					alert(data.msg);
				}
			},
			error : function(data) {
				console.error("new create : ", data);
			}
	    });
	});
	
	$("#btnReset").click(function () {
		$.ajax({
			url : "/dataroom/work-scheduling/reset",
			data: { cDate : $("#cDate").val() },
			method : "post",
			success : function(data) {
				if(data.result) {
					location.reload();
				} else {
					alert(data.msg);
				}
			},
			error : function(data) {
				console.error("new create : ", data);
			}
	    });
	});
	
	$("#btnPopupDelete").click(function () {
		$.ajax({
			url : "/dataroom/work-scheduling/" + $("#workerId").val(),
			method : "delete",
			success : function() {
				location.reload();
			},
			error : function(data) {
				console.error("btnPopupDelete Error = ", data);
			}
	    });
	});
	
	$(document).on('click', 'button[name=btnModal]', function () {
		console.log($(this).text())
		$("#workerId").val($(this).val());
		if($(this).text() == '종합') {
			$("#workType").val("REFERENCE");
		} else if($(this).text() == '어린이') {
			$("#workType").val("CHILD");
		} else if($(this).text() == '총괄') {
			$("#workType").val("GENERAL");
		}
		$("#workType").prop('disabled', true);
		$("#btnPopupSave").hide();
		$("#btnPopupDelete").show();
		console.log($("#workerId").val());
	});
	
	$(document).on('click', 'td.popupDialog', function () {
		$("#workId").val($(this).data('id'));
		$("#memberId").val($(this).data('worker'));
		$("#workType").prop('disabled', false);
		$("#workType").val("DUTY");
		$("#btnPopupSave").show();
		$("#btnPopupDelete").hide();
		
		console.log($("#workId").val());
		console.log($("#memberId").val());
	});
	
	$('td').hover(function() {
	    $(this).addClass('hover');
	    $(this).css('cursor','pointer');
	}, function() {
	    $(this).removeClass('hover');
	});
	 
});