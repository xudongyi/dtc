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
    <title>修改密码</title>
    <link rel="stylesheet" href="${ctx}/css/editPwd.css">
    <script src="${ctx}/js/vue.min.js"></script>
    <script src="${ctx}/js/common.js"></script>
    <script src="${ctx}/js/jquery-1.9.1.min.js"></script>
</head>
<body>

<div id="main">
    <div class="main_body">
        <input type="hidden" value="${sessionScope.get("user").id}" name="id" v-model="center.id">
        <table>
            <tr>
                <td>账户名</td>
                <td><input type="userName" v-model="center.userName" disabled></td>
            </tr>
            <tr>
                <td><span class="red">*</span>原密码</td>
                <td><input type="password" v-model="center.password"></td>
            </tr>
            <tr>
                <td><span class="red">*</span>新密码</td>
                <td><input type="password" v-model="center.newpwd"></td>
            </tr>
            <tr>
                <td><span class="red">*</span>确认密码</td>
                <td><input type="password" v-model="center.configpwd"></td>
            </tr>
            <tr>
                <td></td>
                <td>
                    <a href="javascript:;" @click="saveData" class="button-add">修改密码</a>
                </td>
            </tr>
        </table>
    </div>
</div>
</body>
<script>
    var vm = new Vue({
        el: "#main",
        data: {
            center: {
                id:'${sessionScope.get("user").id}',
                userName:'${sessionScope.get("user").userName}'
            }
        },
        created: function () {
        },
        methods: {
            saveData: function () {

                if(!checkInputFromData(this.center,["password","newpwd","configpwd"])){
                    alert("输入项不能为空!")
                    return;
                }
                if(this.center.newpwd!=this.center.configpwd){
                    alert("两次输入的密码不一致!")
                    return;
                }
                var that = this;
                $.ajax({
                    url: "${ctx}/user/changePwd.do",
                    data: that.center,
                    success: function (data) {
                        if (data > 0) {
                            alert("密码修改成功!")
                            location.reload();
                        } else {
                            alert("密码修改失败!");
                        }
                    }
                });
            }
        }});
</script>
</html>
