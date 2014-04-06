<!DOCTYPE html>
<%@ page import="java.util.*"%>
<%@ page import="uk.bris.esserver.service.PhotosService"%>
<%@ page import="uk.bris.esserver.repository.entities.Photos"%>
<%@ page import="uk.bris.esserver.util.ESSDateUtil"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Add Photo</title>
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
    PhotosService photosSrv = new PhotosService();
    List lphotos = photosSrv.getListOfPhotos();
	if(lphotos != null){    
	    Iterator it = lphotos.iterator();
	    while (it.hasNext()) {
	    	Photos photo = (Photos)it.next();
	        out.print("<img src=\"photo?id=" + photo.getId() + "\"></img>");
	        out.print("<h1>" + ESSDateUtil.formatTimeStamp(photo.getCreateDate()) + "</h1>");
	        out.print("<h1>" + photo.getCreatedBy() + "</h1>");
	        out.print("<h1>" + ESSDateUtil.formatTimeStamp(photo.getLastModified()) + "</h1>");
	        out.print("<h1>" + photo.getModifiedBy() + "</h1>");
	        out.print("<h1>" + photo.getRemarks() + "</h1>");	        
	    }
    }
%>

	<form action="photo" method="post" enctype="multipart/form-data">
		<p>
			<label for="fileToUpload">Select a File to Upload</label>
			<input type="file" name="fileToUpload" id="fileToUpload" required="required" value="${param.fileToUpload}"></input> 
			<span class="error">${messages.fileToUpload}</span>
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