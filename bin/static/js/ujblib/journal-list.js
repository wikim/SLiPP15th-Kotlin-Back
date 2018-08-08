
$(document).ready(function () {
	
	$('#cDate').datepicker({
		autoclose: true,
		format:'yyyy-mm',
		startView: "months", 
		minViewMode: "months",
		language:'kr'
	});
	
	$('#cDate').datepicker().on('changeDate', function (ev) {
		$("#mainForm").attr("action", "/book/journal-list");
		$("#mainForm").attr("method", "GET");
	    $("#mainForm").submit();
	});
	
	$("#btnCreate").click(function () {
		location.href = "/book/journal/new";
	});
	
});