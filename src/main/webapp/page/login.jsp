<%--
  Created by IntelliJ IDEA.
  User: xudy
  Date: 2018/3/14 0014
  Time: 10:48
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@include file="/js/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>欢迎您,请登录Center Random 临床试验中央随机系统</title>
    <link rel="stylesheet" href="${ctx}/css/login.css">
    <script src="${ctx}/js/vue.min.js"></script>
    <script src="${ctx}/js/jquery-1.9.1.min.js"></script>
    <script src="${ctx}/js/common.js"></script>
</head>
<body>

    <div class="main" id="main">
        <div class="main-top">
            Center Random 临床试验中央随机系统
        </div>
        <div class="main-body">
            <div class="body-input">
                <i class="icon-user"></i>
                <input type="text" name="username" v-model="user.username">
            </div>
            <div class="body-input">
                <i class="icon-pass"></i>
                <input type="password" name="password" v-model="user.password">
            </div>
            <a href="javascript:;" @click="login">登&nbsp;&nbsp;&nbsp;&nbsp;录</a>
        </div>
        <div class="main-bottom">
            <input type="checkbox" value="true" v-model="remember">
            记住用户名和密码
        </div>
    </div>
    <div class="bottom">
        Power By  汇聚软件有限公司  Copyright @ 2017-2018 All rights Reserved
    </div>
</body>

<script>

    var app = new Vue({
        el: '#main',
        data: {
            user: {
                username:'',
                password:''
            },
            remember:false
        },
        created:function(){
            if(getCookie("remember")){
                console.log("initCache...............")
                this.remember = true;
                var cacheUser = JSON.parse(getCookie("cacheUser"));
                this.user.username = cacheUser.userName;
                this.user.password = cacheUser.password;
            }
        },
        methods:{
            login:function(){
                var that = this;
                if(this.user.username=="" || this.user.password==''){
                    alert("用户名或密码不能为空!");
                    return;
                }
                $.ajax({
                    url:"${ctx}/user/login.do",
                    data:this.user,
                    success:function(data){
                        if(data.code!=200){
                            alert(data.msg);
                            return;
                        }else{
                            if(that.remember){
                                console.log("setCoookie......................")
                                setCookie("remember",true);
                                setCookie("cacheUser",JSON.stringify(data.data))
                            }else{
                                console.log("delCookie......................")
                                delCookie("remember");
                                delCookie("cacheUser");
                            }
                            if(data.data.role ==1){
                                //管理员
                                location.href ="${ctx}/page/dtc.jsp";
                            }else if(data.data.role ==2){
                                //中心
                                location.href ="${ctx}/page/dtcCenter.jsp";
                            }

                        }
                    }
                })
            }
        }
    })
</script>
</html>
