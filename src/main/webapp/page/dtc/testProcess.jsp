<%--
  Created by IntelliJ IDEA.
  User: xudy
  Date: 2018/3/19 0019
  Time: 11:51
  To change this template use File | Settings | File Templates.
--%>
<%@include file="/js/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>试验分组详情</title>
    <script src="${ctx}/js/vue.min.js"></script>
    <script src="${ctx}/js/jquery-1.9.1.min.js"></script>
    <script src="${ctx}/js/common.js"></script>
    <style>
        body, html {
            margin: 0;
            padding: 0;
            background: #F3F2F0;
        }

        table {
            width: 90%;
            margin: auto;
            margin-top: 10px;
            border-collapse: collapse;
        }

        thead td {
            font-weight: bold;
        }

        table tr td {
            text-align: center;
            height: 35px;
        }

        tr:nth-last-child(2) {
            border-top:3px solid #cccccc;
        }

    </style>
</head>
<body>
<div id="main" v-cloak>
    <div style="width: 90%;margin: auto;margin-top:10px;">
        <span style="font-weight: bold">试验进展报告</span>
        &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
        <span>{{new Date(test.createTime).Format('yyyy/MM/dd hh:mm:ss')}}  至 {{new Date().Format('yyyy/MM/dd hh:mm:ss')}}</span>
    </div>
    <table border="1" bordercolor="#cccccc">
        <thead>
        <tr>
            <td v-for="item in header">{{item}}</td>
        </tr>
        </thead>
        <tr v-for="item in body">
            <td v-for="item1 in item">{{item1}}</td>
        </tr>

    </table>

</div>

</body>
<script>
    var vm = new Vue({
        el: "#main",
        data: {
            header: [],
            body: [],
            test:{}
        },
        created: function () {
            var that = this;
            $.ajax({
                url: "${ctx}/test/testProcess.do",
                success: function (data) {
                    that.header = data.header;
                    that.body = data.body;
                }
            })
            $.ajax({
                url: "${ctx}/test/current.do",
                success: function (data) {
                    that.test = data;
                }
            })
        },
        methods: {}
    })
</script>
</html>
