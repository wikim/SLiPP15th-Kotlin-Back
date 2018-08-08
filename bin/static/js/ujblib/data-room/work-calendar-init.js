    	var evts = JSON.parse( $("#work-calendar").val());
    	var selectedDate = $("#today").val();
    	var monthStatus =  new Date(selectedDate).getMonth()+1;
    	$(document).ready(function() {

    		$('#calendar').fullCalendar({
    			header: {
    				left: '',
    				center: 'prevYear,prev, title ,next,nextYear',
    				right:  ''
    				
    			},
    			//: true, 
    			//views: { agenda: { eventLimit: 1 } },
    			defaultDate: selectedDate,
    			selectable: true,
    			//selectHelper: false,
    			select: function(start, end, jsEvent, view) {
    				
    				location.href="/dataroom/"+$("#menuType").val()+"/view/"+start.format("YYYY-MM-DD");
    				
    			},
    			editable: true	,
    			
    			eventClick: function(calEvent,jsEvent, view) {
    				$("#id").val(calEvent.id);
    				$("#date").val(calEvent.date);
    				location.href="/dataroom/"+$("#menuType").val()+"/view/"+calEvent.date;
    			},
    			events: evts
    		});
    		
    		$(".fc-prev-button span").click(function (e) {
    			var d = new Date(selectedDate);
    			var toYear = d.getFullYear();
    			monthStatus = monthStatus - 1;
    			console.log("selectedDate:"+selectedDate);
    			console.log("monthStatus:"+monthStatus);
    			if(monthStatus == 0){
    				d.setFullYear(d.getFullYear() - 1);
    				location.href="/dataroom/"+$("#menuType").val()+"/"+d.getFullYear()+"-"+(1)+"-"+d.getDate();
    			}
    		})
    		
    		$(".fc-next-button span").click(function (e) {
    			var d = new Date(selectedDate);
    			var toYear = d.getFullYear();
    			monthStatus = monthStatus + 1;
    			console.log("selectedDate:"+selectedDate);
    			console.log("monthStatus:"+monthStatus);
    			if(monthStatus == 13){
    				d.setFullYear(d.getFullYear() + 1);
    				location.href="/dataroom/"+$("#menuType").val()+"/"+d.getFullYear()+"-"+(1)+"-"+d.getDate();
    			}
    		})
    		
    		
    		$(".fc-prevYear-button span").click(function () {
    			var d = new Date(selectedDate);
    			d.setFullYear(d.getFullYear() - 1);
    			location.href="/dataroom/"+$("#menuType").val()+"/"+d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate();
    		})
    		
    		$(".fc-nextYear-button span").click(function () {
    			var d = new Date(selectedDate);
    			d.setFullYear(d.getFullYear() +1);
    			location.href="/dataroom/"+$("#menuType").val()+"/"+d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate();
    		})
    	});
