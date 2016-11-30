// JavaScript Document
$(function(){
$(".dropdown").hover(function(){
		$(this).find(".dropdown-menu").stop().slideDown(500);
		},function(){
			$(this).find(".dropdown-menu").stop().slideUp(500);
	});
$("#menu").click(function(){
	$('#wrapper').toggleClass('click');
});
var centerX = 256/2, 
		centerY = 256/2;
	$('.circle').hover(function(){
		$(this).addClass('changecolor').fadeTo(800,0.60);
		//滑入時圖片縮小
		$(this).find('img').stop().animate({
			left: 0,
			top: 20,
			width: centerX*1.5,//300
			height:centerY*1.5,
			opacity: 1,
			
		}, 500);
	}, function(){
		//滑出時圖片放大(1倍)
		$(this).removeClass('changecolor').fadeTo(800,1);
		$(this).find('img').stop().animate({
			left: 0,
			top: 40,
			width: 165,
			height:165,
			opacity: 1
		}, 500);
	});
});