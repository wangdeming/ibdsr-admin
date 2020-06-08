/**
 * 初始化岗位管理详情对话框
 */
var JobInfoDlg = {
    jobInfoData : {},
    validateFields: {
        name: {
            validators: {
                notEmpty: {
                    message: '名称不能为空'
                },
                regexp: {//正则验证
                    regexp: /^[\u4E00-\u9FA5A-Za-z0-9_]{5,20}$/,
                    message: '5~20个字'
                }
            }
        },
        sort: {
            validators: {
                notEmpty: {
                    message: '权重不能为空'
                },
                regexp: {//正则验证
                    regexp: /^(10|[1-9])$/,
                    message: '可输入1~10之间的数字，数字越小，权重越大'
                }
            }
        },
        jobDuty: {
            validators: {
                notEmpty: {
                    message: '职责不能为空'
                }
            }
        },
        jobRequire: {
            validators: {
                notEmpty: {
                    message: '要求不能为空'
                }
            }
        }
    }
};

/**
 * 清除数据
 */
JobInfoDlg.clearData = function() {
    this.jobInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
JobInfoDlg.set = function(key, val) {
    this.jobInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
JobInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
JobInfoDlg.close = function() {
    parent.layer.close(window.parent.Job.layerIndex);
}

/**
 * 收集数据
 */
JobInfoDlg.collectData = function() {
    this.set('jobTypeId').set('name').set('jobDuty').set('jobRequire').set('num').set('sort');
}

/**
 * 收集数据
 */
JobInfoDlg.collectEditData = function() {
    this.set('id').set('jobTypeId').set('name').set('jobDuty').set('jobRequire').set('num').set('sort');
}

/**
 * 验证数据是否为空
 */
JobInfoDlg.validate = function () {
    $('#JobForm').data("bootstrapValidator").resetForm();
    $('#JobForm').bootstrapValidator('validate');
    return $("#JobForm").data('bootstrapValidator').isValid();
};

/**
 * 提交添加
 */
JobInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    if (!this.validate()) {
        return;
    }
    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/job/add", function(data){
       if(data.code==200){
           parent.layer.open({
               type: 1,
               title: '信息',
               content: '<p style="padding: 18px 20px;margin: 0;">保存成功，是否立即发布？</p>',
               btn: ['是', '否'],
               area: ["260px", "146px"],
               skin: 'myContent',
               yes: function(index, layero){
                   var ajax = new $ax(Feng.ctxPath + "/job/publish", function (data) {
                       Feng.success("发布成功!");
                       window.parent.Job.table.refresh();
                       parent.layer.closeAll();
                   }, function (data) {
                       Feng.error("发布失败!" + data.responseJSON.message + "!");
                   });
                   ajax.set("id",data.data);
                   ajax.start();
               },
               btn2: function(index, layero){
                   Feng.success("添加成功!");
                   window.parent.Job.table.refresh();
                   parent.layer.closeAll();
               }
           });
       }
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.jobInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
JobInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectEditData();

    if (!this.validate()) {
        return;
    }
    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/job/update", function(data){
       if(data.code==200){
           Feng.success("修改成功!");
           window.parent.Job.table.refresh();
           JobInfoDlg.close();
       }
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.jobInfoData);
    ajax.start();
}

JobInfoDlg.Statistics=function(obj){
    var text1=$(obj).val();
    var counter1=text1.length;
    $(obj).siblings('span').find('i').text(counter1);
    $(obj).on('blur keyup input',function(){
        var text=$(obj).val();
        var counter=text.length;
        $(obj).siblings('span').find('i').text(counter);
    });
}

$(function() {
    //获取岗位分类列表
    var ajax = new $ax(Feng.ctxPath + "/jobtype/list", function(data){
        // if(data.code==200){
            for(var i=0;i<data.rows.length;i++){
                var option=$('<option value="'+data.rows[i].id+'">'+data.rows[i].name+'</option>')
                $("#jobTypeId").append(option)
            }
        // }
    },function(data){
        Feng.error("获取岗位分类列表失败!" + data.responseJSON.message + "!");
    });
    ajax.start();
    JobInfoDlg.Statistics('#jobRequire');
    JobInfoDlg.Statistics('#jobDuty');

    Feng.initValidator("JobForm", JobInfoDlg.validateFields);
});
