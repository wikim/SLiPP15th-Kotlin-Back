    	
var bindSearchIcon = function () {
  $("#iconHeadSearch").click(function () {
	  $("#headSearch").submit();
    })
};

function msToTime(s) {
  var ms = s % 1000;
  s = (s - ms) / 1000;
  
  var secs = s % 60;
  s = (s - secs) / 60;
  
  var mins = s % 60;
  var hrs = (s - mins) / 60;
  
  var days = 0;
  if (hrs > 23) {
    days = Math.floor(hrs / 24);
    return "(" + days + ' days) ' + hrs + 'h ' + mins + 'm ' + secs + 's ' + ms;
  } else {
    return hrs + 'h ' + mins + 'm ' + secs + 's ' + ms;
  }
}

 function removeParameter(deleteKey, url) {
   var lsearch = location.search;
   
   if(url != null && url.length > 0) {
     	lsearch = url;
     }
   
   var search = "";
     var question = "?";
     var equal = "=";
     var ampersand = "&";
     
     if (lsearch) {
       var lsearchArray = lsearch.substring(1).split(ampersand); 
       for (var i = 0; i < lsearchArray.length; i++) {
         if (lsearchArray[i].includes(deleteKey + equal)) {
           lsearchArray.splice(i, 1);
           i--;
           continue;
         }
         if (i == 0) {
           search += question + lsearchArray[i];
         } else {
           search += ampersand +  lsearchArray[i];
         }
       }
     }
     return search;
   }

function addOrChangeParameter(key, value, url) {
  var lsearch = location.search;
  
  if(url != null && url.length > 0) {
  	lsearch = url;
  }
  
  var search = "";
  var question = "?";
  var equal = "=";
  var ampersand = "&";
  
  if(lsearch) {
    if (lsearch.includes(key + equal)) {
      lsearch = removeParameter(key, url);
      if (lsearch) {
        search = lsearch + ampersand + key + equal + value;
      } else {
        search = question + key + equal + value;
      }
    } else {
      search = lsearch + ampersand + key + equal + value;
    }
  } else {
    search = question + key + equal + value;
  }
  return search;
}

/* var bindSideNaveClick = function () {
  $("#side-nav").bind("click", function (ev) {
    if (!$.cookie('nav-hide')) {
      $.cookie('nav-hide', true);
    } else {
      $.removeCookie('nav-hide');
    }
    console.log("side-nav nav-hide cookie", $.cookie('nav-hide'));
  });
};

var menuActivator = function () {
  console.log("Cookie nav-hide", $.cookie('nav-hide'));
  if ($.cookie('nav-hide')) {
    $("#nav").addClass('nav-xs');
  } else {
    $("#nav").removeClass('nav-xs');
  }
}; */

var removeCookie = function (target) {
  if ($.cookie) {
    $.removeCookie(target);
  }
};

var addCookie = function (target, value) {
  if ($.cookie) {
    $.cookie(target, value);
  }
};

var bindBtnKeywordSearch = function () {
  var $btnKeywordSearch = $("#btn-keyword-search");
  $btnKeywordSearch.on("click", function () {
    var keyword = $("#keyword").val();
    search = addOrChangeParameter("keyword", keyword);
    search = addOrChangeParameter("pageNumber", 0, search);
    location.href = location.pathname + search;
  });
};

var bindKeywordSerarchKeypress = function () {
  var $keyword = $("#keyword");
  $keyword.on("keypress", function (e) {
    if (e.keyCode === 13) {
      $("#btn-keyword-search").click();
    }
  });
};

var bindRowsSelect = function () {
  $("#btn-rows").children().on("click", function (){
    var pageSize = this.value;
    search = addOrChangeParameter("pageSize", pageSize);
    search = addOrChangeParameter("pageNumber", 0, search);
    search = addOrChangeParameter("keyword", $("#keyword").val(), search);
    
    location.href = location.pathname + search;
  });
};

var readMyProject = function () {
	if ($("select[id='myProject']").length) {
	  $.ajax({
	    url: '/projects/my-projects',
	    async: true,
	    method: 'post',
	    contentType : 'application/json',
	    success: function (data) {
	 		for (var index in data) {
	 			var selectedOption = data[index].selected ? 'selected' : '';
	 			$("#myProject option:eq(0)").after("<option value="+data[index].id+" "+selectedOption+">"+data[index].name+"</option>");
	 	   }
	    }
	  });
	}
  };

var bindChangeMyProject = function () {
	$("#myProject").on("change", function () {
		var pathname = $(location).attr('pathname');
	
		if ($(this).val()) {
			if(pathname.indexOf('jobs') > -1 || pathname.indexOf('job-list') > -1) {
				location.href = pathname + "?project=" + $(this).val() + "&projectId=" + $(this).val();	
			} else {
				location.href = pathname + "?project=" + $(this).val();
			}
		} else {
		  location.href = pappathname;
		}
	});
};

$(document).ajaxError(function (event, jqxhr, settings, thrownError) {
  if (401 === jqxhr.status && "/messages/un-read" !== settings.url) {
    location.href = "/sign-in";
  }
});

//Radio input style change
var radioswitch = function() {
    var bt = function() {
        $(".radio-switch").on("switch-change", function() {
                $(".radio-switch").bootstrapSwitch("toggleRadioState")
            }),
            $(".radio-switch").on("switch-change", function() {
                $(".radio-switch").bootstrapSwitch("toggleRadioStateAllowUncheck")
            }),
            $(".radio-switch").on("switch-change", function() {
                $(".radio-switch").bootstrapSwitch("toggleRadioStateAllowUncheck", !1)
            })
    };
    return {
        init: function() {
            bt()
        }
    }
}();

var getEmbedProfileImg = function () {
	$.ajax({
	    url: "/profile/image/embed",
	    method: "post",
	    success: function (data) {
	      if(data === undefined || data == null || data == "") {
	          $("#member-profile").prop("src", "/images/default-profile.png");
	      } else {
	          $("#member-profile").prop("src", data);
	      }	      
	    },
	    error: function (data) {
	      console.log("error", data);
	      $("#member-profile").prop("src", "/images/default-profile.png");
	    }
	});

};

var bindBtnSetTypeButton = function () {
    $('.btn').each(function() {
      if($(this).attr('type') === undefined || $(this).attr('type') == null || $(this).attr('type') == "") {
    	  $(this).attr('type', 'button');
      }
    });
};

var bindIESelect = function () {
	var agent = navigator.userAgent.toLowerCase();
	$('select').each(function() {
		if ( (navigator.appName == 'Netscape' && agent.indexOf('trident') != -1) || (agent.indexOf("msie") != -1)) {
			$(this).addClass("ieselect");
		}
	});
};

$(document).ready(function () {
  //menuActivator();
  //bindSideNaveClick();
  bindRowsSelect();
  bindSearchIcon();
  if ($("#btn-keyword-search")) {
    bindBtnKeywordSearch();
  }
  if ($("input[id='keyword']")) {
    bindKeywordSerarchKeypress();
  }
  
  //Radio input style change
  $(".bt-switch input[type='checkbox'], .bt-switch input[type='radio']").bootstrapSwitch();
  radioswitch.init();
  
  if (!String.prototype.includes) {
     String.prototype.includes = function() {
       'use strict';
       return String.prototype.indexOf.apply(this, arguments) !== -1;
     };
  }
  
  getEmbedProfileImg();
  bindBtnSetTypeButton();
  bindIESelect();

});

var alertMessage = (function(){
  var messagis;
  var setMessagis = function(messagis){
    this.messagis = messagis;     
  }
    
  var content = function(msgKey){
    var returnMsg;
    $.map(this.messagis, function(i,e){
      if(msgKey == e) { 
      	returnMsg = i;
      }
    });    
    return returnMsg;
  }
  
  return {
    content:content,
    setMessagis:setMessagis
  };
})();

function formatFileSize(bytes,decimalPoint) {
	   if(bytes == 0) return '0 Bytes';
	   var k = 1000,
	       dm = decimalPoint || 2,
	       sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB'],
	       i = Math.floor(Math.log(bytes) / Math.log(k));
	   return parseFloat((bytes / Math.pow(k, i)).toFixed(dm)) + ' ' + sizes[i];
	}