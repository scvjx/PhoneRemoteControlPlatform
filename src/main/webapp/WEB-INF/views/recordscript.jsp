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

    <style type="text/css">
        table tr td{ border-bottom:#B0E0E6 solid 2px;}
        #script {
            margin-left: 5%;
            margin-top: 2%;
            width: 80%;
            text-align: center;
        }
        #script tr td  {
            height: 40px;
            border: #B0E0E6 solid 2px;
        }

        #script button {
            margin-left: 2%;
            background-color: #0e8f9f;
            width: 40px;
            height: 25px;
            color: white;
        }

        #scriptres {
                    margin-left: 8%;
                    margin-top: 2%;
                    width: 80%;
                    text-align: center;
                }
        #scriptres tr td  {
                    height: 40px;
                    border: #B0E0E6 solid 2px;
                }
    </style>
</head>
<body onload="connect('${sn}-${screensize}')">
<br>
<div  style="float:left; width: 100%">
    <table style="width: 100%">
        <tr>
            <td>
                <h3 style="margin-left: 10%; margin-top: 0.8%">您正在脚本录制页面</h3>
                <br>
            </td>
            <td>
                <h3 style="margin-left: 50%; margin-top: 0.8%;">
                    使用结束后请点击<a href="<%=basePath%>/stopdevice?userid=${userid}&deviceid=${deviceid}&model=${model}"><font color="red">停止使用</font></a>
                </h3>
                <br>
            </td>
        </tr>
    </table>
</div>
<div  style="float:left; width: 100%">
    <table style="width: 100%">
        <tr>
            <td style="width: 25%;">
                <div style="text-align: center; background-color: #EAEAEA">
                    <canvas id="phone-screen"></canvas>
                </div>
                <div style="text-align: center; background-color: #EAEAEA">
                    <button id="backevent" name="backevent" style="height: 45px; width: 20%; background: url('assets/images/back.jpg') no-repeat; background-size: 100% 100%"></button>&nbsp;
                    <button id="homeevent" name="homeevent" style="height: 45px;width: 20%; background: url('assets/images/home1.jpg') no-repeat; background-size: 100% 100%"></button>&nbsp;
                    <button id="killevent" name="killevent" style="height: 45px; width: 20%; background: url('assets/images/kill.jpg') no-repeat; background-size: 100% 100%"></button>
                </div>
            </td>
            <td style="width: 1%; border-left: #B0E0E6 solid 2px;"></td>
            <td style="width: 50%">
                <table style="width: 100%">
                    <div class="rebinding-box" style="margin-right: 2%">
                        <div class="step-body" id="myStep">
                            <div class="step-header" style="margin-bottom: 10%">
                                <ul>
                                    <li><p>安装APP</p></li>
                                    <li><p>脚本录制</p></li>
                                    <li><p>脚本回放</p></li>
                                    <li><p>脚本保存</p></li>
                                </ul>
                            </div>
                            <div class="step-content" style="height: 600px; background-color: #EAEAEA; ">
                                <div class="step-list" style="margin-left: 3%; text-align: center;">
                                    <br> <br>
                                    <font color="#d2691e"><strong>
                                        注： 如需上传安装新APP请进行上传，如不需安装则可直接下一步<br></strong></font>
                                    <br><br><br>
                                    <form action="#" method="POST"  id="appfileform" enctype="multipart/form-data">
                                        上传安装APP:&nbsp;&nbsp;&nbsp;<input type="file" id="appfile" name="appfile">&nbsp&nbsp
                                        <input type="submit" id="submit" value="上传" style="background-color: #0e8f9f; width: 40px; height: 25px; color: white;">
                                    </form>
                                </div>
                                <div id="recordscriptdiv" class="step-list" style="height: 600px; overflow:auto; margin-left: 3%; text-align: center; width: 100%">
                                    <br>
                                    <font color="#d2691e"><strong>
                                        注： 请准备好后点击下面按钮开始录制，录制完成后请点击结束录制按钮</strong></font>
                                    <button  id="startrec" name="startrec" value="s" style="background-color: #0e8f9f; width: 100px; height: 30px; color: white; margin-left: 5%">开始录制</button>
                                    <div id="title" name="title"></div>
                                </div>
                                <div class="step-list" style="height: 600px; overflow:auto; margin-left: 3%; text-align: center; width: 100%">
                                    <br>
                                    <font color="#d2691e"><strong>
                                        您录制的测试脚本展示如下：</strong></font>
                                    <div id="scriptrespre" name="scriptrespre"></div>
                                    <br>
                                    <font color="#d2691e"><strong>
                                         点击按钮进行回放,请注意回放前调整左侧手机页面到脚本起始位置</strong></font>
                                     <br><br><button  id="playback" name="playback"  style="background-color: #0e8f9f; width: 100px; height: 30px; color: white;">回放脚本</button>
                                </div>
                                <div class="step-list" style="height: 600px; overflow:auto; margin-left: 3%; text-align: center; width: 100%">
                                    <br>
                                        <font color="#d2691e"><strong>请输入脚本名称：</strong></font><input type="text" name="scriptname" id="scriptname"><br>
                                        <br><button  id="savescript" name="savescript"  style="background-color: #0e8f9f; width: 100px; height: 30px; color: white; margin-left: 5%">保存</button>
                                          </div>
                                          <br>
                                          <div id="savescriptres" name="savescriptres"></div>
                            </div>
                        </div>
                        <div class="twobtn-box" style="margin-top: 3%; float: right; margin-right: 5%">
                            <button class="btn btn-sm btn-primary" id="nextBtn" style="background-color: #00aff0; border: none; width: 100px; height: 50px;"><h3><strong>下一步</strong></h3></button>
                        </div>
                    </div>
                </table>
            </td>
            <td style="border-left: #B0E0E6 solid 2px; text-align: center;">
                <table style=" width: 100%; background-color: #EAEAEA">
                    <tr style="height: 100px;">
                        <td style="vertical-align: middle; text-align: center">
                            <font color="#d2691e">
                                <strong>脚本工具</strong>
                            </font>
                        </td>
                    </tr>
                    <tr style="height: 800px">
                        <td style="vertical-align: top; text-align: left">
                            <br><br>
                            等待时间：<input type='text' id='sleeptime' style="width: 30%">秒
                            <button style='margin-left: 1%; background-color: #0e8f9f; width: 40px; height: 25px; color: white;' id='insertsleep' name='insertsleep'>插入</button>
                            <br><br>
                            输入文本：<input type='text' id='inputtext' style="width: 30%">
                            <button style='margin-left: 1%; background-color: #0e8f9f; width: 40px; height: 25px; color: white;' id='inserttext' name='inserttext'>插入</button>
                            <br><br>
                            截屏操作&nbsp; <button style='margin-left: 1%; background-color: #0e8f9f; width: 40px; height: 25px; color: white;' id='snapshot' name='snapshot'>插入</button>
                            <br>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
    </table>
</div>

</body>
<script src="assets/js/device.js"></script>
<script src="https://cdn.bootcss.com/jquery/3.2.1/jquery.min.js"></script>
<link rel="stylesheet" type="text/css" href="assets/liucheng/css/link.css">
<link rel="stylesheet" type="text/css" href="assets/liucheng/css/jquery.step.css">
<script src="assets/liucheng/js/jquery.min.js"></script>
<script src="assets/liucheng/js/jquery.step.js"></script>
<script src="assets/liucheng/js/layui.js" type="text/javascript" charset="utf-8"></script>
<script type="text/javascript">
    var soutside1ab=document.getElementById("outside1abs");
    var soutside2as=document.getElementById("outside2as");
    var oneforms=document.getElementById("oneform");
    var twoforms=document.getElementById("twoform");
    var threeforms=document.getElementById("threeform");
    function fun(){
        soutside1ab.classList.remove("outside1ab");
        oneforms.style.display="none";
        twoforms.style.display="block";
    }
    function fun1(){
        threeforms.style.display="block";
        twoforms.style.display="none";
        soutside2as.classList.remove("outside2a");
    }
    function fun2(){
        document.getElementById("android").style.display="none";;
    }
    function fun3(){
        document.getElementById("android").style.display="block";;
    }

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
                     alert('成功');
                 }else {
                     alert('失败');
                 }
             }
         })
     });

     $("#startrec").click(function(){
        var flag = $("#startrec").val();
        if(flag=="s"){
          $("#startrec").text("结束录制");
          $("#startrec").val("e");
          $("#title").empty();
          var tbBody="<br><td><font color='red'><strong>录制已开始，请通过左侧手机屏幕进行操作</strong></font></td>";
          $("#title").append(tbBody);
          setRecordScript(true);
        }
        if(flag=="e"){
          $("#startrec").text("开始录制");
          $("#startrec").val("s");
          $("#title").empty();
          var tbBody="<br><td><font color='red'><strong>录制已停止，请检查脚本内容，如需继续录制点击开始录制，如不需录制点击下一步按钮</strong></font></td>"
          $("#title").append(tbBody);
          setRecordScript(false);
        }
     });


     $("#insertsleep").live("click",function(){
         var sleeptime = $("#sleeptime").val();
         sleeptime = stripscript(sleeptime);
         var tbBody="<tr id='step"+scriptnumber+"'><td>等待"+sleeptime+"秒</td><td><button id='del"+scriptnumber+"' onclick='delscript("+scriptnumber+");'>删除</button></td></tr>"
         $("#script").append(tbBody);
         scriptnumber++;
         testscript +="w "+sleeptime+"\n";
         testscriptcn +="等待"+sleeptime+"秒|";
     });

  $("#inserttext").live("click",function(){
         var inputtext = $("#inputtext").val();
         inputtext = stripscript(inputtext);
         var tbBody="<tr id='step"+scriptnumber+"'><td>输入文本"+inputtext+"</td><td><button id='del"+scriptnumber+"' onclick='delscript("+scriptnumber+");'>删除</button></td></tr>"
         $("#script").append(tbBody);
         scriptnumber++;
         testscript +="i "+inputtext+"\n";
         testscriptcn +="输入文本"+inputtext+"|";
     });

       $("#snapshot").live("click",function(){
              var tbBody="<tr id='step"+scriptnumber+"'><td>截屏操作</td><td><button id='del"+scriptnumber+"' onclick='delscript("+scriptnumber+");'>删除</button></td></tr>"
              $("#script").append(tbBody);
              scriptnumber++;
              testscript +="snapshot\n";
              testscriptcn +="截屏操作|";
          });


     function delscript(scriptnumber){

        var scriptno = "step"+scriptnumber;
        $("#"+scriptno).remove();

     }

     $("#nextBtn").click(function(){
        setRecordScript(false);
        testscriptarray = testscriptcn.split("|");
        var i = 0;
         $("#scriptrespre").append("<table id='scriptres' name='scriptres'>");
        for(var i=0;i<testscriptarray.length-1;i++){
            $("#scriptres").append("<tr><td>"+testscriptarray[i]+"</td></tr>");
        }
     });

      $("#playback").click(function(){
              $.ajax({
                  url:'<%=basePath%>/playback',
                  type:'get',
                  data: {
                            "testscript":testscript,
                            "sn":'${deviceid}'
                        },
                  success:function(res){
                      console.log(res.data);
                      if(res.data["code"]=="200"){
                          alert('成功');
                      }else {
                          alert('失败');
                      }
                  }
              })
          });

        $("#savescript").click(function(){
            var scriptname = $("#scriptname").val();
                    $.ajax({
                        url:'<%=basePath%>/savescript',
                        type:'get',
                        data: {
                                  "scriptname":scriptname,
                                  "testscript":testscript,
                                  "userid":'${userid}',
                                  "deviceid":'${deviceid}'
                              },
                        success:function(res){
                            $("#savescriptres").append("<font color='red'>脚本文件保存成功</font>");
                        }
                    })
                });


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
<script>
function stripscript(s)
{
var pattern = new RegExp("[%--`~!@#$^&*()=|{}':;',\\[\\].<>/?~！@#￥……&*（）――|{}【】‘；：”“'。，、？]")        //格式 RegExp("[在中间定义特殊过滤字符]")
var rs = "";
for (var i = 0; i < s.length; i++) {
 rs = rs+s.substr(i,1).replace(pattern,'');
}
return rs;
}
</script>
</html>


