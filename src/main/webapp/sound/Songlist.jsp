<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<html>
<head>
    <title>List of Songs</title>
</head>
<body>
<display:table  cellspacing="20" style="align:center;" export="true" requestURI="/sound/SongMenuPage" pagesize="10" name="allTracks">
    <display:column sortable="true" property="id" title="ID"/>
    <display:column sortable="true" property="title" title="Title"/>
    <display:column url="/sound/SthreeLoginPage" paramId="trackId" paramProperty="id">to S3</display:column>
</display:table>
</body>
</html>