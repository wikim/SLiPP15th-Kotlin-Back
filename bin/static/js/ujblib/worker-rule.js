
$(document).ready(function () {
	
	$('input[name=room]').on('change', function() {
		if($(this).val() == 'REFERENCE') {
			$('input[id=child' + $(this).data('id') + ']').prop('checked', false);  
		} else if($(this).val() == 'CHILD') {
			$('input[id=reference' + $(this).data('id') + ']').prop('checked', false);  
		}
	});
	
	$('input[name=weekdayYn]').on('change', function() {
		$('input[id^=weekdayYn]').not($('input[id=weekdayYn' + $(this).val() + ']')).prop('checked', false);  
	});
	
	$('input[id^=weekendYn-INFOMATION]').on('change', function() {
		$('input[id^=weekendYn-LIBRARY]').prop('checked', false);
	});
	
	$('input[id^=weekendYn-LIBRARY]').on('change', function() {
		$('input[id^=weekendYn-INFOMATION]').prop('checked', false);
	});
	
	$('input[id^=weekendYn-OFFICER]').on('change', function() {
		$('input[id^=weekendYn-OFFICER]').not($('input[id=weekendYn-OFFICER' + $(this).val() + ']')).prop('checked', false);  
	});
	
	$('input[id^=weekendYn-PUBLIC]').on('change', function() {
		$('input[id^=weekendYn-PUBLIC]').not($('input[id=weekendYn-PUBLIC' + $(this).val() + ']')).prop('checked', false);
	});
	
	$(document.body).on("click", ".btn-danger", function () {
		if(confirm("정말 삭제하시겠습니까?")) {
			var data = {id: $(this).val()};
			$.ajax({
				url : "/settings/worker-rule",
				type: "PUT",
	            dataType: "json",
	            data : JSON.stringify(data),
	            cache : false,              
	            async : false,
	            contentType: "application/json; charset=UTF-8",
				success : function(data) {
					if(data.result) {
						alert("삭제되었습니다.");
						location.reload();
					} else {
						alert("에러가 발생했습니다.");
					}
				},
				error : function(data) {
					console.error("error : ", data);
				}
		    });
		}
	});

	$("#btnSave").click(function () {
		
		var datas = [];
		$("#mainTable tbody tr").each(function(index){
			   	var row = $(this).closest("tr");
			   	var data = {};
			   	data.id = row.find("input[name=id]").val();
			   	data.position = row.find("select[name=position] option:selected").val();
				data.room = row.find("input[name=room]:checked").val();
				data.satYn = row.find("input[name=satYn]").is(":checked");
				data.sunYn = row.find("input[name=sunYn]").is(":checked");
				data.weekYn = row.find("input[name=weekYn]").is(":checked");
				data.weekdayYn = row.find("input[name=weekdayYn]").is(":checked");
				data.weekendYn = row.find("input[name=weekendYn]").is(":checked");
				data.printSeq = row.find("input[name=printSeq]").val();
				data.startDate = row.find("input[name=startDate]").val();
				data.endDate = row.find("input[name=endDate]").val();
			   	datas.push(data);
		});
		
		$.ajax({
			url : "/settings/worker-rule",
			type: "POST",
            dataType: "json",
            data : JSON.stringify(datas),
            cache : false,              
            async : false,
            contentType: "application/json; charset=UTF-8",
			success : function(data) {
				if(data.result) {
					alert("저장되었습니다.");
					location.href="/settings/worker-rule";
				} else {
					alert("저장시 에러가 발생했습니다.");
				}
			},
			error : function(data) {
				console.error("error : ", data);
			}
	    });
    });
	
	$("#btnCancel").click(function () {
		location.reload();
	});
	
	$("#btnCreate").click(function () {
		$.ajax({
			url : "/settings/worker-rule/member",
			type: "GET",
            dataType: "json",
            cache : false,              
            async : false,
            contentType: "application/json; charset=UTF-8",
			success : function(data) {
				if(data.result) {
					for (var index in data.members) {
						$("#selectMember").prepend("<option value="+data.members[index].id+">"+data.members[index].name+"</option>");
			 	   	}
					$("#responsive-modal").show();
				}
			},
			error : function(data) {
				console.error("error : ", data);
			}
	    });
	});
	
	$(document.body).on("click", "#btnSelect", function () {
		$("button.close").click();
		console.log($("#selectMember").val())
		
		var data = { memberId: $("#selectMember").val(), memberName: $("#selectMember option:selected").text() };
		
		$.ajax({
			url : "/settings/worker-rule/add",
			type: "POST",
			data : JSON.stringify(data),
            dataType: "json",
            cache : false,              
            async : false,
            contentType: "application/json; charset=UTF-8",
			success : function(data) {
				if(data.result) {
					location.reload();
				} else {
					alert("저장시 에러가 발생했습니다.");
				}
			},
			error : function(data) {
				console.error("error : ", data);
			}
	    });
	});
});