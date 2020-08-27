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
<h2>CFCA远程真机调试后台管理</h2>
<h2>当前所有在线设备</h2>
<table>
<% int number = 1; %>
  <c:forEach var="device" items="${devicesList}" varStatus="status">
  <tr>
  <td>
 <%=number%>
  </td>
   <td>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        </td>
  <td>
  设备名：
  </td>
  <td>
  ${device.phonetype}
  </td>
  <td>
      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      </td>
  <td>
 设备ID：
  </td>
  <td>
  <div id="deviceid<%=number%>" name="deviceid<%=number%>">${device.deviceid}</div>
  </td>
  <td>
      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      </td>
    <td>
   设备型号：
    </td>
    <td>
   <div id="model<%=number%>" name="model<%=number%>">${device.model}</div>
    </td>
    <td>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;

        </td>
          <td>
           占用用户ID：
            </td>
            <td>
           <div id="userid<%=number%>" name="userid<%=number%>">
                         ${device.userid}

          </div>
            </td>
            <td>
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;

                </td>

  <td>
  已分配组：
  </td>
  <td>
  ${device.rolename}
  </td>
    <td>
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    </td>
  <td>
  设置新组：
  </td>
  <td>
  <select name="roleid<%=number%>" id="roleid<%=number%>">
  <option value="0">全部</option>
<c:forEach var="role" items="${rolesList}" varStatus="status">
 <option value="${role.id}">${role.name}</option>


 </c:forEach>
  </select>
  </td>
    <td>
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    </td>
  <td>
   <button style='margin-left: 1%; background-color: #0e8f9f; width: 100px; height: 25px; color: white;' id='editrole' name='editrole' onclick="editrole(<%=number%>)">设置</button>

  </td>
  <td>
   <button style='margin-left: 1%; background-color: #0e8f9f; width: 100px; height: 25px; color: white;' id='editrole' name='editrole' onclick="stopuse(<%=number%>)">踢出</button>
  </td>
  </tr>
   <% number++;%>
    </c:forEach>
</table>



</body>
</html>
<script src="assets/liucheng/js/jquery.min.js"></script>
<script>
function editrole(number){
    var deviceid = $("#deviceid"+number).text();
    var model = $("#model"+number).text();
    var roleid = $("#roleid"+number).find("option:selected").val();
    var url = "<%=basePath%>/editrole";
    $.ajax({
                   type : "get",
                   url : url,
                   data : {
                              deviceid : deviceid,
                              model : model,
                              roleid :roleid,
                              userid : "${userid}"
                          },

                   success:function(res){
                   window.location.reload();
                   },
                   error: function() {
                       }
                  });
}


function stopuse(number){
    var deviceid = $("#deviceid"+number).text();
    var model = $("#model"+number).text();
    var url = "<%=basePath%>/stopdevice";
    $.ajax({
                   type : "get",
                   url : url,
                   data : {
                              deviceid : deviceid,
                              model : model,
                              userid : "${userid}"
                          },

                   success:function(res){
                   window.location.reload();
                   },
                   error: function() {
                       }
                  });
}
</script>
