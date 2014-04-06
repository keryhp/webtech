<!DOCTYPE html>
<%@ page language="java"
	contentType="application/xhtml+xml; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="uk.bris.esserver.service.CityService"%>
<%@ page import="uk.bris.esserver.repository.entities.City"%>
<%@ page import="uk.bris.esserver.service.EventService"%>
<%@ page import="uk.bris.esserver.repository.entities.Event"%>
<%@ page import="uk.bris.esserver.util.ESSDateUtil"%>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en-GB" xml:lang="en-GB">
<head>
<meta charset="utf-8" />
<meta name="description"
	content="This is the 'Event Showcase' landing page. In this site, the events in various cities are listed" />
<meta name="keywords" content="event activities fun" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>Events!</title>
<link href="./favicon.ico" rel="shortcut icon" type="image/ico" />
<link type="text/css" rel="stylesheet" href="./style.css" />
<link type="text/css" rel="stylesheet" href="./responsiveslides.css" />
<link type="text/css" rel="stylesheet" href="./popup.css" />
<link type="text/css" rel="stylesheet" href="./jquery-ui.css" />
<link type="text/css" rel="stylesheet" href="./jquery-ui-timepicker-addon.css" />
<script src="./jquery-1.11.0.min.js" type="text/javascript"></script>
<script src="./jquery-ui.min.js" type="text/javascript"></script>
<script src="./jquery-ui-timepicker-addon.js" type="text/javascript"></script>
<script src="./responsiveslides.min.js" type="text/javascript"></script>
<script src="./eventshowcase.js" type="text/javascript"></script>
<!--[if lt IE 9]>
<script src="./html5shiv.js"></script>
<![endif]-->
</head>
<body>
	<section>
		<!-- Header Wrapper -->
		<div id="header-wrapper">
			<!-- Header -->
			<header id="header">
				<!-- Logo -->
				<a href="index.jsp"> <img src="./logo2.png" class="logo"
					alt="logo" />
				</a>
			</header>
			<!-- Nav -->
			<nav>
				<ul>
					<li class="menu-active"><a href="index.jsp">Home</a></li>
					<%
						CityService citySrv = new CityService();
						List lcity = citySrv.getListOfCity();
						int once = 1;
						if (lcity != null) {
							Iterator it = lcity.iterator();
							while (it.hasNext()) {
								City city = (City) it.next();
								if (once == 1) {
									once = 0;
									out.print("<li><a href=\"index.jsp#"
											+ city.getCityCode()
											+ "\">"
											+ city.getCityName()
											+ "<span class=\"caret\"></span> </a> <div> <ul>");
								} else {
									out.print("<li><a href=\"index.jsp#"
											+ city.getCityCode() + "\">"
											+ city.getCityName() + "</a></li>");
								}
								if (!it.hasNext()) {
									out.print("</ul></div></li>");
								}
							}
						}
					%>
					<li><a href="#popae" id="addevent">Add New Event</a></li>
					<li><a href="#popmb" id="mybookings">My Bookings</a></li>
					<li><a href="report.html">Report</a></li>
					<li><a href="#popac" id="account">demo_user <span
							class="caret"></span>
					</a>
						<div>
							<ul>
								<li><a href="#popln" id="loginb">Logout</a></li>
							</ul>
						</div></li>
				</ul>
			</nav>
		</div>

		<div id="content-wrapper">
			<section id="mc-holder">
				<ul class="rslides">
					<%
						EventService evtSrv = new EventService();
						List levents = evtSrv.getListOfEvent();
						if (levents != null) {
							Iterator it = levents.iterator();
							while (it.hasNext()) {
								Event event = (Event) it.next();
								out.print("<li><a onclick=\"learnmore(" + event.getId() + ");\"> <img src=\"photo?id=" + event.getImageid() + "\" alt=\"" + event.getId() + "\"/> <p class=\"caption\">" + event.getTitle() + "</p></a></li>");
							}
						}
					%>
				</ul>
				<a class="arrows prev mc-prev" onclick="loadimage();">Previous</a> <a
					class="arrows next mc-next" onclick="loadimage();">Next</a>
			</section>
			<section id="h-video">
				<header>
					<section id="welcome">
						<h1>Welcome to Events!</h1>
						<p>
							Explore the <strong>happiness</strong> around you.. Browse &amp;
							book events of your interest, experience them. Want to know how
							easy it is? check this video.
						</p>
					</section>
				</header>
				<div id="div-video">
					<!--<img class="vid-poster" src="./poster.png" alt="Video poster" />-->
					<video width="540" height="300" controls="" poster="./poster.png"
						preload="metadata">
						<source src="movie.mp4" type="video/mp4" />
						<source src="movie.ogg" type="video/ogg" />
					</video>
				</div>
			</section>
		</div>

		<!-- Events List Wrapper -->
		<div id="list-wrapper">
			<!-- events at each city -->
			<%
			if (lcity != null) {
				Iterator it = lcity.iterator();
				int cnt =0;
				while (it.hasNext()) {
					City city = (City) it.next();
					//section
					out.print("<section id=\"" + city.getCityCode() + "\" class=\"city-events hide\">");
					//header
					out.print("<header><h2>Latest Events at <strong>" + city.getCityName() + "</strong>!</h2>");
					out.print("<select class=\"sortcity\" onchange=\"changecity(this);\">");				
					once = 1;
					if (lcity != null) {
						Iterator it2 = lcity.iterator();
						while (it2.hasNext()) {
							City city2 = (City) it2.next();
							if (once == 1) {
								once = 0;
								out.print("<option value=\"" + city2.getCityCode() + "\" selected=\"selected\">" + city2.getCityName() + "</option>");
							} else {
								out.print("<option value=\"" + city2.getCityCode() + "\">" + city2.getCityName() + "</option>");
							}
						}
					}
					out.print("</select><p class=\"fromdate\">Show from: <input id=\"fromdate\" name=\"fromdate\" type=\"text\" readonly=\"readonly\"></input></p>");
					out.print("<p class=\"todate\"> to:<input id=\"todate\" name=\"todate\" type=\"text\" readonly=\"readonly\"></input></p>");
					out.print("<button type=\"button\" class=\"sortdate-btn\" onclick=\"loadmore();\">submit</button></header>");
					//end of header
					//for each event in city
					List cityEvents = evtSrv.getEventByCity(city.getCityCode()); 													
					//latest events
					out.print("<div class=\"latest-events\">");					
					if (cityEvents != null) {
						Iterator it3 = cityEvents.iterator();
						while (it3.hasNext()) {
							cnt++;
							Event event = (Event) it3.next();
							out.print("<div><section><a onclick=\"learnmore(" + event.getId() + ");\"> <img class=\"latest-events-img\" src=\"photo?id=" + event.getImageid() + "\" alt=\"" + event.getId() + "\" /></a>");
							out.print("<button type=\"button\" class=\"buttons learn-more\" onclick=\"learnmore(" + event.getId() + ");\">Learn more!</button>");
							out.print("<header><h3>" + event.getTitle() + "</h3></header>");
							if(event.getTagline().length() > 45){
								out.print("<p>"+ event.getTagline().substring(0,45) + "..</p>");
							}else{
								out.print("<p>"+ event.getTagline() + "</p>");
							}
							out.print("</section></div>");
							if(cnt == 6 ){
								out.print("</div>"); //end of latest events
								out.print("<div id=\"" + city.getCityCode() + "-more-events\" class=\"latest-events hide\">");
							}
						}
					}
					out.print("</div></section>"); //end of section
				}
				if(cnt > 6){
					out.print("<button type=\"button\" id=\"event-button\" class=\"buttons\" onclick=\"loadmore();\">Load more!</button>");
				}
			}
			%>
		</div>
		<div class="svg-holder" id="svg2">
			<span id="hovercar">Hover my car!</span> <img src="./car2.png"
				border="0" class="mycar" />
		</div>

		<aside id="modal">
			<header>
				<h2>Event Details!</h2>
			</header>
			<section>
				<article>
						<strong id="p-mainline"></strong>
				</article>
				<article>
					<p id="p-tagline"></p>
					<p id="p-evt-desc"></p>
				</article>
				<article>
					<strong>Price:</strong>
					<label id="p-price"></label>
				</article>
				<article>
					<strong>#participants:</strong>
					<label id="p-participants"></label>
				</article>
			</section>
			<section id="reviews">
				<strong>Reviews:</strong>
				<div id="reviewsDiv">
				</div>				
				<article class="comment">
					<input id="rtextarea" type="textarea" value="You can post your review.." onclick="clearTextArea(this);"/>
				</article>
			</section>
			<button type="button" id="event-button" class="buttons"
				onclick="postcomment();">Comment</button>
			<button type="button" id="event-button" class="buttons"
				onclick="bookevent();">Book</button>
			<button type="button" id="event-button" class="buttons"
				onclick="closemodal();">Close</button>
		</aside>
		<!-- user actions, initially planed to design as pop ups, but due to transition issues doing it as part of the page -->
		<!-- popup form login -->
		<form id="form-1ogin" autocomplete="on" method="post"
			action="./index.jsp?test=true">
			<a href="#" class="overlay" id="popln"></a>
			<div class="popup">
				<h2>Welcome Guest!</h2>
				<p>Please enter your login and password here</p>
				<div>
					<label for="email">email</label> <input type="text" id="email"
						value="" autocomplete="off" required="required" />
				</div>
				<div>
					<label for="password">Password</label> <input type="password"
						id="password" value="" required="required" />
				</div>
				<input type="submit" class="buttons" value="Log In"
					onclick="validateForm();" /> <a class="close" href="#popcl"></a>
			</div>
		</form>
		<!-- popup form add new event - method get for initial client version-->
		<form id="form-addevent" enctype="multipart/form-data" method="post"
			action="event" autocomplete="on">
			<a href="#x" class="overlay" id="popae"></a>
			<div class="popup">
				<h2 id="popaeh2">Add New Event</h2>
				<p>Please enter the event details here:</p>
				<div>
					<label for="title">Title</label> <input class="eventtext" type="text" id="title" name="title"
						value="" required="required" />
				</div>
				<div>
					<label for="tagline">Tagline</label> <input class="eventtext" type="text" name="tagline"
						id="tagline" value="" required="required" />
				</div>								
				<div>
					<label for="description">Description</label> <input class="eventtextarea" type="textarea"
						name="description" id="description" value="" required="required" />
				</div>				
				<div>
					<label for="location">Location</label> <input class="eventtext" type="text" id="location" name="location"
						value="" required="required" />
				</div>
				<div>
					<label for="ecity">City</label> 
					<!-- <input type="text" name="ecity" id="ecity" value="" required="required" /> -->
				<select id="ecity" name="ecity">
						<% 
						once = 1;
						if (lcity != null) {
							Iterator it = lcity.iterator();
							while (it.hasNext()) {
								City city = (City) it.next();
								if (once == 1) {
									once = 0;
									out.print("<option value=\"" + city.getCityCode() + "\" selected=\"selected\">" + city.getCityName() + "</option>");
								} else {
									out.print("<option value=\"" + city.getCityCode() + "\">" + city.getCityName() + "</option>");
								}
							}
						}
						%>						
					</select>
									</div>
				<div>
					<label for="fileToUpload">Select a File to Upload</label> <input
						type="file" name="fileToUpload" id="fileToUpload"
						onchange="fileSelected();" required="required" />
				</div>
				<div>
					<label for="startDate">Date</label>
					<input id="startDate" name="startDate" type="text" readonly="readonly" required="required" />
				</div>
				<div>
					<label for="price">Price in GBP</label> <input type="number" id="price" name="price"
						value="" required="required" />
				</div>
				<div>
					<label for="remarks">Remarks</label> <input class="eventtextarea" type="textarea" name="remarks"
						id="remarks" value="" required="required" />
				</div>								
				<input type="submit" value="Create" class="buttons" /> <a
					class="close" href="#popcl"></a>
			</div>
		</form>

		<!-- view my bookings data -->
		<a href="#" class="overlay" id="popmb"></a>
		<div class="popup" id="mytable">
			<table>
				<caption>Please find below your booking details.</caption>
				<colgroup span="3" title="Your bookings!"></colgroup>
				<thead>
					<tr>
						<th scope="col">Event Name</th>
						<th scope="col">Event Date</th>
						<th scope="col">Booking Date</th>
					</tr>
				</thead>
				<tfoot>
					<tr>
						<td>River rafting</td>
						<td>09/Mar/2014</td>
						<td>04/Mar/2014</td>
					</tr>
				</tfoot>
				<tbody>
					<tr>
						<td>Musical night</td>
						<td>12/Feb/2014</td>
						<td>06/Feb/2014</td>
					</tr>
					<tr>
						<td>Cycling</td>
						<td>16/Feb/2014</td>
						<td>04/Feb/2014</td>
					</tr>
				</tbody>
			</table>
			<a class="close" href="#popcl"></a>
		</div>

		<!-- user account details -->
		<a href="#" class="overlay" id="popac"></a>
		<div class="popup" id="usracc">
			<h2>Your account details!</h2>
			<a href="#"> <img class="useracc-img" src="./image1.png" alt="" />
			</a>
			<div>
				<label>Name:<strong>Demo User</strong>
				</label>
			</div>
			<div>
				<label>Date Joined:<strong>02/Jan/2014</strong>
				</label>
			</div>
			<div>
				<label>email:<strong>demouser@bristol.ac.uk</strong>
				</label>
			</div>
			<div>
				<label>contact:<strong>+44 12345 54321</strong>
				</label>
			</div>
			<div>
				<label>Post code:<strong>BS5 9JA</strong>
				</label>
			</div>

			<a class="close" href="#popcl"></a>
		</div>

		<!-- Footer Wrapper -->
		<div id="footer-wrapper">
			<!-- footer -->
			<footer>

				<!-- Nav -->

				<nav>
					<ul>
						<li><a onclick="construction();" id="about">About</a></li>
						<li><a onclick="construction();">Feedback</a></li>
						<li><a onclick="construction();">FAQs</a></li>
						<li><a onclick="construction();">Terms &amp; Conditions</a></li>
						<li><a onclick="construction();">sitemap</a></li>
					</ul>
				</nav>
				<p class="copyright">hh13577, University of Bristol | Website
					created as part of COMSM0104 assignment#1.</p>
			</footer>
		</div>
	</section>
</body>
</html>