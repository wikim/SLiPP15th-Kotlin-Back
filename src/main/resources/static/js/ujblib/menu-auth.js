
$(document).ready(function () {
	
	
	$("#readYnAll").click(function () {
		if($("#readYnAll").prop("checked")) {
			$("input[type=checkbox][name=readYn]").prop("checked",true);
		} else {
			$("input[type=checkbox][name=readYn]").prop("checked",false);	
		}
	});
	
	$("#printYnAll").click(function () {
		if($("#printYnAll").prop("checked")) {
			$("input[type=checkbox][name=printYn]").prop("checked",true);
		} else {
			$("input[type=checkbox][name=printYn]").prop("checked",false);	
		}
	});
	
	$("#createYnAll").click(function () {
		if($("#createYnAll").prop("checked")) {
			$("input[type=checkbox][name=createYn]").prop("checked",true);
		} else {
			$("input[type=checkbox][name=createYn]").prop("checked",false);	
		}
	});
	
	$("#updateYnAll").click(function () {
		if($("#updateYnAll").prop("checked")) {
			$("input[type=checkbox][name=updateYn]").prop("checked",true);
		} else {
			$("input[type=checkbox][name=updateYn]").prop("checked",false);	
		}
	});
	
	
	$("#btnSave").click(function () {
		
		var datas = [];
		$("#mainTable1 tbody tr").each(function(index){
			   	var row = $(this).closest("tr");
			   	var data = {};
			   	data.id = row.find("input[name=readYn]").val();
				data.readYn = row.find("input[name=readYn]").is(":checked");
				data.printYn = row.find("input[name=printYn]").is(":checked");
				data.createYn = row.find("input[name=createYn]").is(":checked");
				data.updateYn = row.find("input[name=updateYn]").is(":checked");
			   	datas.push(data);
		});
		
		$.ajax({
			url : "/settings/menu-auth",
			type: "POST",
            dataType: "json",
            data : JSON.stringify(datas),
            cache : false,              
            async : false,
            contentType: "application/json; charset=UTF-8",
			success : function(data) {
				if(data.result) {
					alert("등록되었습니다.");
					location.href="/settings/members";
				} else {
					alert("등록시 에러가 발생했습니다.");
				}
			},
			error : function(data) {
				console.error("error : ", data);
			}
	    });
		
    });
	
});