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
    <script src="${ctx}/js/boot.js"></script>
    <script src="${ctx}/js/vue.min.js"></script>
    <script src="${ctx}/js/laydate/laydate.js"></script>
    <link rel="stylesheet" href="${ctx}/css/addPatient.css">

    <style>
        [v-cloak]{
            display: none;
        }
        body, html {
            margin: 0;
            padding: 0;
            background: #F3F2F0;
            font-family:"微软雅黑","microsoft yahei";
        }

        table{
            font-size:13px;
        }
    </style>
</head>
<body>
<div id="main" v-cloak>
    <div class="main_left">
        <div class="left_title">
            {{test.name}}
        </div>
        <ul style="margin-top:10px;font-weight: bold;border-bottom: 1px solid #999999">
            <li>区组类型</li>
            <li>试验最少人数</li>
            <li>已进组人数</li>
        </ul>
        <ul class="body_class" v-for="(item,index) in info.info" style="height: 35px;line-height: 35px;border-bottom: 1px dotted #999999">
            <li>{{item.GROUP_NAME}}</li>
            <li>{{item.MIN_COUNT}}</li>
            <li>{{item.counts}}</li>
        </ul>

        <ul style="font-weight: bold;">
            <li>总计</li>
            <li>{{info.total[0]}}</li>
            <li>{{info.total[1]}}</li>
        </ul>
        <div class="bottom_title">
            中心允许最多人数:{{info.centerInfo.centerMax}}
        </div>
    </div>
    <div class="main_center">
        <table>
            <tr>
                <td><span class="red">*</span>被试者姓名</td>
                <td>
                    <input type="text" v-model="formData.name">
                </td>
            </tr>
            <tr>
                <td><span class="red">*</span>被试者性别</td>
                <td>
                    <select name="" v-model="formData.sex">
                        <option value="">--请选择--</option>
                        <option value="1">男</option>
                        <option value="2">女</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td><span class="red">*</span>出生年月</td>
                <td>
                    <div class="layui-inline"> <!-- 注意：这一层元素并不是必须的 -->
                        <input v-model="formData.birthday"  type="text" id="birthday" class="layui-input">
                    </div>

                </td>
            </tr>
            <tr>
                <td><span class="red">*</span>年龄(周岁)</td>
                <td>
                    <input type="number" v-model="formData.age" @blur="ageBlur">
                </td>
            </tr>
            <tr>
                <td>婚姻状况</td>
                <td>
                    <select name="" v-model="formData.isMarry">
                        <option value="">--请选择--</option>
                        <option value="1">已婚</option>
                        <option value="2">未婚</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td>联系方式</td>
                <td>
                    <input type="text" v-model="formData.mobile">
                </td>
            </tr>
            <tr>
                <td>住址</td>
                <td>
                    <input type="text" v-model="formData.address">
                </td>
            </tr>
            <tr>
                <td><span class="red">*</span>选号操作者</td>
                <td>
                    <input type="text" readonly value="${sessionScope.get("user").realName}">
                </td>
            </tr>
            <tr>
                <td></td>
                <td>
                    <a href="javascript:;" @click="addPatient" class="button-add">参与试验</a>
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
            info:{total:[0,0],centerInfo:{centerMax:0}},
            formData:{

            },
            test:{}//测试信息
        },
        created: function () {
            var that = this;
            $.ajax({
                url:"${ctx}/center/getCenterInfo.do",
                success:function(data){
                    that.info = data;
                }
            })

            $.ajax({
                url:"${ctx}/test/current.do",
                success:function(data){
                    that.test = data;
                }
            })
        },
        mounted:function(){
            var that = this;
            //常规用法
            laydate.render({
                elem: '#birthday',
                done: function(value, date, endDate){
                    that.formData.birthday = value;
                    if(that.formData.age && that.formData.age!=""){
                        $.ajax({
                            url:"${ctx}/center/checkAgeWithBirthday.do",
                            data:{
                                age:that.formData.age,
                                birthday:value
                            },
                            success:function(data){
                                if(!data){
                                    $("#birthday").val("");
                                    that.formData.birthday = "";
                                    mini.alert("选择的生日和年龄不匹配,请检查数据!");
                                }
                            }
                        })
                    }
                }
            });
        },
        methods: {
            ageBlur:function(){
                var that = this;
                if(that.formData.age && that.formData.age!=""
                    && that.formData.birthday && that.formData.birthday!=""){
                    $.ajax({
                        url:"${ctx}/center/checkAgeWithBirthday.do",
                        data:{
                            age:that.formData.age,
                            birthday:that.formData.birthday
                        },
                        success:function(data){
                            if(!data){
                                that.formData.age = "";
                                mini.alert("生日和年龄不匹配,请检查数据!");
                            }
                        }
                    })
                }

            },
            checkInput:function(){
                var check = checkInputFromData(this.formData, ["name","sex","age","birthday"]);
                return check;
            },
            addPatient:function(){
                var that = this;
                if(this.checkInput()){
                    this.formData.centerId = '${sessionScope.get("user").id}';
                    this.formData.birthday =  new Date(this.formData.birthday.replace(/-/,"/"));
                    $.ajax({
                        url:"${ctx}/center/addPatient.do",
                        type:"post",
                        data:that.formData,
                        success:function(data){
                            if(data>0){
                                mini.alert("添加成功!试验号为:"+data,"提示",function(msg){
                                    location.reload();
                                });
                            }else{
                                that.clearForm();
                                mini.alert("添加失败!")
                            }
                        }
                    })
                }else{
                    mini.alert("必填项没有填写")
                }
            },
            clearForm:function(){
                this.formData = {};
                $("#birthday").val("")
            }
        }
    })
</script>
</html>
