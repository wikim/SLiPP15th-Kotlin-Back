
$(document).ready(function () {
	
	$('#cDate').datepicker({
		autoclose: true,
		format:'yyyy-mm',
		startView: "months", 
		minViewMode: "months",
		language:'ko'
	});
	
	$('#cDate').datepicker().on('changeDate', function (ev) {
		$("#mainForm").attr("action", "/book/transfer-list");
		$("#mainForm").attr("method", "GET");
	    $("#mainForm").submit();
	});
	
	$("#btnCreate").click(function () {
		location.href = "/book/transfer/new";
	});
	
});