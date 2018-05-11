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
    <link rel="stylesheet" href="${ctx}/css/test.css">

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
    <table>
        <tr>
            <td>试验标题<span class="red">*</span></td>
            <td><input type="text" name="name" v-model="data.name"></td>
            <td>中心允许最多人数 <span class="red">*</span></td>
            <td><input type="text" name="centerMax" v-model="data.centerMax"></td>
        </tr>
        <tr>
            <td>试验描述</td>
            <td><textarea v-model="data.description"></textarea></td>
            <td>中心年龄区组最少人数<span class="red">*</span></td>
            <td colspan="3">
                <div v-for="(item,index) in groups">
                    <input type="checkbox" :id="'group'+index" name="group" :value="item.id" v-model="data.group">
                    <lable :for="'group'+index"> {{item.groupName}}</lable>
                    <input  :id="'min'+index" class="groupmin" type="text" name="name">
                </div>

            </td>

        </tr>
        <tr>
            <td>试验人数<span class="red">*</span></td>
            <td colspan="3">
                <input style="height: 30px;width: 300px" type="text" name="name" v-model="data.testCount">
            </td>
        </tr>
        <tr>
            <td>试验中心<span class="red">*</span></td>
            <td colspan="2">
                <div v-for="(item,index) in centers">
                    <input type="checkbox" :id="'center'+index" name="center" :value="item.id" v-model="data.center">
                    <lable :for="'center'+index">{{item.centerName}}</lable>
                </div>
            </td>
            <td>
                <a v-if="showButton" href="javascript:;" @click="saveTest">实验初始化</a>
                <a v-if="!showButton" href="javascript:;" style="background: #ac2925">试验正在进行中。。。</a>
            </td>
        </tr>
    </table>
</div>

</body>

<script>
    var vm = new Vue({
        el: "#main",
        data: {
            centers: [],
            groups: [],
            //数据
            data: {
                name: "",
                centerMax: "",
                testCount: "",
                group:[],
                center:[],
                min:[]
            },
            showButton:true
        },
        created: function () {

        },
        mounted:function(){
            var that = this;
            $.ajax({
                url: "${ctx}/test/getCenterList.do",
                success: function (data) {
                    that.centers = data;
                }

            })

            $.ajax({
                url: "${ctx}/test/getAgeGroupList.do",
                success: function (data) {
                    that.groups = data;
                }

            })
            $.ajax({
                url: "${ctx}/test/currentTest.do",
                success: function (data) {
                    if(data){
                        that.showButton = false;
                        that.groups = data.groups;
                        that.centers = data.centers;
                        that.data.name = data.name;
                        that.data.centerMax = data.centerMax;
                        that.data.testCount = data.testCount;
                        that.data.group = data.group;
                        that.data.center = data.center;
                        for(var i = 0;i<data.min.length;i++){
                            $("#min"+i).val(data.min[i]);
                        }
                        $("input,textarea").attr("disabled",true);

                    }

                }

            })
        },
        methods: {
            saveTest: function () {
                var that = this;
                //校验数据
                console.log(this.data.group)
                if (this.data.name == "" || this.data.centerMax == "" ||
                    this.data.testCount == "") {
                    alert("还有必填项尚未填写");
                    return;
                }
                if(this.data.group.length==0 || this.data.center.length==0){
                    alert("区组或试验中心未选择,请检查数据!");
                    return;
                }
                for(var i=0;i<this.data.group.length;i++){
                    for(var j = 0;j<this.groups.length;j++){
                        if(this.data.group[i]==this.groups[j].id){
                            if($("#min"+i).val()==""){
                                alert("选择的年龄段没有设置最小年龄!");
                                return;
                            }else{
                               this.data.min.push($("#min"+i).val())
                            }
                        }
                    }
                }

                $.ajax({
                    url: "${ctx}/test/createTest.do",
                    type:"post",
                    data: {
                        json: JSON.stringify(that.data),
                        groupIds: that.data.group.join(","),
                        centerIds: that.data.center.join(","),
                        min: that.data.min.join(","),
                        centerMax: that.data.centerMax
                    },
                    success: function (data) {
                        if(data){
                            alert("添加成功!");
                            location.reload();
                        }else{
                            alert("添加失败!")
                        }
                    }

                })
            }
        }
    })
</script>
</html>
