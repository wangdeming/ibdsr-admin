/**
 * 留言管理管理初始化
 */
var Message = {
    id: "MessageTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
Message.initColumn = function () {
    return [
        {field: 'selectItem', checkbox: true,},
        {title: '留言id', field: 'id', visible: false, align: 'center', valign: 'middle'},
        {title: '阅读状态', field: 'isRead', visible: false, align: 'center', valign: 'middle'},
        {title: '留言来源', field: 'sourceName', align: 'center', valign: 'middle'},
        {title: '姓名', field: 'name',align: 'center', valign: 'middle'},
        {title: '公司', field: 'company',align: 'center', valign: 'middle',width:'400px'},
        {title: '留言内容', field: 'content',align: 'center', valign: 'middle',width:'400px'},
        {title: '联系电话', field: 'phone',align: 'center', valign: 'middle'},
        {title: '投递时间', field: 'createdTime',align: 'center', valign: 'middle',sortable: true},
        {title: '状态', field: 'readStatus',align: 'center', valign: 'middle'},
        {title: '操作', field: '',align: 'center', valign: 'middle',
            formatter: function (value, row, index) {
                return ''
                    + '<a style="margin-right: 15px;" onclick="Message.checkDetail('+row.id+')">查看</a>'
                    + '<a style="margin-right: 15px;" onclick="Message.export('+row.id+')">导出</a>'
            }
        },
    ];
};

/**
 * 检查是否选中
 */
Message.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        Message.seItem = selected;
        return true;
    }
};

/**
 * 打开查看留言详情
 */
Message.checkDetail = function (id) {
        var index = layer.open({
            type: 2,
            title: '留言详情',
            area: ['660px', '420px'], //宽高
            fix: false, //不固定
            maxmin: false,
            content: Feng.ctxPath + '/message/message_detail/' + id,
            end: function () {
                Message.search();
            }
        });
        this.layerIndex = index;
};

/**
 * 标记为已读
 */
Message.hasRead=function(){
   if(this.check()){
       var messageIdsData=[];
       for(var i=0;i<Message.seItem.length;i++){
           messageIdsData.push(Message.seItem[i].id);
       }
       var messageIds = messageIdsData.join(",");
       var ajax = new $ax(Feng.ctxPath + "/message/read", function (data) {
           if(data.code==200){
               Feng.success("标记成功!");
               Message.table.refresh();
           }
       }, function (data) {
           Feng.error("标记失败!" + data.responseJSON.message + "!");
       });
       ajax.set('messageIds',messageIds);
       ajax.start();
   }
};

/**
 * 导出
 */
Message.export = function (id) {
    window.location = Feng.ctxPath + "/message/export?messageIds="+id;
};


/**
 * 批量导出
 */
Message.batchExport=function(){
    if(this.check()){
        var messageIdsData=[];
        for(var i=0;i<Message.seItem.length;i++){
            messageIdsData.push(Message.seItem[i].id);
        }
        var messageIds = messageIdsData.join(",");
        window.location = Feng.ctxPath + "/message/export?messageIds="+messageIds;
    }
};

Message.search = function () {
    var queryData = {};
    queryData['messageSource'] = $('#messageSource').val();
    queryData['isRead']=$('.radio input[type="radio"]:checked').val();
    Message.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = Message.initColumn();
    var table = new BSTable(Message.id, "/message/list", defaultColunms);
    table.type='GET';
    table.showToolbar=false;
    table.queryParams={'isRead': 0};
    Message.table = table.init();

    var ajax = new $ax(Feng.ctxPath + "/message/source/list", function (data) {
        if(data.code==200){
            for(var i=0;i<data.data.length;i++){
                var option=$('<option value="'+data.data[i].num+'">'+data.data[i].name+'</option>');
                $("#messageSource").append(option)
            }
        }
    }, function (data) {
        Feng.error("留言来源列表加载失败!" + data.responseJSON.message + "!");
    });
    ajax.type="get";
    ajax.start();

    $('.radio input[type="radio"]').change(function(e){
        if($(this).is(':checked')){
           var state=$(this).val();
            Message.table.refresh({query: {'isRead': state}});
        }
    });

    $('#messageSource').change(function(e){
        Message.table.refresh({query: {'messageSource':$('#messageSource').val() }});
    });
});
