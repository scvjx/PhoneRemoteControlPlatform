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
    <title>CFCA远程真机系统</title>
    <link href="assets/newui/css/bootstrap.css" rel='stylesheet' type='text/css' />
    <link href="assets/phone/sx.css" rel='stylesheet'/>
    <!-- Custom CSS -->
    <link href="assets/newui/css/style.css" rel='stylesheet' type='text/css' />
    <link href="assets/phone/sx.css" rel='stylesheet'/>
    <!-- font-awesome icons CSS -->
    <link href="assets/newui/css/font-awesome.css" rel="stylesheet">
    <!-- //font-awesome icons CSS-->

    <!-- side nav css file -->
    <link href='assets/newui/css/SidebarNav.min.css' media='all' rel='stylesheet' type='text/css'/>
    <!-- //side nav css file -->

    <!-- js-->
    <script src="assets/newui/js/jquery-1.11.1.min.js"></script>
    <script src="assets/newui/js/modernizr.custom.js"></script>

    <!--webfonts-->
    <link href="http://fonts.googleapis.com/css?family=PT+Sans:400,400i,700,700i&amp;subset=cyrillic,cyrillic-ext,latin-ext" rel="stylesheet">
    <!--//webfonts-->

    <!-- chart -->
    <script src="assets/newui/js/Chart.js"></script>
    <!-- //chart -->

    <!-- Metis Menu -->
    <script src="assets/newui/js/metisMenu.min.js"></script>
    <script src="assets/newui/js/custom.js"></script>
    <link href="assets/newui/css/custom.css" rel="stylesheet">

    <!--//Metis Menu -->
    <style>
        #chartdiv {
            width: 100%;
            height: 295px;
        }

    </style>
    <!--pie-chart --><!-- index page sales reviews visitors pie chart -->
    <script src="assets/newui/js/pie-chart.js" type="text/javascript"></script>
    <script type="text/javascript">

        $(document).ready(function () {
            $('#demo-pie-1').pieChart({
                barColor: '#2dde98',
                trackColor: '#eee',
                lineCap: 'round',
                lineWidth: 8,
                onStep: function (from, to, percent) {
                    $(this.element).find('.pie-value').text(Math.round(percent) + '%');
                }
            });

            $('#demo-pie-2').pieChart({
                barColor: '#8e43e7',
                trackColor: '#eee',
                lineCap: 'butt',
                lineWidth: 8,
                onStep: function (from, to, percent) {
                    $(this.element).find('.pie-value').text(Math.round(percent) + '%');
                }
            });

            $('#demo-pie-3').pieChart({
                barColor: '#ffc168',
                trackColor: '#eee',
                lineCap: 'square',
                lineWidth: 8,
                onStep: function (from, to, percent) {
                    $(this.element).find('.pie-value').text(Math.round(percent) + '%');
                }
            });


        });

    </script>
    <!-- //pie-chart --><!-- index page sales reviews visitors pie chart -->

    <!-- requried-jsfiles-for owl -->
    <link href="assets/newui/css/owl.carousel.css" rel="stylesheet">
    <script src="assets/newui/js/owl.carousel.js"></script>
    <script>
        $(document).ready(function() {
            $("#owl-demo").owlCarousel({
                items : 3,
                lazyLoad : true,
                autoPlay : true,
                pagination : true,
                nav:true,
            });
        });
    </script>

</head>
<body>
<!-- /tile header -->
<div align="center">
    <a id="choice">手机设备筛选请&nbsp;&nbsp;&nbsp;<strong style="color: #0000FF">点击这里</strong></a>
</div>
<div id="sort" class="table-responsive bs-example widget-shadow" style="background-color: unset;display: none;">
    <div class="sortbox">
        <div id="filter">
            <dl class="clearfix">
                <dt>当前状态：</dt>
                <dd>
                    <form method="post" enctype="multipart/form-data" action="<%=basePath%>/devicelist?userid=${userid}">
                        <div><input id="allsearchstatus" name="allsearchstatus" type="submit" style="font-size: 14px;background-color: unset;border: unset;color: #1e88e5" value="全部"/></div>
                    </form>
                </dd>
                <dd>
                    <form method="post" enctype="multipart/form-data" action="<%=basePath%>/devicelist?userid=${userid}">
                        <div><input id="useablesearchstatus" name="useablesearchstatus" type="submit" style="font-size: 14px;background-color: unset;border: unset;color: #1e88e5" value="可使用"/></div>
                    </form>
                </dd>
            </dl>
            <dl class="clearfix">
                <dt>品牌：</dt>
                <dd>
                    <form method="post" enctype="multipart/form-data" action="<%=basePath%>/devicelist?userid=${userid}">
                        <div><input id="allsearchphonetype" name="allsearchphonetype" type="submit" style="font-size: 14px;background-color: unset;border: unset;color: #1e88e5" value="全部"/></div>
                    </form>
                </dd>
                <c:forEach var="phonetype" items="${phoneTypeList}" varStatus="status">
                <dd>
                    <form method="post" enctype="multipart/form-data" action="<%=basePath%>/devicelist?userid=${userid}">
                        <div><input id="searchphonetype" name="searchphonetype" type="submit" style="background-color: unset;border: unset;color: #1e88e5;font-size: 14px;" value="${phonetype.phonebrand}"/></div>
                    </form>
                </dd>
                </c:forEach>
            </dl>
            <dl class="clearfix">
                <dt>分辨率：</dt>
                <dd>
                    <form method="post" enctype="multipart/form-data" action="<%=basePath%>/devicelist?userid=${userid}">
                        <div><input id="allsearchresolutionratio" name="allsearchresolutionratio" type="submit" style="background-color: unset;border: unset;color: #1e88e5;font-size: 14px;" value="全部"/></div>
                    </form>
                </dd>
                <c:forEach var="phoneResolutionratio" items="${phoneResolutionratioList}" varStatus="status">
                <dd>
                    <form method="post" enctype="multipart/form-data" action="<%=basePath%>/devicelist?userid=${userid}">
                        <div><input id="searchresolutionratio" name="searchresolutionratio" type="submit" style="background-color: unset;border: unset;color: #1e88e5;font-size: 14px;" value="${phoneResolutionratio.resolutionratio}"/></div>
                    </form>
                </dd>
                </c:forEach>
            </dl>
            <dl class="clearfix">
                <dt>系统版本：</dt>
                <dd>
                    <form method="post" enctype="multipart/form-data" action="<%=basePath%>/devicelist?userid=${userid}">
                        <div><input id="allsearchandroidversion" name="allsearchandroidversion" type="submit" style="background-color: unset;border: unset;color: #1e88e5;font-size: 14px;" value="全部"/></div>
                    </form>
                </dd>
                    <c:forEach var="phoneAndroidVersion" items="${phoneAndroidVersionList}" varStatus="status">
                <dd>
                    <form method="post" enctype="multipart/form-data" action="<%=basePath%>/devicelist?userid=${userid}">
                        <div><input id="searchandroidversion" name="searchandroidversion" type="submit" style="background-color: unset;border: unset;color: #1e88e5;font-size: 14px;" value="${phoneAndroidVersion.androidversion}"/></div>
                    </form>
                </dd>
                    </c:forEach>
            </dl>
        </div>
    </div>
</div>


<div align="center">
<table border="1"bordercolor="#2F2F2F" style="border:1px solid rgba(0,0,0,0.51);font-family: 仿宋">
    <tr>
        <c:forEach var="phone" items="${phoneList}" varStatus="status">
        <c:if test="${status.index % 5 == 0 }">
    </tr><tr>
    </c:if>

    <td style="width: 200px; height: 150px;">
        <div style="height: 25px; background-color: #CCCCCC;font-size: 14px;">
            <strong>${phone.phonetype}</strong>
        </div>
        <div style="text-align: center;">
            <a>
                <c:if test="${not empty phone.iconpath }">
                <img style="width: 150px;height: 150px;" src="<%=basePath%>${phone.iconpath}">
                </c:if>
                <c:if test="${empty phone.iconpath }">
                <img style="width: 150px;height: 150px;" src="<%=basePath%>/assets/shoujipicture/default.jpg">
                </c:if>
            </a>
        </div><p></p>
        <div style="text-align: center;font-size: 14px;">系统版本：${phone.androidversion}</div><p></p>
        <div style="text-align: center;font-size: 14px;">分辨率：${phone.resolutionratio}</div><p></p>
        <div style="text-align: center;font-size: 14px;">
            <c:if test="${phone.status == '开始使用' }">
            <button class="btn btn-success btn-flat btn-pri btn-xs" type="button"><a href="<%=basePath%>/devicecontrol?userid=${userid}&deviceid=${phone.deviceid}&model=${phone.model}&phonetype=${phone.phonetype}"><font color="white"><strong>开始使用</strong></font></a></button>
            </c:if>
            <c:if test="${phone.status == '停止使用' }">
                <button class="btn btn-danger btn-flat btn-pri btn-xs" type="button"><a href="<%=basePath%>/devicelist?userid=${userid}&deviceid=${phone.deviceid}&model=${phone.model}&action=stop"><font color="white"><strong>停止使用</strong></font></a></button>
            </c:if>
            <c:if test="${phone.status == '占用' }">
                <button class="btn btn-warning btn-flat btn-pri btn-xs" type="button"><strong>占用中</strong></button>
            </c:if>
        </div><p></p>
    </td>
    </c:forEach>
</tr>
</table>
</div>
</body>
</html>
<style>

    .toscriptrecord ul li {
        float: right;
        list-style:none;
        margin-right: 5px;
        margin-buttom: 20px;
    }

    .toscriptrecord ul li a {
        color: #fafafa;
    }

    .title1{
        font-size: 1.7em;
        color: #629aa9;
        margin-bottom: 0.8em;
    }  }
</style>


<script type="text/javascript">
    $(document).ready(function () {
        $("#choice").mouseenter(function () {
            $("#sort").show();
        });
        $("#sort").mouseleave(function () {
            $("#sort").hide();
        });
    })
</script>
