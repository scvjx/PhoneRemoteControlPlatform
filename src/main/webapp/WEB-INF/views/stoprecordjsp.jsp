<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path;
    String ip =request.getLocalAddr();
%>
<html>
<head>
    <meta charset="UTF-8">
    <title>CFCA脚本录制</title>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <!-- Tell the browser to be responsive to screen width -->
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">
    <!-- Favicon icon -->
    <link rel="icon" type="image/png" sizes="16x16" href="assets/images/favicon.png">

</head>
<body onload="connect('${sn}-${screensize}')">
<br>
<div  style="float:left; width: 100%">
    <tr>
        <td>
            <h3 style="margin-left: 10%; margin-top: 0.8%">您已经停止录制脚本，如需继续录制请重新点击左侧功能栏！</h3>
            <br>
        </td>
    </tr>
</div>
<div  style="float:left; width: 100%">

</div>

</body>
</html>


