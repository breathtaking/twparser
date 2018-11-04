<!DOCTYPE html>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>User Data</title>
    <style><%@include file='style.css'%></style>
</head>

<body>

<div class="wholePage">
<div class="pageContainer">


    <div class="headerTW">
    <div class="headerTWLeft">
        <h1>User: ${user.profileIdentifier}</h1>
        <h2>User Name: ${user.userName}</h2>
        <h2>User Followers: ${user.numberOfFollowers}</h2>
        <h2>User Followings: ${user.numberOfFollowings}</h2>
    </div>
    <div class="headerTWRight">
        <ul>
            <li>
                <a href="${post.postUrl}">
                    <img src="${user.profileAvatarUrl}">
                </a>
            </li>
        </ul>
    </div>
    </div>

    <div class="contentTWContainer">
    <div class="contentTW">

        <table class="tableTW" border="3" cellpadding="5" cellspacing="5">
            <c:forEach var="post" items="${postList}">
                <c:choose>
                    <c:when test="${post.postType eq 'PHOTO'}">
                        <ul>
                            <li><a href="${post.postUrl}"><img src="${post.imageUrl}"></a></li>
                        </ul>
                    </c:when>
                    <c:when test="${post.postType eq 'VIDEO'}">
                        <p>${post.embedVideoHtml}</p>
                    </c:when>
                    <c:when test="${post.postType eq 'GIF'}">
                        <p>${post.embedVideoHtml}</p>
                    </c:when>
                    <c:otherwise>
                        <p>UNKNOWN TYPE</p>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </table>

    </div>
    </div>

    <div class="contentTWPaggination">

        <%--For displaying Previous link except for the 1st page --%>
        <c:if test="${currentPage != 1}">
            <td><a href="post.do?page=${currentPage - 1}">Previous</a></td>
        </c:if>

        <%--For displaying Page numbers.
        The when condition does not display a link for the current page--%>
        <table class="outer" border="1" cellpadding="5" cellspacing="5">
            <tr class="vscrolling_container">
                <c:forEach begin="1" end="${noOfPages}" var="i">
                    <c:choose>
                        <c:when test="${currentPage eq i}">
                            <td>${i}</td>
                        </c:when>
                        <c:otherwise>
                            <td><a href="${pageContext.request.contextPath}/adduser?page=${i}">${i}</a></td>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
            </tr>
        </table>

        <%--For displaying Next link --%>
        <c:if test="${currentPage lt noOfPages}">
            <td><a href="adduser?page=${currentPage + 1}">Next</a></td>
        </c:if>

    </div>

    <div style="clear: both; background: floralwhite; height: 155px; margin: 0 auto;"> TWPARSER (c)
    </div>

</div>
</div>

</body>

</html>