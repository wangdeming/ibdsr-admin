/**
 * 初始化员工数风采管理详情对话框
 */
var EmployeeInfoDlg = {
    employeeInfoData : {},
    validateFields: {
        title: {
            validators: {
                notEmpty: {
                    message: '标题不能为空'
                },
                regexp: {//正则验证
                    regexp: /^.{2,80}$/,
                    message: '2-80字符，支持汉字、数字和一般符号的组合'
                }
            }
        }
    }
};

var E= window.wangEditor;
var editor = new E('#editor');
// editor.customConfig.uploadImgShowBase64 = true;  // 使用 base64 保存图片
editor.customConfig.uploadImgServer = Feng.ctxPath + '/fastDfs/upload';
editor.customConfig.uploadImgHooks = {
    customInsert: function (insertImg, result, editor) {
        if(result.code==200){
            var url = result.data;
            insertImg(url);
        }else{
            Feng.error("添加失败!" + result.message + "!");
        }
    }
};
//粘贴来的内容过滤图片
editor.customConfig.pasteIgnoreImg = true;
// 关闭粘贴样式的过滤
editor.customConfig.pasteFilterStyle = false;
// 自定义处理粘贴的文本内容
editor.customConfig.pasteTextHandle = function (content) {
    // content 即粘贴过来的内容（html 或 纯文本），可进行自定义处理然后返回
    // return content + '<p>在粘贴内容后面追加一行</p>'
    if (content == '' && !content) return ''
    var input = content;
    console.log('input----',input)
    // 1. remove line breaks / Mso classes
    var stringStripper = /(\n|\r| class=(")?Mso[a-zA-Z]+(")?)/g;
    var output = input.replace(stringStripper, ' ');
    // 2. strip Word generated HTML comments
    var commentSripper = new RegExp('<!--(.*?)-->','g');
    var output = output.replace(commentSripper, '');
    var tagStripper = new RegExp('<(/)*(meta|link|span|\\?xml:|st1:|o:|font)(.*?)>','gi');
    // 3. remove tags leave content if any
    output = output.replace(tagStripper, '');
    // 4. Remove everything in between and including tags '<style(.)style(.)>'
    var badTags = ['style', 'script','applet','embed','noframes','noscript'];

    for (var i=0; i< badTags.length; i++) {
        tagStripper = new RegExp('<'+badTags[i]+'.*?'+badTags[i]+'(.*?)>', 'gi');
        output = output.replace(tagStripper, '');
    }
    // 5. remove attributes ' style="..."'
    var badAttributes = ['style', 'start'];
    for (var i=0; i< badAttributes.length; i++) {
        var attributeStripper = new RegExp(' ' + badAttributes[i] + '="(.*?)"','gi');
        output = output.replace(attributeStripper, '');
    }
    return output;
};
editor.customConfig.showLinkImg = false; // 隐藏“网络图片”tab
editor.customConfig.onchange = function (html) {
    if(editor.txt.text()){
        $('#editor').parents('.form-group').find('.help-block').remove();
        $('#editor').parents('.form-group').addClass('has-success').removeClass('has-error');
    }else{
        $('#editor').parents('.form-group').find('.help-block').remove();
        $('#editor').parents('.form-group').addClass('has-error').removeClass('has-success');
        var tip=$('<small class="help-block" data-bv-validator="notEmpty" data-bv-for="title" data-bv-result="INVALID" style="">' +
            '正文内容不能为空' +
            '</small>')
        $('#editor').after(tip);
    }
};
editor.create();


/**
 * 关闭此对话框
 */
EmployeeInfoDlg.close = function() {
    var operation = function(){
        window.location=Feng.ctxPath + "/employee"
    };
    Feng.confirm("系统将不会保存当前页面所填写的内容。是否取消？", operation);
}

/**
 * 关闭此对话框
 */
EmployeeInfoDlg.closeUp = function() {
        window.location=Feng.ctxPath + "/employee"
}

/**
 * 验证数据是否为空
 */
EmployeeInfoDlg.validate = function () {
    $('#EmployeeForm').data("bootstrapValidator").resetForm();
    $('#EmployeeForm').bootstrapValidator('validate');
    return $("#EmployeeForm").data('bootstrapValidator').isValid();
};

/**
 * 提交添加
 */
EmployeeInfoDlg.addSubmit = function() {
    var showTimeType=$('.radio input[type="radio"]:checked').val();
    var showTime;
    if(showTimeType==0){
        showTime=getNowFormatDate();
    }else{
        showTime=$('#customTime').val();
    }
    if (!this.validate()) {
        if(!$('#coverImage').val()){
            $('#coverImage').parents('.form-group').find('.help-block').remove();
            $('#coverImage').parents('.form-group').addClass('has-error');
            var tip=$('<small class="help-block" data-bv-validator="notEmpty" data-bv-for="title" data-bv-result="INVALID" style="">' +
                '封面图片不能为空' +
                '</small>')
            $('#coverImage').parent('.div-input').after(tip);
        }
        if(!editor.txt.text()){
            $('#editor').parents('.form-group').find('.help-block').remove();
            $('#editor').parents('.form-group').addClass('has-error');
            var tip=$('<small class="help-block" data-bv-validator="notEmpty" data-bv-for="title" data-bv-result="INVALID" style="">' +
                '正文内容不能为空' +
                '</small>')
            $('#editor').after(tip);
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
        if(!$('#coverImage').val()){
            $('#coverImage').parents('.form-group').find('.help-block').remove();
            $('#coverImage').parents('.form-group').addClass('has-error');
            var tip=$('<small class="help-block" data-bv-validator="notEmpty" data-bv-for="title" data-bv-result="INVALID" style="">' +
                '封面图片不能为空' +
                '</small>')
            $('#coverImage').parent('.div-input').after(tip);
            return;
        }
        if(!editor.txt.text()){
            $('#editor').parents('.form-group').find('.help-block').remove();
            $('#editor').parents('.form-group').addClass('has-error');
            var tip=$('<small class="help-block" data-bv-validator="notEmpty" data-bv-for="title" data-bv-result="INVALID" style="">' +
                '正文内容不能为空' +
                '</small>')
            $('#editor').after(tip);
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

    var content = editor.txt.html();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/employee/add", function(data){
        if(data.code==200){
            // Feng.success("添加成功!");
            var id=data.data;
            layer.confirm('保存成功，是否立即发布？', {
                btn: ['是','否'] //按钮
            }, function(){
                var ajax = new $ax(Feng.ctxPath + "/employee/publish", function (data) {
                    if(data.code==200){
                        Feng.success("发布成功!");
                        window.location=Feng.ctxPath + "/employee"
                    }else{
                        Feng.error("发布失败!" + data.message + "!");
                    }
                }, function (data) {
                    Feng.error("发布失败!" + data.responseJSON.message + "!");
                });
                ajax.set("employeeId",id);
                ajax.start();
            }, function(){
                window.location=Feng.ctxPath + "/employee"
            });
        }


    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.contentType='application/json';
    ajax.dataType='json';
    EmployeeInfoDlg.employeeInfoData['title']=$('#title').val();
    EmployeeInfoDlg.employeeInfoData['mainContent']=content;
    if(showTimeType==0){
        EmployeeInfoDlg.employeeInfoData['showTime']='';
    }else{
        EmployeeInfoDlg.employeeInfoData['showTime']=showTime;
    }
    // EmployeeInfoDlg.employeeInfoData['showTime']=showTime;
    EmployeeInfoDlg.employeeInfoData['coverImage']=$('#coverImage').attr('data-src');
    ajax.setData(JSON.stringify(EmployeeInfoDlg.employeeInfoData));
    ajax.start();
}

/**
 * 提交修改
 */
EmployeeInfoDlg.editSubmit = function() {
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
        if(!$('#coverImage').attr('data-src')){
            $('#coverImage').parents('.form-group').find('.help-block').remove();
            $('#coverImage').parents('.form-group').addClass('has-error');
            var tip=$('<small class="help-block" data-bv-validator="notEmpty" data-bv-for="title" data-bv-result="INVALID" style="">' +
                '封面图片不能为空' +
                '</small>')
            $('#coverImage').parent('.div-input').after(tip);
        }
        if(!editor.txt.text()){
            $('#editor').parents('.form-group').find('.help-block').remove();
            $('#editor').parents('.form-group').addClass('has-error');
            var tip=$('<small class="help-block" data-bv-validator="notEmpty" data-bv-for="title" data-bv-result="INVALID" style="">' +
                '正文内容不能为空' +
                '</small>')
            $('#editor').after(tip);
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
        if(!$('#coverImage').attr('data-src')){
            $('#coverImage').parents('.form-group').find('.help-block').remove();
            $('#coverImage').parents('.form-group').addClass('has-error');
            var tip=$('<small class="help-block" data-bv-validator="notEmpty" data-bv-for="title" data-bv-result="INVALID" style="">' +
                '封面图片不能为空' +
                '</small>')
            $('#coverImage').parent('.div-input').after(tip);
            return;
        }
        if(!editor.txt.text()){
            $('#editor').parents('.form-group').find('.help-block').remove();
            $('#editor').parents('.form-group').addClass('has-error');
            var tip=$('<small class="help-block" data-bv-validator="notEmpty" data-bv-for="title" data-bv-result="INVALID" style="">' +
                '正文内容不能为空' +
                '</small>')
            $('#editor').after(tip);
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

    var content = editor.txt.html();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/employee/update", function(data){
        if(data.code==200){
            Feng.success("修改成功!");
            window.location=Feng.ctxPath + "/employee";
        }else{
            Feng.error("添加失败!" + data.message + "!");
        }
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.contentType='application/json';
    ajax.dataType='json';
    EmployeeInfoDlg.employeeInfoData['id']=$('#id').val();
    EmployeeInfoDlg.employeeInfoData['title']=$('#title').val();
    EmployeeInfoDlg.employeeInfoData['mainContent']=content;
    if(showTimeType==0){
        EmployeeInfoDlg.employeeInfoData['showTime']='';
    }else{
        EmployeeInfoDlg.employeeInfoData['showTime']=showTime;
    }
    // EmployeeInfoDlg.employeeInfoData['showTime']=showTime;
    if($('#coverImage').attr('data-src')==$('#coverImage').attr('data-oldSrc')){
        EmployeeInfoDlg.employeeInfoData['coverImage']=$('#coverImage').attr('data-src');
    }else{
        EmployeeInfoDlg.employeeInfoData['coverImage']=$('#coverImage').attr('data-src');
        EmployeeInfoDlg.employeeInfoData['oldCoverImage']=$('#coverImage').attr('data-oldSrc');
    }
    ajax.setData(JSON.stringify(EmployeeInfoDlg.employeeInfoData));
    ajax.start();
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
    var filePath=$('#coverImage').attr('data-src');
    var ajax = new $ax(Feng.ctxPath + "/fastDfs/delete", function (data) {
        if(data.code==200){
            $(obj).parent('.div-img').remove();
            $(".div-input").show();
            $('#coverImage').val('');
            $('#coverImage').attr('data-src','');
            var tip=$('<small class="help-block" data-bv-validator="notEmpty" data-bv-for="title" data-bv-result="INVALID" style="">' +
                '封面图片不能为空' +
                '</small>')
            $('#coverImage').parent('.div-input').after(tip);
            $('#coverImage').parents('.form-group').addClass('has-error').removeClass('has-success');
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
    var hours=date.getHours();
    var minutes=date.getMinutes();
    var seconds=date.getSeconds();
    if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
    }
    if (hours >= 0 && hours <= 9) {
        hours = "0" + hours;
    }
    if (minutes >= 0 && minutes <= 9) {
        minutes = "0" + minutes;
    }
    if (seconds >= 0 && seconds <= 9) {
        seconds = "0" + seconds;
    }
    var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate
        + " " + hours + seperator2 + minutes + seperator2 + seconds;
    return currentdate;
}

$(function() {
    if($('#mainFile').val()){
        editor.txt.html($('#mainFile').val());
    }

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

    $("#coverImage").change(function(){
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
                    $(".div-input").hide();
                    var divImg=$(' <div class="div-img form-control">' +
                        ' <img src="'+imgSrcI+'" alt="图片缺失">' +
                        ' <button onclick="delImg(this)"><i class="fa fa-close"></i></button>' +
                        '</div>')
                    $('.upImg-div').append(divImg);
                    $('#coverImage').attr('data-src',data.data);
                    $('#coverImage').parents('.form-group').find('.help-block').remove();
                    $('#coverImage').parents('.form-group').addClass('has-success').removeClass('has-error');
                }else{
                    Feng.info(data.message);
                }
            },
            error: function(data) {
                Feng.error("上传失败!" + data.responseJSON.message + "!");
            }
        });

    });

    Feng.initValidator("EmployeeForm", EmployeeInfoDlg.validateFields);

    $('input[type=radio]').change(function() {
       var type=$(this).val();
       if(type==0||type==2){
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
    });

});
