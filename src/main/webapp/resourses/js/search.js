$(function(){
	
	//AutoComplete
	var ids = ["401170341", "401170327" ,"401170456", "401170286", "401422029"];
	var names = ["許俊山", "陳怡安", "謝明翰", "范振軒", "陳姵安"];
	var comboplete = new Awesomplete('input.awesomplete', {
		minChars: 1,
	});
	Awesomplete.$('.awesomplete').addEventListener("click", function() {
		if (comboplete.ul.childNodes.length === 0) {
			comboplete.minChars = 1;
			comboplete.evaluate();
		}
		else if (comboplete.ul.hasAttribute('hidden')) {
			comboplete.open();
		}
		else {
			comboplete.close();
		}
	});
	var comboplete2 = new Awesomplete('input.awesomplete2', {
		minChars: 1,
	});
	Awesomplete.$('.awesomplete2').addEventListener("click", function() {
		if (comboplete2.ul.childNodes.length === 0) {
			comboplete2.minChars = 1;
			comboplete2.evaluate();
		}
		else if (comboplete2.ul.hasAttribute('hidden')) {
			comboplete2.open();
		}
		else {
			comboplete2.close();
		}
	});
//	$("#search_id").autocomplete({
//		lookup: ids
//	});
//	$("#search_name").autocomplete({
//		lookup: names
//	});
	//設定月分
	$("#start").on("change keydown keyup", function() {
		if ($("#end").val() < $(this).val())
			$("#end").val($(this).val());
	});
	
	//設定訖月初始值
	$(document).on("mousedown", "#end" ,function() {
		var end_min = ($("#start").val() == "") ? 1 : $("#start").val(); 
		$("#end").attr("min", end_min);
	});
	
	//設定年分
	$(document).on("change keydown keyup", "#start_year" ,function() {
		if ($("#end_year").val() < $(this).val())
			$("#end_year").val($(this).val());
	});
	
	//設定訖年初始值
	$(document).on("mousedown", "#end_year" ,function() {
		var end_year_min = ($("#start_year").val() == "") ? 2012 : $("#start_year").val(); 
		$("#end_year").attr("min", end_year_min);
	});
	
	$(".search_btn").on("click", function(){
		if ($("#search_id").val() == "" && $("#search_name").val() == "" && $("#start").val() == "") {
			alertify.error("請至少填寫一個搜尋條件");
		}
		else {
			$('.bg').fadeIn(200);
			$(this).attr("disabled", "true");
			$("#loading").removeAttr("hidden");
			$(".resultList").empty();
//			$.get("/IISI_Project/searchResult", {
//				search_id : $("#search_id").val(),
//				search_name : $("#search_name").val(),
//				start : $("#start").val(),
//				end : $("#end").val()
//			}, function(result) {
//				$("#manager_block1").html(result);
//				$("#loading").hide();
//			}, "html");
			$.ajax({
				url : '/IISI_Project/searchResult',
				type : 'post',
				contentType: "application/x-www-form-urlencoded; charset=utf-8", 
				data : $("#search").serialize()
			})
			.done(function(result) {
				setTimeout(function() {
					$("#loading").hide();
					$(".search_btn").removeAttr("disabled");
					$(".resultList").html(result);
					$("html, body").animate({
					 scrollTop: $('.resultList').offset().top
		            }, "show");
				}, 1000);
			})
			.fail(function(){
				alertify.error("查詢失敗！請重新再試一次！");
			});
		}
	});
	$("#searchBtn").on("click", function(){
		$(this).attr("disabled", "true");
		$("#loading").show();
		$("#loading").removeAttr("hidden");
		$(".resultList").empty();
//		$.get("/IISI_Project/searchResult", {
//			search_id : $("#search_id").val(),
//			search_name : $("#search_name").val(),
//			start : $("#start").val(),
//			end : $("#end").val()
//		}, function(result) {
//			$("#manager_block1").html(result);
//			$("#loading").hide();
//		}, "html");
		$.ajax({
			url : '/IISI_Project/query',
			type : 'post',
			contentType: "application/x-www-form-urlencoded; charset=utf-8", 
			data : $("#search").serialize()
		})
		.done(function(result) {
			setTimeout(function() {
				$("#loading").hide();
				$("#searchBtn").removeAttr("disabled");
				$(".resultList").html(result);
				$("html, body").animate({
				 scrollTop: $('.resultList').offset().top
	            }, "show");
			}, 1000);
		})
		.fail(function(){
			alertify.error("查詢失敗！請重新再試一次！");
		});
	});
	$(document).on("click", "#export", function() {
		alertify.confirm("確定匯出目前所有搜尋結果之工時紀錄？", function(e) {
			if (e) {
				$("#search").attr("action", "/IISI_Project/exportExcel");
				search.submit();
//				$.ajax({
//					url : "/IISI_Project/exportExcel",
//					type : "post",
//					contentType: "application/x-www-form-urlencoded; charset=utf-8",
//					data: $("#search").serialize()
//				})
//				.done(function(response) {
//					if (response == "false")
//						alertify.error("檔案已開啟！請關閉檔案在試");
//					else
//						alertify.alert(response);
//				})
//				.fail(function() {
//					alertify.error("匯出失敗！請稍後在試");
//				});
			}
		});
	});
	
	$(document).on("click", "#search_leave_results td", function() {
		var id = $(this).parent("#search_leave_results").children(".datas").children("#user_id").val();
		var leaveDate = $(this).parent("#search_leave_results").children(".datas").children("#leave_date").val();
		$("#userid").val(id);
		$("#leaveDate").val(leaveDate);
		$.ajax({
			url : '/IISI_Project/queryDetail',
			type : 'post',
			contentType: "application/x-www-form-urlencoded; charset=utf-8", 
			data : $("#search").serialize()
		})
		.done(function(datas) {
			$(".content").html(datas);
		})
		.fail(function(){
			alertify.error("請假紀錄讀取失敗！<br />請重新再試一次！");
		});
	});
});