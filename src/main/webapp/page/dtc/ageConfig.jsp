<%--
  Created by IntelliJ IDEA.
  User: xudy
  Date: 2018/3/19 0019
  Time: 11:52
  To change this template use File | Settings | File Templates.
--%>
<%@include file="/js/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>年龄维护</title>
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
    <div class="search-content" id="formSearch">
        <div class="row-button">
            <a class="btn btn-info btn-sm"  href="javascript:addData()">新增</a>
        </div>
    </div>
    <div id="datagrid" style="width: 100%;height:93%;" class="mini-datagrid" url="${ctx}/base/group/list.do"  showPager="fasle">
        <div property="columns">
            <div type="indexcolumn">序号</div>
            <div field="groupName" width="60">年龄组名称</div>
            <div field="minAge" width="120">年龄组下限</div>
            <div field="maxAge" width="140">年龄组上限</div>
            <div field="opt" width="120">操作</div>
        </div>
    </div>


    <div id="win" class="mini-window"  style="width:400px;height:320px;" showShadow="true"
         showFooter="true" showModal="false" allowDrag="true">
        <div id="editForm1" style="padding: 10px;" align="center">
            <input name="id" class="mini-hidden"/>
            <input name="isDeleted" class="mini-hidden" value="1"/>
            <table>
                <tr>
                    <td width="110">名称:</td>
                    <td width="200">
                        <input name="groupName" class="mini-textbox" required="true"/>
                    </td>
                </tr>
                <tr>
                    <td width="110">年龄组下限(包含):</td>
                    <td width="200">
                        <input name="minAge" class="mini-textbox" required="true"/>
                    </td>
                </tr>
                <tr>
                    <td width="110">年龄组上限(包含):</td>
                    <td width="200">
                        <input name="maxAge" class="mini-textbox" required="true"/>
                    </td>
                </tr>
            </table>
        </div>
        <div property="footer" style="text-align:center;padding:5px;padding-right:15px;">
            <a class="btn btn-info btn-sm" iconCls="icon-add" href="javascript:saveData()">保存</a>
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <a class="btn btn-danger btn-sm" href="javascript:cancleWin()">取消</a>
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
    var test = null;
    $.ajax({
        url:"${ctx}/test/current.do",
        async:false,
        success:function(data){
            test = data;
        }
    })
    grid.on("drawcell", function (e) {
        var record = e.record, field = e.field, value = e.value,rowIndex = e.rowIndex;
        if(field=='opt'){
            var state = 1;
            var html = "启用";
            if(record.userState==1){
                state = 2;
                html = "禁用";
            }
            if(test!=null){
                e.cellHtml ='<span style="color:red">测试正在进行中,无法操作!</span>'
            }else{
                e.cellHtml ='<a href="javascript:showDetail(\''+rowIndex+'\')">编辑</a>&nbsp;&nbsp;' +
                    '<a href="javascript:deleteCenter(\''+record.id+'\')">删除</a>&nbsp;&nbsp;'
            }

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
                url: "${ctx}/base/group/checkAge.do",
                type: "post",
                async:false,
                data: formData,
                success: function (data) {
                    if(data){
                        mini.alert("年龄段已存在!不能重复添加");
                        return;
                    }
                }
            })
            var formData = editForm.getData(true, true);
            $.ajax({
                url: "${ctx}/base/group/save.do",
                type: "post",
                data: formData,
                success: function (data) {
                    if (data == 1) {
                        cancleWin();
                        grid.load();
                    } else if (data == 0) {
                        mini.alert("保存失败!");
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
