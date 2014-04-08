<!DOCTYPE html>
<%@page import="uk.bris.esserver.service.UserService"%>
<%@page import="uk.bris.esserver.repository.entities.Users"%>
<%@ page language="java"
	contentType="application/xhtml+xml; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="uk.bris.esserver.service.CityService"%>
<%@ page import="uk.bris.esserver.repository.entities.City"%>
<%@ page import="uk.bris.esserver.service.EventService"%>
<%@ page import="uk.bris.esserver.repository.entities.Event"%>
<%@ page import="uk.bris.esserver.service.BookingService"%>
<%@ page import="uk.bris.esserver.repository.entities.Booking"%>
<%@ page import="uk.bris.esserver.util.ESSDateUtil"%>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en-GB" xml:lang="en-GB">
<head>
<meta charset="utf-8" />
<meta name="description"
	content="This is the 'Event Showcase' Edit events page" />
<meta name="keywords" content="event activities fun" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>Edit Events!</title>
<link href="./favicon.ico" rel="shortcut icon" type="image/ico" />
<link type="text/css" rel="stylesheet" href="./style.css" />
<link type="text/css" rel="stylesheet" href="./responsiveslides.css" />
<link type="text/css" rel="stylesheet" href="./popup.css" />
<link type="text/css" rel="stylesheet" href="./jquery-ui.css" />
<link type="text/css" rel="stylesheet"
	href="./jquery-ui-timepicker-addon.css" />
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
						Users user = null;
						String cmessage = null;
						if (session.getAttribute("userObj") != null) {
							user = (Users) session.getAttribute("userObj");
						}
						if (session.getAttribute("cmessage") != null) {
							cmessage = (String) session.getAttribute("cmessage");
						}
						String userID = null;
						String sessionID = null;
						Cookie[] cookies = request.getCookies();
						if (cookies != null) {
							for (Cookie cookie : cookies) {
								if (cookie.getName().equals("userid")) {
									userID = cookie.getValue();
								}
								if (cookie.getName().equals("JSESSIONID")) {
									sessionID = cookie.getValue();
								}
							}
						}
						CityService citySrv = new CityService();
						List lcity = citySrv.getListOfCity();
						int once = 1;
						if (lcity != null) {
							Iterator it = lcity.iterator();
							while (it.hasNext()) {
								City city = (City) it.next();
								if (once == 1) {
									once = 0;
									out.print("<li><a id=\"mcc\" href=\"index.jsp#"
											+ city.getCityCode()
											+ "\">"
											+ city.getCityName()
											+ "<span class=\"caret\"></span> </a> <div> <ul id=\"mccul\">");
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
						if (user != null) {
							if (user.getRole().equalsIgnoreCase("A")) {
								out.print("<li><a href=\"index.jsp#popae\" id=\"addevent\">Add New Event<span class=\"caret\"></span></a>");
								out.print("<div><ul><li><a href=\"editevent.jsp\">Edit Events</a></li></ul></div></li>");
							}
							out.print("<li><a href=\"index.jsp#popmb\" id=\"mybookings\">My Bookings</a></li>");
						}
					%>
					<li><a href="report.jsp">Report</a></li>
					<%
						if (user != null) {
							out.print("<li id=\"usraccount\"><a href=\"index.jsp#popac\" id=\"account\">"
									+ user.getEmail().substring(0,
											user.getEmail().indexOf('@'))
									+ "<span class=\"caret\"></span></a>");
							out.print("<div><ul><li><a href=\"index.jsp#popln\" onclick=\"logout();\">Logout</a></li></ul></div></li>");
						} else {
							out.print("<li id=\"signin\"><a href=\"index.jsp#popln\" id=\"loginb\">Login<span class=\"caret\"></span></a>");
							out.print("<div><ul><li><a href=\"index.jsp#popreg\" id=\"register\">Register</a></li></ul></div></li>");
						}
					%>
				</ul>
			</nav>
		</div>

		<!-- Events List Wrapper -->
		<div id="list-wrapper">
			<!-- get event to edit -->
			<%
				if (user != null) {
					EventService evtSrv = new EventService();
					List<Event> events = evtSrv.getEventByUserId(new Integer(user
							.getId()).toString());
					out.print("<table><caption>Please find below the list of events posted by you.</caption><colgroup span=\"3\" title=\"Your Events!\"></colgroup>");
					out.print("<thead><tr><th scope=\"col\">Event ID</th><th scope=\"col\">Title</th><th scope=\"col\">Date</th><th scope=\"col\">Edit</th></tr></thead><tbody>");
					if (events != null) {
						Iterator it3 = events.iterator();
						while (it3.hasNext()) {
							Event event = (Event) it3.next();
							out.print("<tr><td>"
									+ event.getId()
									+ "</td><td>"
									+ event.getTitle()
									+ "</td><td>"
									+ ESSDateUtil.formatTimeStamp(event
											.getStartDate())
									+ "</td><td><button type=\"button\" style=\"margin: 2%;\" class=\"buttons learn-more\" onclick=\"editEvent("
									+ event.getId()
									+ ");\">Edit</button></td></tr>");
						}
					}
					out.print("</tbody></table>");
				} else {
					out.print("<p>There are no events posted by you.<p>");
				}
			%>

			<!-- popup form add new event - method get for initial client version-->
			<form id="form-editevent" enctype="multipart/form-data" method="post"
				action="editevent" autocomplete="on" class="hide">
				<div class="popup" style="display: inline-block;">
					<h2 id="popaeh2">Edit Event</h2>
					<p>Please edit the event details here:</p>
					<div>
						<label for="eventid">EventID</label> <input type="text"
							id="eventid" name="eventid" value="" hidden="hidden" />
					</div>
					<div>
						<label for="title">Title</label> <input class="eventtext"
							type="text" id="title" name="title" value="" required="required" />
					</div>
					<div>
						<label for="tagline">Tagline</label> <input class="eventtext"
							type="text" name="tagline" id="tagline" value=""
							required="required" />
					</div>
					<div>
						<label for="description">Description</label> <input
							class="eventtextarea" type="textarea" name="description"
							id="description" value="" required="required" />
					</div>
					<div>
						<label for="location">Location</label> <input class="eventtext"
							type="text" id="location" name="location" value=""
							required="required" />
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
											out.print("<option value=\"" + city.getCityCode()
													+ "\" selected=\"selected\">"
													+ city.getCityName() + "</option>");
										} else {
											out.print("<option value=\"" + city.getCityCode()
													+ "\">" + city.getCityName() + "</option>");
										}
									}
								}
							%>
						</select>
					</div>
					<div>
						<img id="editimg" class="latest-events-img" src="" alt="" />
					</div>
					<div>
						<label for="fileToUpload">Select a File to Upload</label> <input
							type="file" name="fileToUpload" id="fileToUpload"
							onchange="fileSelected();" />
					</div>
					<div>
						<label for="startDate">Date</label> <input id="startDate"
							name="startDate" type="text" readonly="readonly"
							required="required" />
					</div>
					<div>
						<label for="price">Price in GBP</label> <input type="number"
							id="price" name="price" value="" required="required" />
					</div>
					<div>
						<label for="remarks">Remarks</label> <input class="eventtextarea"
							type="textarea" name="remarks" id="remarks" value=""
							required="required" />
					</div>
					<input type="submit" value="Create" class="buttons" /> <a
						class="close" href="#popcl"></a>
				</div>
			</form>
		</div>
		<%
			if (user != null) {
				out.print("<p hidden=\"hidden\" id=\"loggedin\">"
						+ user.getId() + "</p>");
			} else {
				out.print("<p hidden=\"hidden\" id=\"loggedin\">" + 0 + "</p>");
			}
			if (cmessage != null) {
				out.print("<p hidden=\"hidden\" id=\"cmessage\">" + cmessage
						+ "</p>");
			}
		%>

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
					created as part of COMSM0104 assignments #1 and #2.</p>
			</footer>
		</div>
	</section>
</body>
</html>