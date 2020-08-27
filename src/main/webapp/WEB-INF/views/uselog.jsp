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
<title>CFCA远程真机调试后台管理</title>
 <style type="text/css">
        table tr td{ border-bottom:#B0E0E6 solid 2px;}
     </style>
</head>
<body>
<h2>安卓远程真机调试记录</h2>
<% int number = 1; %>
<table>
<tr>
<td>序号</td>
<td>调试设备</td>
<td>调试用户</td>
<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
 <td>调试时间</td><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
 <td>调试用时（分钟）</td>
</tr>
 <c:forEach var="userlog" items="${userloglist}" varStatus="status">
  <tr>
   <td>
   <%=number%>
    </td>
  <td>${userlog.phonetype}</td>
    <td>${userlog.username}</td><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
    <td>${userlog.starttime}
    至
     ${userlog.endtime}</td><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
     <td>${userlog.usetime}</td>
<tr>
 <% number++;%>
  </c:forEach>
  <tr>
                                                                                                                                                                                                                                                                                                          </td>
  <td>用时总计（分钟）</td>  <td>${usetimesum}</td>
  </tr>
</table>



</body>
</html>
