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
        <form id="exportForm" method="post">
            <table>
                <tr>

                    <td class="form-label" style="width: 70px;">入组时间：</td>
                    <td style="width: 390px">
                        <input style="width: 180px" name="beginTime" id="beginTime" class="mini-datepicker"
                               nullValue="null" emptyText="" format="yyyy-MM-dd H:mm:ss" showTime="true" showOkButton="true" showClearButton="false"/>
                        -
                        <input style="width: 180px" name="endTime" id="endTime" class="mini-datepicker"
                               nullValue="null" emptyText="" showOkButton="true" showClearButton="false" format="yyyy-MM-dd H:mm:ss" showTime="true"/>

                    </td>
                    <td class="form-label" style="width: 70px;">试验中心：</td>
                    <td>
                        <input class="mini-combobox"
                               url="${ctx}/test/getCenterList.do"
                               textField="centerName" valueField="id" emptyText="请选择..."
                               name="centerId" showNullItem="true" nullItemText="请选择..."/>
                    </td>
                    <td style="width: 70px"><a style="margin: 0 0 0 0;" class="btn btn-info btn-sm"  onclick="searchQuery()">查询</a></td>
                    <td><a class="btn btn-danger btn-sm"  href="javascript:daoChu()">导出</a></td>

                </tr>
            </table>
        </form>
    </div>
    <div id="datagrid" style="width: 100%;height:93%;" class="mini-datagrid" url="${ctx}/test/groupDetail.do" >
        <div property="columns">
            <div type="indexcolumn">序号</div>
            <div field="PATIENT_NUMBER" width="120">被试者编号</div>
            <div field="NAME" width="120">被试者姓名</div>
            <div field="SEX" width="60">性别</div>
            <div field="AGE" width="80">年龄</div>
            <div field="CREATE_USER" width="80">操作者</div>
            <div field="CENTER_NAME" width="120">试验中心</div>
            <div field="NUMBER" width="120">随机号</div>
            <div field="GROUP_NAME" width="120">分组</div>
            <div field="ASSIGN_TIME" width="120">入组时间</div>
        </div>
    </div>
</div>
</body>
<script>

    mini.parse();
    var grid = mini.get("datagrid");
    grid.load();
    grid.on("drawcell", function (e) {
        var record = e.record, field = e.field, value = e.value,rowIndex = e.rowIndex;
        if(field=='ASSIGN_TIME'){
            e.cellHtml = new Date(value).Format("yyyy-MM-dd hh:mm:ss");
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

    function daoChu() {
        var queryForm = new mini.Form("#queryForm");
        queryForm.validate();
        if (queryForm.isValid()) {
            var url = "${ctx}/securityRisk/back/allRiskListExport.do";
            $('#exportForm').attr("action", url);
            $("#exportForm").submit();
        }

    }
</script>
</html>
