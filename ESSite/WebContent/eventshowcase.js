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
function loadmore(){
	"use strict";
	var hash = window.location.hash;
	if(hash === "" || hash === "#login"){
		hash="#" + defaultCity;
	}else if(hash === "#close" || hash.substring(0,4) === "#pop"){
		var val = ($("#citysel")[0]).value;
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

		$('html').animate({scrollTop: $(hash).offset().top},'slow');
	}else if(hash == ""){
		hash = "#" + defaultCity;
		$(hash).show();
	}
};

$(window).on('hashchange', function() {
	showevents();
});

function changecity(sel){
	"use strict";
	var value = sel.options[sel.selectedIndex].value; 
	/*console.log("City selection changed to" + value);*/
	window.location.href="#" + value;
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
	window.location.href="#popcl";
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
		$("#p-price").text(price);
		var participants = rdata.participants;
		$("#p-participants").text(participants);
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
	window.location.href="#modal";
}

function closemodal(){
	"use strict";
	/*console.log("Modal closed.");*/
	window.location.href="#";
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
	/*console.log("Event booking.");*/
	alert("Functionality to book an event is pending.");
}

function postcomment(){
	"use strict";
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

/* login() not used anymore */
function login(){
	"use strict";
	/*console.log("login try..");*/
	var val = validateForm();
	if(val === true){
		window.location.href="#";
	}
}

function validateForm()
{
	"use strict";
	/*console.log("validating form..");*/
	var x=document.forms["form-1ogin"]["email"].value;
	var y=document.forms["form-1ogin"]["password"].value;
	var atpos=x.indexOf("@");
	var dotpos=x.lastIndexOf(".");
	if (atpos<1 || dotpos<atpos+2 || dotpos+2>=x.length || y.length == 0)
	{
		alert("Invalid login! Please retry.");
		return false;
	}else{
		submit();
		return true;
	}
}

$(function() {
	$("#fromdate").datetimepicker({ minDate: 0});
	$("#todate").datetimepicker({ minDate: 0});	
	$("#startDate").datetimepicker({ minDate: 0});
});

function clearTextArea(elt){
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