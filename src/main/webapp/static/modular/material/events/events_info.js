/**
 * 初始化大事记管理详情对话框
 */
var EventsInfoDlg = {
    eventsInfoData : {},
    validateFields: {
        title: {
            validators: {
                notEmpty: {
                    message: '事记标题不能为空'
                },
                // regexp: {//正则验证
                //     regexp: /^[\u4e00-\u9fa5]{2,40}$/,
                //     message: '2-40字符'
                // }
            }
        }
    }
};

var imagesData=[];

/**
 * 清除数据
 */
EventsInfoDlg.clearData = function() {
    this.eventsInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
EventsInfoDlg.set = function(key, val) {
    this.eventsInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
EventsInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
EventsInfoDlg.close = function() {
    parent.layer.close(window.parent.Events.layerIndex);
}

/**
 * 收集数据
 */
EventsInfoDlg.collectData = function() {
    this.set('eventYear').set('eventMonth').set('eventDay').set('title').set('content');
}

/**
 * 收集数据
 */
EventsInfoDlg.collectEditData = function() {
    this.set('id').set('eventYear').set('eventMonth').set('eventDay').set('title').set('content');
}

/**
 * 验证数据是否为空
 */
EventsInfoDlg.validate = function () {
    $('#EventsForm').data("bootstrapValidator").resetForm();
    $('#EventsForm').bootstrapValidator('validate');
    return $("#EventsForm").data('bootstrapValidator').isValid();
};

/**
 * 提交添加
 */
EventsInfoDlg.addSubmit = function() {
    if (!this.validate()) {
        return;
    }

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/events/add", function(data){
        if(data.code==200){
            Feng.success("添加成功!");
            window.parent.Events.table.refresh();
            EventsInfoDlg.close();
        }else{
            Feng.error("添加失败!" + data.message + "!");
        }

    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.eventsInfoData);
    var c=imagesData.join(",");
    ajax.set("images",c);
    ajax.start();

}

/**
 * 提交修改
 */
EventsInfoDlg.editSubmit = function() {
    if (!this.validate()) {
        return;
    }
    this.clearData();
    this.collectEditData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/events/update", function(data){
        if(data.code==200){
            Feng.success("修改成功!");
            window.parent.Events.table.refresh();
            EventsInfoDlg.close();
        }else{
            Feng.error("修改失败!" + data.message + "!");
        }
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.eventsInfoData);
    var c=imagesData.join(",");
    ajax.set("images",c);
    ajax.start();
}

EventsInfoDlg.Statistics=function(obj){
    var text1=$(obj).val();
    var counter1=text1.length;
    $(obj).siblings('span').find('i').text(counter1);
    $(obj).on('blur keyup input',function(){
        var text=$(obj).val();
        var counter=text.length;
        $(obj).siblings('span').find('i').text(counter);
    });
}

function getObjectURL(file) {
    var url = null ;
    if (window.createObjectURL!=undefined) { // basic
        url = window.createObjectURL(file) ;
    } else if (window.URL!=undefined) { // mozilla(firefox)
        url = window.URL.createObjectURL(file) ;
    } else if (window.webkitURL!=undefined) { // webkit or chrome
        url = window.webkitURL.createObjectURL(file) ;
    }
    return url ;
}

function delImg(obj){
    $('#images').parents('li').show();
    var filePath=$(obj).parent('li').attr('data-src');
    var index=$(obj).parent('li').index();
    var ajax = new $ax(Feng.ctxPath + "/fastDfs/delete", function (data) {
        if(data.code==200){
            $(obj).parent('.div-img').remove();
            imagesData.splice(index-1,1);
        }else{
            Feng.error("删除失败!" + data.message + "!");
        }
    }, function (data) {
        Feng.error("删除失败!" + data.responseJSON.message + "!");
    });
    ajax.set("filePath",filePath);
    ajax.start();
}

// 设置每个月份的天数
function setDays(year,month,day) {
    var monthDays = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];
    var yea = year.options[year.selectedIndex].value;
    var mon = month.options[month.selectedIndex].value;
    var num = monthDays[mon - 1];
    if (mon == 2 && isLeapYear(yea)) {
        num++;
    }
    for (var j = day.options.length - 1; j >=num; j--) {
        day.remove(j);
    }
    for (var i = day.options.length; i <= num; i++) {
        addOption(eventDay,i+"日",i);
    }
}

// 判断是否闰年
function isLeapYear(year)
{
    return (year % 4 == 0 || (year % 100 == 0 && year % 400 == 0));
}

// 向select尾部添加option
function addOption(selectbox, text, value) {
    var option = document.createElement("option");
    option.text = text;
    option.value = value;
    selectbox.options.add(option);
}

$(function() {
    EventsInfoDlg.Statistics('#content');
    EventsInfoDlg.Statistics('#title');
    Feng.initValidator("EventsForm", EventsInfoDlg.validateFields);

    $("#images").change(function(){
        if(imagesData.length<3){
            $('#images').parents('li').show();
            var self = this;
            var file = self.files[0];
            if(!(new RegExp('\.(jpeg|jpg|png)$', 'i')).test(file.name)){
                Feng.error('图片格式不对');
                $(self).val('');
                return false;
            }
            var fileData = new FormData();
            fileData.append('file', file);
            fileData.append('maxSize', 5 * 1024 * 1024);
            var imgSrcI = getObjectURL(file);
            $.ajax({
                type: "post",
                url: Feng.ctxPath + "/fastDfs/upload",
                dataType: "json",
                async: false,
                data: fileData,
                processData: false,
                contentType:false,
                success: function(data) {
                    if(data.code==200){
                        var divImg=$(' <li class="div-img"  data-src="'+data.data+'">' +
                            ' <img src="'+imgSrcI+'" alt="图片缺失">' +
                            ' <button onclick="delImg(this)"><i class="fa fa-close"></i></button>' +
                            '</li>')
                        $("#images").parents('ul').append(divImg);
                        imagesData.push(data.data);
                        $('#images').parents('.form-group').find('.help-block').remove();
                        $('#images').parents('.form-group').addClass('has-success').removeClass('has-error');
                        if(imagesData.length==3){
                            $('#images').parents('li').hide();
                        }
                    }else{
                        Feng.info(data.message);
                    }
                },
                error: function(data) {
                    Feng.error("上传失败!" + data.responseJSON.message + "!");
                }
            });
        }
    });

    var i = -1;
    var date = new Date();
    var curYear=date.getFullYear();
    var curMonth = date.getMonth() + 1;

    // 添加年份，从2016年开始
    for (i = 2016; i <= new Date().getFullYear(); i++) {
        addOption(eventYear, i+"年", i);
        // 默认选中当前年份
        if (i == curYear) {
            eventYear.options[i-2016].selected = true;
        }
    }
    // 添加月份
    for (i = 1; i <= 12; i++) {
        addOption(eventMonth, i+"月", i);
        if (i == curMonth) {
            eventMonth.options[i-1].selected = true;
        }
    }
    // 添加天份，先默认31天
    for (i = 1; i <= 31; i++) {
        addOption(eventDay, i+"日", i);
    }
    setDays(eventYear,eventMonth,eventDay)

});
