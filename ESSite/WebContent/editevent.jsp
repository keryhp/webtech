<!DOCTYPE html>
<%@ page language="java"
	contentType="application/xhtml+xml; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="uk.bris.esserver.service.EventService"%>
<%@ page import="uk.bris.esserver.repository.entities.Event"%>
<%@ page import="uk.bris.esserver.service.CityService"%>
<%@ page import="uk.bris.esserver.repository.entities.City"%>
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
		<!-- Events List Wrapper -->
		<div id="list-wrapper">
		<!-- get event to edit -->
		<%
		EventService evtSrv = new EventService();
		List<Event> events = evtSrv.getEventByUserId("1"); //change this to actual userid
		out.print("<table><caption>Please find below the list of events posted by you.</caption><colgroup span=\"3\" title=\"Your Events!\"></colgroup>");
		out.print("<thead><tr><th scope=\"col\">Event ID</th><th scope=\"col\">Title</th><th scope=\"col\">Date</th><th scope=\"col\">Edit</th></tr></thead><tbody>");
		if (events != null) {
			Iterator it3 = events.iterator();
			while (it3.hasNext()) {	
				Event event = (Event) it3.next();
				out.print("<tr><td>" + event.getId() + "</td><td>" + event.getTitle() +"</td><td>" + ESSDateUtil.formatTimeStamp(event.getStartDate()) + "</td><td><button type=\"button\" class=\"buttons learn-more\" onclick=\"editEvent(" + event.getId() + ");\">Edit</button></td></tr>");				
			}
		}
		out.print("</tbody></table>");
		%>
		
				<!-- popup form add new event - method get for initial client version-->
		<form id="form-editevent" enctype="multipart/form-data" method="post"
			action="editevent" autocomplete="on" class="hide">
			<div class="popup" style="display:inline-block;">
				<h2 id="popaeh2">Edit Event</h2>
				<p>Please edit the event details here:</p>
				<div>
					<label for="eventid">EventID</label> <input type="text" id="eventid" name="eventid"
						value="" hidden="hidden"/>
				</div>
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
					<img id="editimg" class="latest-events-img" src="" alt=""/>
				</div>
				<div>
					<label for="fileToUpload">Select a File to Upload</label> <input
						type="file" name="fileToUpload" id="fileToUpload"
						onchange="fileSelected();"/>
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