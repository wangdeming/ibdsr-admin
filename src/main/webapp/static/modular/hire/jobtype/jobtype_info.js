/**
 * 初始化岗位分类管理详情对话框
 */
var JobtypeInfoDlg = {
    jobtypeInfoData : {},
    validateFields: {
        name: {
            validators: {
                notEmpty: {
                    message: '名称不能为空'
                },
                regexp: {//正则验证
                    regexp: /^[\u4E00-\u9FA5A-Za-z0-9_]{0,16}$/,
                    message: '16个字符以内'
                }
            }
        }
    }

};

/**
 * 清除数据
 */
JobtypeInfoDlg.clearData = function() {
    this.jobtypeInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
JobtypeInfoDlg.set = function(key, val) {
    this.jobtypeInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
JobtypeInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
JobtypeInfoDlg.close = function() {
    parent.layer.close(window.parent.Jobtype.layerIndex);
}

/**
 * 收集数据
 */
JobtypeInfoDlg.collectData = function() {
    this.set('name');
}
JobtypeInfoDlg.collectEditData = function() {
    this.set('name');
    this.set('id');
}

/**
 * 验证数据是否为空
 */
JobtypeInfoDlg.validate = function () {
    $('#JobTypeForm').data("bootstrapValidator").resetForm();
    $('#JobTypeForm').bootstrapValidator('validate');
    return $("#JobTypeForm").data('bootstrapValidator').isValid();
};


/**
 * 提交添加
 */
JobtypeInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    if (!this.validate()) {
        return;
    }

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/jobtype/add", function(data){
       if(data.code==200){
           Feng.success("添加成功!");
           window.parent.Jobtype.table.refresh();
           JobtypeInfoDlg.close();
       }
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.jobtypeInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
JobtypeInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectEditData();

    if (!this.validate()) {
        return;
    }
    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/jobtype/update", function(data){
        if(data.code==200){
            Feng.success("添加成功!");
            window.parent.Jobtype.table.refresh();
            JobtypeInfoDlg.close();
        }
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.jobtypeInfoData);
    ajax.start();
}

$(function() {
    Feng.initValidator("JobTypeForm", JobtypeInfoDlg.validateFields);
});
