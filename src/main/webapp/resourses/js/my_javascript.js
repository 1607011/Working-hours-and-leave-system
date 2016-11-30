$(function(){
	//清單定位
	var i = 0;
	$(".menus").each(function(){
		$(this).css('left',190*i);
		i++;
	});
	
	//滑入 & 滑出 & hover事件
	$(".lists").hide();
	$(".menus").hover(function(){
		$(this).css("background-color", "#222222");
		$(this).children("ul").stop(true,true).slideDown(1000);
		},function(){
			$(this).css("background-color", "#666666");
			$(this).children("ul").stop(true,true).slideUp(500);
	});
	
	//每個清單項目的hover事件
	$(".lists li").hover(function(){
		$(this).css("background-color","#222222");
	},function(){
		$(this).css("background-color","#AAAAAA");
	});
	
	//滑鼠滑入 & hover事件
	$("fieldset ul li").hover(function(){
		$(this).stop(true, true).animate({left:'0px'},800,'easeOutBounce');
		$(this).css("background-color","#222222");
		},function(){
			$(this).css("background-color","#888888");
			$(this).stop(true, true).animate({left:'-50px'},500);
	});
	
	$(document).on("mouseover", ".trash", function(){
//			$(this).attr('src', 'img/icon/trash_open.png');
			$(this).css({width : '33px', height : '33px'});
	});
	
	$(document).on("mouseout", ".trash", function(){
//		$(this).attr('src', 'img/icon/trash.png');
		$(this).css({width : '30px', height : '30px'});
	});
	
	
	//--新增工時/修改工時--資料刪除
	$(document).on("click", "#manager_block .trash", function(){
		var data = $(this).closest("tr");
		var index = $(this).parent("td").parent("tr").children(".item").attr("id").substr(4);
		console.log(index);
		recordPosition();
		alertify.confirm("注意：資料刪除後將不能恢復\n確認繼續刪除動作?", function(e) {
			if (e) {
//				temp ++;
//				data.stop(true, true).slideUp(1000);
				data.fadeOut(800, function() {
					data.remove();
					$(".times").click();
					addTask(index);
				});
				alertify.log("您所選的資料已成功刪除！");
//				manager_block.submit();
			}
			recoverPosition();
		});
	});
//	var temp = 0;
	$("#manager_block1").on("click", ".trash", function(){
		var data = $(this).closest("tr");
		var name = data.children("td").children("a").children("input").val();
		recordPosition();
//		$("#manager_block1").attr("action", "/IISI_Project/workTime_modify.jsp");
		alertify.confirm("注意：資料刪除後將不能恢復\n確認繼續刪除動作?", function(e) {
			if (e) {
				recoverPosition();
//				temp++;
//				$("#deletedNum").val(temp);
				$("#deleteTask").val(name);
				$.ajax({
					url : '/IISI_Project/delete',
					type : 'post',
					data : {
						deleteTask : name			
					}
				})
				.done(function(msg) {
					data.fadeOut(800, function() {data.remove();});
					alertify.log(msg + "工時紀錄已成功刪除！");
				})
				.fail(function(){
					alertify.error("資料刪除失敗！請重新再試一次！");
				});
			}
			recoverPosition();
		});
	});
	
	$("#leave_block1").on("click", ".trash", function(){
		var data = $(this).closest("tr");
		var name = data.children("td").children("a").children("input").val();
		recordPosition();
		alertify.confirm("注意：資料刪除後將不能恢復\n確認繼續刪除動作?", function(e) {
			if (e) {
				recoverPosition();
				$("#deleteLeave").val(name);
				$.ajax({
					url : '/IISI_Project/deleteLeave',
					type : 'post',
					data : {
						deleteLeave : name			
					}
				})
				.done(function(msg) {
					data.fadeOut(800, function() {data.remove();});
					alertify.log(msg + "請假紀錄已成功刪除！");
				})
				.fail(function(){
					alertify.error("資料刪除失敗！請重新再試一次！");
				});
			}
			recoverPosition();
		});
	});
	
//	var table = document.getElementById("datas_new");
	$(document).on("click", ".trash", function(){
		setDay();
	});
	
	//新增工做項目並檢查有無重複
	var addTask = function(index) {
		var tds = "";
		var item = "";
//		var valid = true;
		var str;
//			row = table.rows.length - 2;
//		var row = 0;
//		$("tr").each(function() {
//			row++;
//		});
//		row -= 11;
//		if (table != null)
//			row = table.rows.length - 2;
//		for (var i = 0; i < (row + temp); i++) {
//			if(item == $("#item" + i).children("input").val()) {
//				alertify.error("工作項目已存在！<br />請輸入新的工作項目");
//				valid = false;
//				break;
//			}
//		}
//			if (item.length == 0 || item.length > 20) {
//				alertify.error("新增工作摘要的字節<br />必須介於1~20個字元之間！");
//				valid = false;
//			}
//		if (item != null && valid) {
			for (var j = 0; j < 7; j++) {
				var title = $("#t" + j).children("input").val();
				str = title.split("(");
				if (title == "" || str[1] == "六)" || str[1] == "日)" || str[0] == "1/1" || str[0] == "2/29" || str[0] == "4/4" || str[0] == "4/5" || str[0] == "6/9" || str[0] == "6/10" || str[0] == "9/15" || str[0] == "9/16" || str[0] == "10/10"|| str[0] == "2/8"|| str[0] == "2/9"|| str[0] == "2/10"|| str[0] == "2/11"|| str[0] == "2/12") 
					tds += "<td><input type='number' min='0' max='8' id='time" + index + j + "' class='times form-control' name='time" + index + j + "' readonly='readonly' /></td>";
				else
					tds += "<td><input type='number' min='0' max='8' id='time" + index + j + "' class='times form-control-static' name='time" + index + j + "' value='' disabled required /></td>";
			}
			tds += "<td><input type='text' id='week" + index + "' class='week times form-control' name='week" + index + "' readonly='readonly'></td><td><img src='../resourses/img/icon/trash.png' class='trash'></td>";	
			$("#datas_new").append("<tr style='color: black'><td id='item" + index + "' class='item' ' align='center' valign='center'><input name='item" + index + "' type='text' placeholder='工時摘要' value='" + item + "' readonly='readonly' /></td>" + tds + "</tr>");
//			alertify.log("已新增工作摘要：[" + item + "]");
//		}
		recoverPosition();
	};
	
	
	//設置每日時數上限
	$(document).on("mouseover", "#manager_block", function() {
		if ($('#list').length == 0)
			return false;
		var table = document.getElementById("leaveTable");
		var row = table.rows.length - 1;
		var upperBound = [8, 8, 8, 8, 8];
		for (var i = 0; i < row; i++) {
			for (var j = 0; j < 5; j++) {
//				max = Number($('#day' + j).attr('max'));
//				console.log("max=" + max);
				var date = $('#leave' + i).text().split("-");
				var leaveDate = Number(date[1]) + "/" + Number(date[2]);
//				console.log($('#t' + j).children('input').val().substr(0, 4) + "//" + leaveDate);
				if($('#t' + j).children('input').val().substr(0, 4) == leaveDate)
					upperBound[j] -= Number($('#leaveTime' + i).text())
					$('#day' + j).attr('max', upperBound[j]);
			}
		}
	});
	
	//設置每日可填時數上限
	$(document).on("focus mouseup change", ".times", function() {
		var thisMoment = $(this).attr("id").substr(4, 1);
		var index = $(this).attr("id").substr(5, 1);
		var table = document.getElementById("datas_new");
		var row = table.rows.length - 2;
		var dayHours = 0;
		//計算當日其他摘要時數總和
		for (var j = 0; j < row; j++) {
			if (j != thisMoment)
				dayHours += Number($("#time" + j + index).val());
		}
//		console.log(row);
		var max = Number($("#day" + index).attr("max")) - dayHours;
		var maxKeyCode1 = 96 + max;
		var maxKeyCode2 = 48 + max;
//		if ($(this).val() == 0)
		$(this).attr("max", max);
		$(this).attr("min", 0);
		//限制特定按鍵輸入
		$(this).keydown(function(e) {
			if (e.keyCode == 109 || e.keyCode == 189 || e.keyCode == 110 || (e.keyCode > maxKeyCode1 && e.keyCode <= 105) || (e.keyCode > maxKeyCode2 && e.keyCode <= 57 )){
				e.returnValue=false;
	            e.keyCode = 0;
	            return false; 
			}
//			if (($(this).val() > 0)) {
//				if ((e.keyCode >= 96 && e.keyCode <= 111) || (e.keyCode >= 48 && e.keyCode <= 57)) {
//					return false;
//				}
////				$(this).val("");
//			}
			if(($(this).val() <= 8)) {
				if ((e.keyCode >= 96 && e.keyCode <= 111) || (e.keyCode >= 48 && e.keyCode <= 57)) { 
					$(this).val("");
				}
				$(this).on("blur", function() {
					if ($(this).val() == "" && $(this).attr("readonly") != "readonly")
						$(this).val(0);
				});
				event = e;
//				console.log(e.keyCode);
			}
		});
	});
	
	//點選『+』新增工時
	$(document).keydown(function (e) {
		if (e.keyCode == 107 && $("#send").length > 0) {
			recordPosition();
//			addTask();
			$("#add").click();
		}
	});
	$(document).on("click", "#add", function(){
		var table = document.getElementById("datas_new");
		var row = table.rows.length - 2;
		if (row == 8)
			return false;
		recordPosition();
		for (var i = 0; i < (8 - row); i++) {
			var tds = "";
			for (var j = 0; j < 7; j++) {
				var title = $("#t" + j).children("input").val();
				str = title.split("("); 
				if (title == "" || str[1] == "六)" || str[1] == "日)" || str[0] == "1/1" || str[0] == "2/29" || str[0] == "4/4" || str[0] == "4/5" || str[0] == "6/9" || str[0] == "6/10" || str[0] == "9/15" || str[0] == "9/16" || str[0] == "10/10"|| str[0] == "2/8"|| str[0] == "2/9"|| str[0] == "2/10"|| str[0] == "2/11"|| str[0] == "2/12") 
					tds += "<td><input type='number' min='0' max='8' id='time" + (row + i) + j + "' class='times form-control' name='time" + (row + i) + j + "' readonly='readonly' /></td>";
				else
					tds += "<td><input type='number' min='0' max='8' id='time" + (row + i) + j + "' class='times form-control-static' name='time" + (row + i) + j + "' value='' disabled required /></td>";
			}
			tds += "<td><input type='text' id='week" + (row + i) + "' class='week times form-control' name='week" + (row + i) + "' readonly='readonly'></td><td><img src='../resourses/img/icon/trash.png' class='trash'></td>";
			$("#datas_new").append("<tr style='color: black'><td id='item" + (row + i) + "' class='item' ' align='center' valign='center'><input name='item" + (row + i) + "' type='text' placeholder='工時摘要' value='' readonly='readonly' /></td>" + tds + "</tr>");
		}
	});
	
	$(document).on("mouseover", "#add", function(){
		$(this).css("width", "31px");
	});
	$(document).on("mouseout", "#add", function(){
		$(this).css("width", "30px");
	});
	
	//讓使用者可新增修改工作項目
	$(document).on("click", ".item", function(){
//		row = table.rows.length - 3;
		var table = document.getElementById("datas_new");
		var row = table.rows.length - 2;
		var itemid = "#" + $(this).attr("id");
		var item = $(this).children("input").val();
		var index = $(this).attr("id").substr(4);
		var valid = true;
		recordPosition();
		alertify.prompt("請輸入您的工作摘要", function(e, modify_item) {
//			$(".alertify-button-ok").attr("type", "button");
//			$(".alertify-button-ok").attr("disabled", "true");
			if(e) {
				for (var i = 0; i < (row + 1); i++) {
					if(modify_item.length != 0 && modify_item == $("#item" + i).children("input").val()) {
						alertify.error("工作項目已存在！<br />請輸入新的工作項目");
						valid = false;
						break;
					}
				}
				if (modify_item.length == 0 || modify_item.length > 20) {
					alertify.error("新增工作摘要的字節<br />必須介於1~20個字元之間！");
					valid = false;
				}
				if (modify_item != null && valid) {
					$(itemid).children("input").val(modify_item);
					if(item != "") {
						alertify.log("工作摘要：[" + item + "]<br />已成功修改為 [" + modify_item + "]");
					}
					else {
						for (var j = 0; j < 5; j++) {
							if ($("#time" + index + j).attr("disabled") == "disabled") {
								$("#time" + index + j).removeAttr("disabled");
								$("#time" + index + j).val(0);
							}
						}
						alertify.log("您已新增了[" + modify_item + "]工作摘要");
					}
				}
			}
			recoverPosition();
		}, item);
	});
	
	//計算各工作摘要總時數
	var weekTotal = new Array(8);
	$(document).on("mousemove click keydown keyup", ".times", function(){
//		var row = table.rows.length - 3;
//		var row = $("#datas_new").prevAll().length; //讀取表格列數
		var table = document.getElementById("datas_new");
		var row = table.rows.length - 2;
		for (var i = 0; i < row; i++) {
			weekTotal[i] = 0;
			for (var j = 0; j < 5; j++) {
				if ($("#time" + i + j).val() == "")
					weekTotal[i] += 0;
				else if (!isNaN($("#time" + i + j).val()))
					weekTotal[i] += parseInt($("#time" + i + j).val());
				if ($(".focus").length > 0) {
					if ($("#time" + i + j).val() != 0) 
						$("#time" + i + j).addClass("out-border");
					else 
						$("#time" + i + j).removeClass("out-border");
				}
				$("#week" + i).val(weekTotal[i]);
				if ($("#week" + i).val() == 0)
					$("#week" + i).val("");
			}
		}
	});
	
	//計算各日總時數
	var dayTotal = new Array(5);
	function setDay() {
		$(document).on("mousemove click keydown keyup", ".times", function(){
//		row = table.rows.length - 3;
//		var row = $("datas_new").prevAll().length; //讀取表格列數
//		console.log(row);	
		var table = document.getElementById("datas_new");
		var row = table.rows.length - 2;
		for (var int = 0; int < 5; int++) {
				dayTotal[int] = 0;
				for (var int2 = 0; int2 < row; int2++) {
					if ($("#time" + int2 + int).val() == "")
						weekTotal[i] += 0;
					else if (!isNaN($("#time" + int2 + int).val()))
						dayTotal[int] += parseInt($("#time" + int2 + int).val());
					$("#day" + int).val(dayTotal[int]);
					if ($("#day" + int).val() == 0)
						$("#day" + int).val("");
				}
				if (dayTotal[int] == $('#day' + int).attr("max")) {
					$("#day" + int).css("color", "black");
				}
			}	
//			for (var i = 0; i < 5; i++) {
//				$("#day" + i).tooltipster({
//					animation: 'grow',
//				    theme: 'tooltipster-punk',
//				    interactive : 'true',
//				    multiple : 'true',
//				    position : 'bottom',
//				    trigger : 'clcik'
//				});
//				if (dayTotal[i] <= 8) {
//					$("#day" + i).css("color", "black");
//					$("#day" + i).tooltipster('hide');
//				}
//				else if (dayTotal[i] > 8) {
//					$("#day" + i).css("color", "red");
//					$("#day" + i).tooltipster('show');
//				}
//			}
		});
	}
	setDay();
	
//	$(".close").on("click", function() {	
//		$(".days").tooltipster('hide');
//	});
//	
	//計算週總時數
	$(document).on("mousemove click keydown keyup", ".times", function() {
		var total = 0;
		for (var i = 0; i < 5; i++) {
			total += dayTotal[i];
			$("#weektotal").val(total);
		}
		if (total <= 40)
			$("#weektotal").css("color", "black");
		else if (total > 40)
			$("#weektotal").css("color", "red");
		if ($("#weektotal").val() == 0)
			$("#weektotal").val("");
	});
	
	//暫存工時紀錄
	$(document).on("click", "#save", function() {
		$("#status").val("4");
		alertify.confirm("確認暫存目前已填寫的工時資紀錄?", function(e) {
			if (e) {
//				$("#manager_block").attr("onsubmit", "return true;");
				if($(".item").length > 0) {
					$.ajax({
						url : $("#manager_block").attr("action"),
						type : 'post',
						contentType: "application/x-www-form-urlencoded; charset=utf-8", 
						data : $("#manager_block").serialize()
					})
					.done(function(response){
						setTimeout(function() { alertify.alert(response, function(e) {
							if(e) {
								history.go(0);
							}
						});}, 800);
//						setTimeout(history.go(0), 1200);
					})
					.fail(function() {
						alertify.error("工時記錄暫存失敗！<br />請重新提交一次");
					});
				}
				else {
					alertify.confirm("請至少新增一個工時摘要", function(e) {
						if(e)
//							addTask();
							$("#add").click();
					});
				}
			}
			else
				$("#manager_block").attr("onsubmit", "return false;");
		});
	});
	
	//送出按鈕觸發click事件後檢查一日總時數是否超過規定時數
	$(document).on("click", "#send", function(){
		var isValid = true;
		var empty = 0;
		var table = document.getElementById("datas_new");
		var row = table.rows.length - 2;
		var itemNum = 0;
		$("#status").val("1");
		alertify.confirm("確認提交工時資紀錄?", function(e) {
			if (e) {
//				for (var i = 0; i < row; i++) {
//					if ($('#item' + i).children('input').val() != "")
//						itemNum++;
//				}
				for (var i = 0; i < 5; i++) {
					if ($(".item").length == 0) {
						isValid = false;
						alertify.error("您並沒有新增任何工時摘要<br/ >請重新新增填寫");
						break;
					}
//					console.log(row);
					console.log($("#time"+ row + i).val());
					if ($("#time"+ 0 + i).attr("readonly") != "readonly") {
						if (dayTotal[i] == "")
							dayTotal[i] = 0;
						if (dayTotal[i] != $("#day" + i).attr("max")) {	
							isValid = false;
							alertify.error("您尚未填滿一日工作時數！");
							break;
						}
					}
					else
						empty++;
				}
				for (var i = 0; i < 5; i++) {
					if (dayTotal[i] != $('#day' + i).attr("max"))
						$("#day" + i).css("color", "red");
				}
				if (empty == 5)
					alertify.error("本周次無任何工時可填寫<br />請選擇其他周次填寫");
				else if (isValid) {
//					alertify.alert("您的工時紀錄已提交，請靜心等候主管審核");
//					setTimeout(function() { manager_block.submit(); }, 500);
					$.ajax({
						url : $("#manager_block").attr("action"),
						type : 'post',
						contentType: "application/x-www-form-urlencoded; charset=utf-8", 
						data : $("#manager_block").serialize()
					})
					.done(function(response){
						if (response == '10') {
							alertify.alert('您尚有假單未審核完成！請耐心等候審核完成');
							return false;
						}
						setTimeout(function() { alertify.alert(response, function(e) {
							if(e) {
								history.go(0);
							}
						});}, 800);
					})
					.fail(function() {
						alertify.error("工時記錄提交失敗！<br />請重新提交一次");
					});
				}
				else
					$("#manager_block").attr("onsubmit", "return false;");
			}
		});
	});
	
	//清空資訊警告及初始化表單
	$(document).on("click", "#clear", function(){
		alertify.confirm("注意：資料清除後將不能恢復\n確認繼續刪除動作?", function(e) {
			if(e) {
				$(".times").click();
				for (var i = 0; i < 5; i++) {
					$("#day" + i).css("color", "black");
				}
			$(".times").each(function() {
				if ($(this).attr("readonly") != "readonly" && $(this).attr("disabled") != "disabled") {
					$(this).val(0);
				}
			});
			$(".times").click();
//			manager_block.reset();
			}
		});
	});
	
	//離開--新增工時--時提醒
//	$(window).on("beforeunload", function () { 
//		manger_block.submit();
//		alertify.confirm("準備要離開本頁面，請再確認一遍紀錄已提交或已儲存", function(e) {
//			if (e)
//				alertify.alert("已成功離開")
//		});
//	});

	//滑鼠移入時變色
	$(document).on("mouseover", "#tbody tr", function() {
		$(this).css("cursor", "pointer");
		$(this).css("background-color", "#FFFFF0");
		$(this).children("td").children(".newTab").children("#task").css("color", "black");
		$(this).children(".datas").css("color", "black");
		$(this).children("td").children("a").children("input").css("background-color", "#FFFFF0");
		$(this).children("td").children("input").css("background-color", "#FFFFF0");
	});
	$(document).on("mouseout", "#tbody tr", function() {
		$(this).children("td").children(".newTab").children("#task").css("color", "#666666");
		$(this).css("background-color", "#F5FFFA");
		$(this).children(".datas").css("color", "#666666");
		$(this).children("td").children("a").children("input").css("background-color", "#F5FFFA");
		$(this).children("td").children("input").css("background-color", "#F5FFFA");
	});
	
	//設定資料狀態-暫存/退回顏色
	$(document).on("click", "#myModal", function () {
		$(".result").each(function() {
			var status_color = ($(this).children("img").attr("class") == "save") ? "green" : "red";
			if ($(this).children("img").attr("class") == "approved")
				status_color = "blue";
			$(this).css("color" , status_color);
		});
	});
	$(".result").each(function() {
		var status_color = ($(this).children("img").attr("class") == "save") ? "green" : "red";
		$(this).css("color" , status_color);
	});
	
	//檢查--修改工時--裏頭有無資料
	var datas_table = document.getElementById("datas_modify");
//	$("#datas_modify").on("mousemove", function(){
//		var datas_row = datas_table.rows.length;
//		if (datas_row == 2) {
//			$("#tfoot").removeAttr("hidden");
////			$("#tfoot").css("background-color", "#EEEEEE");
//		}
//	});
	if ($(".datas").length == 0) {
		$("#tfoot").removeAttr("hidden");
	};
	
	//設定表單可排序圖示
	var count = 0;
	var count1 = 0;
	$(document).on("click", "#thead .date", function() {
		count++;	
		if (count1 == 0) {
			if (count > 1 && count % 2 == 0)
				$(".date").children("div").children("span").attr("class", "glyphicon glyphicon-sort-by-attributes-alt");
			else if (count > 1 && count % 2 == 1)
				$(".date").children("div").children("span").attr("class", "glyphicon glyphicon-sort-by-attributes");
		}
		else {
			if (count % 2 == 0)
				$(".date").children("div").children("span").attr("class", "glyphicon glyphicon-sort-by-attributes-alt");
			else
				$(".date").children("div").children("span").attr("class", "glyphicon glyphicon-sort-by-attributes");
		}
	});
	$(document).on("click", "#thead .status", function() {
		count1++;	
		if (count1 % 2 == 1)
			$(this).children("div").children("span").attr("class", "glyphicon glyphicon-sort-by-alphabet-alt");
		else
			$(this).children("div").children("span").attr("class", "glyphicon glyphicon-sort-by-alphabet");
	});
	$(document).on("click", "#thead .allTime", function() {
		count1++;	
		if (count1 % 2 == 1)
			$(this).children("div").children("span").attr("class", "glyphicon glyphicon-sort-by-order");
		else
			$(this).children("div").children("span").attr("class", "glyphicon glyphicon-sort-by-order-alt");
	});
	
	$(document).on("mouseover", "#datas_modify", function() {
		// 可拖曳排序
		$(this).tablesorter();
	});
	$(document).on("mouseover", ".tableSortable", function() {
		$(".tableSortable").sortable({
		    helper: fixWidthHelper
		}).disableSelection();
		
		function fixWidthHelper(e, ui) {
		    ui.children().each(function() {
		        $(this).width($(this).width());
		    });
		    return ui;
		}
	});
	
//	//設定拖曳指標
//	$(document).on("mousedown", "tbody tr", function() {
//		$(this).addClass("cursorMove");
////		$(this).css("cursor", "url('cursor/Move.cur')");
//	});
//	$(document).on("mouseup", "tbody tr", function() {
//		$(this).removeClass("cursorMove");
////		$(this).css("cursor", "url('cursor/Normal Select.cur')");
//	});
	
	//設定網頁標題
	$(document).on("click", ".datas", function() {
//		$(".modal-body").empty();
		var title = $(this).parent("tr").children("td").children("a").children("#task").val();
		var leaveDate = $(this).parent("#search_leave_results").children(".datas").children("#leave_date").val();
		var status = "";
		if ($(this).parent("tr").children(".result").text() != "")
			status = "(" + $(this).parent("tr").children(".result").text() + ")";
		$("#Go").val(title);
		$(".modal-title").text(title + "工時紀錄" + status);
		if ($("#searchBtn").length > 0)
			$(".modal-title").text(leaveDate + "請假紀錄" + status);
		var id = $(this).parent("#search_results").children("td").children("#id").val();
		$("#userid").val(id);
		if ($("#manager_block1").length > 0 && $("#vertify_datas").length == 0) {
			$.ajax({
				url : '/IISI_Project/workTime_modify_data',
				type : 'post',
				contentType: "application/x-www-form-urlencoded; charset=utf-8", 
				data : $("#manager_block1").serialize()
			})
			.done(function(datas) {
				$(".content").html(datas);
				$(".times").click();
			})
			.fail(function(){
				alertify.error("工時紀錄讀取失敗！<br />請重新再試一次！");
			});
		}
//		else if ($("#search").length > 0) {
////			$("#search").attr("action", "/IISI_Project/acess.jsp");
//			$.ajax({
//				url : '/IISI_Project/acess',
//				type : 'post',
//				contentType: "application/x-www-form-urlencoded; charset=utf-8", 
//				data : $("#search").serialize()
//			})
//			.done(function(datas) {
//				$(".content").html(datas);
//			})
//			.fail(function(){
//				alertify.error("工時紀錄讀取失敗！<br />請重新再試一次！");
//			});
//		}
	});
	
	//取得待審核資料者id
	$(document).on("click", "#vertify_datas", function() {
		var id = $(this).children("td").children("#id").val();
		$("#userid").val(id);
//		$("#manager_block1").attr("action", "/IISI_Project/workTime_vertify_data.jsp");
//		manager_block1.submit();
		$.ajax({
			url : '/IISI_Project/vertify_data',
			type : 'post',
			contentType: "application/x-www-form-urlencoded; charset=utf-8", 
			data : $("#manager_block1").serialize()
		})
		.done(function(data) {
			$(".content").html(data);
			$(".times").click();
		})
		.fail(function(){
			alertify.error("工時紀錄讀取失敗！<br />請重新再試一次！");
		});
	});
	
	var pageChange = function() {
		$.ajax({
			url : $(".pageForm").attr("action"),
			type : 'post',
			contentType: "application/x-www-form-urlencoded; charset=utf-8", 
			data : $(".pageForm").serialize()
		})
		.done(function(result) {
			if ($(".resultList").length > 0) {
				$(".resultList").empty();
				$(".resultList").html(result);
			}
		})
		.fail(function(){
			alertify.error("換頁失敗！請重新再試一次！");
		});
	}
	
	//換頁
	$(document).on("mouseover", "#datas_modify", function() {
		$("#totalPage").val($("#nowPage > option").length);
	});
	$(document).on("change", "#nowPage", function() {
		$("#now").val($('#nowPage').val());
		pageChange();
	});
	$(document).on("click", "#backward", function() {
		var backPage = parseInt($('#nowPage').val()) - 1;
		$("#now").val(backPage);
		pageChange();
	});
	$(document).on("click", "#fast-backward", function() {
		var initalPage = 1;
		$("#now").val(initalPage);
		pageChange();
	});
	$(document).on("click", "#forward", function() {
		var nextPage = parseInt($('#nowPage').val()) + 1;
		$("#now").val(nextPage);
		pageChange();
	});
	$(document).on("click", "#fast-forward", function() {
		var finalPage = $("#nowPage > option").length;
		$("#now").val(finalPage);
		pageChange();
	});
	
	//展現textArea讓主管填寫退回原因
//	$("#cause").hide();
//	var click = 0;
//	$(document).on("click", "#back", function() {
//		click ++;
//		$("#cause").stop(true, true).fadeIn(800);
//		$("#goBack").hide();
//		$("#acess span").attr("class", "glyphicon glyphicon-envelope");
//		$("#acess #acessSpan").attr("class", "");
//		$("#acess #acessSpan").text("送出");
//		$("#back #backSpan").text("取消");
//		if (click % 2 ==0) {
//			$("#cause").stop(true, true).fadeOut(500);
//			$("#goBack").show();
//			$("#acess span").attr("class", "");
//			$("#acess #acessSpan").attr("class", "glyphicon glyphicon-check");
//			$("#acess #acessSpan").text("核可");
//			$("#back #backSpan").text("退回");
//		}
//	});
	$(document).on("click", "#back", function() {
		$("#vertifyStatus").val(3);
		alertify.confirm("確定提交退回的審核結果？", function(e) {
			if (e) {
				$.ajax({
					url : "/IISI_Project/vertifys",
					type : 'post',
					contentType: "application/x-www-form-urlencoded; charset=utf-8", 
					data : $("#manager_block").serialize()
				})
				.done(function(response){
					alertify.alert(response, function(e) {
						if(e) {
							history.go(0);
						}
					});
				})
				.fail(function() {
					alertify.error("審核結果送出失敗！請重新提交一次");
				});
			}	
		});
	});
	
	$(document).on("click", "#acess", function() {
		$("#vertifyStatus").val(2);
		alertify.confirm("確定提交核可的審核結果？", function(e) {
			if (e) {
				$.ajax({
					url : "/IISI_Project/vertifys",
					type : 'post',
					contentType: "application/x-www-form-urlencoded; charset=utf-8", 
					data : $("#manager_block").serialize()
				})
				.done(function(response){
					alertify.alert(response, function(e) {
						if(e) {
							history.go(0);
						}
					});
				})
				.fail(function() {
					alertify.error("審核結果送出失敗！請重新提交一次");
				});
			}	
		});
	});
	
	$(document).on("click", "#search_results", function() {
		var id = $(this).children(".datas").children("#id").val();
		$("#userid").val(id);
		$.ajax({
			url : '/IISI_Project/acess',
			type : 'post',
			contentType: "application/x-www-form-urlencoded; charset=utf-8", 
			data : $("#search").serialize()
		})
		.done(function(datas) {
			$(".content").html(datas);
			$(".times").click();
		})
		.fail(function(){
			alertify.error("工時紀錄讀取失敗！<br />請重新再試一次！");
		});
	});
	
	$(document).keydown(function (e) {
		if ($("#search_results").length > 0) {
			switch(e.keyCode) {
			case 37:
				$("#backward").click();
				break;
			case 39:
				$("#forward").click();
				break;
			}
		}
	});
	
//	$(document).on("change", ".content", function() {
//		$(".times").click();
//		$(".times").off();
//	});
	
	$(document).on("click", "#new", function() {
		location.replace("workTime_new.jsp");
	});
	
	//提示訊息
	if ($('.tooltip').length > 0) {
		$('.tooltip').tooltipster({
			animation: 'grow',
		    arrow : 'false',
			theme: 'tooltipster-punk',
		    interactive : 'true',
		    position : 'top',
		    offsetX : '500px'
		});
		$('.tooltip').tooltipster('show');
		setTimeout(function() {$('.tooltip').tooltipster('hide');}, 4000);
		$(document).on("click", "td", function() {
			$('.tooltip').tooltipster('hide');
		});
	}
	var click = 0;
	$(document).on("click", "#list", function() {
		click++;
		if (click % 2 == 1) {
			$(this).children('#text').text('隱藏請假資料');
			$(this).children('#icon').attr('class', 'glyphicon glyphicon-eye-close pos');
			$("#LEAVE").stop(true, true).slideDown(800);
		}
		else {
			$(this).children('#text').text('顯示請假資料');
			$(this).children('#icon').attr('class', 'glyphicon glyphicon-eye-open pos');
			$("#LEAVE").stop(true, true).slideUp(800);
		}
	});
	
//	$(document).on("mouseover", "#add", function() {
//		$('#add').tooltipster({
//			animation: 'grow',
//			theme: 'tooltipster-punk',
//		    position : 'top',
//		    multiple : 'true',
//		    trigger : 'custom'
//		});
//		$('#add').tooltipster('show');
//		setTimeout(function() {$('#add').tooltipster('hide');}, 4000);
//		$(document).on("click", "#add", function() {
//			$('#add').tooltipster('disable');
//		});	
//		$(document).on("click", ".year-title", function() {
//			$('#add').tooltipster('disable');
//			$('.days').tooltipster('disable');
//		});
//	}); 
});

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

//window.onbeforeunload = recordPosition();
//
//window.onload = recoverPosition();