
$(document).ready(function () {
	
	$('#cDate').datepicker({
		autoclose: true,
		format: "yyyy",
		viewMode: "years", 
		minViewMode: "years",
		language:'ko'
	});
	//$("#cDate").datepicker("update", new Date());
	
	$('#cDate').datepicker().on('changeDate', function (ev) {
		$("#mainForm").attr("action", "/book/journal-statistic");
		$("#mainForm").attr("method", "GET");
	    $("#mainForm").submit();
	});
	
});