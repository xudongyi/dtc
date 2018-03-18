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
            <a class="mini-button" iconCls="icon-add" onclick="addData()">新增</a>
        </div>
    </div>
    <div id="datagrid" style="width: 100%;height:93%;" class="mini-datagrid" url="${ctx}/base/center/list.do"  showPager="fasle">
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


    <div id="win" class="mini-window" title="新增" style="width:400px;height:320px;" showShadow="true"
         showFooter="true" showModal="false" allowDrag="true">
        <div id="editForm1" style="padding: 10px;" align="center">
            <input name="id" class="mini-hidden"/>
            <input name="isDeleted" class="mini-hidden" value="1"/>
            <input name="userState" class="mini-hidden" value="1"/>
            <table>
                <tr>
                    <td width="110">中心编号:</td>
                    <td width="200">
                        <input name="centerNo" class="mini-textbox" required="true"/>
                    </td>
                </tr>
                <tr>
                    <td width="110">中心名称:</td>
                    <td width="200">
                        <input name="centerName" class="mini-textbox" required="true"/>
                    </td>
                </tr>
                <tr>
                    <td width="110">地址:</td>
                    <td width="200">
                        <input name="address" class="mini-textbox"/>
                    </td>
                </tr>
                <tr>
                    <td width="110">联系方式:</td>
                    <td width="200">
                        <input name="mobile" class="mini-textbox"/>
                    </td>
                </tr>
                <tr>
                    <td width="110">账户:</td>
                    <td width="200">
                        <input name="userName" class="mini-textbox" required="true"/>
                    </td>
                </tr>
                <tr>
                    <td width="110">密码:</td>
                    <td width="200">
                        <input name="password" class="mini-password" required="true"/>
                    </td>
                </tr>
                <tr>
                    <td width="110">确认密码:</td>
                    <td width="200">
                        <input name="password1" class="mini-password" required="true"/>
                    </td>
                </tr>
                <tr>
                    <td width="110">描述:</td>
                    <td width="200">
                        <input name="description" class="mini-textbox"/>
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

    <div id="win2" class="mini-window" title="编辑" style="width:400px;height:320px;" showShadow="true"
         showFooter="true" showModal="false" allowDrag="true">
        <div id="editForm2" style="padding: 10px;" align="center">
            <input name="id" class="mini-hidden"/>
            <table>
                <tr>
                    <td width="110">中心编号:</td>
                    <td width="200">
                        <input name="centerNo" class="mini-textbox" required="true"/>
                    </td>
                </tr>
                <tr>
                    <td width="110">中心名称:</td>
                    <td width="200">
                        <input name="centerName" class="mini-textbox" required="true"/>
                    </td>
                </tr>
                <tr>
                    <td width="110">地址:</td>
                    <td width="200">
                        <input name="address" class="mini-textbox"/>
                    </td>
                </tr>
                <tr>
                    <td width="110">联系方式:</td>
                    <td width="200">
                        <input name="mobile" class="mini-textbox"/>
                    </td>
                </tr>
                <tr>
                    <td width="110">描述:</td>
                    <td width="200">
                        <input name="description" class="mini-textbox"/>
                    </td>
                </tr>
            </table>
        </div>
        <div property="footer" style="text-align:center;padding:5px;padding-right:15px;">
            <a class="btn btn-info btn-sm" iconCls="icon-add" href="javascript:saveData1()">保存</a>
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <a class="btn btn-danger btn-sm" href="javascript:cancleWin()">取消</a>
        </div>
    </div>

    <div id="win3" class="mini-window" title="修改密码" style="width:400px;height:320px;" showShadow="true"
         showFooter="true" showModal="false" allowDrag="true">
        <div id="editForm3" style="padding: 10px;" align="center">
            <input name="id" class="mini-hidden"/>
            <table>
                <tr>
                    <td width="110">账户:</td>
                    <td width="200">
                        <input name="userName" class="mini-textbox" required="true"/>
                    </td>
                </tr>
                <tr>
                    <td width="110">密码:</td>
                    <td width="200">
                        <input name="password" class="mini-password" required="true"/>
                    </td>
                </tr>
                <tr>
                    <td width="110">确认密码:</td>
                    <td width="200">
                        <input id="configpwd" name="password1" class="mini-password" required="true"/>
                    </td>
                </tr>
            </table>
        </div>
        <div property="footer" style="text-align:center;padding:5px;padding-right:15px;">
            <a class="btn btn-info btn-sm" iconCls="icon-add" href="javascript:savePwd()">保存</a>
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
    var editForm2 = new mini.Form("#editForm2");//编辑
    var editForm3 = new mini.Form("#editForm3");//修改密码
    //绑定表单
    grid.load();
    grid.on("drawcell", function (e) {
        var record = e.record, field = e.field, value = e.value,rowIndex = e.rowIndex;
        if(field=='opt'){
            var state = 1;
            var html = "启用";
            if(record.userState==1){
                state = 2;
                html = "禁用";
            }
            e.cellHtml = '<a href="javascript:editPwd(\''+rowIndex+'\')">修改密码</a>&nbsp;&nbsp;' +
             '<a href="javascript:enableUser(\''+record.id+'\',\''+state+'\')">'+html+'</a>&nbsp;&nbsp;' +
              '<a href="javascript:showDetail(\''+rowIndex+'\')">编辑</a>&nbsp;&nbsp;' +
               '<a href="javascript:deleteCenter(\''+record.id+'\')">删除</a>&nbsp;&nbsp;'
        }
    });

    function showDetail(rowIndex) {
        var win = mini.get("win2");
        win.showAtPos("center", "middle");
        editForm2.setData(grid.getData()[rowIndex])
        editForm2.validate();
    }

    function addData() {
        editForm.reset();
        var win = mini.get("win");
        win.showAtPos("center", "middle");

    }

    function saveData() {
        editForm.validate();
        if (editForm.isValid()) {
            var formData = editForm.getData(true, true);
            if(formData.password!=formData.password1){
                mini.alert("两次密码不一致!");
                return;
            }
            $.ajax({
                url: "${ctx}/base/center/save.do",
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
    function saveData1() {
        editForm2.validate();
        if (editForm2.isValid()) {
            var formData = editForm2.getData(true, true);
            if(formData.password!=formData.password1){
                mini.alert("两次密码不一致!");
                return;
            }
            $.ajax({
                url: "${ctx}/base/center/save.do",
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

    function editPwd(rowIndex){
        var win = mini.get("win3");
        win.showAtPos("center", "middle");
        editForm3.setData(grid.getData()[rowIndex]);
        console.log(grid.getData()[rowIndex])
        mini.get("configpwd").setValue(grid.getData()[rowIndex].password);
        editForm3.validate();
    }

    function savePwd() {
        editForm3.validate();
        if (editForm3.isValid()) {
            var formData = editForm3.getData(true, true);
            if(formData.password!=formData.password1){
                mini.alert("两次密码不一致!");
                return;
            }
            $.ajax({
                url: "${ctx}/base/center/save.do",
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

    //禁用用户,启用用户
    function enableUser(id,state){
        $.ajax({
            url: "${ctx}/base/center/enable.do",
            type: "post",
            data: {
                id:id,
                state:state
            },
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

    //删除中心
    function deleteCenter(id){
        mini.confirm("确定删除记录？", "确定？",
            function (action) {
                if (action == "ok") {
                    $.ajax({
                        url: "${ctx}/base/center/delete.do",
                        data: {
                            id:id
                        },
                        success: function (data) {
                            if(data==1){
                                mini.alert("删除成功!");
                                grid.load();
                            }
                        }
                    })
                }
            }
        );

    }






    function cancleWin() {
        editForm.reset();
        editForm2.reset();
        var win = mini.get("win");
        win.hide();
        var win2 = mini.get("win2");
        win2.hide();
        var win3 = mini.get("win3");
        win3.hide();
    }
</script>
</html>
