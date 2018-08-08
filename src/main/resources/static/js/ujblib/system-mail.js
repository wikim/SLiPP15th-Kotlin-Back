/**
 * Javascript for settings/system-mail.html
 */

var validateForm = function () {
  $("#systemMailForm").validate({
    rules: {
      host: {
        required: true,
        maxlength: 100
      },
      port: {
        required: true,
        maxlength: 6,
        digits: true
      },
      username: {
        required: true,
        email: true
      },
      password: "required"
    },
    messages: {
      host: "ex) 'smtp.gmail.com'",
      port: "required digit port number",
      username: "Username format: email-address@domain",
      password: "required password for username"
    },
    submitHandler: function(systemMailForm) {
      systemMailForm.submit();
    }
  });
};

var bindBtnSave = function () {
  $("#btnSave").on("click", function (e) {
    e.preventDefault();
    $("#systemMailForm").submit();
  });
}

var bindBtnCancel = function () {
  $("#btnCancel").on("click", function (e) {
    e.preventDefault();
    location.href = "/settings/system-mail";
  });
};

var bindBtnSendTestMail = function () {
  $("#btnSendTestEmail").on("click", function () {
    var $testMail = $("#testMail");
    if ($testMail.val().length) {
      sendTestMail($testMail.val());
    } else {
      //error처리
    }
  });
};


var sendTestMail = function (mailAddress) {  	
  var request = {
    mailAddress: mailAddress
  };   

  $.ajax({
    url: "/system-mail/send/test-mail",
    method: "get",
    contentType: "application/json",
    dataType: "json",
    data: request,
    success: function () {
      console.log("status", "mail success");
    },
    error: function(data) {
      console.log("error", data);
    }
  });
};

$(function () {
  $.validator.setDefaults({
    debug: true,
    success: "valid"
  });
  validateForm();
  bindBtnSave();
  bindBtnCancel();
  bindBtnSendTestMail();
});
 