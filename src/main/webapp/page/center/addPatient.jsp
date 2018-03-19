<%--
  Created by IntelliJ IDEA.
  User: xudy
  Date: 2018/3/18 0018
  Time: 14:47
  To change this template use File | Settings | File Templates.
--%>
<%@include file="/js/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>初始化</title>
    <script src="${ctx}/js/vue.min.js"></script>
    <script src="${ctx}/js/jquery-1.9.1.min.js"></script>
    <link rel="stylesheet" href="${ctx}/css/addPatient.css">

    <style>
        [v-cloak]{
            display: none;
        }
        body, html {
            margin: 0;
            padding: 0;
            background: #F3F2F0;
        }

        table tr td:first-of-type {

        }
    </style>
</head>
<body>
<div id="main" v-cloak>

</div>

</body>

<script>
    var vm = new Vue({
        el: "#main",
        data: {
        },
        created: function () {
            var that = this;

        },
        methods: {

        }
    })
</script>
</html>
