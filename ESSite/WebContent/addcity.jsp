<!DOCTYPE html>
<%@ page import="java.util.*"%>
<%@ page import="uk.bris.esserver.service.CityService"%>
<%@ page import="uk.bris.esserver.repository.entities.City"%>
<%@ page import="uk.bris.esserver.util.ESSDateUtil"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Testing</title>
<style>
.error {
	color: red;
}

.success {
	color: green;
}
</style>
</head>
<body>
	<% 
    CityService citySrv = new CityService();
    List lcity = citySrv.getListOfCity();
	if(lcity != null){    
	    Iterator it = lcity.iterator();
	    while (it.hasNext()) {
	    	City city = (City)it.next();
	        out.print("<h1>" + city.getCityCode() + "</h1>");
	        out.print("<h1>" + city.getCityName() + "</h1>");
	        out.print("<h1>" + city.getPostCode() + "</h1>");
	        out.print("<h1>" + city.getCountry() + "</h1>");
	        out.print("<h1>" + ESSDateUtil.formatTimeStamp(city.getCreateDate()) + "</h1>");
	        out.print("<h1>" + city.getCreatedBy() + "</h1>");
	        out.print("<h1>" + ESSDateUtil.formatTimeStamp(city.getLastModified()) + "</h1>");
	        out.print("<h1>" + city.getModifiedBy() + "</h1>");
	        out.print("<h1>" + city.getRemarks() + "</h1>");	        
	    }
    }
%>

    <form action="city" method="get">
		<select onchange="changeCity(this);" name="city" id="city">
			<c:forEach var="city" items="${lcity}">
				<li><option value="<c:out value="${city.getCityName()}"/>" <c:if test="${city.getCityName().compareTo(defaultCity) == 0}">selected="true"</c:if>><c:out value="${city.getCityName()}"/><span
							class="caret"></span></option></li>
			</c:forEach>
		</select>
	</form>

					<li><a href="index.html#bristol">Bristol <span
							class="caret"></span>
					</a>
						<div>
							<ul>
								<li><a href="index.html#london">London</a></li>
								<li><a href="index.html#manchester">Manchester</a></li>
								<li><a href="index.html#others">Others</a></li>
							</ul>
						</div></li>

	<form action="city" method="post">
		<p>
			<label for="citycode">City code:</label> <input id="citycode"
				name="citycode" value="${param.citycode}"></input> <span
				class="error">${messages.citycode}</span>
		</p>
		<p>
			<label for="cityname">City name:</label> <input id="cityname"
				name="cityname" value="${param.cityname}"></input> <span
				class="error">${messages.cityname}</span>
		</p>
		<p>
			<label for="postcode">Post code:</label> <input id="postcode"
				name="postcode" value="${param.postcode}"></input> <span
				class="error">${messages.postcode}</span>
		</p>
		<p>
			<label for="country">City name:</label> <input id="country"
				name="country" value="${param.country}"></input> <span class="error">${messages.country}</span>
		</p>
		<p>
			<label for="remarks">Remarks:</label> <input id="remarks"
				name="remarks" value="${param.remarks}"></input> <span class="error">${messages.remarks}</span>
		</p>
		<p>
			<input type="submit"></input> <span class="success">${messages.success}</span>
		</p>
	</form>
</body>
</html>