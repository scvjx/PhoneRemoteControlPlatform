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
    <title>CFCA脚本列表</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <!-- Tell the browser to be responsive to screen width -->
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">
    <!-- Favicon icon -->
    <link rel="icon" type="image/png" sizes="16x16" href="assets/images/favicon.png">
    <link rel="stylesheet" type="text/css" href="assets/plugins/datatables/jquery.dataTables.min.css"/>  
    <link rel="stylesheet" type="text/css" href="assets/newui/css/bootstrap.css"/>  
    <link rel="stylesheet" type="text/css" href="assets/plugins/datatables/dataTables.bootstrap.min.css"/>  
    <link rel="stylesheet" href="assets/plugins/datatables/dataTables.bootstrap4.min.css">
    <script src="assets/plugins/jquery/jquery.min.js"></script>
    <script src="assets/plugins/datatables/dataTables.bootstrap4.min.js"></script>
    <script src="assets/plugins/datatables/fnReloadAjax.js"></script>

    <!-- DataTables -->
    <script src="assets/plugins/datatables/jquery.dataTables.min.js"></script>
    <script src="assets/newui/js/bootstrap.js"> </script>
</head>
<body>
   <div>
                  <div class="card-body">
                      <table id="example1" class="display" style="width:50%">
                          <thead>
                          <tr>
                              <th>脚本名称</th>
                              <th>创建时间</th>
                              <th>操作</th>

                          </tr>
                          </thead>

                      </table>
                  </div>
                  <div class="clearfix"> </div>
              </div>

    <script type="text/javascript" src="jquery-1.8.0.min.js"></script>    
    <script type="text/javascript" src="assets/plugins/jquery.dataTables.min.js"></script>    
    <script type="text/javascript" src="assets/plugins/dataTables.bootstrap.min.js"></script>    
        
    <script type="text/javascript">    


</body>


<script>
 var scripttb ;
 $(function () {
       scripttb =  $("#example1").dataTable({
            //lengthMenu: [5, 10, 20, 30],//这里也可以设置分页，但是不能设置具体内容，只能是一维或二维数组的方式，所以推荐下面language里面的写法。
            bLengthChange: false,
            serverSide: true,
            processing: true,		//显示加载信息
            autoWidth : true, 	//让Datatables自动计算宽度
            ordering: false,//是否启用排序
            searching: false,//搜索
            ScrollX:true,
            searchable: false,
            language: {
                processing: "玩命加载中...",
                paginate: {//分页的样式内容。
                    previous: "上一页",
                    next: "下一页",
                    first: "第一页",
                    last: "最后"
                },
                emptyTable: "表中数据为空",
                loadingRecords: "正在加载数据...",
                zeroRecords: "没有内容",//table tbody内容为空时，tbody的内容。
                //下面三者构成了总体的左下角的内容。
                info: "总共_PAGES_ 页，显示第_START_ 到第 _END_ ，共_MAX_ 条 ",//左下角的信息显示，大写的词为关键字。
                infoEmpty: "0条记录",//筛选为空时左下角的显示。
                infoFiltered: ""//筛选之后的左下角筛选提示，
            },
            stateSave: true,
            pagingType: "simple_numbers",//分页样式的类型
            ajax: {
                contentType: "application/json",
                url: '<%=basePath%>/getscriptlist',
                type: 'POST',
                data : {'userid':'${userid}'}
            },
            columns:[
                {"data":"scriptname"},
                {"data":"createtime"},
                {"data" : null}
            ],
             columnDefs: [
             {
              "targets": -1,//编辑
              "data": null,//下面这行，添加了编辑按钮和，删除按钮
               "render": function ( data, type, full, meta ) {
                    return  "<button id='btn_edit' type='button' class='btn bg-info' onclick='editscript('"+data.scriptid+"')'> <span class='glyphicon glyphicon-pencil' aria-hidden='true'></span>编辑</button><button id='btn_edit' type='button' class='btn bg-info'> <span class='glyphicon glyphicon-pencil' aria-hidden='true'></span>回放</button><button id='btn_edit' type='button' class='btn bg-info' onclick=delscript('"+data.scriptid+"')> <span class='glyphicon glyphicon-pencil' aria-hidden='true'></span>删除</button>"

               }
               }
             ],
        });
        $("#table_local_filter input[type=search]").css({ width: "auto" });//右上角的默认搜索文本框，不写这个就超出去了。
    });

function editscript(scriptid){



}

function delscript(scriptid){

    scripttb.fnReloadAjax();
}

</script>
</html>


