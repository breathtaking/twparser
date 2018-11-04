<!DOCTYPE html>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>User Data</title>
    <style><%@include file='style.css'%></style>
</head>
<body>
<h1>User was successfully added!</h1>
<h2>User Data:</h2>
<p><strong>URL:</strong> ${userUrl}</p>
<p><strong>URL:</strong> ${posts}</p>
<c:forEach items="${posts}" var="item">
    ID:${item.postTitle}<br>
    ID:${item.postId}<br>
    USER:${item.userId}<br>
    LIKES:${item.numberofLikes}<br>
    RETWEETS:${item.numberOfRetwits}<br>
    COMMENTS:${item.numberOfComments}<br>
    IMAGE:${item.imageUrl}<br>
    DATE:${item.postDate}<br>
    <ul>
        <li><a href="#"><img src="${item.imageUrl}"></a></li>
    </ul>
</c:forEach>

</body>
</html>