
$(document).ready(function () {
	
	$("#btnPrint").click(function () {
		$("#divContents").printThis();
	});
	
	$("#btnWork").click(function () {
		location.href = "/dataroom/work-scheduling?cDate=" +  $("#cDate").val();
	});
});