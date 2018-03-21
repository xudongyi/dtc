<%--
  Created by IntelliJ IDEA.
  User: xudy
  Date: 2018/2/2 0002
  Time: 9:20
  To change this template use File | Settings | File Templates.
--%>
<%@include file="/js/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>试验分组详情</title>
    <script type="application/javascript" src="${ctx}/js/boot.js"></script>
    <style>
        .mini-window .mini-panel-footer {
            background: #fff !important;
        }

        .mini-textbox, #editForm1 .mini-combobox {
            width: 200px !important;
        }

    </style>
</head>
<body>
<div class="mini-fit">
    <div class="top-search" id="queryForm">
        <input type="text" class="mini-hidden" name="centerId" value="${sessionScope.get("user").id}">
    </div>
    <div id="datagrid" style="width: 100%;height:99%;" class="mini-datagrid" url="${ctx}/test/groupDetail.do" >
        <div property="columns">
            <div type="indexcolumn">序号</div>
            <div field="PATIENT_NUMBER" width="120">被试者编号</div>
            <div field="NAME" width="120">被试者姓名</div>
            <div field="SEX" width="60">性别</div>
            <div field="AGE" width="80">年龄</div>
            <div field="BIRTHDAY" width="80">出生年月</div>
            <div field="IS_MARRYED" width="80">婚姻状况</div>
            <div field="MOBILE" width="80">联系电话</div>
            <div field="ADDRESS" width="80">住址</div>
            <div field="CREATE_USER" width="80">操作者</div>
            <div field="ASSIGN_TIME" width="120">入组时间</div>
        </div>
    </div>
</div>
</body>
<script>

    mini.parse();
    var grid = mini.get("datagrid");
    searchQuery();
    grid.on("drawcell", function (e) {
        var record = e.record, field = e.field, value = e.value,rowIndex = e.rowIndex;
        if(field=='ASSIGN_TIME'){
            e.cellHtml = new Date(value).Format("yyyy-MM-dd hh:mm:ss");
        }
        if(field=='BIRTHDAY'){
            e.cellHtml = new Date(value).Format("yyyy-MM-dd");
        }
        if(field=="SEX"){
            if(value==1){
                e.cellHtml="男";
            }else if(value==2){
                e.cellHtml="女";
            }else{
                e.cellHtml="未知";
            }
        }
        if(field=="IS_MARRYED"){
            if(value==1){
                e.cellHtml="已婚";
            }else if(value==2){
                e.cellHtml="未婚";
            }else{
                e.cellHtml="未知";
            }
        }
    });

    function searchQuery(){
        var queryForm = new mini.Form("#queryForm");
        queryForm.validate();
        if (queryForm.isValid()) {
            var data = queryForm.getData(true, false);
            grid.load(data, null, showLoadErrorMessageBox);
        }
    }
</script>
</html>
