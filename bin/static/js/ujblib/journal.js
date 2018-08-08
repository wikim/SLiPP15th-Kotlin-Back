
$(document).ready(function () {
	
	$("#registDate").datepicker({
		autoclose: true,
		format:'yyyy-mm-dd',
		todayHighlight: true,
		setDate: new Date(),
		language:'ko'
	});
	$("#registDate").datepicker("update", new Date());
	
	$("#mainTable1").editableTableWidget().find('td:first').focus();
	$("#mainTable2").editableTableWidget().numericInputExample().find('td:first').focus();
	$("#mainTable3").editableTableWidget().numericInputExample().find('td:first').focus();
	
	$("#btnCancel").click(function () {
		location.href = "/book/journal-list";
	});
	
	$("#btnSave").click(function () {
		 
		var data = {
			id: $("#id").val(),
			journalType: $("#journalType").val(),
			journalSeq: $("td[id=journalSeq]").text(),
			itemTitle: $("td[id=itemTitle]").text(),
			registDateStr: $("#registDate").val(),
			paperDay: $("td[id=paperDay]").text(),
			paperHalfWeek: $("td[id=paperHalfWeek]").text(),
			paperWeek: $("td[id=paperWeek]").text(),
			paperTwoWeek: $("td[id=paperTwoWeek]").text(),
			magazineWeek: $("td[id=magazineWeek]").text(),
			magazineTwoWeek: $("td[id=magazineTwoWeek]").text(),
			magazineMonth: $("td[id=magazineMonth]").text(),
			magazineTwoMonth: $("td[id=magazineTwoMonth]").text(),
			magazineFourMonth: $("td[id=magazineFourMonth]").text(),
			magazineHalfMonth: $("td[id=magazineHalfMonth]").text()
		};
		
		console.log(data);
		
		$.ajax({
			url : "/book/journal",
			type: "POST",
            dataType: "json",
            data : JSON.stringify(data),
            cache : false,              
            async : false,
            contentType: "application/json; charset=UTF-8",
			success : function(result) {
				alert("등록되었습니다.");
				location.href="/book/journal-list";
			},
			error : function(data) {
				console.error("error : ", data);
			}
	    });
		
    });
	
});