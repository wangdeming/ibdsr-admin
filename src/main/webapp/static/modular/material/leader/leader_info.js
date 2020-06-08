/**
 * 初始化领导关怀管理详情对话框
 */
var LeaderInfoDlg = {
    leaderInfoData : {},
    validateFields: {
        leaderName: {
            validators: {
                notEmpty: {
                    message: '姓名不能为空'
                },
                regexp: {//正则验证
                    regexp: /^[\u4E00-\u9FA5A-Za-z0-9_]{2,40}$/,
                    message: '2-40字符'
                }
            }
        },
        content: {
            validators: {
                notEmpty: {
                    message: '新闻内容不能为空'
                }
            }
        },
        images: {
            validators: {
                notEmpty: {
                    message: '图片不能为空'
                }
            }
        },
    }
};
var imagesData=[];

/**
 * 清除数据
 */
LeaderInfoDlg.clearData = function() {
    this.leaderInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
LeaderInfoDlg.set = function(key, val) {
    this.leaderInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
LeaderInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
LeaderInfoDlg.close = function() {
    parent.layer.close(window.parent.Leader.layerIndex);
}

/**
 * 收集新增数据
 */
LeaderInfoDlg.collectData = function() {
    this.set('leaderName').set('content').set('isTop');
}


/**
 * 收集编辑数据
 */
LeaderInfoDlg.collectEditData = function() {
    this.set('id').set('leaderName').set('content').set('isTop');
}


/**
 * 验证数据是否为空
 */
LeaderInfoDlg.validate = function () {
    $('#LeaderForm').data("bootstrapValidator").resetForm();
    $('#LeaderForm').bootstrapValidator('validate');
    return $("#LeaderForm").data('bootstrapValidator').isValid();
};

/**
 * 提交添加
 */
LeaderInfoDlg.addSubmit = function() {
    var showTimeType=$('.radio input[type="radio"]:checked').val();
    var showTime;
    if(showTimeType==0){
        showTime=getNowFormatDate();
    }else{
        showTime=$('#customTime').val();
    }

    if (!this.validate()) {
        if(imagesData.length==0){
            $('#images').parents('.form-group').find('.help-block').remove();
            $('#images').parents('.form-group').addClass('has-error');
            var tip=$('<small class="help-block" data-bv-validator="notEmpty" data-bv-for="title" data-bv-result="INVALID" style="">' +
                '封面图片不能为空' +
                '</small>')
            $('#images').parents('.upImg-div').append(tip);
        }
        if(!showTime){
            $('#customTime').parents('.form-group').find('.help-block').remove();
            $('#customTime').parents('.form-group').addClass('has-error');
            var tip=$('<small class="help-block" data-bv-validator="notEmpty" data-bv-for="title" data-bv-result="INVALID" style="">' +
                '展示时间不能为空' +
                '</small>')
            $('#customTime').parents('.radio').after(tip);
        }
        return;
    }else{
        if(imagesData.length==0){
            $('#images').parents('.form-group').find('.help-block').remove();
            $('#images').parents('.form-group').addClass('has-error');
            var tip=$('<small class="help-block" data-bv-validator="notEmpty" data-bv-for="title" data-bv-result="INVALID" style="">' +
                '封面图片不能为空' +
                '</small>')
            $('#images').parents('.upImg-div').append(tip);
            return;
        }
        if(!showTime){
            $('#customTime').parents('.form-group').find('.help-block').remove();
            $('#customTime').parents('.form-group').addClass('has-error');
            var tip=$('<small class="help-block" data-bv-validator="notEmpty" data-bv-for="title" data-bv-result="INVALID" style="">' +
                '展示时间不能为空' +
                '</small>')
            $('#customTime').parents('.radio').after(tip);
            return;
        }
    }

    this.clearData();
    this.collectData();


    var c=imagesData.join(",");
    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/leader/add", function(data){

        if(data.code==200){
            var id=data.data;
            parent.layer.confirm('保存成功，是否立即发布？', {
                btn: ['是','否'] //按钮
            }, function(){
                var ajax = new $ax(Feng.ctxPath + "/leader/publish", function (data) {
                    if(data.code==200){
                        Feng.success("发布成功!");
                        window.parent.Leader.table.refresh();
                        LeaderInfoDlg.close();
                    }else{
                        Feng.error("发布失败!" + data.message + "!");
                    }
                }, function (data) {
                    Feng.error("发布失败!" + data.responseJSON.message + "!");
                });
                ajax.set("leaderId",id);
                ajax.start();
            }, function(){
                Feng.success("添加成功!");
                window.parent.Leader.table.refresh();
                LeaderInfoDlg.close();
            });
        }else{
            Feng.error("添加失败!" + data.message + "!");
        }

    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.leaderInfoData);
    if(showTimeType==0){
        ajax.set("showTime",'');
    }else{
        ajax.set("showTime",showTime);
    }
    ajax.set("images",c);
    ajax.start();
}

/**
 * 提交修改
 */
LeaderInfoDlg.editSubmit = function() {
    var showTimeType=$('.radio input[type="radio"]:checked').val();
    var showTime;
    if(showTimeType==0){
        showTime=getNowFormatDate();
    }
    if(showTimeType==1){
        showTime=$('#customTime').val();
    }
    if(showTimeType==2){
        showTime=$('#showTime').text();
    }

    if (!this.validate()) {
        if($('.upImg-div ul li').length==1){
            $('#imagesEdit').parents('.form-group').find('.help-block').remove();
            $('#imagesEdit').parents('.form-group').addClass('has-error');
            var tip=$('<small class="help-block" data-bv-validator="notEmpty" data-bv-for="title" data-bv-result="INVALID" style="">' +
                '封面图片不能为空' +
                '</small>')
            $('#imagesEdit').parents('.upImg-div').append(tip);
        }
        if(!showTime){
            $('#customTime').parents('.form-group').find('.help-block').remove();
            $('#customTime').parents('.form-group').addClass('has-error');
            var tip=$('<small class="help-block" data-bv-validator="notEmpty" data-bv-for="title" data-bv-result="INVALID" style="">' +
                '展示时间不能为空' +
                '</small>')
            $('#customTime').parents('.radio').after(tip);
        }
        return;
    }else{
        if($('.upImg-div ul li').length==1){
            $('#imagesEdit').parents('.form-group').find('.help-block').remove();
            $('#imagesEdit').parents('.form-group').addClass('has-error');
            var tip=$('<small class="help-block" data-bv-validator="notEmpty" data-bv-for="title" data-bv-result="INVALID" style="">' +
                '封面图片不能为空' +
                '</small>')
            $('#imagesEdit').parents('.upImg-div').append(tip);
            return;
        }
        if(!showTime){
            $('#customTime').parents('.form-group').find('.help-block').remove();
            $('#customTime').parents('.form-group').addClass('has-error');
            var tip=$('<small class="help-block" data-bv-validator="notEmpty" data-bv-for="title" data-bv-result="INVALID" style="">' +
                '展示时间不能为空' +
                '</small>')
            $('#customTime').parents('.radio').after(tip);
            return;
        }
    }

    this.clearData();
    this.collectEditData();
    var c=imagesData.join(",");

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/leader/update", function(data){
        if(data.code==200){
            Feng.success("修改成功!");
            window.parent.Leader.table.refresh();
            LeaderInfoDlg.close();
        }else{
            Feng.error("修改失败!" + data.message + "!");
        }

    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.leaderInfoData);
    if(showTimeType==0){
        ajax.set("showTime",'');
    }else{
        ajax.set("showTime",showTime);
    }
    // ajax.set("showTime",showTime);
    ajax.set("images",c);
    ajax.start();
}

LeaderInfoDlg.Statistics=function(obj){
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
            if(imagesData.length==0){
                $('#images').parents('.form-group').find('.help-block').remove();
                $('#images').parents('.form-group').addClass('has-error');
                var tip=$('<small class="help-block" data-bv-validator="notEmpty" data-bv-for="title" data-bv-result="INVALID" style="">' +
                    '封面图片不能为空' +
                    '</small>')
                $('#images').parents('.upImg-div').append(tip);
            }
        }else{
            Feng.error("删除失败!" + data.message + "!");
        }
    }, function (data) {
        Feng.error("删除失败!" + data.responseJSON.message + "!");
    });
    ajax.set("filePath",filePath);
    ajax.start();
}

function getNowFormatDate() {
    var date = new Date();
    var seperator1 = "-";
    var seperator2 = ":";
    var month = date.getMonth() + 1;
    var strDate = date.getDate();
    if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
    }
    var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate
        + " " + date.getHours() + seperator2 + date.getMinutes()
        + seperator2 + date.getSeconds();
    return currentdate;
}


$(function() {
    var r_data = {
        elem: '#customTime',
        type:'datetime',
        format: 'yyyy-MM-dd HH:mm:ss',
        max:getNowFormatDate(),
        done:function(value, date, endDate){
            if(value){
                $('#customTime').parents('.form-group').find('.help-block').remove();
                $('#customTime').parents('.form-group').addClass('has-success').removeClass('has-error');
            }else{
                $('#customTime').parents('.form-group').find('.help-block').remove();
                $('#customTime').parents('.form-group').addClass('has-error');
                var tip=$('<small class="help-block" data-bv-validator="notEmpty" data-bv-for="title" data-bv-result="INVALID" style="">' +
                    '展示时间不能为空' +
                    '</small>')
                $('#customTime').parents('.radio').after(tip);
            }
        }
    };
    laydate.render(r_data);
    Feng.initValidator("LeaderForm", LeaderInfoDlg.validateFields);
    LeaderInfoDlg.Statistics('#content');
    $("#images").change(function(){
        if(imagesData.length<9){
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
                        if(imagesData.length==9){
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

    $('input[type=radio]').change(function() {
        var type=$(this).val();
        if(type==0||type==2){
            $('#customTime').parents('.form-group').find('.help-block').remove();
            $('#customTime').parents('.form-group').addClass('has-success').removeClass('has-error');
        }else{
            // $('#customTime').parents('.form-group').find('.help-block').remove();
            // $('#customTime').parents('.form-group').addClass('has-error');
            // var tip=$('<small class="help-block" data-bv-validator="notEmpty" data-bv-for="title" data-bv-result="INVALID" style="">' +
            //     '展示时间不能为空' +
            //     '</small>')
            // $('#customTime').parents('.radio').after(tip);
            $('#customTime').val(getNowFormatDate());
            $('#customTime').parents('.form-group').find('.help-block').remove();
            $('#customTime').parents('.form-group').addClass('has-success').removeClass('has-error');
        }
    });
});
