
var inext = 0;
var getBack = function(ch){
	this.inext++;
	console.log(inext);
	getBackgroundImage(inext);	
}
var getBackgroundImage = function(ch){
	var month = "0000";
	var imageName = ["0000","0101","0201","0301","0401","0501","0601","0701","0801","0901","1001","1101","1201"];
	var imagePath = "/images/sign/kr/";
	var imageExt = ".jpg";
	var backgroundImage = imagePath + imageName[0] + 	imageExt;
	
	$.ajax({
	    url: '/current-date-time',
	    method: 'get',
	    contentType: 'application/json',
	    success: function (result) {
	    	month = result.date.split("-")[1];
			day = result.date.split("-")[2];
			if(ch >0 ) month = ch;
			if(ch > 12) {
				inext=1; month=1;
			}
			var index = Number(month);
			//var imgNum = Number(imageName[index]);
			//var dtNum  = Number(month+day);
			
			backgroundImage =imagePath + imageName[index] + imageExt;
			//console.log(backgroundImage);
			//$("#wrapper").css("background","url("+backgroundImage+") no-repeat center / cover");
			

			console.log($("#wrapper").css("background"));
	    }
      });
}

$(document).ready(function() {
	//getBackgroundImage();
});
