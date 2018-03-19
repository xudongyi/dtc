Date.prototype.Format = function (fmt) { //author: meizz
    var o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "h+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds() //毫秒
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}

////////////////////////////////////////////////////////////////////////////////////////
function getCookie(sName) {
    var aCookie = document.cookie.split("; ");
    var lastMatch = null;
    for (var i = 0; i < aCookie.length; i++) {
        var aCrumb = aCookie[i].split("=");
        if (sName == aCrumb[0]) {
            lastMatch = aCrumb;
        }
    }
    if (lastMatch) {
        var v = lastMatch[1];
        if (v === undefined) return v;
        return unescape(v);
    }
    return null;
}



function setCookie(name, value) {
    var Days = 30;
    var exp = new Date();
    exp.setTime(exp.getTime() + Days * 24 * 60 * 60 * 1000);
    document.cookie = name + "=" + escape(value) + ";expires=" + exp.toGMTString();
}

function delCookie(name)
{
    var exp = new Date();
    exp.setTime(exp.getTime() - 1);
    var cval=getCookie(name);
    if(cval!=null)
        document.cookie= name + "="+cval+";expires="+exp.toGMTString();
}
/******************************************************************************************
 * miniUI的方法集
 *****************************************************************************************/
/**
 * 操作成功的通用提示
 * @author fengjian
 * @param content
 */
function showSuccessTip(content) {
    mini.showTips({
        content : content,
        state : 'success',// default、success、info、warning、danger
        x : 'center',// left、center、right
        y : 'top',// top、center、bottom
        timeout : 3000
    });
}
/**
 * 操作失败的通用提示
 * @author fengjian
 * @param content
 */
function showFailTip(content) {
    mini.showTips({
        content : content,
        state : 'warning',// default、success、info、warning、danger
        x : 'center',// left、center、right
        y : 'top',// top、center、bottom
        timeout : 3000
    });
}
/**
 * 提醒信息的通用提示
 * @author fengjian
 * @param content
 */
function showInfoTip(content) {
    mini.showTips({
        content : content,
        state : 'info',// default、success、info、warning、danger
        x : 'center',// left、center、right
        y : 'top',// top、center、bottom
        timeout : 3000
    });
}
/**
 * 提示信息的通用提示
 * @author fengjian
 * @param message
 */
function showInfoMessageBox(message) {
    var title = "信息";
    var icon = "mini-messagebox-info";
    mini.showMessageBox({
        title : title,
        message : message,
        buttons : [ "ok" ],
        iconCls : icon
    });
}
/**
 * 警告信息的通用提示
 * @author fengjian
 * @param message
 */
function showWarnMessageBox(message) {
    var title = "警告";
    var icon = "mini-messagebox-warn";
    mini.showMessageBox({
        title : title,
        message : message,
        buttons : [ "ok" ],
        iconCls : icon
    });
}
/**
 * 错误信息的通用提示
 * @author fengjian
 * @param message
 */
function showErrorMessageBox(message) {
    var title = "错误";
    var icon = "mini-messagebox-error";
    mini.showMessageBox({
        title : title,
        message : message,
        buttons : [ "ok" ],
        iconCls : icon
    });
}
/**
 * 列表数据加载失败时的提示信息
 * @author fengjian
 * @param message
 */
function showLoadErrorMessageBox() {
    showErrorMessageBox("列表数据加载失败！");
}
/**
 * 绑定二个控件不能一个非空，一个空值
 * @author fengjian
 * @param c1, c2 - 二个控件
 */
function synchronousRequired(c1, c2) {
    if (c1.value) {
        c1.setRequired(true);
        c2.setRequired(true);
    } else if (c2.value) {
        c1.setRequired(true);
        c2.setRequired(true);
    } else {
        c1.setRequired(false);
        c2.setRequired(false);
    }
    c1.validate();
    c2.validate();
}

/**
 * 从列表中根据键名和键值取得对应的所有属性数据
 * @author fengjian
 * @createDate 2015-03-09
 * @param list - 列表
 * @param key - 键名
 * @param val - 键值
 */
function getDetail(list, key, val) {
    for ( var i in list) {
        if (list[i][key] == val) {
            return list[i];
        }
    }
    return null;
}

/**
 * 从列表中根据键名取得对应的键值，拼接成字符串，以逗号隔开
 * @author fengjian
 * @createDate 2015-05-27
 * @param list - 列表
 * @param key - 键名
 */
function getListKeys(list, key) {
    var str = "";
    for (var i = 0; i < list.length; i++) {
        if (list[i][key] != "") {
            str = str + list[i][key] + ',';
        }
    }
    if (str != "") {
        str = str.substr(0, str.length - 1);
    }
    return str;
}

/**
 * 部分更新Json格式数据
 * @author fengjian
 * @createDate 2015-03-09
 * @param source - 源数据
 * @param target - 目标数据
 */
function updateJson(source, target) {
    for ( var key in source) {
        target[key] = source[key];
    }
}

/**
 * 从源到目的更新Json格式数据，根据Key参数是否存在执行更新或新增操作
 * @author fengjian
 * @createDate 2015-03-09
 * @param source - 源数据
 * @param target - 目标数据
 * @param key - 主键
 */
function updateJsonList(source, target, key) {
    for ( var i in source) {
        // 如果源数据中主键存在，则更新此数据至目标对象
        if (source[i][key]) {
            var t = getDetail(target, key, source[i][key]);
            updateJson(source[i], t);
        }
        // 如果源数据中主键不存在，则添加此数据至目标对象
        else {
            target.push(source[i]);
        }
    }
}

/**
 * 设置表单的所有控件是否可编辑
 * @author fengjian
 * @createDate 2015-03-09
 * @param form - 表单对象
 * @param enable - true or false
 */
function setFormControlEnable(form, enable) {
    var controls = form.getFields();
    for (var i = 0; i < controls.length; i++) {
        controls[i].setEnabled(enable);
    }
}

/**
 * 加遮罩层
 * @author fengjian
 * @createDate 2015-03-24
 * @param title - 标题
 */
function miniUIMask(title) {
    mini.mask({
        el : document.body, // 要遮罩的元素
        cls : 'mini-mask-loading', // 图标样式
        html : title
        // 内容
    });
}

/**
 * 取消遮罩层
 * @author fengjian
 * @createDate 2015-03-24
 */
function unMiniUIMask() {
    mini.unmask(document.body);
}