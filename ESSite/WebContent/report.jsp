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
	content="This is the 'Event Showcase' report page. In this site, the events in various cities are listed" />
<meta name="keywords" content="event activities fun" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>Assignment report!</title>
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

		<div id="content-wrapper">
			<p></p>
			<p></p>
			<div id="myserverreport">
				<h1>This is my server report part (hh13577, Candidate#66889).</h1>
				<ul>
					<li>My favourites
						<ol>
							<li>Working with java, eclipse, tomcat, which are my
								favourite. Learning Derby was fun.</li>
							<li>Working with plain JDBC and HTTP Session was interesting
								as the use of JPAs or Hibernate and Spring security framework or
								Apache shiro would have made this much easy.</li>
							<li>I have worked further on the static web application that
								I created during my previous asignment. Now it is developed to
								work with dynamic data from derby and with tomcat7 server.</li>
							<li>My Object oriented deign with MVC pattern.</li>
							<li>I managed to keep the same styling and fluid layout of
								the client that I developed earlier. Also, the application is
								almost single page except theedit events page</li>
							<li>I have made further use of jQuery DOM manupulation and
								used it's AJAX api functions for a few interactions with the
								server. However, major server POST requests are through forms.</li>
							<li>I have used the jQuery Datepicker with time picker
								addon, which I mentioned earlier in my client assignment (HTML5
								date input is not yet supported by all browsers).</li>
						</ol>
					</li>

					<li>Searches and Learnings
						<ol>
							<li>Working with Derby database (embedded) and using
								Squirrel client. Including the Apache server configurations and
								the use of JDBC datasource for database connectivity and the
								import, export and creating data scripts: <a
								href="http://db.apache.org/derby/docs/10.10/getstart/"
								target="_blank">Derby</a> and <a
								href="http://sourceforge.net/apps/trac/squirrel-sql/wiki/GettingStarted"
								target="_blank">Squirrel SQL client</a>
							</li>
							<li>Logging from <a
								href="http://www.vogella.com/tutorials/Logging/article.html"
								target="_blank">Vogella</a>
							</li>
							<li>Connection pooling <a
								href="http://www.javaranch.com/journal/200601/JDBCConnectionPooling.html"
								target="_blank">Java Ranch</a></li>
							<li>Working with JSP Multipart forms<a
								href="http://javaandj2eetutor.blogspot.co.uk/2013/12/upload-image-file-into-database-using_3150.html#.U0MqtnW9gjA"
								target="_blank">Java and J2EE Tutorials blogspot</a></li>
							<li>JQuery Date picker from <a
								href="http://jqueryui.com/datepicker/" target="_blank">Jqueryui.com</a>
								and timepicker addon from <a href="http://trentrichardson.com/"
								target="_blank">trentrichardson</a>
							</li>
							<li>Java Servlet Session Management with HttpSession <a
								href="http://www.journaldev.com/1907/java-servlet-session-management-tutorial-with-examples-of-cookies-httpsession-and-url-rewriting"
								target="_blank">JournelDev</a></li>
							<li>Java password protection using SHA1 and Salt:<a
								href="http://www.quicklyjava.com/salt-in-java/" target="_blank">Quickly
									Java</a>
							</li>
							<li>Used BLOB to store the images in the database</li>
							<li>Object oriented design with use of MVC adjusted to work
								with JSP servlets</li>
							<li>Standard ways to work with the forms and JSP servlets</li>
							<li>Using SimpleDateFormat to format and display date in
								required format and store it in database as sql timestamp</li>
							<li>Posting the form data (including multi part), getting
								the data out of the request in the correcposning servlet and
								storing and retrieving the data took a long time.</li>
							<li>I have used the HTTPS with SSL using self generated
								keystore and certificate from tomcat.</li>
							<li>I have used the JAAS for authentication and managed the
								roles using a parameter in my Users java class. So a guest user
								(not logged in to the site) will not have access to a few
								features until he or she logs in. A logged in user could be a
								user or an admin. An admin would have more features such as
								adding and editing event data.</li>
							<li>Testing consumed a lot of time and required huge efforts
								for fixing issues.</li>
						</ol>
					</li>
					<li>Instructions for setup
						<ol>
							<li>The zip file submitted has the apache-tomcat folder with
								only required files to overwrite (conf and webapps - contains
								ESSite.war for deployment), databases (derby database),
								datamodel.sql, photos.del, photos.dat. Derby jars need to be
								present in apache lib folder, I did not provide them as derby
								versions may differ and may lead to NoClassDefFound issues.</li>
							<li>Source also put on Git: <a href="https://github.com/keryhp/webtech" target="_blank">https://github.com/keryhp/webtech</a></li>								
							<li>Unzip the zip file. Modify the apache-tomcat
								conf/context.xml file to provide the proper URL. Overwrite the
								apache-tomcat folder on apache-tomcat server (merge conf and
								webapps folders). This would set up the required server
								configurations.</li>
							<li>You could use the databases folder as it is. Or, a data
								migration script is provided as well with the required insert
								and import scripts in case a new database needs to be created.
								From command prompt connect to derby database and run the
								following:
								<ol>
									<li>connect
										'jdbc:derby:/home/harish/Desktop/WebTech/databases/eventsdb;create=true;user=essuser;password=esspwd';</li>
									<li>run 'datamodel.sql'</li>
									<li>Place the photos.del and photos.dat files in /home
										folder and run "CALL
										SYSCS_UTIL.SYSCS_IMPORT_TABLE_LOBS_FROM_EXTFILE(null,'PHOTOS','/home/photos.del',',','"','UTF-8',0);"</li>
									<li>This should get the fresh database with required text
										and BLOB data.</li>
								</ol>
							</li>
							<li>Now run the apache tomcat server and type
								"https://localhost:8443/ESSite/index.jsp" to access the Events
								showcase website.</li>
							<li>You can register new user or use admin users such as <strong>admin@bristol.ac.uk</strong>
								or <strong>testadmin@bris.ac.uk</strong> or guest user <strong>testuser@bris.ac.uk</strong>
								with <strong>Testing2$</strong> as common password for all users
							</li>
							<li>Complete source code is attached as ESSite project</li>
						</ol>
					</li>
					<li>Tools and techniques used
						<ol>
							<li>eclipse IDE, Squirrel</li>
							<li>MVC pattern with four tier architecture (Entities and
								DAO, Services, Business and UI)</li>
						</ol>
					</li>
					<li>Features and sample screen shots of the website</li>
					<li><a><img src="screenshots/1.png" style="width: 80%;" /></a></li>
					<li><a><img src="screenshots/2.png" style="width: 80%;" /></a></li>
					<li><a><img src="screenshots/3.png" style="width: 80%;" /></a></li>
					<li><a><img src="screenshots/4.png" style="width: 80%;" /></a></li>
					<li><a><img src="screenshots/5.png" style="width: 80%;" /></a></li>
					<li><a><img src="screenshots/6.png" style="width: 80%;" /></a></li>
					<li><a><img src="screenshots/7.png" style="width: 80%;" /></a></li>
					<li><a><img src="screenshots/8.png" style="width: 80%;" /></a></li>
					<li><a><img src="screenshots/9.png" style="width: 80%;" /></a></li>
					<li><a><img src="screenshots/10.png" style="width: 80%;" /></a></li>
					<li><a><img src="screenshots/11.png" style="width: 80%;" /></a></li>
					<li><a><img src="screenshots/12.png" style="width: 80%;" /></a></li>
					<li><a><img src="screenshots/13.png" style="width: 80%;" /></a></li>
					<li><a><img src="screenshots/14.png" style="width: 80%;" /></a></li>
					<li><a><img src="screenshots/15.png" style="width: 80%;" /></a></li>
					<li><a><img src="screenshots/16.png" style="width: 80%;" /></a></li>
					<li><a><img src="screenshots/17.png" style="width: 80%;" /></a></li>
					<li><a><img src="screenshots/18.png" style="width: 80%;" /></a></li>
					<li><a><img src="screenshots/19.png" style="width: 80%;" /></a></li>
					<li><a><img src="screenshots/20.png" style="width: 80%;" /></a></li>
					<li><a><img src="screenshots/21.png" style="width: 80%;" /></a></li>
					<li><a><img src="screenshots/22.png" style="width: 80%;" /></a></li>
					<li><a><img src="screenshots/23.png" style="width: 80%;" /></a></li>
					<li><a><img src="screenshots/25.png" style="width: 80%;" /></a></li>
					<li><a><img src="screenshots/26.png" style="width: 80%;" /></a></li>
					<li><a><img src="screenshots/27.png" style="width: 80%;" /></a></li>
					<li><a><img src="screenshots/28.png" style="width: 80%;" /></a></li>
				</ul>
			</div>
			<div id="myreport">
				<h1>This is my client report part.</h1>
				<ul>
					<li>My favourites
						<ol>
							<li>Simple and fluid design of my website layout and
								navigation.</li>
							<li>My Video created from Animoto.</li>
							<li>My Logo.</li>
							<li>Event details Modal window</li>
							<li>Hanging up the logo over header and the content parts of
								my page.</li>
							<li>My planning to keep the look and feel as simple as
								possible, eventualy helped me save a lot of time.</li>
						</ol>
					</li>

					<li>Searches
						<ol>
							<li>To learn HTML5, XHTML, CSS, CSS3 and Javascript I
								referred <a href="http://www.w3.org/" target="_blank">W3.org</a>,
								<a href="http://www.w3schools.com/" target="_blank">W3
									Schools</a> and Stack Overflow.
							</li>
							<li>Adapted the basic navigation menu idea from <a
								href="http://html5up.net/dopetrope" target="_blank">Dopetrope
									Website</a>.
							</li>
							<li>The form submissions are adapted from <a
								href="http://dhirajkumarsingh.wordpress.com/2013/10/19/css3-modal-window-popups-sliding-forms-with-fancybox-effect/"
								target="_blank">Dhiraj</a>. The site provides them as pop up
								windows with transitional effects, but I have used the concept
								to display the hidden inline tags.
							</li>
							<li><a
								href="http://cameronbaney.com/2012/07/26/pure-html5css3-responsive-modal-window"
								target="_blank">Event Data Modal Window</a> I have adapted this
								to match my fluid deisgn and data display.</li>
							<li>Carousel using <a href="http://responsiveslides.com/"
								target="_blank">Responsive Slides</a>. I have adapted it to make
								my own theme for it.
							</li>
							<li>JQuery for DOM manipulations and ease of accessing the
								html data</li>
							<li>Hover my car - Simple CSS3 animation adapted from <a
								href="http://imajineweb.com/cssanimation" target="_blank">Imajine</a>
							</li>
						</ol>
					</li>
					<li>Learnings and findings
						<ol>
							<li>Standard ways to develop a website using XHTML5, CSS and
								CSS3.</li>
							<li>The input type="date" does not work in most of the
								browsers.</li>
							<li>For date, I could have used JQuery Datepicker instead,
								but that would have made the client side heavy in terms of image
								files and .js. However, for the next assignment the need for
								date input would be fullfilled using JQuery datepicker.</li>
							<li>The ogg video does not play audio in Firefox 23.+
								versions.</li>
							<li>The css3 transform for my car animation does not work
								with Mozilla.</li>
							<li>The SVG animation with the image animate and the circle
								animateTransform does not work on SAFE on all browsers, hence
								removed it from the website.</li>
						</ol>
					</li>
					<li>Long time
						<ol>
							<li>Fluid design of the website, yet major improvements to
								be compatible with various browsers and versions</li>
							<li>Code was hosted on Github.io and tested for cross
								browser and device compatibility.</li>
							<li>Testing limited to and carried out on: IE 7,8, 9, 10, 11
								and Chrome 28+, 30+, 33+, Firefox 18+, 20+, 23+ versions and on
								mobile using chrome and Opera mini</li>
							<li>Cross browser compatibility testing carried out using <a
								href="http://browsershots.org">Browsershots</a>
							</li>
							<ul>
								<li>Improvements for proper rendering pending on IE &lt; 9
									- ex: the page layout, the add new event, my bookings etc</li>
								<li>Date field does not work with IE and firefox</li>
								<li>The Video poster message not rendered in IE &lt; 9</li>
								<li>The Fluid design has issues in Opera mini on mobile
									phone.</li>
							</ul>
							<li>Designing the logo - idea to realisation</li>
						</ol>
					</li>
					<li>Tools and techniques used
						<ol>
							<li>JEdit with XML, HTML and CSS plugins</li>
							<li>Gimp to deign the logo and other images used in the
								website: used the simple layered and blending techniques</li>
							<li>Inkscape to create Some images and favicon</li>
						</ol>
					</li>
				</ul>
			</div>
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