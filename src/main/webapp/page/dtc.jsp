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
    <title>Center Random 临床试验中央随机系统</title>
    <script src="${ctx}/js/boot.js" type="text/javascript" charset="utf-8"></script>
    <script src="${ctx}/js/vue.min.js" type="text/javascript" charset="utf-8"></script>
    <link rel="stylesheet" type="text/css" href="${ctx}/css/dtc.css" />
    <style>
        .level2 {
            position: absolute;
            height: 40px;
            width: 100%;
            left: 0;
            top: 58px;
            background: #f8f8f8;
            border-bottom: 1px solid #d1d7d8;
        }

        .level2 li {
            float: left;
            height: 40px;
            line-height: 40px;
            width: 70px;
            margin: 0px 15px;
            text-align: center;
            font-weight: bold;
            font-family: "微软雅黑";
            font-size: 13px;
        }

        .level2 .level2-active {
            height: 38px;
            line-height: 40px;
            color: #2196f3;
            border-bottom: 2px solid #2196f3;
        }
    </style>
</head>
<body>
<div id="app">
    <div class="header">
        <div class="left-title">CenterRandom临床试验中央随机系统</div>
        <div class="middle-tab">
            <ul class="level1">
                <li v-for="(ele,index) in tabList" :class="{active:tabIndex==index}">
							<span @click="levelOneClick(ele.url,index,ele.level2)">
								<img :src="ele.img" alt="" />
								<div>{{ele.name}}</div>
							</span>
                    <ul class="level2" v-if="(ele.level2.length>0&&ele.show)?true:false">
                        <li v-for="(elel,index1) in ele.level2" :class="{'level2-active':tabIndex2==index1}" @click="levelTwoClick(elel.url,index1)">{{elel.name}}</li>
                    </ul>
                </li>
            </ul>
            <div class="user">
                <div class="left-button">
                    <img src="${ctx}/images/dtc/a-xgmm.png" alt="" />
                    <img src="${ctx}/images/dtc/b-help.png" alt="" />
                    <img src="${ctx}/images/dtc/c-tuic.png" alt="" @click="loginOut"/>
                </div>

                <div class="right-info">
                    <img src="${ctx}/images/dtc/toux.png" alt="" />
                    <div class="detail">
                        <div>欢迎您，${sessionScope.get("user").realName}</div>
                        <div>{{dateTime}}</div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="content" id="main" :class="[tabLevel==1?'iframeOne':'',tabLevel==2?'iframeTwo':'']"><iframe id="iframe-main" :src="initSrc"></iframe></div>
    <div class="footer">Power By  汇聚软件有限公司  Copyright @ 2017-2018 All rights Reserved</div>
</div>
</body>
<script type="text/javascript">
    var vm = new Vue({
        el: "#app",
        data: {
            tabIndex: 0,
            tabIndex2: 0,
            tabLevel: 0,
            dateTime: new Date().Format("yyyy-MM-dd hh:mm:ss"),
            tabList: [],
            initSrc: "",
        },
        created: function() {
            //获取顶部菜单数据
            var json = [{
                name: "试验初始化",
                url: "http://www.baidu.com",
                img: "${ctx}/images/dtc/1-GISdt.png",
                parent_id: "",
                id: 1
            }, {
                name: "试验分组详情",
                url: "http://www.baidu.com",
                img: "${ctx}/images/dtc/2-jcsj.png",
                parent_id: "",
                id: 2

            }, {
                name: "试验进展报告",
                url: "http://www.baidu.com",
                img: "${ctx}/images/dtc/3-tjfx.png",
                parent_id: "",
                id: 3
            }, {
                name: "基础信息维护",
                url: "",
                img: "${ctx}/images/dtc/4-cbbj.png",
                parent_id: "",
                id: 4
            },{
                name: "中心维护",
                url: "http://www.baidu.com",
                img: "${ctx}/images/dtc/4-cbbj.png",
                parent_id: 4,
                id: 41
            },{
                name: "年龄组维护",
                url: "http://www.baidu.com",
                img: "${ctx}/images/dtc/4-cbbj.png",
                parent_id: 4,
                id: 42
            },{
                name: "系统设置",
                url: "http://www.baidu.com",
                img: "${ctx}/images/dtc/6-xtgl.png",
                parent_id: "",
                id: 5
            }];
            //数据整理
            var level1 = [];
            for(var i = 0; i < json.length; i++) {
                if(json[i].parent_id == "" || json[i].parent_id == null) {
                    json[i].show = false;
                    level1.push(json[i]);
                }
            }
            for(var i = 0; i < level1.length; i++) {
                var level2 = [];
                for(var j = 0; j < json.length; j++) {
                    if(level1[i].id == json[j].parent_id) {
                        level2.push(json[j]);
                    }
                }
                level1[i].level2 = level2;
            }

            //首页页面渲染
            //判断首页默认展示的是一级还是二级
            this.tabList = level1;
            if(this.tabList[0].level2 > 0) {
                this.tabLevel = 2;
                this.initSrc = this.tabList.level2[0].url;
            } else {
                this.tabLevel = 1;
                this.initSrc = this.tabList[0].url;
            }
        },
        mounted: function() {
            //初始化时间
            var that = this;
            setInterval(function() {
                that.dateTime = new Date().Format("yyyy-MM-dd hh:mm:ss")
            }, 1000);
        },
        methods: {
            levelOneClick: function(url, index, level) {
                this.clearLevel1();
                this.tabIndex2 = 0;
                this.tabIndex = index;
                if(level.length > 0) {
                    Vue.set(this.tabList[index], "show", true);
                    this.tabLevel = 2;
                    this.initSrc = this.tabList[index].level2[0].url+"?d="+new Date().getTime();
                } else {
                    this.tabLevel = 1;
                    Vue.set(this.tabList[index], "show", false);
                    this.initSrc = url+"?d="+new Date().getTime();
                }
            },
            levelTwoClick: function(url, index) {
                this.tabIndex2 = index;
                this.initSrc = url;
            },
            clearLevel1: function() {
                for(var i = 0; i < this.tabList.length; i++) {
                    if(this.tabList[i].show) {
                        Vue.set(this.tabList[i], "show", false);
                    }
                }
            },
            loginOut:function(){
                $.ajax({
                    url:"${ctx}/user/loginOut.do",
                    success:function(data){
                        if(data){
                            location.href = "${ctx}/page/login.jsp";
                        }
                    }
                })
            }
        },
    })
</script>
</html>
