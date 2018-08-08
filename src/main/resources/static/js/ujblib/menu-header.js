
$(document).ready(function () {
	
	$(document).on('click', '#btnChangePasswd', function () {
		var data = {userId: $("#userId").val(), password: $("#userPassword").val() };
		console.log(data)
		$.ajax({
			url : "/settings/password",
			type: "POST",
            dataType: "json",
            data : JSON.stringify(data),
            cache : false,              
            async : false,
            contentType: "application/json; charset=UTF-8",
			success : function(data) {
				if(data.result) {
					alert("변경되었습니다.");
					$('#password-modal').modal('hide');
					location.href="/system/sign-out";
				} else {
					alert("에러가 발생했습니다.");
				}
			},
			error : function(data) {
				console.error("error : ", data);
			}
	    });
    });
	
});