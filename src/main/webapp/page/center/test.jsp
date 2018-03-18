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

    <style>
        body,html{
            margin: 0;
            padding: 0;
        }
        table tr td:first-of-type{

        }
    </style>
</head>
<body>
    <div id="main">
        <table>
            <tr>
                <td>试验标题</td>
                <td><input type="text" name="name"></td>
                <td>中心允许最多人数</td>
                <td><input type="text" name="name"></td>
            </tr>
            <tr>
                <td>试验描述</td>
                <td><input type="text" name="name"></td>
                <td>中心年龄区组最少人数</td>
                <td><input type="text" name="name"></td>
            </tr>
            <tr>
                <td>试验人数</td>
                <td colspan="3"><input type="text" name="name"></td>
            </tr>
            <tr>
                <td>试验中心</td>
                <td colspan="3"><input type="text" name="name"></td>
            </tr>
        </table>
    </div>

</body>

<script>
/*    json
    groupIds
    centerIds
    min
    centerMax*/
    var vm = new Vue({
        el: "#main",
        data: {

        },
        created: function() {},
        methods:{
            saveTest:function(){
                $.ajax({
                    url:"${ctx}/test/createTest.do",
                    data:{
                        json:"",
                        groupIds:"",
                        centerIds:"",
                        min:"",
                        centerMax:"",
                    },
                    success:function(data){

                    }

                })
            }
        }
    })
</script>
</html>
