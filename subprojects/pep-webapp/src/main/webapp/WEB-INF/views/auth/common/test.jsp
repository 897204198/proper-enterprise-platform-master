<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Index</title>
</head>
<body>
    test page
    <a href="#" onclick="logout()">log out</a>

    <form action="/pep/logout" method="post" id="logoutForm">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
    </form>

    <script>
    function logout() {
        document.getElementById('logoutForm').submit();
    }
    </script>
</body>
</html>