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

    <script src="https://cdn.bootcss.com/html2canvas/0.5.0-beta4/html2canvas.min.js"></script>

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
                <h3 style="margin-left: 10%; margin-top: 0.8%">您正在图像识别脚本录制页面</h3>
                <br>
            </td>
        </tr>
    </table>
</div>
<div  style="float:left; width: 100%">
    <table style="width: 100%">
        <tr>
            <td style="width: 25%;">
                <div id="cantox" style="text-align: center; background-color: #EAEAEA;">
                    <canvas id="phone-screen" style="display: block"></canvas>
                    <canvas id="phone" style="display: none;"></canvas>
                </div>
                <div id="cliptox" style="position: absolute;display:none"></div>
                <div style="text-align: center; background-color: #EAEAEA">
                    <button style="height: 45px; width: 20%; background: url('assets/images/back.jpg') no-repeat; background-size: 100% 100%"></button>&nbsp;
                    <button style="height: 45px;width: 20%; background: url('assets/images/home1.jpg') no-repeat; background-size: 100% 100%"></button>&nbsp;
                    <button style="height: 45px; width: 20%; background: url('assets/images/kill.jpg') no-repeat; background-size: 100% 100%"></button>
                </div>
            </td>
            <td style="width: 1%; border-left: #B0E0E6 solid 2px;"></td>
            <td style="width: 58%">
                <table style="width: 100%">
                    <div class="rebinding-box" style="margin-right: 2%;">
                        <div class="step-body" id="myStep">
                            <div class="step-header" style="margin-bottom: 10%">
                                <ul>
                                    <li><p>安装APP</p></li>
                                    <li><p>脚本录制与回放</p></li>
                                    <li><p>脚本保存</p></li>
                                </ul>
                            </div>
                            <div class="step-content" style="height: 750px; background-color: #EAEAEA; ">
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
                                <div id="recordscriptdiv" class="step-list" style="height: 750px; overflow:auto; margin-left: 1%; text-align: center; width: 100%">
                                    <br>
                                    <font color="#d2691e"><strong>
                                        注： 请准备好后点击下面按钮开始录制，录制完成后请点击结束录制按钮</strong></font>
                                    <button  id="startrec" name="startrec" value="s" style="background-color: #0e8f9f; width: 80px; height: 30px; color: white; margin-left: 2%">开始录制</button>
                                    <div id="title" name="title" style="width: 100%"></div>
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
                            <br>
                            <button style='margin-left: 1%; background-color: #0e8f9f; width: 100px; height: 25px; color: white;' id='btnclip' name='btnclip'>截图确认</button>
                            <br><br>
                            <button style='margin-left: 1%; background-color: #0e8f9f; width: 100px; height: 25px; color: white;' id='touchbtn' name='touchbtn'>点击</button>
                            <br><br>
                            <button style='margin-left: 1%; background-color: #0e8f9f; width: 100px; height: 25px; color: white;' id='swipebtn' name='swipebtn'>滑动</button>
                            <br><br>
                            <button style='margin-left: 1%; background-color: #0e8f9f; width: 100px; height: 25px; color: white;' id='trueassertbtn' name='trueassertbtn'>存在检查点</button>
                            <br><br>
                            <button style='margin-left: 1%; background-color: #0e8f9f; width: 100px; height: 25px; color: white;' id='falseassertbtn' name='falseassertbtn'>不存在检查点</button>
                            <br><br>
                            <button style='margin-left: 1%; background-color: #0e8f9f; width: 100px; height: 25px; color: white;' id='waitbtn' name='waitbtn'>等待</button>
                            <br><br>
                            <button style='margin-left: 1%; background-color: #0e8f9f; width: 100px; height: 25px; color: white;' id='inputbtn' name='inputbtn'>文本输入</button>
                            <br><br>
                            <button style='margin-left: 1%; background-color: #0e8f9f; width: 100px; height: 25px; color: white;' id='sleepbtn' name='sleepbtn'>睡眠</button>
                            <br><br>
                            <button style='margin-left: 1%; background-color: #0e8f9f; width: 100px; height: 25px; color: white;' id='snapshotbtn' name='snapshotbtn'>截图</button>
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

     var scripttext = new Map();
     var scriptid = "";
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

    //点击操作处理
    $("#touchbtn").click(function (){
      if(scriptid==""){
        alert("请点击开始录制按钮");
      }else{
        var canvas = document.getElementById("phone-screen");
        var cantox = document.getElementById("cantox");
        var cliptox = document.getElementById("cliptox");
        var btnclip = document.getElementById("btnclip");
        //获取到canvas元素
        var oldcanvas = document.getElementById("phone");
        oldcanvas.width = 440;
        oldcanvas.height = 860;
        var nowcanvas = "";
        //获取canvas中的画图环境
        var oldcontext = oldcanvas.getContext('2d');
        var nowcontext = "";
        var img = new Image();
        img.setAttribute('crossOrigin', 'Anonymous');
        img.src=canvas.toDataURL('image/jpg');
        img.onload = function(){
            oldcontext.drawImage(img,0,0,oldcanvas.width,oldcanvas.height);
        }

        document.getElementById("phone-screen").style.display="none";
        document.getElementById("phone").style.display="block";
        var scriptnumber = 0;
        var optype = "";
        var swipenumber = "0";
        if ( $("#script").find("tr").length == 0 ) {
            $("#recordscriptdiv").append("<table id='script' name='script'>");
            scriptnumber = 0;
        }else{
            var contrTemp=[];
            var a=document.getElementById("script").getElementsByTagName("tr")
            // var tab = document.getElementById("script");
            // var rows = tab.rows.length;
            // alert(a.length+"---"+rows);
            for (var i=0;i<a.length;i++) {
                contrTemp.push(a[i].id.substring(8));
            }
            //alert(a.length);
            var len = contrTemp.length;
            contrTemp = contrTemp.sort(function(a, b){return a - b});
            var trId = contrTemp[len-1];
            scriptnumber = parseInt(trId);
            //alert(scriptnumber);
        }

        var startPoint;//截取图像开始坐标
        var endPoint;//截图图像的结束坐标
        var w; //截取图像的宽度
        var h; //截取图像的高度
        var flag = false; //用于判断移动事件事物控制
        //鼠标按下事件
        cantox.onmousedown = function (e){
            flag = true;
            cliptox.style.display = 'block';
            startPoint = windowToCanvas(oldcanvas, e.clientX, e.clientY);
            cliptox.style.left = startPoint.x+'px';
            cliptox.style.top = startPoint.y+90+'px';
        }

        //鼠标移动事件
        cantox.onmousemove = function (e){
            if(flag){
                cliptox.style.background = 'rgba(0,0,0,0.5)';
                endPoint = windowToCanvas(oldcanvas, e.clientX, e.clientY);
                w = endPoint.x - startPoint.x;
                h = endPoint.y - startPoint.y;
                cliptox.style.width = w +'px';
                cliptox.style.height = h+'px';
            }
        }
        //鼠标释放事件
        cantox.onmouseup = function (e){
            flag = false;
        }
        //alert(scriptnumber);
        scriptnumber = parseInt(scriptnumber)+1;

        $("#script").append("<tr id='scripttd"+scriptnumber+"'><td>点击操作</td></tr>");
        optype = "1";

        //按钮截取事件
        btnclip.onclick = function (){
            $("#scripttd"+scriptnumber).append("<td colspan='3'><canvas id='nowcanvas"+scriptnumber+"' width='"+w+"' height='"+h+"' ></td><td><button id='del"+scriptnumber+"' onclick='delscript("+scriptnumber+");'>删除</button></td><td>" +
                "<select name='autotestip"+scriptnumber+"' id='autotestip"+scriptnumber+"'>" +
                "<option value='0' Selected>请选择</option>" +
                "<option value='1'>点击</option>" +
                "<option value='2'>滑动</option>" +
                "<option value='3'>存在检查点</option>" +
                "<option value='4'>不存在检查点</option>" +
                "<option value='5'>等待</option>" +
                "<option value='6'>文本输入</option>" +
                "<option value='7'>睡眠</option>" +
                "<option value='8'>截图</option>" +
                "</select><button id='ins"+scriptnumber+"' onclick='insscript("+scriptnumber+");'>插入</button></td> ");

            nowcanvas = document.getElementById("nowcanvas"+scriptnumber);
            nowcontext = nowcanvas.getContext('2d');
            imgCut(nowcontext,img,oldcanvas.width,oldcanvas.height,startPoint.x,startPoint.y,w,h);
            document.getElementById("phone-screen").style.display="block";
            document.getElementById("phone").style.display="none";
            picname = Date.now();
            canvaSavePng(nowcanvas,picname);
            touchscript = "touch(dev,Template(r'"+picname+".png', resolution=(${screenw}, ${screenh})))";
            addscript(touchscript,scriptnumber);
        }
      }
    });


    $("#swipebtn").click(function (){
      if(scriptid==""){
            alert("请点击开始录制按钮");
      }else{
        var canvas = document.getElementById("phone-screen");
        var cantox = document.getElementById("cantox");
        var cliptox = document.getElementById("cliptox");
        var btnclip = document.getElementById("btnclip");
        //获取到canvas元素
        var oldcanvas = document.getElementById("phone");
        oldcanvas.width = 440;
        oldcanvas.height = 860;
        var nowcanvas = "";
        //获取canvas中的画图环境
        var oldcontext = oldcanvas.getContext('2d');
        var nowcontext = "";
        var img = new Image();
        img.setAttribute('crossOrigin', 'Anonymous');
        img.src=canvas.toDataURL('image/jpg');
        img.onload = function(){
            oldcontext.drawImage(img,0,0,oldcanvas.width,oldcanvas.height);
        }

        document.getElementById("phone-screen").style.display="none";
        document.getElementById("phone").style.display="block";

        var scriptnumber = 0 ;
        var optype = "";
        var swipenumber = "0";
        var swipescript = "";
        if ( $("#script").find("tr").length == 0 ) {
            $("#recordscriptdiv").append("<table id='script' name='script'>");
            scriptnumber = 0;
        }else{
            var contrTemp=[];
            var a=document.getElementById("script").getElementsByTagName("tr")
            for (var i=0;i<a.length;i++) {
                contrTemp.push(a[i].id.substring(8));
            }
            //alert(a.length);
            var len = contrTemp.length;
            contrTemp = contrTemp.sort(function(a, b){return a - b});
            var trId = contrTemp[len-1];
            scriptnumber = parseInt(trId);
        }

        var startPoint;//截取图像开始坐标
        var endPoint;//截图图像的结束坐标
        var w; //截取图像的宽度
        var h; //截取图像的高度
        var flag = false; //用于判断移动事件事物控制
        //鼠标按下事件
        cantox.onmousedown = function (e){
            flag = true;
            cliptox.style.display = 'block';
            startPoint = windowToCanvas(oldcanvas, e.clientX, e.clientY);
            cliptox.style.left = startPoint.x+'px';
            cliptox.style.top = startPoint.y+90+'px';
        }

        //鼠标移动事件
        cantox.onmousemove = function (e){
            if(flag){
                cliptox.style.background = 'rgba(0,0,0,0.5)';
                endPoint = windowToCanvas(oldcanvas, e.clientX, e.clientY);
                w = endPoint.x - startPoint.x;
                h = endPoint.y - startPoint.y;
                cliptox.style.width = w +'px';
                cliptox.style.height = h+'px';
            }
        }
        //鼠标释放事件
        cantox.onmouseup = function (e){
            flag = false;
        }

        scriptnumber = parseInt(scriptnumber)+1;
        $("#script").append("<tr id='scripttd"+scriptnumber+"'><td>滑动操作</td></tr>");
        optype = "2";

        //按钮截取事件
        btnclip.onclick = function (){
            if(optype =="2"){
                if(swipenumber=="0"){
                    $("#scripttd"+scriptnumber).append("<td><canvas id='nowcanvas"+scriptnumber+"' width='"+w+"' height='"+h+"' ></canvas></td><td>到</td>");
                    nowcanvas = document.getElementById("nowcanvas"+scriptnumber);
                    nowcontext = nowcanvas.getContext('2d');
                    imgCut(nowcontext,img,oldcanvas.width,oldcanvas.height,startPoint.x,startPoint.y,w,h);
                    swipenumber = "1";
                    picname = Date.now();
                    canvaSavePng(nowcanvas,picname);
                    swipescript = "swipe(dev,Template(r'"+picname+".png', resolution=(${screenw}, ${screenh})),";
                }else{
                    $("#scripttd"+scriptnumber).append("<td><canvas id='nowcanvas"+scriptnumber+"-1' width='"+w+"' height='"+h+"' ></td><td><button id='del"+scriptnumber+"' onclick='delscript("+scriptnumber+");'>删除</button></td><td>" +
                        "<select name='autotestip"+scriptnumber+"' id='autotestip"+scriptnumber+"'>" +
                        "<option value='0' Selected>请选择</option>" +
                        "<option value='1'>点击</option>" +
                        "<option value='2'>滑动</option>" +
                        "<option value='3'>存在检查点</option>" +
                        "<option value='4'>不存在检查点</option>" +
                        "<option value='5'>等待</option>" +
                        "<option value='6'>文本输入</option>" +
                        "<option value='7'>睡眠</option>" +
                        "<option value='8'>截图</option>" +
                        "</select><button id='ins"+scriptnumber+"' onclick='insscript("+scriptnumber+");'>插入</button></td> ");

                    nowcanvas = document.getElementById("nowcanvas"+scriptnumber+"-1");
                    nowcontext = nowcanvas.getContext('2d');
                    imgCut(nowcontext,img,oldcanvas.width,oldcanvas.height,startPoint.x,startPoint.y,w,h);
                    swipenumber = 0;
                    document.getElementById("phone-screen").style.display="block";
                    document.getElementById("phone").style.display="none";
                    picname = Date.now();
                    canvaSavePng(nowcanvas,picname);
                    swipescript += "Template(r'"+picname+".png', resolution=(${screenw}, ${screenh})))";
                    addscript(swipescript,scriptnumber);
                }
            }
        }
      }
    });

    $("#trueassertbtn").click(function (){

     if(scriptid==""){
                alert("请点击开始录制按钮");
     }else{
        var canvas = document.getElementById("phone-screen");
        var cantox = document.getElementById("cantox");
        var cliptox = document.getElementById("cliptox");
        var btnclip = document.getElementById("btnclip");
        //获取到canvas元素
        var oldcanvas = document.getElementById("phone");
        oldcanvas.width = 440;
        oldcanvas.height = 860;
        var nowcanvas = "";
        //获取canvas中的画图环境
        var oldcontext = oldcanvas.getContext('2d');
        var nowcontext = "";
        var img = new Image();
        img.setAttribute('crossOrigin', 'Anonymous');
        img.src=canvas.toDataURL('image/jpg');
        img.onload = function(){
            oldcontext.drawImage(img,0,0,oldcanvas.width,oldcanvas.height);
        }

        document.getElementById("phone-screen").style.display="none";
        document.getElementById("phone").style.display="block";

        var scriptnumber = 0;
        var optype = "";
        var swipenumber = "0";

        if ( $("#script").find("tr").length == 0 ) {
            $("#recordscriptdiv").append("<table id='script' name='script'>");
            scriptnumber = 0;
        }else{
            var contrTemp=[];
            var a=document.getElementById("script").getElementsByTagName("tr")
            for (var i=0;i<a.length;i++) {
                contrTemp.push(a[i].id.substring(8));
            }
            //alert(a.length);
            var len = contrTemp.length;
            contrTemp = contrTemp.sort(function(a, b){return a - b});
            var trId = contrTemp[len-1];
            scriptnumber = parseInt(trId);
        }

        var startPoint;//截取图像开始坐标
        var endPoint;//截图图像的结束坐标
        var w; //截取图像的宽度
        var h; //截取图像的高度
        var flag = false; //用于判断移动事件事物控制
        //鼠标按下事件
        cantox.onmousedown = function (e){
            flag = true;
            cliptox.style.display = 'block';
            startPoint = windowToCanvas(oldcanvas, e.clientX, e.clientY);
            cliptox.style.left = startPoint.x+'px';
            cliptox.style.top = startPoint.y+90+'px';
        }

        //鼠标移动事件
        cantox.onmousemove = function (e){
            if(flag){
                cliptox.style.background = 'rgba(0,0,0,0.5)';
                endPoint = windowToCanvas(oldcanvas, e.clientX, e.clientY);
                w = endPoint.x - startPoint.x;
                h = endPoint.y - startPoint.y;
                cliptox.style.width = w +'px';
                cliptox.style.height = h+'px';
            }
        }
        //鼠标释放事件
        cantox.onmouseup = function (e){
            flag = false;
        }

        scriptnumber = parseInt(scriptnumber)+1;
        $("#script").append("<tr id='scripttd"+scriptnumber+"'><td>检查点</td><td>存在</td></tr>");
        optype = "3";

        //按钮截取事件
        btnclip.onclick = function (){
            if(optype =="1"||optype=="3"||optype=="4"){
                $("#scripttd"+scriptnumber).append("<td colspan='2'><canvas id='nowcanvas"+scriptnumber+"' width='"+w+"' height='"+h+"' ></td><td><button id='del"+scriptnumber+"' onclick='delscript("+scriptnumber+");'>删除</button></td><td>" +
                    "<select name='autotestip"+scriptnumber+"' id='autotestip"+scriptnumber+"'>" +
                    "<option value='0' Selected>请选择</option>" +
                    "<option value='1'>点击</option>" +
                    "<option value='2'>滑动</option>" +
                    "<option value='3'>存在检查点</option>" +
                    "<option value='4'>不存在检查点</option>" +
                    "<option value='5'>等待</option>" +
                    "<option value='6'>文本输入</option>" +
                    "<option value='7'>睡眠</option>" +
                    "<option value='8'>截图</option>" +
                    "</select><button id='ins"+scriptnumber+"' onclick='insscript("+scriptnumber+");'>插入</button></td> ");

                nowcanvas = document.getElementById("nowcanvas"+scriptnumber);
                nowcontext = nowcanvas.getContext('2d');
                imgCut(nowcontext,img,oldcanvas.width,oldcanvas.height,startPoint.x,startPoint.y,w,h);
                picname = Date.now();
                canvaSavePng(nowcanvas,picname);
                assertexit = "assert_exists(dev,Template(r'"+picname+".png',  resolution=(${screenw}, ${screenh})), '')";
                addscript(assertexit,scriptnumber);
            }
            document.getElementById("phone-screen").style.display="block";
            document.getElementById("phone").style.display="none";
        }
     }
    });

    $("#falseassertbtn").click(function (){
      if(scriptid==""){
            alert("请点击开始录制按钮");
      }else{
        var canvas = document.getElementById("phone-screen");
        var cantox = document.getElementById("cantox");
        var cliptox = document.getElementById("cliptox");
        var btnclip = document.getElementById("btnclip");
        //获取到canvas元素
        var oldcanvas = document.getElementById("phone");
        oldcanvas.width = 440;
        oldcanvas.height = 860;
        var nowcanvas = "";
        //获取canvas中的画图环境
        var oldcontext = oldcanvas.getContext('2d');
        var nowcontext = "";
        var img = new Image();
        img.setAttribute('crossOrigin', 'Anonymous');
        img.src=canvas.toDataURL('image/jpg');
        img.onload = function(){
            oldcontext.drawImage(img,0,0,oldcanvas.width,oldcanvas.height);
        }

        //alert(oldcanvas.width+"   "+oldcanvas.height);
        document.getElementById("phone-screen").style.display="none";
        document.getElementById("phone").style.display="block";

        var scriptnumber ;
        var optype = "";
        var swipenumber = "0";

        if ( $("#script").find("tr").length == 0 ) {
            $("#recordscriptdiv").append("<table id='script' name='script'>");
            scriptnumber = 0;
        }else{
            var contrTemp=[];
            var a=document.getElementById("script").getElementsByTagName("tr")
            for (var i=0;i<a.length;i++) {
                contrTemp.push(a[i].id.substring(8));
            }
            //alert(a.length);
            var len = contrTemp.length;
            contrTemp = contrTemp.sort(function(a, b){return a - b});
            var trId = contrTemp[len-1];
            scriptnumber = parseInt(trId);
        }

        var startPoint;//截取图像开始坐标
        var endPoint;//截图图像的结束坐标
        var w; //截取图像的宽度
        var h; //截取图像的高度
        var flag = false; //用于判断移动事件事物控制
        //鼠标按下事件
        cantox.onmousedown = function (e){
            flag = true;
            cliptox.style.display = 'block';
            startPoint = windowToCanvas(oldcanvas, e.clientX, e.clientY);
            cliptox.style.left = startPoint.x+'px';
            cliptox.style.top = startPoint.y+90+'px';
        }

        //鼠标移动事件
        cantox.onmousemove = function (e){
            if(flag){
                cliptox.style.background = 'rgba(0,0,0,0.5)';
                endPoint = windowToCanvas(oldcanvas, e.clientX, e.clientY);
                w = endPoint.x - startPoint.x;
                h = endPoint.y - startPoint.y;
                cliptox.style.width = w +'px';
                cliptox.style.height = h+'px';
            }
        }
        //鼠标释放事件
        cantox.onmouseup = function (e){
            flag = false;
        }

        scriptnumber = parseInt(scriptnumber)+1;
        $("#script").append("<tr id='scripttd"+scriptnumber+"'><td>检查点</td><td>不存在</td></tr>");
        optype = "4";

        //按钮截取事件
        btnclip.onclick = function (){
            if(optype =="1"||optype=="3"||optype=="4"){
                $("#scripttd"+scriptnumber).append("<td colspan='2'><canvas id='nowcanvas"+scriptnumber+"' width='"+w+"' height='"+h+"' ></td><td><button id='del"+scriptnumber+"' onclick='delscript("+scriptnumber+");'>删除</button></td><td>" +
                    "<select name='autotestip"+scriptnumber+"' id='autotestip"+scriptnumber+"'>" +
                    "<option value='0' Selected>请选择</option>" +
                    "<option value='1'>点击</option>" +
                    "<option value='2'>滑动</option>" +
                    "<option value='3'>存在检查点</option>" +
                    "<option value='4'>不存在检查点</option>" +
                    "<option value='5'>等待</option>" +
                    "<option value='6'>文本输入</option>" +
                    "<option value='7'>睡眠</option>" +
                    "<option value='8'>截图</option>" +
                    "</select><button id='ins"+scriptnumber+"' onclick='insscript("+scriptnumber+");'>插入</button></td> ");

                nowcanvas = document.getElementById("nowcanvas"+scriptnumber);
                nowcontext = nowcanvas.getContext('2d');
                imgCut(nowcontext,img,oldcanvas.width,oldcanvas.height,startPoint.x,startPoint.y,w,h);
                picname = Date.now();
                canvaSavePng(nowcanvas,picname);
                assertnotexit = "assert_not_exists(dev,Template(r'"+picname+".png',  resolution=(${screenw}, ${screenh})), '')";
                addscript(assertnotexit,scriptnumber);
            }
            document.getElementById("phone-screen").style.display="block";
            document.getElementById("phone").style.display="none";
        }
      }
    });
    $("#waitbtn").click(function (){

      if(scriptid==""){
            alert("请点击开始录制按钮");
      }else{
        var canvas = document.getElementById("phone-screen");
        var cantox = document.getElementById("cantox");
        var cliptox = document.getElementById("cliptox");
        var btnclip = document.getElementById("btnclip");
        //获取到canvas元素
        var oldcanvas = document.getElementById("phone");
        oldcanvas.width = 440;
        oldcanvas.height = 860;
        var nowcanvas = "";
        //获取canvas中的画图环境
        var oldcontext = oldcanvas.getContext('2d');
        var nowcontext = "";
        var img = new Image();
        img.setAttribute('crossOrigin', 'Anonymous');
        img.src=canvas.toDataURL('image/jpg');
        img.onload = function(){
            oldcontext.drawImage(img,0,0,oldcanvas.width,oldcanvas.height);
        }

        //alert(oldcanvas.width+"   "+oldcanvas.height);
        document.getElementById("phone-screen").style.display="none";
        document.getElementById("phone").style.display="block";

        var scriptnumber ;
        var optype = "";
        var swipenumber = "0";

        if ( $("#script").find("tr").length == 0 ) {
            $("#recordscriptdiv").append("<table id='script' name='script'>");
            scriptnumber = 0;
        }else{
            var contrTemp=[];
            var a=document.getElementById("script").getElementsByTagName("tr")
            for (var i=0;i<a.length;i++) {
                contrTemp.push(a[i].id.substring(8));
            }
            //alert(a.length);
            var len = contrTemp.length;
            contrTemp = contrTemp.sort(function(a, b){return a - b});
            var trId = contrTemp[len-1];
            scriptnumber = parseInt(trId);
        }

        var startPoint;//截取图像开始坐标
        var endPoint;//截图图像的结束坐标
        var w; //截取图像的宽度
        var h; //截取图像的高度
        var flag = false; //用于判断移动事件事物控制
        //鼠标按下事件
        cantox.onmousedown = function (e){
            flag = true;
            cliptox.style.display = 'block';
            startPoint = windowToCanvas(oldcanvas, e.clientX, e.clientY);
            cliptox.style.left = startPoint.x+'px';
            cliptox.style.top = startPoint.y+90+'px';
        }

        //鼠标移动事件
        cantox.onmousemove = function (e){
            if(flag){
                cliptox.style.background = 'rgba(0,0,0,0.5)';
                endPoint = windowToCanvas(oldcanvas, e.clientX, e.clientY);
                w = endPoint.x - startPoint.x;
                h = endPoint.y - startPoint.y;
                cliptox.style.width = w +'px';
                cliptox.style.height = h+'px';
            }
        }
        //鼠标释放事件
        cantox.onmouseup = function (e){
            flag = false;
        }

        scriptnumber = parseInt(scriptnumber)+1;
        $("#script").append("<tr id='scripttd"+scriptnumber+"'><td>等待</td></tr>");
        optype = "5";

        //按钮截取事件
        btnclip.onclick = function (){
            if(optype =="1"||optype=="3"||optype=="4"||optype=="5"){
                $("#scripttd"+scriptnumber).append("<td><canvas id='nowcanvas"+scriptnumber+"' width='"+w+"' height='"+h+"' ></td><td colspan='2'><input id='waitsecond"+scriptnumber+"' onBlur='waitblur("+scriptnumber+")' autofocus='autofocus'>秒</td><td><button id='del"+scriptnumber+"' onclick='delscript("+scriptnumber+");'>删除</button></td><td>" +
                    "<select name='autotestip"+scriptnumber+"' id='autotestip"+scriptnumber+"'>" +
                    "<option value='0' Selected>请选择</option>" +
                    "<option value='1'>点击</option>" +
                    "<option value='2'>滑动</option>" +
                    "<option value='3'>存在检查点</option>" +
                    "<option value='4'>不存在检查点</option>" +
                    "<option value='5'>等待</option>" +
                    "<option value='6'>文本输入</option>" +
                    "<option value='7'>睡眠</option>" +
                    "<option value='8'>截图</option>" +
                    "</select><button id='ins"+scriptnumber+"' onclick='insscript("+scriptnumber+");'>插入</button></td> ");

                nowcanvas = document.getElementById("nowcanvas"+scriptnumber);
                nowcontext = nowcanvas.getContext('2d');
                imgCut(nowcontext,img,oldcanvas.width,oldcanvas.height,startPoint.x,startPoint.y,w,h);
                picname = Date.now();
                canvaSavePng(nowcanvas,picname);
                waitscript="wait(dev,Template(r'"+picname+"'.png', resolution=(${screenw}, ${screenh}))";
                addscript(waitscript,scriptnumber);
            }
            document.getElementById("phone-screen").style.display="block";
            document.getElementById("phone").style.display="none";
        }
      }
    });
    $("#inputbtn").click(function (){
      if(scriptid==""){
            alert("请点击开始录制按钮");
      }else{
        var scriptnumber ;

        if ( $("#script").find("tr").length == 0 ) {
            $("#recordscriptdiv").append("<table id='script' name='script'>");
            scriptnumber = 0;
        }else{
            var contrTemp=[];
            var a=document.getElementById("script").getElementsByTagName("tr")
            for (var i=0;i<a.length;i++) {
                contrTemp.push(a[i].id.substring(8));
            }
            //alert(a.length);
            var len = contrTemp.length;
            contrTemp = contrTemp.sort(function(a, b){return a - b});
            var trId = contrTemp[len-1];
            scriptnumber = parseInt(trId);
        }

        scriptnumber = scriptnumber+1;
        $("#script").append("<tr id='scripttd"+scriptnumber+"'><td>输入文本</td><td colspan='3'><input id='inputtext"+scriptnumber+"'  autofocus='autofocus' onBlur='inputblur("+scriptnumber+")'></td><td><button id='del"+scriptnumber+"' onclick='delscript("+scriptnumber+");'>删除"+
            "<td><select name='autotestip"+scriptnumber+"' id='autotestip"+scriptnumber+"'>" +
            "<option value='0' Selected>请选择</option>" +
            "<option value='1'>点击</option>" +
            "<option value='2'>滑动</option>" +
            "<option value='3'>存在检查点</option>" +
            "<option value='4'>不存在检查点</option>" +
            "<option value='5'>等待</option>" +
            "<option value='6'>文本输入</option>" +
            "<option value='7'>睡眠</option>" +
            "<option value='8'>截图</option>" +
            "</select><button id='ins"+scriptnumber+"' onclick='insscript("+scriptnumber+");'>插入</button></td> ");

          inputscript = "text(dev,";
        addscript(inputscript,scriptnumber);
      }
    });


      $("#sleepbtn").click(function (){
        if(scriptid==""){
            alert("请点击开始录制按钮");
        }else{
            var scriptnumber ;

            if ( $("#script").find("tr").length == 0 ) {
                $("#recordscriptdiv").append("<table id='script' name='script'>");
                scriptnumber = 0;
            }else{
                var contrTemp=[];
                var a=document.getElementById("script").getElementsByTagName("tr")
                for (var i=0;i<a.length;i++) {
                    contrTemp.push(a[i].id.substring(8));
                }
                //alert(a.length);
                var len = contrTemp.length;
                contrTemp = contrTemp.sort(function(a, b){return a - b});
                var trId = contrTemp[len-1];
                scriptnumber = parseInt(trId);
            }

            scriptnumber = scriptnumber+1;
            $("#script").append("<tr id='scripttd"+scriptnumber+"'><td>睡眠</td><td colspan='3'><input id='sleepsecond"+scriptnumber+"' autofocus='autofocus' onBlur='sleepblur("+scriptnumber+")'>秒</td><td><button id='del"+scriptnumber+"' onclick='delscript("+scriptnumber+");'>删除"+
                "<td><select name='autotestip"+scriptnumber+"' id='autotestip"+scriptnumber+"'>" +
                "<option value='0' Selected>请选择</option>" +
                "<option value='1'>点击</option>" +
                "<option value='2'>滑动</option>" +
                "<option value='3'>存在检查点</option>" +
                "<option value='4'>不存在检查点</option>" +
                "<option value='5'>等待</option>" +
                "<option value='6'>文本输入</option>" +
                "<option value='7'>睡眠</option>" +
                "<option value='8'>截图</option>" +
                "</select><button id='ins"+scriptnumber+"' onclick='insscript("+scriptnumber+");'>插入</button></td> ");

            sleepscript = "sleep(";
            addscript(sleepscript,scriptnumber);
        }
      });

      $("#snapshotbtn").click(function(){
          if(scriptid==""){
                  alert("请点击开始录制按钮");
          }else{
            var scriptnumber ;
            if ( $("#script").find("tr").length == 0 ) {
                $("#recordscriptdiv").append("<table id='script' name='script'>");
                   scriptnumber = 0;
            }else{
                var contrTemp=[];
                var a=document.getElementById("script").getElementsByTagName("tr")
                for (var i=0;i<a.length;i++) {
                    contrTemp.push(a[i].id.substring(8));
                }
                //alert(a.length);
                var len = contrTemp.length;
                contrTemp = contrTemp.sort(function(a, b){return a - b});
                var trId = contrTemp[len-1];
                scriptnumber = parseInt(trId);
            }
            scriptnumber = scriptnumber+1;
              $("#script").append("<tr id='scripttd"+scriptnumber+"'><td>截图操作</td><td colspan='4'><button id='del"+scriptnumber+"' onclick='delscript("+scriptnumber+");'>删除"+
                "<td><select name='autotestip"+scriptnumber+"' id='autotestip"+scriptnumber+"'>" +
                "<option value='0' Selected>请选择</option>" +
                "<option value='1'>点击</option>" +
                "<option value='2'>滑动</option>" +
                "<option value='3'>存在检查点</option>" +
                "<option value='4'>不存在检查点</option>" +
                "<option value='5'>等待</option>" +
                "<option value='6'>文本输入</option>" +
                "<option value='7'>睡眠</option>" +
                "<option value='8'>截图</option>" +
                "</select><button id='ins"+scriptnumber+"' onclick='insscript("+scriptnumber+");'>插入</button></td> ");

            snapshotscript = "screennum = getscreen(dev, deviceid, screennum, apkonlynumber, taskId, onlyDistinction)";
            addscript(snapshotscript,scriptnumber);
          }
    });

    function waitblur(scriptnumber){
        waittimeid = "#waitsecond"+scriptnumber;
        waitsecond = $(waittimeid).val();
        if(waitsecond==""){
            str = scripttext.get(scriptnumber) +")";
            scripttext.set(scriptnumber,str);
            console.log(scripttext);
        }else{
            str = scripttext.get(scriptnumber) +","+waitsecond+")";
            scripttext.set(scriptnumber,str);
            console.log(scripttext);
        }
    }

    function inputblur(scriptnumber){
            inputtextid = "#inputtext"+scriptnumber;
            inputtext = $(inputtextid).val();
            str = scripttext.get(scriptnumber);
            scripttext.set(scriptnumber,str+inputtext+",False)");
            console.log(scripttext);
    }

   function sleepblur(scriptnumber){
            sleepsecondid = "#sleepsecond"+scriptnumber;
            sleepsecond = $(sleepsecondid).val();
            str = scripttext.get(scriptnumber);
            scripttext.set(scriptnumber,str+sleepsecond+")");
            console.log(scripttext);
    }

    function strMapToObj(strMap){
        obj= Object.create(null);
        for (let[k,v] of strMap) {
          obj[k] = v;
        }
        return obj;
      }


    $("#savescript").click(function (){
        scriptextstr = JSON.stringify(strMapToObj(scripttext));
        scriptname = $("#scriptname").val();
        if(scriptname==""){
            alert("请输入脚本名称后保存");
        }else if(scriptid==""){
             alert("请点击开始录制按钮");
        }
        else{
             $.ajax({
                   dataType:"json",
                   contentType:'application/json;charset=UTF-8',
                   url: "<%=basePath%>/savepicturescript",
                   type:'post',
                   data: {"userid": "${userid}",
                          "scriptname":scriptname,
                          "scriptstring":scriptextstr,
                          "scriptid":scriptid
                          },
                   success:function (data)
                   {
                         if(data.code==200){
                            alert("保存脚本成功");
                         }else{
                            alert("保存脚本失败");
                         }
                   }
            });
        }
    });

    $("#startrec").click(function(){
        var flag = $("#startrec").val();
        if(flag=="s"){
            $("#startrec").text("结束录制");
            $("#startrec").val("e");
            $("#title").empty();
            var tbBody="<br><td><font color='red'><strong>录制已开始，请通过左侧手机屏幕进行操作</strong></font></td>";
            $("#title").append(tbBody);
            scriptid = uuid();
        }
        if(flag=="e"){
            $("#startrec").text("开始录制");
            $("#startrec").val("s");
            $("#title").empty();
            var tbBody="<br><td><font color='red'><strong>录制已停止，请检查脚本内容，如需继续录制点击开始录制，如不需录制点击下一步按钮</strong></font><br><br><font color='#d2691e'><strong>点击按钮,请注意回放前调整左侧手机页面到脚本起始位置</strong></font><button id='playback' name='playback'  style='background-color: #0e8f9f; width: 80px; height: 30px; color: white; margin-left: 2%;'>回放脚本</button></td>"
            $("#title").append(tbBody);
        }
    });

    function insscript(scriptnumber){

        //alert(scriptnumber);
        var contrTemp=[];
        var a=document.getElementById("script").getElementsByTagName("tr")
        // for (var i=0;i<a.length;i++) {
        //     contrTemp.push(a[i].id);
        // }
        // var len = contrTemp.length;
        // contrTemp = contrTemp.sort(function(a, b){return a - b});
        // var trId = contrTemp[len-1].substring(8);
        for (var i=0;i<a.length;i++) {
            contrTemp.push(a[i].id.substring(8));
        }
        //alert(a.length);
        var len = contrTemp.length;
        contrTemp = contrTemp.sort(function(a, b){return a - b});
        var trId = contrTemp[len-1];

        var scriptnumber1 = parseInt(trId)+1;
        //scriptnumber1 = scriptnumber1+1;
        var scriptno1 = "scripttd"+scriptnumber1;
        var scriptno = "scripttd"+scriptnumber;
        // var tb = document.getElementById("script");
        var value = $("#autotestip"+scriptnumber).val();
        //alert(scriptno1);
        if(value=="1"){
            $("tr[id='"+scriptno+"']").after("<tr id='"+scriptno1+"'><td>点击操作</td></tr>");
            var canvas = document.getElementById("phone-screen");
            var cantox = document.getElementById("cantox");
            var cliptox = document.getElementById("cliptox");
            var btnclip = document.getElementById("btnclip");
            //获取到canvas元素
            var oldcanvas = document.getElementById("phone");
            oldcanvas.width = 440;
            oldcanvas.height = 860;
            var nowcanvas = "";
            //获取canvas中的画图环境
            var oldcontext = oldcanvas.getContext('2d');
            var nowcontext = "";
            var img = new Image();
            img.setAttribute('crossOrigin', 'Anonymous');
            img.src=canvas.toDataURL('image/jpg');
            img.onload = function(){
                oldcontext.drawImage(img,0,0,oldcanvas.width,oldcanvas.height);
            }
            document.getElementById("phone-screen").style.display="none";
            document.getElementById("phone").style.display="block";

            var startPoint;//截取图像开始坐标
            var endPoint;//截图图像的结束坐标
            var w; //截取图像的宽度
            var h; //截取图像的高度
            var flag = false; //用于判断移动事件事物控制
            //鼠标按下事件
            cantox.onmousedown = function (e){
                flag = true;
                cliptox.style.display = 'block';
                startPoint = windowToCanvas(oldcanvas, e.clientX, e.clientY);
                cliptox.style.left = startPoint.x+'px';
                cliptox.style.top = startPoint.y+90+'px';
            }

            //鼠标移动事件
            cantox.onmousemove = function (e){
                if(flag){
                    cliptox.style.background = 'rgba(0,0,0,0.5)';
                    endPoint = windowToCanvas(oldcanvas, e.clientX, e.clientY);
                    w = endPoint.x - startPoint.x;
                    h = endPoint.y - startPoint.y;
                    cliptox.style.width = w +'px';
                    cliptox.style.height = h+'px';
                }
            }
            //鼠标释放事件
            cantox.onmouseup = function (e){
                flag = false;
            }
            optype = "1";
            //按钮截取事件
            btnclip.onclick = function (){
                $("#scripttd"+scriptnumber1).append("<td colspan='3'><canvas id='nowcanvas"+scriptnumber1+"' width='"+w+"' height='"+h+"' ></td><td><button id='del"+scriptnumber1+"' onclick='delscript("+scriptnumber1+");'>删除</button></td><td>" +
                    "<select name='autotestip"+scriptnumber1+"' id='autotestip"+scriptnumber1+"'>" +
                    "<option value='0' Selected>请选择</option>" +
                    "<option value='1'>点击</option>" +
                    "<option value='2'>滑动</option>" +
                    "<option value='3'>存在检查点</option>" +
                    "<option value='4'>不存在检查点</option>" +
                    "<option value='5'>等待</option>" +
                    "<option value='6'>文本输入</option>" +
                    "<option value='7'>睡眠</option>" +
                    "<option value='8'>截图</option>" +
                    "</select><button id='ins"+scriptnumber1+"' onclick='insscript("+scriptnumber1+");'>插入</button></td> ");

                // var newscript = "<tr id='"+scriptno1+"'><td>点击操作</td>";
                // $("#script").append(newscript);

                nowcanvas = document.getElementById("nowcanvas"+scriptnumber1);
                nowcontext = nowcanvas.getContext('2d');
                imgCut(nowcontext,img,oldcanvas.width,oldcanvas.height,startPoint.x,startPoint.y,w,h);
                document.getElementById("phone-screen").style.display="block";
                document.getElementById("phone").style.display="none";
                picname = Date.now();
                canvaSavePng(nowcanvas,picname);
                touchscript = "touch(dev,Template(r'"+picname+".png', resolution=(${screenw}, ${screenh})))";
                updatescript(touchscript,scriptnumber1,scriptnumber);
                //addscript(touchscript,scriptnumber1);
            }
        }
        if(value=="2"){
            $("tr[id='"+scriptno+"']").after("<tr id='"+scriptno1+"'><td>滑动操作</td></tr>");
            var canvas = document.getElementById("phone-screen");
            var cantox = document.getElementById("cantox");
            var cliptox = document.getElementById("cliptox");
            var btnclip = document.getElementById("btnclip");
            //获取到canvas元素
            var oldcanvas = document.getElementById("phone");
            oldcanvas.width = 440;
            oldcanvas.height = 860;
            var nowcanvas = "";
            //获取canvas中的画图环境
            var oldcontext = oldcanvas.getContext('2d');
            var nowcontext = "";
            var img = new Image();
            img.setAttribute('crossOrigin', 'Anonymous');
            img.src=canvas.toDataURL('image/jpg');
            img.onload = function(){
                oldcontext.drawImage(img,0,0,oldcanvas.width,oldcanvas.height);
            }
            document.getElementById("phone-screen").style.display="none";
            document.getElementById("phone").style.display="block";

            var optype = "";
            var swipenumber = "0";
            var swipescript = "";
            var startPoint;//截取图像开始坐标
            var endPoint;//截图图像的结束坐标
            var w; //截取图像的宽度
            var h; //截取图像的高度
            var flag = false; //用于判断移动事件事物控制
            //鼠标按下事件
            cantox.onmousedown = function (e){
                flag = true;
                cliptox.style.display = 'block';
                startPoint = windowToCanvas(oldcanvas, e.clientX, e.clientY);
                cliptox.style.left = startPoint.x+'px';
                cliptox.style.top = startPoint.y+90+'px';
            }

            //鼠标移动事件
            cantox.onmousemove = function (e){
                if(flag){
                    cliptox.style.background = 'rgba(0,0,0,0.5)';
                    endPoint = windowToCanvas(oldcanvas, e.clientX, e.clientY);
                    w = endPoint.x - startPoint.x;
                    h = endPoint.y - startPoint.y;
                    cliptox.style.width = w +'px';
                    cliptox.style.height = h+'px';
                }
            }
            //鼠标释放事件
            cantox.onmouseup = function (e){
                flag = false;
            }

            optype = "2";

            //按钮截取事件
            btnclip.onclick = function (){
                if(optype =="2"){

                    if(swipenumber=="0"){
                        $("#scripttd"+scriptnumber1).append("<td><canvas id='nowcanvas"+scriptnumber1+"' width='"+w+"' height='"+h+"' ></canvas></td><td>到</td>");
                        nowcanvas = document.getElementById("nowcanvas"+scriptnumber1);
                        nowcontext = nowcanvas.getContext('2d');
                        imgCut(nowcontext,img,oldcanvas.width,oldcanvas.height,startPoint.x,startPoint.y,w,h);
                        swipenumber = "1";
                        picname = Date.now();
                        canvaSavePng(nowcanvas,picname);
                        swipescript = "swipe(dev,Template(r'"+picname+".png', resolution=(${screenw}, ${screenh})),";

                    }else{
                        $("#scripttd"+scriptnumber1).append("<td><canvas id='nowcanvas"+scriptnumber1+"-1' width='"+w+"' height='"+h+"' ></td><td><button id='del"+scriptnumber1+"' onclick='delscript("+scriptnumber1+");'>删除</button></td><td>" +
                            "<select name='autotestip"+scriptnumber1+"' id='autotestip"+scriptnumber1+"'>" +
                            "<option value='0' Selected>请选择</option>" +
                            "<option value='1'>点击</option>" +
                            "<option value='2'>滑动</option>" +
                            "<option value='3'>存在检查点</option>" +
                            "<option value='4'>不存在检查点</option>" +
                            "<option value='5'>等待</option>" +
                            "<option value='6'>文本输入</option>" +
                            "<option value='7'>睡眠</option>" +
                            "<option value='8'>截图</option>" +
                            "</select><button id='ins"+scriptnumber1+"' onclick='insscript("+scriptnumber1+");'>插入</button></td>");

                        nowcanvas = document.getElementById("nowcanvas"+scriptnumber1+"-1");
                        nowcontext = nowcanvas.getContext('2d');
                        imgCut(nowcontext,img,oldcanvas.width,oldcanvas.height,startPoint.x,startPoint.y,w,h);

                        swipenumber = 0;

                        document.getElementById("phone-screen").style.display="block";
                        document.getElementById("phone").style.display="none";
                        picname = Date.now();
                        canvaSavePng(nowcanvas,picname);
                        swipescript += "Template(r'"+picname+".png', resolution=(${screenw}, ${screenh})))";
                        updatescript(swipescript,scriptnumber1,scriptnumber);
                        //addscript(swipescript,scriptnumber1);
                    }
                }
            }
        }
        if(value=="3"){
            $("tr[id='"+scriptno+"']").after("<tr id='"+scriptno1+"'><td>检查点</td><td>存在</td></tr>");
            var canvas = document.getElementById("phone-screen");
            var cantox = document.getElementById("cantox");
            var cliptox = document.getElementById("cliptox");
            var btnclip = document.getElementById("btnclip");
            //获取到canvas元素
            var oldcanvas = document.getElementById("phone");
            oldcanvas.width = 440;
            oldcanvas.height = 860;
            var nowcanvas = "";
            //获取canvas中的画图环境
            var oldcontext = oldcanvas.getContext('2d');
            var nowcontext = "";
            var img = new Image();
            img.setAttribute('crossOrigin', 'Anonymous');
            img.src=canvas.toDataURL('image/jpg');
            img.onload = function(){
                oldcontext.drawImage(img,0,0,oldcanvas.width,oldcanvas.height);
            }
            document.getElementById("phone-screen").style.display="none";
            document.getElementById("phone").style.display="block";

            var optype = "";
            var swipenumber = "0";
            var startPoint;//截取图像开始坐标
            var endPoint;//截图图像的结束坐标
            var w; //截取图像的宽度
            var h; //截取图像的高度
            var flag = false; //用于判断移动事件事物控制
            //鼠标按下事件
            cantox.onmousedown = function (e){
                flag = true;
                cliptox.style.display = 'block';
                startPoint = windowToCanvas(oldcanvas, e.clientX, e.clientY);
                cliptox.style.left = startPoint.x+'px';
                cliptox.style.top = startPoint.y+90+'px';
            }

            //鼠标移动事件
            cantox.onmousemove = function (e){
                if(flag){
                    cliptox.style.background = 'rgba(0,0,0,0.5)';
                    endPoint = windowToCanvas(oldcanvas, e.clientX, e.clientY);
                    w = endPoint.x - startPoint.x;
                    h = endPoint.y - startPoint.y;
                    cliptox.style.width = w +'px';
                    cliptox.style.height = h+'px';
                }
            }
            //鼠标释放事件
            cantox.onmouseup = function (e){
                flag = false;
            }
            optype = "3";

            //按钮截取事件
            btnclip.onclick = function (){
                if(optype =="1"||optype=="3"||optype=="4"){
                    $("#scripttd"+scriptnumber1).append("<td colspan='2'><canvas id='nowcanvas"+scriptnumber1+"' width='"+w+"' height='"+h+"' ></td><td><button id='del"+scriptnumber1+"' onclick='delscript("+scriptnumber1+");'>删除</button></td><td>" +
                        "<select name='autotestip"+scriptnumber1+"' id='autotestip"+scriptnumber1+"'>" +
                        "<option value='0' Selected>请选择</option>" +
                        "<option value='1'>点击</option>" +
                        "<option value='2'>滑动</option>" +
                        "<option value='3'>存在检查点</option>" +
                        "<option value='4'>不存在检查点</option>" +
                        "<option value='5'>等待</option>" +
                        "<option value='6'>文本输入</option>" +
                        "<option value='7'>睡眠</option>" +
                        "<option value='8'>截图</option>" +
                        "</select><button id='ins"+scriptnumber1+"' onclick='insscript("+scriptnumber1+");'>插入</button></td> ");

                    nowcanvas = document.getElementById("nowcanvas"+scriptnumber1);
                    nowcontext = nowcanvas.getContext('2d');
                    imgCut(nowcontext,img,oldcanvas.width,oldcanvas.height,startPoint.x,startPoint.y,w,h);
                    picname = Date.now();
                    canvaSavePng(nowcanvas,picname);
                    assertexit = "assert_exists(dev,Template(r'"+picname+".png',  resolution=(${screenw}, ${screenh})), '')";
                    updatescript(assertexit,scriptnumber1,scriptnumber);
                    //addscript(assertexit,scriptnumber1);
                    document.getElementById("phone-screen").style.display="block";
                    document.getElementById("phone").style.display="none";
                }
            }
        }
        if(value=="4"){
            $("tr[id='"+scriptno+"']").after("<tr id='"+scriptno1+"'><td>检查点</td><td>不存在</td></tr>");
            var canvas = document.getElementById("phone-screen");
            var cantox = document.getElementById("cantox");
            var cliptox = document.getElementById("cliptox");
            var btnclip = document.getElementById("btnclip");
            //获取到canvas元素
            var oldcanvas = document.getElementById("phone");
            oldcanvas.width = 440;
            oldcanvas.height = 860;
            var nowcanvas = "";
            //获取canvas中的画图环境
            var oldcontext = oldcanvas.getContext('2d');
            var nowcontext = "";
            var img = new Image();
            img.setAttribute('crossOrigin', 'Anonymous');
            img.src=canvas.toDataURL('image/jpg');
            img.onload = function(){
                oldcontext.drawImage(img,0,0,oldcanvas.width,oldcanvas.height);
            }

            document.getElementById("phone-screen").style.display="none";
            document.getElementById("phone").style.display="block";

            var optype = "";
            var swipenumber = "0";
            var startPoint;//截取图像开始坐标
            var endPoint;//截图图像的结束坐标
            var w; //截取图像的宽度
            var h; //截取图像的高度
            var flag = false; //用于判断移动事件事物控制
            //鼠标按下事件
            cantox.onmousedown = function (e){
                flag = true;
                cliptox.style.display = 'block';
                startPoint = windowToCanvas(oldcanvas, e.clientX, e.clientY);
                cliptox.style.left = startPoint.x+'px';
                cliptox.style.top = startPoint.y+90+'px';
            }

            //鼠标移动事件
            cantox.onmousemove = function (e){
                if(flag){
                    cliptox.style.background = 'rgba(0,0,0,0.5)';
                    endPoint = windowToCanvas(oldcanvas, e.clientX, e.clientY);
                    w = endPoint.x - startPoint.x;
                    h = endPoint.y - startPoint.y;
                    cliptox.style.width = w +'px';
                    cliptox.style.height = h+'px';
                }
            }
            //鼠标释放事件
            cantox.onmouseup = function (e){
                flag = false;
            }
            optype = "4";

            //按钮截取事件
            btnclip.onclick = function (){
                if(optype =="1"||optype=="3"||optype=="4"){
                    $("#scripttd"+scriptnumber1).append("<td colspan='2'><canvas id='nowcanvas"+scriptnumber1+"' width='"+w+"' height='"+h+"' ></td><td><button id='del"+scriptnumber1+"' onclick='delscript("+scriptnumber1+");'>删除</button></td><td>" +
                        "<select name='autotestip"+scriptnumber1+"' id='autotestip"+scriptnumber1+"'>" +
                        "<option value='0' Selected>请选择</option>" +
                        "<option value='1'>点击</option>" +
                        "<option value='2'>滑动</option>" +
                        "<option value='3'>存在检查点</option>" +
                        "<option value='4'>不存在检查点</option>" +
                        "<option value='5'>等待</option>" +
                        "<option value='6'>文本输入</option>" +
                        "<option value='7'>睡眠</option>" +
                        "<option value='8'>截图</option>" +
                        "</select><button id='ins"+scriptnumber1+"' onclick='insscript("+scriptnumber1+");'>插入</button></td> ");

                    nowcanvas = document.getElementById("nowcanvas"+scriptnumber1);
                    nowcontext = nowcanvas.getContext('2d');
                    imgCut(nowcontext,img,oldcanvas.width,oldcanvas.height,startPoint.x,startPoint.y,w,h);
                    picname = Date.now();
                    canvaSavePng(nowcanvas,picname);
                    assertnotexit = "assert_not_exists(dev,Template(r'"+picname+".png',  resolution=(${screenw}, ${screenh})), '')";
                    updatescript(assertnotexit,scriptnumber1,scriptnumber);
                    //addscript(assertnotexit,scriptnumber1);
                    document.getElementById("phone-screen").style.display="block";
                    document.getElementById("phone").style.display="none";
                }
            }
        }
        if(value=="5"){
            $("tr[id='"+scriptno+"']").after("<tr id='"+scriptno1+"'><td>等待</td></tr>");
            var canvas = document.getElementById("phone-screen");
            var cantox = document.getElementById("cantox");
            var cliptox = document.getElementById("cliptox");
            var btnclip = document.getElementById("btnclip");
            //获取到canvas元素
            var oldcanvas = document.getElementById("phone");
            oldcanvas.width = 440;
            oldcanvas.height = 860;
            var nowcanvas = "";
            //获取canvas中的画图环境
            var oldcontext = oldcanvas.getContext('2d');
            var nowcontext = "";
            var img = new Image();
            img.setAttribute('crossOrigin', 'Anonymous');
            img.src=canvas.toDataURL('image/jpg');
            img.onload = function(){
                oldcontext.drawImage(img,0,0,oldcanvas.width,oldcanvas.height);
            }

            document.getElementById("phone-screen").style.display="none";
            document.getElementById("phone").style.display="block";
            var optype = "";
            var swipenumber = "0";
            var startPoint;//截取图像开始坐标
            var endPoint;//截图图像的结束坐标
            var w; //截取图像的宽度
            var h; //截取图像的高度
            var flag = false; //用于判断移动事件事物控制
            //鼠标按下事件
            cantox.onmousedown = function (e){
                flag = true;
                cliptox.style.display = 'block';
                startPoint = windowToCanvas(oldcanvas, e.clientX, e.clientY);
                cliptox.style.left = startPoint.x+'px';
                cliptox.style.top = startPoint.y+90+'px';
            }

            //鼠标移动事件
            cantox.onmousemove = function (e){
                if(flag){
                    cliptox.style.background = 'rgba(0,0,0,0.5)';
                    endPoint = windowToCanvas(oldcanvas, e.clientX, e.clientY);
                    w = endPoint.x - startPoint.x;
                    h = endPoint.y - startPoint.y;
                    cliptox.style.width = w +'px';
                    cliptox.style.height = h+'px';
                }
            }
            //鼠标释放事件
            cantox.onmouseup = function (e){
                flag = false;
            }

            optype = "5";
            //按钮截取事件
            btnclip.onclick = function (){
                if(optype =="1"||optype=="3"||optype=="4"||optype=="5"){
                    $("#scripttd"+scriptnumber1).append("<td><canvas id='nowcanvas"+scriptnumber1+"' width='"+w+"' height='"+h+"' ></td><td colspan='2'><input id='waitsecond"+scriptnumber1+"' onBlur='waitblur("+scriptnumber1+")' autofocus='autofocus'>秒</td><td><button id='del"+scriptnumber1+"' onclick='delscript("+scriptnumber1+");'>删除</button></td><td>" +
                        "<select name='autotestip"+scriptnumber1+"' id='autotestip"+scriptnumber1+"'>" +
                        "<option value='0' Selected>请选择</option>" +
                        "<option value='1'>点击</option>" +
                        "<option value='2'>滑动</option>" +
                        "<option value='3'>存在检查点</option>" +
                        "<option value='4'>不存在检查点</option>" +
                        "<option value='5'>等待</option>" +
                        "<option value='6'>文本输入</option>" +
                        "<option value='7'>睡眠</option>" +
                        "<option value='8'>截图</option>" +
                        "</select><button id='ins"+scriptnumber1+"' onclick='insscript("+scriptnumber1+");'>插入</button></td> ");

                    nowcanvas = document.getElementById("nowcanvas"+scriptnumber1);
                    nowcontext = nowcanvas.getContext('2d');
                    imgCut(nowcontext,img,oldcanvas.width,oldcanvas.height,startPoint.x,startPoint.y,w,h);
                    picname = Date.now();
                    canvaSavePng(nowcanvas,picname);
                    waitscript="wait(dev,Template(r'"+picname+"'.png', resolution=(${screenw}, ${screenh}))";
                    updatescript(waitscript,scriptnumber1,scriptnumber);
                    //addscript(waitscript,scriptnumber1);
                    document.getElementById("phone-screen").style.display="block";
                    document.getElementById("phone").style.display="none";
                }
            }
        }
        if(value=="6"){
            $("tr[id='"+scriptno+"']").after("<tr id='scripttd"+scriptnumber1+"'><td>输入文本</td><td colspan='3'><input id='inputtext"+scriptnumber1+"'  autofocus='autofocus' onBlur='inputblur("+scriptnumber1+")'></td><td><button id='del"+scriptnumber1+"' onclick='delscript("+scriptnumber1+");'>删除</button></td><td>" +
                "<select name='autotestip"+scriptnumber1+"' id='autotestip"+scriptnumber1+"'>" +
                "<option value='0' Selected>请选择</option>" +
                "<option value='1'>点击</option>" +
                "<option value='2'>滑动</option>" +
                "<option value='3'>存在检查点</option>" +
                "<option value='4'>不存在检查点</option>" +
                "<option value='5'>等待</option>" +
                "<option value='6'>文本输入</option>" +
                "<option value='7'>睡眠</option>" +
                "<option value='8'>截图</option>" +
                "</select><button id='ins"+scriptnumber1+"' onclick='insscript("+scriptnumber1+");'>插入</button></td></tr> ");

            inputscript = "text(dev,";
            updatescript(inputscript,scriptnumber1,scriptnumber);
            //addscript(inputscript,scriptnumber1);
        }
        if(value=="7"){
            $("tr[id='"+scriptno+"']").after("<tr id='scripttd"+scriptnumber1+"'><td>睡眠</td><td colspan='3'><input id='sleepsecond"+scriptnumber1+"'  autofocus='autofocus' onBlur='sleepblur("+scriptnumber1+")'>秒</td><td><button id='del"+scriptnumber1+"' onclick='delscript("+scriptnumber1+");'>删除</button></td><td>" +
                "<select name='autotestip"+scriptnumber1+"' id='autotestip"+scriptnumber1+"'>" +
                "<option value='0' Selected>请选择</option>" +
                "<option value='1'>点击</option>" +
                "<option value='2'>滑动</option>" +
                "<option value='3'>存在检查点</option>" +
                "<option value='4'>不存在检查点</option>" +
                "<option value='5'>等待</option>" +
                "<option value='6'>文本输入</option>" +
                "<option value='7'>睡眠</option>" +
                "<option value='8'>截图</option>" +
                "</select><button id='ins"+scriptnumber1+"' onclick='insscript("+scriptnumber1+");'>插入</button></td></tr> ");

            sleepscript = "sleep(";
            updatescript(sleepscript,scriptnumber1,scriptnumber);
            //addscript(sleepscript,scriptnumber1);
        }
        if(value=="8"){
            $("tr[id='"+scriptno+"']").after("<tr id='scripttd"+scriptnumber1+"'><td>截图操作</td><td colspan='4'><button id='del"+scriptnumber1+"' onclick='delscript("+scriptnumber1+");'>删除</button></td><td>" +
                "<select name='autotestip"+scriptnumber1+"' id='autotestip"+scriptnumber1+"'>" +
                "<option value='0' Selected>请选择</option>" +
                "<option value='1'>点击</option>" +
                "<option value='2'>滑动</option>" +
                "<option value='3'>存在检查点</option>" +
                "<option value='4'>不存在检查点</option>" +
                "<option value='5'>等待</option>" +
                "<option value='6'>文本输入</option>" +
                "<option value='7'>睡眠</option>" +
                "<option value='8'>截图</option>" +
                "</select><button id='ins"+scriptnumber1+"' onclick='insscript("+scriptnumber1+");'>插入</button></td></tr> ");

            snapshotscript = "screennum = getscreen(dev, deviceid, screennum, apkonlynumber, taskId, onlyDistinction)";
            updatescript(snapshotscript,scriptnumber1,scriptnumber);
            //addscript(snapshotscript,scriptnumber1);
        }
        console.log(scripttext);
    }

     function delscript(scriptnumber){
        var scriptno = "scripttd"+scriptnumber;
        $("#"+scriptno).remove();
        scripttext.delete(scriptnumber);
        console.log(scripttext);
     }

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

//向后台发送请求保存截图图片方法
  function canvaSavePng(canvas,picname){
        var MIME_TYPE = "image/jpg";
        var imgURL = canvas.toDataURL(MIME_TYPE);
        var blob = dataURItoBlob(imgURL);
        var fd = new FormData();
        fd.append('file', blob, picname+ '.jpg');
        fd.append('picname',picname);
        fd.append('scriptid',scriptid);
        var b64 = imgURL.substring(22);
        $.ajax({
            contentType: false,
            processData: false,
            url: "<%=basePath%>/savepicture",
            type:'post',
            data: fd,
            success:function ()
            {
            }
        });
    }

  function addscript(scriptitem,scriptnumber){
       //console.log(scriptnumber);
       scripttext.set(scriptnumber,scriptitem);
       console.log(scripttext);
  }

  function updatescript(touchscript,scriptnumber1,scriptnumber){
      var scripttext1 = new Map();
      for (var [key, value] of scripttext) {
          //console.log(key + ' = ' + value);
          if(parseInt(key) == scriptnumber){
              //console.log(key);
              scripttext1.set(key,value);
              scripttext1.set(scriptnumber1,touchscript);
          }else{
              scripttext1.set(key,value);
          }
      }
      scripttext = scripttext1;
      console.log(scripttext);
  }

  function dataURItoBlob(dataURI) {//base64转buffer
                var byteString = atob(dataURI.split(',')[1]);
                var mimeString = dataURI.split(',')[0].split(':')[1].split(';')[0];
                var ab = new ArrayBuffer(byteString.length);
                var ia = new Uint8Array(ab);
                for (var i = 0; i < byteString.length; i++) {
                    ia[i] = byteString.charCodeAt(i);
                }
                return new Blob([ab], {type: mimeString});
  }

    function imgCut(context,image,imgElementW,imgElementH,sx,sy,w,h){
        //清理画布，便于重新绘制
        context.clearRect(0,0,imgElementW,imgElementH);
        //计算 ：比例 = 原图像/显示图像
        var ratioW = image.width/imgElementW;
        var ratioH = image.height/imgElementH;
        //根据截取图像的所占位置及大小计算出在原图所占的位置及大小
        //.drawImage(图像对象,原图像截取的起始X坐标,原图像截取的起始Y坐标,原图像截取的宽度,原图像截取的高度，
        // 绘制图像的起始X坐标,绘制图像的起始Y坐标,绘制图像所需要的宽度,绘制图像所需要的高度);
        context.drawImage(image,ratioW*sx,ratioH*sy,ratioW*w,ratioH*h,0,0,w,h);

    }

    /*
     * 坐标转换：将window中的坐标转换到元素盒子中的坐标，并返回(x,y)坐标
     * element：canvas元素对象
     * x:鼠标在当前窗口X坐标值
     * y:鼠标在当前窗口Y坐标值
     * */
    function windowToCanvas(element,x,y){
        //获取当前鼠标在window中的坐标值
        // alert(event.clientX+"-------"+event.clientY);
        //获取元素的坐标属性
        var box = element.getBoundingClientRect();
        var bx = x - box.left;
        var by = y - box.top;
        return {x:bx,y:by};
    }

    function uuid() {
        var s = [];
        var hexDigits = "0123456789abcdef";
        for (var i = 0; i < 36; i++) {
            s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
        }
        s[14] = "4"; // bits 12-15 of the time_hi_and_version field to 0010
        s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1); // bits 6-7 of the clock_seq_hi_and_reserved to 01
        s[8] = s[13] = s[18] = s[23] = "-";

        var uuid = s.join("");
        return uuid;
    }


</script>

</html>


