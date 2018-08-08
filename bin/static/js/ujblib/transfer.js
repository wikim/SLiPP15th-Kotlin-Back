
$(document).ready(function () {
	
	$("#transferDate").datepicker({
		autoclose: true,
		format:'yyyy-mm-dd',
		todayHighlight: true,
		setDate: new Date(),
		language:'ko'
	});
	
	$("#pDate").datepicker({
		autoclose: true,
		format:'yyyy-mm-dd',
		todayHighlight: true,
		setDate: new Date(),
		language:'ko'
	});
	
	//$('#pDate').datepicker('update', new Date());
	
	$("#btnCancel").click(function () {
		location.href = "/book/transfer-list";
	});
	
	$(document.body).on("change", "#incomeType, #bookCode", function () {
		console.log($(this).val());
		
		var data = { bookCode: $("#bookCode").val() };
		
		$.ajax({
			url : "/book/transfer/code",
			type: "POST",
            dataType: "json",
            data : JSON.stringify(data),
            cache : false,              
            async : false,
            contentType: "application/json; charset=UTF-8",
			success : function(data) {
				console.log(data)
				if(data.result) {
					$("#bookDataRow").nextAll().remove();
					var contents = "";
					for (var index in data.codes) {
						contents += "<tr class=\"bookType\" data-value=\"" + data.codes[index].id + "\">";
						contents += "<th class=\"bookTypeStr\">" + data.codes[index].bookType + "</th>";
						contents += "<th class=\"incomeType\" data-value=\"" + $("#incomeType option:selected").val() + "\">" + $("#incomeType option:selected").text() + "</th>";
						contents += "<td class=\"type000\">0</td>";
						contents += "<td class=\"type100\">0</td>";
						contents += "<td class=\"type200\">0</td>";
						contents += "<td class=\"type300\">0</td>";
						contents += "<td class=\"type400\">0</td>";
						contents += "<td class=\"type500\">0</td>";
						contents += "<td class=\"type600\">0</td>";
						contents += "<td class=\"type700\">0</td>";
						contents += "<td class=\"type800\">0</td>";
						contents += "<td class=\"type900\">0</td>";
						contents += "</tr>";
					}
					
					$("#contentBody").last().append(contents);
				}
				
				$("#mainTable").editableTableWidget().find('td:first').focus();
			},
			error : function(data) {
				console.error("error : ", data);
			}
	    });
	});
	
	if(isEmpty($("#id").val())) {
		$("#bookCode").trigger("change");
	} else {
		$("#bookCode").attr("disabled", "disabled");
		$("#incomeType").attr("disabled", "disabled");
	}
	
	$("#mainTable").editableTableWidget().find('td:first').focus();
	
	$("#btnSave").click(function () {
		
		var datas = [];
		$("#contentBody tr:gt(6)").each(function(index){
		   	var row = $(this).closest("tr");
		   	var data = {};
		   	data.id = row.attr("data-id");
		   	data.bookType = row.attr("data-value");
		   	data.incomeType = row.find("th.incomeType").attr("data-value");
		   	data.type000 = row.find("td.type000").text();
		   	data.type100 = row.find("td.type100").text();
		   	data.type200 = row.find("td.type200").text();
		   	data.type300 = row.find("td.type300").text();
		   	data.type400 = row.find("td.type400").text();
		   	data.type500 = row.find("td.type500").text();
		   	data.type600 = row.find("td.type600").text();
		   	data.type700 = row.find("td.type700").text();
		   	data.type800 = row.find("td.type800").text();
		   	data.type900 = row.find("td.type900").text();
		   	datas.push(data);
		});
		
		console.log(datas);
		
		$('#bookCode').removeAttr("disabled"); 
		$('#incomeSeq').removeAttr("disabled"); 

		var transferData = {
			id: $("#id").val(),
			curDate: $("#pDate").val(),
			incomeSeq: $("td[id=incomeSeq]").text(),
			incomeType: $("th.incomeType").attr("data-value"),
			bookCode: $("#bookCode").val(),
			regDigit: $("td[id=regDigit]").text(),
			itemTitle: $("td[id=itemTitle]").text(),
			fromName: $("td[id=fromName]").text(),
			toName: $("td[id=toName]").text(),
			confirmName: $("td[id=confirmName]").text(),
			transferDateStr: $("#transferDate").val(),
			transfers:datas
		};
		
		console.log(transferData);
		
		$.ajax({
			url : "/book/transfer",
			type: "POST",
            dataType: "json",
            data : JSON.stringify(transferData),
            cache : false,              
            async : false,
            contentType: "application/json; charset=UTF-8",
			success : function(result) {
				alert("등록되었습니다.");
				location.href="/book/transfer-list";
			},
			error : function(data) {
				console.error("error : ", data);
			}
	    });
    });
	
	$("#btnDelete").click(function () {
		if(confirm("정말로 삭제하시겠습니까?")) {
			var data = {id: $("#id").val() };
			$.ajax({
				url : "/book/transfer",
				type: "DELETE",
	            dataType: "json",
	            data : JSON.stringify(data),
	            cache : false,              
	            async : false,
	            contentType: "application/json; charset=UTF-8",
				success : function(data) {
					if(data.result) {
						alert("삭제되었습니다.");
						location.href="/book/transfer-list";
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
	
	
	$("#btnPrint").click(function () {
		$("#divContents").show();
		
		$('#bookCode').removeAttr("disabled"); 
		$('#incomeSeq').removeAttr("disabled"); 
		
		$("#print-incomeType").text($("#incomeType option:selected").text());
		$("#print-incomeSeq").text($("td[id=incomeSeq]").text());
		$("#print-bookCode").text($("#bookCode option:selected").text());
		$("#print-regDigit").text($("td[id=regDigit]").text());
		$("#print-itemTitle").text($("td[id=itemTitle]").text());
		
		$("#print-fromName").text($("#fromName").text());
		$("#print-toName").text($("#toName").text());
		$("#print-confirmName").text($("#confirmName").text());
		
		$("#bookCode").attr("disabled", "disabled");
		$("#incomeType").attr("disabled", "disabled");
		
		var datas = [];
		$("#contentBody tr:gt(6)").each(function(index){
		   	var row = $(this).closest("tr");
		   	var data = {};
		   	data.id = row.attr("data-id");
		   	data.bookType = row.find("th.bookTypeStr").text();
		   	data.incomeType = row.find("th.incomeType").attr("data-value");
		   	data.type000 = row.find("td.type000").text();
		   	data.type100 = row.find("td.type100").text();
		   	data.type200 = row.find("td.type200").text();
		   	data.type300 = row.find("td.type300").text();
		   	data.type400 = row.find("td.type400").text();
		   	data.type500 = row.find("td.type500").text();
		   	data.type600 = row.find("td.type600").text();
		   	data.type700 = row.find("td.type700").text();
		   	data.type800 = row.find("td.type800").text();
		   	data.type900 = row.find("td.type900").text();
		   	datas.push(data);
		});
		
		$("#print-bookDataRow").nextAll().remove();
		
		var printTotal = {};
		printTotal.type000 = 0;
		printTotal.type100 = 0;
		printTotal.type200 = 0;
		printTotal.type300 = 0;
		printTotal.type400 = 0;
		printTotal.type500 = 0;
		printTotal.type600 = 0;
		printTotal.type700 = 0;
		printTotal.type800 = 0;
		printTotal.type900 = 0;
		
		var contents = "";
		for (var index in datas) {
			contents += "<tr>";
			contents += "<th style=\"text-align:center\" >" + datas[index].bookType + "</th>";
			contents += "<th style=\"text-align:center\" >" + $("#incomeType option:selected").text() + "</th>";
			contents += "<td style=\"text-align:center\" class=\"print-type000\">" + datas[index].type000 + "</td>";
			contents += "<td style=\"text-align:center\" class=\"print-type100\">" + datas[index].type100 + "</td>";
			contents += "<td style=\"text-align:center\" class=\"print-type200\">" + datas[index].type200 + "</td>";
			contents += "<td style=\"text-align:center\" class=\"print-type300\">" + datas[index].type300 + "</td>";
			contents += "<td style=\"text-align:center\" class=\"print-type400\">" + datas[index].type400 + "</td>";
			contents += "<td style=\"text-align:center\" class=\"print-type500\">" + datas[index].type500 + "</td>";
			contents += "<td style=\"text-align:center\" class=\"print-type600\">" + datas[index].type600 + "</td>";
			contents += "<td style=\"text-align:center\" class=\"print-type700\">" + datas[index].type700 + "</td>";
			contents += "<td style=\"text-align:center\" class=\"print-type800\">" + datas[index].type800 + "</td>";
			contents += "<td style=\"text-align:center\" class=\"print-type900\">" + datas[index].type900 + "</td>";
			contents += "</tr>";
			
			printTotal.type000 += Number(datas[index].type000);
			printTotal.type100 += Number(datas[index].type100);
			printTotal.type200 += Number(datas[index].type200);
			printTotal.type300 += Number(datas[index].type300);
			printTotal.type400 += Number(datas[index].type400);
			printTotal.type500 += Number(datas[index].type500);
			printTotal.type600 += Number(datas[index].type600);
			printTotal.type700 += Number(datas[index].type700);
			printTotal.type800 += Number(datas[index].type800);
			printTotal.type900 += Number(datas[index].type900);
			
		}
		
		contents += "<tr>";
		contents += "<th style=\"text-align:center\" colspan=\"2\">총계</th>";
		contents += "<td style=\"text-align:center\" class=\"print-type000\">" + printTotal.type000 + "</td>";
		contents += "<td style=\"text-align:center\" class=\"print-type100\">" + printTotal.type100 + "</td>";
		contents += "<td style=\"text-align:center\" class=\"print-type200\">" + printTotal.type200 + "</td>";
		contents += "<td style=\"text-align:center\" class=\"print-type300\">" + printTotal.type300 + "</td>";
		contents += "<td style=\"text-align:center\" class=\"print-type400\">" + printTotal.type400 + "</td>";
		contents += "<td style=\"text-align:center\" class=\"print-type500\">" + printTotal.type500 + "</td>";
		contents += "<td style=\"text-align:center\" class=\"print-type600\">" + printTotal.type600 + "</td>";
		contents += "<td style=\"text-align:center\" class=\"print-type700\">" + printTotal.type700 + "</td>";
		contents += "<td style=\"text-align:center\" class=\"print-type800\">" + printTotal.type800 + "</td>";
		contents += "<td style=\"text-align:center\" class=\"print-type900\">" + printTotal.type900 + "</td>";
		contents += "</tr>";
		
		$("#print-contentBody").last().append(contents);
		
		$("#divContents").printThis({
			debug: false,               // show the iframe for debugging
			importCSS: true,            // import page CSS
			importStyle: false,         // import style tags
			printContainer: true,       // grab outer container as well as the contents of the selector
			loadCSS: "",  // path to additional css file - us an array [] for multiple
			pageTitle: "",              // add title to print page
			removeInline: false,        // remove all inline styles from print elements
			printDelay: 333,            // variable print delay
			header: "",               // prefix to html
			footer: null,               // postfix to html
			base: false,                // preserve the BASE tag, or accept a string for the URL
			formValues: true,           // preserve input/form values
			canvas: false,              // copy canvas elements (experimental)
			doctypeString: '',       // enter a different doctype for older markup
			removeScripts: false,       // remove script tags from print content
			copyTagClasses: false       // copy classes from the html & body tag
		});
		
	});
	
	$("#divContents").hide();
});