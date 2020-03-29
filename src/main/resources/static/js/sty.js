$(document).ready(function scrolled(){
  $(window).scroll(function(){
  	var scroll = $(window).scrollTop();
	  if (scroll > 300) {
	    $(".navbar.navbar-fixed-top").css("background" , "blue");
	  }

	  else{
		  $(".navbar.navbar-fixed-top").css("background" , "#333");  	
	  }
  })
})