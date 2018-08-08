/**
   * Convert MilliSecond to a time string.
   * @param {MilliSecond} long
   * @return {String} TimeString
   */
var transMsecToStr = function(msec) {
	
	if(msec === undefined || msec == null || msec == 0)
		return "00:00:00 sec";
	
	var strTime = "";
	
	var day = Math.floor(msec / (1000*60*60*24));
	
	if(day > 0)
		strTime = day + "Days ";
	
	msec = msec % (1000*60*60*24);
	
	var hr = Math.floor(msec / (1000*60*60));
	
	if(hr >= 0) {
		if(hr < 10)
			strTime = strTime + "0";
		
		strTime = strTime + hr + ":";
	}
	
	msec = msec % (1000*60*60);
	
	var mi = Math.floor(msec / (1000*60));
	
	if(mi >= 0) {
		if(mi < 10)
			strTime = strTime + "0";
		
		strTime = strTime + mi + ":";
	}
		
	msec = msec % (1000*60);
	
	var se = Math.floor(msec / (1000));
	
	if(se >= 0) {
		if(se < 10)
			strTime = strTime + "0";
		
		strTime = strTime + se;
	}
	
	if(strTime == "")
		strTime = "00:00:00";
	
	return strTime + ' sec';
}

var isEmpty = function(value) {
	return typeof value == 'string' && !value.trim() || typeof value == 'undefined' || value === null;
}
