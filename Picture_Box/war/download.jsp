<%@ page import="com.google.appengine.api.blobstore.BlobstoreServiceFactory" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreService" %>

<%-- Declare and assign the blobstore service --%>
<%BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService(); %>

<html>
	 <head>
 		<title>Upload Test</title>
 	</head>
 	
 	<body>
 		<%-- Define the action for this page to be to create an UploadUrl using the blobstore service... --%>
 		<%--... and to use that to call the /upload servlet with the blobkey when the upload is done --%>
 		<form action="<%= blobstoreService.createUploadUrl("/upload") %>" method="post" enctype="multipart/form-data">
 			
 			<input type="text" name="foo">
			<input type="file" name="myFile"> 
 			<input type="submit" value="Submit"> 
 			<%-- When we select the submit key, the file is uploaded to the blobstore and the /upload servlet... --%>
 			<%-- is loaded using the blobkey as the parameter --%>
		</form>
 	</body>
</html>