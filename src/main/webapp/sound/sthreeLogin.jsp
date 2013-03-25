<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<html>
<head>
    <title>Sign On</title>
</head>

<body>
<h3>Song to Send</h3>
<s:form >
    <s:label label="Id" name="track.id"/>
    <s:label label="Title" name="track.title"/>
    <s:hidden name="trackId" value="%{track.id}"/>
    <s:textfield label="S3 AccessKey" name="s3username"/>
    <s:password label="S3 SecretKey" name="s3password"/>
    <s:textfield label="Bucket Name" name="bucketName"/>
    <s:submit action="LoginS3"/>
</s:form>

</body>
</html>
