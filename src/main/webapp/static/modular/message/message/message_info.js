/**
 * 初始化留言管理详情对话框
 */
var MessageInfoDlg = {
    messageInfoData : {}
};

/**
 * 关闭此对话框
 */
MessageInfoDlg.close = function() {
    parent.Message.search();
    parent.layer.close(window.parent.Message.layerIndex);
}


$(function() {
    var ajax = new $ax(Feng.ctxPath + "/message/detail", function (data) {
        if(data.code==200){
            $('#name').text(data.data.name);
            $('#company').text(data.data.company);
            $('#content').text(data.data.content);
            $('#phone').text(data.data.phone);
        }
    }, function (data) {
        Feng.error("标记失败!" + data.responseJSON.message + "!");
    });
    ajax.set('id',$('#id').val());
    ajax.start();
});
