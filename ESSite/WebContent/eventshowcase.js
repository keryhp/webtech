if (window.addEventListener){ 
	window.addEventListener("load",showevents,false);
}else {
	window.attachEvent("onload", showevents);
}

$(function() {
	"use strict";
	$(".rslides").responsiveSlides({
		auto: true,
		pagination: true,
		fade: 1000,
		speed: 300,
		/*pause: true*/
	});
});

var defaultCity="BRI";
var userid = 0;
function loadmore(){
	"use strict";
	var hash = window.location.hash;
	if(hash === "" || hash === "#login"){
		hash="#" + defaultCity;
	}else if(hash === "#close" || hash.substring(0,4) === "#pop"){
		var val = ($(".sortcity")[0]).value;
		hash="#" + val;
	}

	var b = document.getElementById('event-button');
	if(b.textContent === 'Load more!' || b.innerText === 'Load more!'){
		b.textContent = 'Load less!'; 
		b.innerText = 'Load less!';
		/*console.log("Loading more events..");*/
	}else{
		b.textContent = 'Load more!';
		b.innerText = 'Load more!';
		/*console.log("UnLoading additional events..");*/
	}

	var id = hash + "-more-events";
	id = id.replace('#','');
	var e = document.getElementById(id);
	if(e != null){
		if(e.style.display == 'inline-block'){
			e.style.display = 'none';
		}else{
			e.style.display = 'inline-block';
		}
	}
	return false;
}

function showevents(){
	"use strict";
	var hash = window.location.hash;
	if(hash != "" && hash != "#login" 
		&& hash != "#close" && hash.substring(0,4) != "#pop" && hash != "#modal"){
		$('.city-events').hide();
		$(hash).show();
		var tmp = "option[value=\"" + hash.substring(1, hash.length) + "\"]";
		$(".sortcity " + tmp).prop("selected", true);

		$('html').animate({scrollTop: $(hash).offset().top},'slow');

		//redo the menu for cities
		/*		out.print("<li><a id="mcc" href=\"index.jsp#" + city.getCityCode() + "\">" + city.getCityName() + "<span class=\"caret\"></span> </a> <div> <ul>");
		out.print("<li><a href=\"index.jsp#" + city.getCityCode() + "\">" + city.getCityName() + "</a></li>");
		out.print("</ul></div></li>");
		 */		

		$.ajax({
			url: "city",
			type: "get",
			cache: false,
			data: {}
		}).done(function(response){		
			//event data received successfully, show that in the city selections
			var rdata = JSON.parse(response);
			$("#mccul").empty();
			var dateNow = new Date(new Date().getTime());
			year = dateNow.getFullYear();
			month = dateNow.getMonth() + 1;
			var now = month + "/"  + dateNow.getDate() + "/" + year + " " + dateNow.getHours() + ":" + dateNow.getMinutes();
			year = year + 1;
			var yearlater = month + "/"  + dateNow.getDate() + "/" + year + " " + dateNow.getHours() + ":" + dateNow.getMinutes();
			var hash = window.location.hash;
			$.each(rdata, function(i, city) {
				if(hash.substring(1, hash.length) == city.citycode){
					$("#mcc").attr("href", "index.jsp#" + city.citycode);
					$("#mcc")[0].innerHTML = city.cityname + "<span class=\"caret\"></span>";
				}else{
					var contents = "<li><a href=\"index.jsp#" + city.citycode + "\">" + city.cityname + "</a></li>";
					$("#mccul").append(contents);					
				}
				var citySel = "#" + city.citycode;
				$(citySel).find(".fromdateI")[0].value = now;
				$(citySel).find(".todateI")[0].value = yearlater;	
				$(citySel).find(".fromdateI").datetimepicker({ minDate: 0});
				$(citySel).find(".todateI").datetimepicker({ minDate: 0});	

			});		
		}).fail(function(a, b, c){
			return false;
		});			
		eventsbydate();
		var b = document.getElementById('event-button');
		b.textContent = 'Load more!';
	}else if(hash == ""){
		var csel = $(".sortcity").val();
		hash = "#" + csel;
		$(hash).show();
		eventsbydate();
	}
	if($("#cmessage").text().length != 0){
		window.alert($("#cmessage").text());
		$("#cmessage")[0].textContent="";
	}
};

$(window).on('hashchange', function() {
	showevents();
});

function changecity(sel){
	"use strict";
	var value = sel.options[sel.selectedIndex].value; 
	/*console.log("City selection changed to" + value);*/
	window.location.href="index.jsp#" + value;
	return false;
}


/* file upload */
function fileSelected() {
	"use strict";
	var file = document.getElementById('fileToUpload').files[0];
}

function uploadFile() {
	"use strict";
	$("#popfm").hide();
	alert('Event created successfully! Pending POST implementation.');
	window.location.href="index.jsp#popcl";
}

/* construction */
function construction(){
	"use strict";
	/*console.log("Page under construction.");*/
	alert('This page is under construction');
}
var curevt = null;
function learnmore(eventid){
	"use strict";
	curevt = eventid;
	//do get request to server and get event data
	$.ajax({
		url: "event",
		type: "get",
		cache: false,
		data: {id:eventid}
	}).done(function(response){		
		//response is event data, use it to build the modal window
		//sample {id=11, startDate=Thu Jan 01 1970 00:00 GMT, title=aaaaaaaaaaaaaaaa, price=5555, location=aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa, description=aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa, createdate=Thu Jan 01 1970 00:00 GMT, userid=1, remarks=fffffffffffffffffffffffffffffffffffffffffffff, createdby=1, imageid=17, tagline=aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa, city=BRI}
		var rdata = JSON.parse(response);
		var mainline = rdata.title + " @starts from " + rdata.location + " on " + rdata.startDate;
		$("#p-mainline").text(mainline);
		var tagline = rdata.tagline;
		$("#p-tagline").text(tagline);
		var desc = rdata.description;
		$("#p-evt-desc").text(desc);
		var price = rdata.price;
		$("#p-price").text(price + " GBP");
		/*var participants = rdata.participants;
		$("#p-participants").text(participants);*/
		$("#reviewsDiv").empty();
		var cmts = rdata.comments;
		$.each(cmts, function(i, cmt) {
			/*<article>
				<em>"This is a good event. My friends like to attend it as well!"</em> - user 1
			</article>*/
			var rvwContents = "<article><em>\"" + cmt.review + "\"</em> - " + cmt.userid + "</article>"; 
			$("#reviewsDiv").append(rvwContents);
		});
	}).fail(function(a, b, c){
		return false;
	});		

	/*console.log("Learn more.");*/
	window.location.href="index.jsp#modal";
}

function closemodal(){
	"use strict";
	/*console.log("Modal closed.");*/
	window.location.href="index.jsp#";
}

function loadimage(){
	"use strict";
	/*console.log("load image.");*/
	/*alert("Functionality to slide the carousel image is pending.");*/
	var curimg = $(".rslides1_on")[0].id;
	$(curimg).className = "";
	var tmp = curimg.slice(-1);
	tmp++;
	tmp = (tmp % 4);
	var newimg = "#rslides1_s" + tmp;
	$(newimg)[0].className = "rslides1_on";
	$(newimg)[0].style.zIndex = "2";
}

function bookevent(){
	"use strict";
	if($("#loggedin").text() == "0"){
		window.alert("Please login to book event");
	}else{
		/*console.log("Event booking.");*/
		$.ajax({
			url: "booking",
			type: "post",
			cache: false,
			data: {eventid:curevt, remarks:null}
		}).done(function(response){		
			//comment posted successfully, show that in the event reviews
			$("#bkgBody").empty();
			var rdata = JSON.parse(response);
			var elistData = rdata.events;
			$.each(elistData, function(i, evt) {
				var contents = "<tr><td>" + evt.title + "</td><td>" + evt.startDate + "</td><td>" + evt.createdate + "</td></tr>";
				$("#bkgBody").append(contents);
			});		
			window.location.href = "index.jsp#popmb";
		}).fail(function(a, b, c){
			return false;
		});
	}
}

function postcomment(){
	"use strict";
	if($("#loggedin").text() == "0"){
		window.alert("Please login to post comment");
	}else{
		/*console.log("Post event comment.");*/
		var rvw = $("#rtextarea")[0].value;
		$.ajax({
			url: "comment",
			type: "post",
			cache: false,
			data: {eventid:curevt, review:rvw, remarks:null}
		}).done(function(response){		
			//comment posted successfully, show that in the event reviews
			learnmore(curevt);
		}).fail(function(a, b, c){
			return false;
		});		
	}
}

/* login() not used anymore */
function login(){
	"use strict";
	/*console.log("login try..");*/
	var val = validateLogin();
	if(val === true){
		window.location.href="index.jsp#";
	}
}

function validateRegistrationForm()
{
	"use strict";
	/*console.log("validating form..");*/
	var frm = document.forms["form-register"];
	if(frm != undefined){
		var x=frm["remail"].value;
		var y=frm["rpass"].value;
		var atpos=x.indexOf("@");
		var dotpos=x.lastIndexOf(".");
		if (atpos<1 || dotpos<atpos+2 || dotpos+2>=x.length || y.length == 0)
		{
			alert("Invalid registration! Please retry with proper email id and password (like Testing2$).");
			return false;
		}else{
			frm.submit();
			return true;
		}
	}
}

function validateLogin()
{
	"use strict";
	/*console.log("validating form..");*/
	var frm =document.forms["form-1ogin"]; 
	if(frm != undefined){
		var x=frm["lemail"].value;
		var y=frm["lpassword"].value;
		var atpos=x.indexOf("@");
		var dotpos=x.lastIndexOf(".");
		if (atpos<1 || dotpos<atpos+2 || dotpos+2>=x.length || y.length == 0)
		{
			alert("Invalid login! Please retry.");
			return false;
		}else{
			frm.submit();
			return true;
		}
	}
}

$(function() {
	var dateNow = new Date(new Date().getTime());
	year = dateNow.getFullYear();
	month = dateNow.getMonth() + 1;
	var now = month + "/"  + dateNow.getDate() + "/" + year + " " + dateNow.getHours() + ":" + dateNow.getMinutes();
	year = year + 1;
	var yearlater = month + "/"  + dateNow.getDate() + "/" + year + " " + dateNow.getHours() + ":" + dateNow.getMinutes();
	var hash = window.location.hash;
	var citySel = "#BRI";
	if(hash != "" && hash != "#login" 
		&& hash != "#close" && hash.substring(0,4) != "#pop" && hash != "#modal"){
		citySel = hash;
	}
	$(citySel).find(".fromdateI")[0].value = now;
	$(citySel).find(".todateI")[0].value = yearlater;	
	$("#startDate")[0].value = now;
	$(citySel).find(".fromdateI").datetimepicker({ minDate: 0});
	$(citySel).find(".todateI").datetimepicker({ minDate: 0});	
	$("#startDate").datetimepicker({ minDate: 0});
});

function clearTextArea(elt){
	"use strict";
	document.getElementById(elt.id).value='';              
}

function editEvent(eventid){
	"use strict";
	//do get request to server and get event data
	$.ajax({
		url: "editevent",
		type: "get",
		cache: false,
		data: {id:eventid}
	}).done(function(response){		
		//event data received successfully, show that in the event edit
		var rdata = JSON.parse(response);
		$("#title")[0].value = rdata.title;
		$("#eventid")[0].value = eventid;
		$("#tagline")[0].value = rdata.tagline;
		$("#description")[0].value = rdata.description;
		$("#location")[0].value = rdata.location;
		var tmp = "option[value=\"" + rdata.city + "\"]";
		$("#ecity " + tmp).prop("selected", true);
		$("#editimg").attr("src", "photo?id=" + rdata.imageid);
		$("#startDate")[0].value = rdata.startDate;
		$("#price")[0].value = rdata.price;
		$("#remarks")[0].value = rdata.remarks;
		$("#form-editevent").toggleClass("hide show");
	}).fail(function(a, b, c){
		return false;
	});			
}

function eventsbydate(){
	"use strict";
	var sd = $(".fromdateI")[0].value;
	var ed = $(".todateI")[0].value;
	var hash = window.location.hash;
	var citySel = "#BRI";
	if(hash != "" && hash != "#login" 
		&& hash != "#close" && hash.substring(0,4) != "#pop" && hash != "#modal"){
		citySel = hash;
	}

	//do get request to server and get event data by date
	$.ajax({
		url: "eventbydate",
		type: "get",
		cache: false,
		data: {startDate:sd, endDate:ed, city:citySel.substring(1, citySel.length)}
	}).done(function(response){		
		//event data received successfully, show that in the event edit
		var rdata = JSON.parse(response);
		$(citySel).find(".latest-events").empty();
		$(citySel).find(citySel + "latest-events").empty();
		if(response.length > 2){
			var appendId = ".latest-events";
			$.each(rdata, function(i, event) {
				var contents = "<div><section><a onclick=\"learnmore(" + event.id + ");\"> <img class=\"latest-events-img\" src=\"photo?id=" + event.imageid + "\" alt=\"" + event.id + "\" /></a>";
				contents = contents + "<button type=\"button\" class=\"buttons learn-more\" onclick=\"learnmore(" + event.id + ");\">Learn more!</button>";
				contents = contents + "<header><h3>" + event.title + "</h3></header>";
				var tagline = event.tagline;
				if(tagline.length > 45){
					contents = contents + "<p>"+ tagline.substring(0,45) + "..</p>";
				}else{
					contents = contents + "<p>"+ tagline + "</p>";
				}
				contents = contents + "</section></div>";
				$(citySel).find(appendId).append(contents);
				if(i==6){
					appendId = citySel.substring(1, citySel.length) + "-more-events";
				}
			});	
		}
	}).fail(function(a, b, c){
		return false;
	});			
}

function logout(){
	"use strict";
	$.ajax({
		url: "logout",
		type: "post",
		cache: false,
		data: {}
	}).done(function(response){		
		window.location.reload(true);
		return false;
	}).fail(function(a, b, c){
		return false;
	});		
}