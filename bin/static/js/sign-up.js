var bindFormValidate = function() {

	$.validator.addMethod("alphanumeric", function(value, element) {
		return this.optional(element) || /^[a-zA-Z0-9]+$/.test(value);
	});
	$.validator.addMethod("nemberic", function(value, element) {
		return this.optional(element) || /^[0-9\-+ ]+$/.test(value);
	});
	$.validator
			.addMethod(
					"emailcheck",
					function(value, element) {
						return this.optional(element)
								|| /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i
										.test(value);
					});

	$("#sign_up").validate({
		rules : {
			agreeCheck : {
				required : true
			},
			signinId : {
				required : true,
				rangelength : [ 4, 20 ],
				alphanumeric : true,
				remote : {
					url : "/members/duplicated-check-signin-id",
					type : "get",
					data : {
						signinId : function() {
							return $("#signinId").val();
						}
					}
				}

			},
			nickName : {
				required : true,
				rangelength : [ 2, 10 ]
			},
			password : {
				required : true,
				rangelength : [ 4, 20 ]
			},
			passwordconfirm : {
				required : true,
				rangelength : [ 4, 20 ],
				equalTo : "#password"
			},
			name : {
				required : true
			},
			email : {
				required : true,
				email : true,
				maxlength : 50,
				emailcheck : true,
				remote : {
					url : "/members/duplicated-check-email",
					type : "get",
					data : {
						email : function() {
							return $("#email").val();
						}
					}
				}
			},
			cellPhoneNumber : {
				required : true,
				rangelength : [ 10, 20 ],
				nemberic : true
			},
			company : {
				required : true,
				maxlength : 50
			},
			department : {
				required : true,
				maxlength : 50
			}
		},
		messages : {
			agreeCheck : {
				required : $("#viewAgreeCheck").html()
			},
			signinId : {
				required : $("#viewSigninIdRequired").html(),
				rangelength : $("#viewRangelength").html(),
				alphanumeric : $("#viewSigninIdAlphanumeric").html(),
				remote : $("#viewSigninIdRemote").html()
			},
			nickName : {
				required : $("#viewNicknameRequired").html(),
				rangelength : $("#viewRangelength").html()
			},
			password : {
				required : $("#viewPwRequired").html(),
				rangelength : $("#viewRangelength").html()
			},
			passwordconfirm : {
				required : $("#viewConfirmPwRequired").html(),
				rangelength : $("#viewRangelength").html(),
				equalTo : $("#viewConfirmPwFail").html()
			},
			name : {
				required : $("#viewNameRequired").html()
			},
			email : {
				required : $("#viewEmailRequired").html(),
				email : $("#viewEmailFormat").html(),
				emailcheck : $("#viewEmailFormat").html(),
				maxlength : $("#viewLimitrange").html(),
				remote : $("#viewEmailRemote").html()
			},
			cellPhoneNumber : {
				required : $("#viewCellphoneRequired").html(),
				rangelength : $("#viewRangelength").html(),
				nemberic : $("#viewCellphoneAlphahan").html()
			},
			company : {
				required : $("#viewCompanyRequired").html(),
				maxlength : $("#viewLimitrange").html()
			},
			department : {
				required : $("#viewDepartmentRequired").html(),
				maxlength : $("#viewLimitrange").html()
			}
		},
		invalidHandler : function(event, validator) { //실패했을 경우 나타나는 증상
			$("#btnSave").button('reset');
			$("#btnSave").prop("disabled", false);
		}
	});
};

var bindBtnSave = function() {
	$("#btnSave").on("click", function() {
		console.log("click");
		if ($("#agreeCheck").is(":checked")) {
			$("#agreeTxt").addClass("hide");
			$(this).prop("disabled", true);
			$(this).button('complete');
			$("#sign_up").submit();
		} else {
			$("#agreeTxt").removeClass("hide");
			$("#sign_up").validate();
			return false;
		}
	});
};

$(document).ready(function() {
	bindFormValidate();
	bindBtnSave();
});