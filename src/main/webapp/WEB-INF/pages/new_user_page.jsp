
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Add new User</title>
</head>
<body>
<h1>Add new User</h1>

<form action="${pageContext.request.contextPath}/adduser" method="post">
    <label for="userUrl">Profile URL : </label>
    <input type="text" name="userUrl" id="userUrl" value="${userUrl}">
</form>
</body>
</html>