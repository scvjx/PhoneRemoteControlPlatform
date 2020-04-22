<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path;
%>
<html>
<head>
    <meta charset="UTF-8">
    <title>CFCA远程真机系统</title>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <!-- Tell the browser to be responsive to screen width -->
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">
    <!-- Favicon icon -->
    <link rel="icon" type="image/png" sizes="16x16" href="assets/images/favicon.png">

    <style type="text/css">
        table tr td{ border-bottom:#B0E0E6 solid 2px;}
    </style>
    <script>
        function two_char(n) {
            return n >= 10 ? n : "0" + n;
        }

        function time_fun() {
            var sec=0;
            setInterval(function () {
                sec++;
                var date = new Date(0, 0)
                date.setSeconds(sec);
                var h = date.getHours(), m = date.getMinutes(), s = date.getSeconds();
                document.getElementById("mytime").innerText = two_char(h) + ":" + two_char(m) + ":" + two_char(s);
            }, 1000);
        }
    </script>
</head>
<body onload="connect('${sn}-${screensize}');time_fun()">
<br>
<div  style="float:left; width: 100%">
    <table style="width: 100%">
        <tr>
            <td>
                <h3 style="margin-left: 10%">您正在远程调试：<font color="red">${phonetype}</font></h3>
            </td>
            <td style="text-align: right">
                <form action="#" method="POST"  id="resolu">
                    可根据您网络情况调整屏幕清晰度：<select name="resolu" class="resolu">
                    <option value="80"
                            <c:if test="${resolution == '80' }">   selected="selected"</c:if>
                    >高清</option>
                    <option value="40"
                            <c:if test="${resolution == '40' }">   selected="selected"</c:if>
                    >标清</option>
                    <option value="20"
                            <c:if test="${resolution == '20' }">   selected="selected"</c:if>
                    >普通</option>
                </select>
                </form>
            </td>
            <td style="width: 5%"></td>
        </tr>
    </table>
</div>
<br>
<div  style="float:left; width: 100%">
        <table style="width: 100% ;">
            <tr>
                <td style="width: 35%;">
                    <div style="text-align: center; background-color: #EAEAEA">
                        <canvas id="phone-screen"></canvas>
                    </div>
                    <div style="text-align: center; background-color: #EAEAEA">
                        <button id="backevent" name="backevent" style="height: 45px; width: 10%; background: url('assets/images/back.jpg') no-repeat; background-size: 100% 100%"></button>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        <button id="homeevent" name="homeevent" style="height: 45px;width: 10%; background: url('assets/images/home1.jpg') no-repeat; background-size: 100% 100%"></button>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        <button id="killevent" name="killevent" style="height: 45px; width: 10%; background: url('assets/images/kill.jpg') no-repeat; background-size: 100% 100%"></button>
                    </div>
                </td>
                <td style="width: 5%; border-left: #B0E0E6 solid 2px;"></td>
                <td style="width: 60%;">
                    <table style="width: 92%;">
                        <tr style="width: 100%;">
                            <td style="border: none;">
                                <h4>使用结束后请点击<a href="<%=basePath%>/devicelist?userid=${userid}&deviceid=${deviceid}&model=${model}&action=stop"><font color="red">停止使用</font></a>,否则按全天计算使用时长
                                    <span style="float: right">已经使用时长：<h3 id="mytime" >00:00:00</h3></span>
                                </h4>
                                <br>
                                <br>
                                <br>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <form action="#" method="POST"  id="appfileform" enctype="multipart/form-data">
                                    上传安装待调试APK：<input type="file" id="appfile" name="appfile" style="width:30%">&nbsp&nbsp
                                    <input style="background-color: #00ccff; height: 30px" type="submit" id="submit" value="上传">
                                    <span id="imgWait"></span>
                                </form>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <br>
                                如需远程调试设备请执行如下命令：<input style="width: 50%;" type="text" value="adb connect ${serverip}:${adbkitport}" readonly>
                                <br>
                                <br>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <br>

                                <input style="background-color: #00ccff; height: 30px" type="button" id="getlogcat" value="获取logcat日志">&nbsp;&nbsp;（注：每次点击可获取${logcatRows}行）<p>
                                <textarea id="logcat" style="width: 100%;background-color: #EDEDED" rows="30" readonly></textarea>
                            </td>
                        </tr>
                    </table>
                </td>
            <tr>
        </table>

 </div>

</body>
<script src="assets/js/device.js"></script>
<script src="https://cdn.bootcss.com/jquery/3.2.1/jquery.min.js"></script>
<script type="text/javascript">
   $(".resolu").change(function(){
     var resolu = $(".resolu option:selected").val();
     $.ajax({
        type : "post",
        dataType:"json",
        contentType:'application/json;charset=UTF-8',
        url : "<%=basePath%>/devicesetting",
        data : {
            'sn':'${deviceid}',
            'model':'${model}',
            'userid':'${userid}',
            'quality':resolu
        },
        success : function(data) {
            if(data.code==200){

              window.location.reload();
            }else{
            alert(data.msg);
           }
        },
        error : function(data) {
           alert(data.msg);
        }
        })
        });


 $("#submit").click(function(){
     var formData = new FormData();
     formData.append('appfile',$('#appfile')[0].files[0]);
     formData.append('sn','${deviceid}');
     $.ajax({
         url:'<%=basePath%>/installapp',
         type:'post',
         data: formData,
         contentType: false,
         processData: false,
         cache: false,
         success:function(res){
             console.log(res.data);
             if(res.data["code"]=="200"){
                 alert("成功");
             }else {
                 alert('失败');
             }
         }
     })
 });


 $("#getlogcat").click(function getlogcat (){
  var url = "<%=basePath%>/getlogcat";
    var logcatkey = $("#logcatkey").val();
    $.ajax({
       type : "get",
       url : url,
       data : {
                  sn : '${deviceid}',
                  logcatkey : logcatkey
              },
       timeout:1000,
       success:function(res){
        var b = $.parseJSON(res);
        if(b.code=="200"){
             jQuery("#logcat").append(b.msg);
        }
       },
       error: function() {
             // alert("失败，请稍后再试！");
           }
      })
 }

  );


   $("#backevent").click(function (){
      var url = "<%=basePath%>/inputevent";
      $.ajax({
         type : "get",
         url : url,
         data : {
                    sn : '${deviceid}',
                    eventnumber : "4"
                },

         success:function(res){
         },
         error: function() {

             }
        });
   });

   $("#homeevent").click(function (){
         var url = "<%=basePath%>/inputevent";
         $.ajax({
            type : "get",
            url : url,
            data : {
                       sn : '${deviceid}',
                       eventnumber : "3"
                   },
            success:function(res){
            },
            error: function() {

                }
           });
      });

      $("#killevent").click(function(){
            var url = "<%=basePath%>/inputevent";
            $.ajax({
               type : "get",
               url : url,
               data : {
                          sn : '${deviceid}',
                          eventnumber : "82"
                      },

               success:function(res){
               },
               error: function() {
                   }
              });
         });
</script>
</html>


