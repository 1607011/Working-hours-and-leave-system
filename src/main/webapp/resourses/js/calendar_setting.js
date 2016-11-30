/**
 * 
 */

$(function() {
	var date;
	var days = [31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31]
	var weekday = ["一", "二", "三", "四", "五", "六", "日"];
	var month="", day="", weekDay="";
	function transformDate() {
		switch(date[1]) {
		case 'Jan':
			date[1] = 1;
			break;
		case 'Feb':
			date[1] = 2;
			break;
		case 'Mar':
			date[1] = 3;
			break;
		case 'Apr':
			date[1] = 4;
			break;
		case 'May':
			date[1] = 5;
			break;
		case 'Jun':
			date[1] = 6;
			break;
		case 'Jul':
			date[1] = 7;
			break;
		case 'Aug':
			date[1] = 8;
			break;
		case 'Sep':
			date[1] = 9;
			break;
		case 'Oct':
			date[1] = 10;
			break;
		case 'Nov':
			date[1] = 11;
			break;
		case 'Dec':
			date[1] = 12;
			break;
		}
		switch(date[0]) {
		case 'Sun':
			date[0] = '日';
			break;
		case 'Mon':
			date[0] = '一';
			break;
		case 'Tue':
			date[0] = '二';
			break;
		case 'Wed':
			date[0] = '三';
			break;
		case 'Thu':
			date[0] = '四';
			break;
		case 'Fri':
			date[0] = '五';
			break;
		case 'Sat':
			date[0] = '六';
			break;
		}
	}
	var startDay = 0;
	var endDay = 0;
	var sysDate = new Date();
	var Month = parseInt(sysDate.getMonth());
	var Day = parseInt(sysDate.getDate());
	var WeekDay = parseInt(sysDate.getDay());
	function systemDateS() {
		if (WeekDay == 0)
			WeekDay += 7;
		for (var i = 0; i < 7; i++) {
			if (WeekDay - i == 1) {
				Day -= i;
				WeekDay -= i;
				if (Day < 1)
					Day = 1;
				break;
			}
		}
		startDay = Day;
		return startDay;
	}
	var recordPosition = function() {
		var scrollPos;    
	    if (typeof window.pageYOffset != 'undefined') {
	        scrollPos = window.pageYOffset;
	    }
	    else if (typeof document.compatMode != 'undefined' &&
	        document.compatMode != 'BackCompat') {
	        scrollPos = document.documentElement.scrollTop;
	    }
	    else if (typeof document.body != 'undefined') {
	        scrollPos = document.body.scrollTop;
	    }
	    document.cookie="scrollTop="+scrollPos; //存儲捲動條位置到cookies中
	}

	var recoverPosition = function() { 
		if(document.cookie.match(/scrollTop=([^;]+)(;|$)/)!= null){
	        var arr=document.cookie.match(/scrollTop=([^;]+)(;|$)/); //cookies中不為空，則讀取捲動條位置
	        if (arr[1] != 'undefined') {
	        	document.documentElement.scrollTop = parseInt(arr[1]);
	        	document.body.scrollTop = parseInt(arr[1]);
	        }
		}
	}
	function systemDateE() {
		endDay = systemDateS() + 6;
		var WeekDayTemp = parseInt(sysDate.getDay());
		if (WeekDayTemp == 0)
			WeekDayTemp += 7;
	//	if (WeekDay == 0)
	//		WeekDay += 7;
		if (endDay > days[Month])
			endDay = days[Month];
		if (WeekDay > 1)
			endDay -= (WeekDayTemp - 1);
		return endDay;
	}
	
	function setDate() {
		$(document).clickDay(function(e) {
			$("#datas_new tbody tr").remove();
			if ($(".times").length == 0)
				$(".days").val("");
			date = (e.date).toString().split(" ");
			transformDate();
			month = date[1];
			day = date[2];
			weekDay = date[0];
			recordPosition();
			$(".form").stop(true, true).fadeIn(800);
			$(".months-container").stop(true,true).slideUp(500);
			$(".htmleaf-container").animate({width : "1000px"}, 300);
			$("#datas_new thead").remove();
			var ths = "";
				for (var i = 0; i < 7; i++) {
					if ((parseInt(day) - findIndex(weekDay)) > 0) {
						if (parseInt(day) + i - findIndex(weekDay) > days[month - 1])
							break;
						ths += "<th id='t" + i + "' style='width: 65px; font-size: 14px; line-height: 26px;'><input name='t" + i + "' type='text' value='" + month + "/" + (parseInt(day) + i - findIndex(weekDay)) + "(" + weekday[i] + ")" + "' readonly='readonly' /></th>";
					}
					else {
						if (findIndex(weekDay) - day + 1 + i > 6)
							break;
						ths += "<th id='t" + i + "' style='width: 65px; font-size: 14px; line-height: 26px;'><input name='t" + i + "' type='text' value='" + month + "/" + (1 + i) + "(" + weekday[findIndex(weekDay) + i - parseInt(day) + 1] + ")" + "' readonly='readonly' /></th>";
					}
			}
			for (var j = i; j < 7; j++) {
				ths += "<th id='t" + j + "' style='width: 65px;'><input name='t" + j + "'type='text' value='' readonly='readonly' /></th>"
			}	
			$("#datas_new").append("<thead id='thead_new'><tr><th style='width: 130px'><img id='add' src='../resourses/img/icon/ad.png' title='點選此或點選鍵盤『+』鍵新增工時摘要' /></th>" + ths + "<th style='width: 65px;'></th><th style='width: 65px;'></tr></thead>");
			for (var i = 0; i < 4; i++) {
				var tds = "";
				for (var j = 0; j < 7; j++) {
					var title = $("#t" + j).children("input").val();
					str = title.split("("); 
					if (title == "" || str[1] == "六)" || str[1] == "日)" || str[0] == "1/1" || str[0] == "2/29" || str[0] == "4/4" || str[0] == "4/5" || str[0] == "6/9" || str[0] == "6/10" || str[0] == "9/15" || str[0] == "9/16" || str[0] == "10/10"|| str[0] == "2/8"|| str[0] == "2/9"|| str[0] == "2/10"|| str[0] == "2/11"|| str[0] == "2/12") 
						tds += "<td><input type='number' min='0' max='8' id='time" + i + j + "' class='times form-control' name='time" + i + j + "' readonly='readonly' /></td>";
					else
						tds += "<td><input type='number' min='0' max='8' id='time" + i + j + "' class='times form-control-static' name='time" + i + j + "' value='' disabled required /></td>";
				}
				tds += "<td><input type='text' id='week" + i + "' class='week times form-control' name='week" + i + "' readonly='readonly'></td><td><img src='../resourses/img/icon/trash.png' class='trash'></td>";
				$("#datas_new").append("<tr style='color: black'><td id='item" + i + "' class='item' ' align='center' valign='center'><input name='item" + i + "' type='text' placeholder='工時摘要' value='' readonly='readonly' /></td>" + tds + "</tr>");
			}
			$.ajax({
				url : "/IISI_Project/addTask",
				type : "post",
				contentType: "application/x-www-form-urlencoded; charset=utf-8",
				data : {
					t0 : $("#t0").children("input").val()
				}
			})
			.done(function(status) {
				$("#LEAVE").empty();
				if (status.substr(0, 1) == 4 || status.substr(0, 1) == 3) {
					alertify.confirm("本週工時紀錄已存在！無法再度新增<br />如需修改請移轉至『工時修改』頁面<br />確定：前往頁面<br />取消：變更日期", function(e) {
						if (e) {
							var task_name = "#" + status.substr(1);
//							window.location.replace('workTime_modify.jsp');
							$("#manager_block").attr("action", "/IISI_Project/jsp/workTime_modify.jsp")
							$.ajax({
								url : "/IISI_Project/taskModify",
								type : "post",
							})
							.done(function(result) {
								$("#content").empty();
								$("#content").html(result);
								$(task_name).click();
							})
							.fail(function() {
								alertify.error("連線錯誤！")
							});
						}
						else {
							recoverPosition();
							$(".months-container").stop(true,true).slideDown(1000);
							$(".htmleaf-container").animate({width : "100%"}, 600);
							$(".form").stop(true,true).slideUp(800);
						}
					});
				}
				else if (status.substr(0, 1) == 1) {
					alertify.alert("您本週工時紀錄已提交審核！請靜候主管審核", function(e) {
						if (e) {
							recoverPosition();
							$(".months-container").stop(true,true).slideDown(1000);
							$(".htmleaf-container").animate({width : "100%"}, 600);
							$(".form").stop(true,true).slideUp(800);
						}
					});
				}
				else if (status.substr(0, 1) == 2) {
					alertify.confirm("您本週工時紀錄已審核通過！<br />如欲察看，請前往『工時查詢』頁面<br />確定：前往頁面<br />取消：變更日期", function(e) {
						if (e)
							location.replace("search.jsp")
						else {
							recoverPosition();
							$(".months-container").stop(true,true).slideDown(1000);
							$(".htmleaf-container").animate({width : "100%"}, 600);
							$(".form").stop(true,true).slideUp(800);
						}
					});
				} 
				else if (status.substr(0, 1) == 5) {
					alertify.confirm("您本週尚有假單未完成！<br />如欲察看，請前往『請假系統』頁面<br />確定：前往頁面<br />取消：變更日期", function(e) {
						if (e)
							location.replace("leave_index.jsp")
						else {
							recoverPosition();
							$(".months-container").stop(true,true).slideDown(1000);
							$(".htmleaf-container").animate({width : "100%"}, 600);
							$(".form").stop(true,true).slideUp(800);
						}
					});
				}
				else {
					if (status.length > 100)
						$("#leaveBlock").removeAttr('hidden');
					$("#LEAVE").html(status);
					alertify.log("您現在已可填寫" + month + "/" + day + "(" + weekDay + ")該週工時");
//					setTimeout(function() {
//						$("#add").click();
//					}, 1000)
				}
			})
			.fail(function() {
				alertify.error("資料讀取錯誤，請稍後在試！");
			});
		});
	}
	setDate();
	
	function findIndex(weekDay) {
	var index = 0;
		for (var i = 0; i < weekday.length; i++) {
			if (weekDay == weekday[i])
				index = i;
		}
		return index;
	}
	
	$(".form").hide();
	$(document).on("mouseover", ".year-title", function() {
		if($('#datas_new').is(':visible')) {
			$(".year-title").off();
//			$(".months-container").hide();
			$(".prev").off();
			$(".next").off();
		}
	});
	$(document).on("click", ".year-title", function() {
		recordPosition();
		$('#add').tooltipster({
			animation: 'grow',
			theme: 'tooltipster-punk',
		    position : 'top',
		    multiple : 'true'
		});
		$('.days').tooltipster({
			animation: 'grow',
			theme: 'tooltipster-punk',
		    position : 'bottom',
		    multiple : 'true'
		});
		if($('.only').is(':visible')) {
			alertify.confirm("變更日期將會造成資料遺失，確定繼續？", function(e) {
				if(e) {
					$('#add').tooltipster('disable');
					$('.days').tooltipster('disable');
					$(".months-container").stop(true,true).slideDown(1000);
//					$(".days").val("");
//					$(".form").fadeOut(1200);
					$(".htmleaf-container").animate({width : "100%"}, 600);
					$(".form").stop(true,true).slideUp(800);
//					$("#datas_new thead").remove();
				}	
				recoverPosition();
			});
		}
	});
	
//	$(document).on("mousemove", "#datas_new", function() {
//		var table = document.getElementById("datas_new");
//		var row = table.rows.length - 3;
//		for (var i = 0; i < 5; i++) {
//			var title = $("#t" + i).text();
//			if (title.length < 1) {
//				for (var j = 0; j < row; j++)
//					$("#time" + j + i).val("");
//					$("#time" + j + i).attr("readonly", "readonly");
//			}
//		}
//	});
	
    $('#calendar').calendar({ 
    	displayWeekNumber: true,
        enableContextMenu: true,
        enableRangeSelection: true,
//        contextMenuItems:[
//            {
//                text: "填寫工時紀錄",
//                click: setDate
//            } ],
        selectRange: function(e) {
        },
        mouseOnDay: function(e) {
            if(e.events.length > 0) {
                var content = '';
                
//                $(e.element).popover('show');
            }
        },
        mouseOutDay: function(e) {
            if(e.events.length > 0) {
//                $(e.element).popover('hide');
            }
        },
        dayContextMenu: function(e) {
            $(e.element).popover('hide');
        },
        dataSource: [
         			{
         				startDate: new Date(new Date().getFullYear(), new Date().getMonth(), systemDateS()),
         				endDate: new Date(new Date().getFullYear(), new Date().getMonth(), systemDateE())
         			},
         			{
         				startDate: new Date(new Date().getFullYear(), month, day),
         				endDate: new Date(new Date().getFullYear(), month, day)
         			},
         ]
    });
});
