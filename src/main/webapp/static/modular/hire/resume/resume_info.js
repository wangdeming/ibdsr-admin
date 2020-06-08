/**
 * 初始化简历管理详情对话框
 */
var ResumeInfoDlg = {
    resumeInfoData : {}
};

/**
 * 清除数据
 */
ResumeInfoDlg.clearData = function() {
    this.resumeInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
ResumeInfoDlg.set = function(key, val) {
    this.resumeInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
ResumeInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
ResumeInfoDlg.close = function() {
    parent.layer.close(window.parent.Resume.layerIndex);
}

/**
 * 收集数据
 */
ResumeInfoDlg.collectData = function() {
    this.set('id');
}

/**
 * 提交添加
 */
ResumeInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/resume/add", function(data){
        Feng.success("添加成功!");
        window.parent.Resume.table.refresh();
        ResumeInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.resumeInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
ResumeInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/resume/update", function(data){
        Feng.success("修改成功!");
        window.parent.Resume.table.refresh();
        ResumeInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.resumeInfoData);
    ajax.start();
}

$(function() {

});
