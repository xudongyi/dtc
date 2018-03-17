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
    <title>中心维护</title>
    <script type="application/javascript" src="${ctx}/js/boot.js"></script>
    <style>
        .search-content {
            width: 100%;
            height: auto;
            padding: 5px 0;
            float: left;
        }

        .search-content div {
            float: left;
        }

        .row-search, .row-value, .row-button {
            margin-left: 5px;
        }

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
    <div id="datagrid" style="width: 100%;height:99.5%;" class="mini-datagrid" url="${ctx}/base/center/list.do"  showPager="fasle">
        <div property="columns">
            <div type="indexcolumn">序号</div>
            <div field="centerNo" width="60">中心编号</div>
            <div field="centerName" width="120">中心名称</div>
            <div field="address" width="140">地址</div>
            <div field="mobile" width="80">联系方式</div>
            <div field="userName" width="80">账户</div>
            <div field="opt" width="120">操作</div>
        </div>
    </div>


    <div id="win" class="mini-window" title="编辑/查看" style="width:400px;height:320px;" showShadow="true"
         showFooter="true" showModal="false" allowDrag="true">
        <div id="editForm1" style="padding: 10px;" align="center">
            <input name="id" class="mini-hidden"/>
            <table>
                <tr>
                    <td width="110">个推appid:</td>
                    <td width="200">
                        <input name="gtAppId" class="mini-textbox"/>
                    </td>
                </tr>
                <tr>
                    <td width="110">个推appkey:</td>
                    <td width="200">
                        <input name="gtAppKey" class="mini-textbox"/>
                    </td>
                </tr>
                <tr>
                    <td width="110">个推appsecret:</td>
                    <td width="200">
                        <input name="gtAppSecret" class="mini-textbox"/>
                    </td>
                </tr>
                <tr>
                    <td width="110">个推mastersecret:</td>
                    <td width="200">
                        <input name="gtMasterSecret" class="mini-textbox"/>
                    </td>
                </tr>
            </table>
        </div>
        <div property="footer" style="text-align:center;padding:5px;padding-right:15px;">
            <a class="btn btn-info btn-sm" iconCls="icon-add" href="javascript:saveData()">保存</a>
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <a class="btn btn-primary btn-sm" href="javascript:cancleWin()">取消</a>
        </div>
    </div>
</div>

</body>

<script>
    mini.parse();
    var grid = mini.get("datagrid");
    var editForm = new mini.Form("#editForm1");
    //绑定表单
    grid.load();
    grid.on("drawcell", function (e) {
        var record = e.record, field = e.field, value = e.value,rowIndex = e.rowIndex;
        if(field=='opt'){
            e.cellHtml = '<a href="">修改密码</a>&nbsp;&nbsp;<a href="">禁用</a>&nbsp;&nbsp;<a href="">编辑</a>&nbsp;&nbsp;<a href="">删除</a>&nbsp;&nbsp;'
        }
    });

    function showDetail(rowIndex) {
        var win = mini.get("win");
        win.showAtPos("center", "middle");
        editForm.setData(grid.getData()[rowIndex])
        editForm.validate();
    }

    function addData() {
        editForm.reset();
        var win = mini.get("win");
        win.showAtPos("center", "middle");

    }

    function saveData() {
        editForm.validate();
        if (editForm.isValid()) {
            $.ajax({
                url: "${ctx}/m/appConfig/save.do",
                type: "post",
                data: editForm.getData(true, true),
                success: function (data) {
                    if (data == 1) {
                        cancleWin();
                        grid.load();
                    } else if (data == 0) {
                        mini.alert("保存失败!");
                    } else if (data == -1) {
                        mini.alert("数据已存在!");
                    } else {
                        mini.alert("未知错误!");
                    }
                }
            })
        }
    }

    function cancleWin() {
        editForm.reset();
        var win = mini.get("win");
        win.hide();
    }
</script>
</html>
