<!DOCTYPE html>
<%@page import="uk.bris.esserver.util.ESSDateUtil"%>
<%@page import="java.sql.Timestamp"%>
<%@ page import="java.util.*" %>
<%@ page import="uk.bris.esserver.service.PeopleService" %>
<%@ page import="uk.bris.esserver.repository.entities.People" %>
<%@ page import="uk.bris.esserver.service.EventService" %>
<%@ page import="uk.bris.esserver.repository.entities.Event" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Testing</title>
<style>.error { color: red; } .success { color: green; }</style>
</head>
<body>
<p>Testing Java: <% out.print("OK"); %>.</p>

<% 
    PeopleService pplSrv = new PeopleService();
    List lpeople = pplSrv.getListOfPeople();
	if(lpeople != null){    
	    Iterator it = lpeople.iterator();
	    while (it.hasNext()) {
	    	People ppl = (People)it.next();
	        out.print("<h1>" + ppl.getForenames() + "</h1>");
	        out.print("<h1>" + ppl.getSurname() + "</h1>");
	    }
    }
%>

<% 
    EventService evtSrv = new EventService();
    List levents = evtSrv.getEventByCity("BRI");
	if(levents != null){    
	    Iterator it = levents.iterator();
	    while (it.hasNext()) {
	    	Event evt = (Event)it.next();
	        //evt.setStartDate(new Timestamp(ESSDateUtil.testFormatter.parse("2014-05-25 16:49:23.434").getTime()));
	        //evtSrv.updateEvent(evt);
	        out.print("<h1>" + evt.getTitle() + "</h1>");
	        out.print("<h1>" + evt.getDescription() + "</h1>");
	        out.print("<h1>" + evt.getLocation() + "</h1>");
	        out.print("<h1>" + evt.getCity() + "</h1>");
	        out.print("<h1>" + evt.getTagline() + "</h1>");
	        out.print("<h1>" + evt.getStartDate() + "</h1>");
	        out.print("<h1>" + evt.getPrice() + "</h1>");
	        out.print("<h1>" + evt.getUserid() + "</h1>");	        
	    }
    }
	
%>

<form action="test" method="post">
            <p>
                <label for="forenames">What's your forename?</label>
                <input id="forenames" name="forenames" value="${param.forenames}"></input>
                <span class="error">${messages.forenames}</span>
            </p>
            <p>
                <label for="surname">What's your surname?</label>
                <input id="surname" name="surname" value="${param.surname}"></input>
                <span class="error">${messages.surname}</span>
            </p>
            <p>
                <input type="submit"></input>
                <span class="success">${messages.success}</span>
            </p>
        </form>
</body>
</html>