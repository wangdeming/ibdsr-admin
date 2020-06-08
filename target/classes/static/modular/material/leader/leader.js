/**
 * 领导关怀管理管理初始化
 */
var Leader = {
    id: "LeaderTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
Leader.initColumn = function () {
    return [
        {title: 'id', field: 'id', visible: false, align: 'center', valign: 'middle'},
        {title: 'isPublish', field: 'isPublish', visible: false, align: 'center', valign: 'middle'},
        {title: '序号', field: '', align: 'center', valign: 'middle',
            formatter: function (value, row, index) {
                var pageSize=$('#LeaderTable').bootstrapTable('getOptions').pageSize;//通过表的#id 可以得到每页多少条
                var pageNumber=$('#LeaderTable').bootstrapTable('getOptions').pageNumber;//通过表的#id 可以得到当前第几页
                return pageSize * (pageNumber - 1) + index + 1;//返回每条的序号： 每页条数 * （当前页 - 1 ）+ 序号
            }
        },
        {title: '领导/来访者姓名', field: 'leaderName', align: 'center', valign: 'middle'},
        {title: '状态', field: 'publishStatus', align: 'center', valign: 'middle'},
        {title: '是否置顶', field: 'isTop', align: 'center', valign: 'middle',sortable: true,
            formatter: function (value, row, index) {
                if(row.isTop==0){
                    return '否'
                }else{
                    return '是'
                }
            }
        },
        {title: '展示时间', field: 'showTime', align: 'center', valign: 'middle',sortable: true},
        {title: '最后编辑时间', field: 'modifiedTime', align: 'center', valign: 'middle',sortable: true},
        {title: '操作', field: '', align: 'center', valign: 'middle',
            formatter: function (value, row, index) {
                if(row.isPublish){
                    return ''
                        + '<a style="margin-right: 15px;" onclick="Leader.openLeaderDetail('+row.id+')">详情</a>'
                        + '<a style="margin-right: 15px;" onclick="Leader.CancelPublish('+row.id+')">取消发布</a>'
                }else{
                    return ''
                        + '<a style="margin-right: 15px;" onclick="Leader.openEdit('+row.id+')">编辑</a>'
                        + '<a style="margin-right: 15px;" onclick="Leader.Publish('+row.id+')">发布</a>'
                        + '<a style="margin-right: 15px;" onclick="Leader.delete('+row.id+')">删除</a>'
                }

            }
        }
    ];
};

/**
 * 检查是否选中
 */
Leader.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        Leader.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加领导关怀管理
 */
Leader.openAddLeader = function () {
    var index = layer.open({
        type: 2,
        title: '新增领导关怀',
        area: ['800px', '700px'], //宽高
        fix: false, //不固定
        maxmin: false,
        content: Feng.ctxPath + '/leader/leader_add'
    });
    this.layerIndex = index;
};

/**
 * 点击编辑领导关怀管理
 */
Leader.openEdit = function (id) {
    var index = layer.open({
        type: 2,
        title: '编辑领导关怀',
        area: ['800px', '700px'], //宽高
        fix: false, //不固定
        maxmin: false,
        content: Feng.ctxPath + '/leader/leader_update/'+id
    });
    this.layerIndex = index;
};

/**
 * 打开查看领导关怀管理详情
 */
Leader.openLeaderDetail = function (id) {
    var index = layer.open({
        type: 2,
        title: '领导关怀详情',
        area: ['800px', '700px'], //宽高
        fix: false, //不固定
        maxmin: false,
        content: Feng.ctxPath + '/leader/leader_detail/'+id
    });
    this.layerIndex = index;
};

/**
 * 发布领导关怀管理
 */
Leader.Publish = function (id) {
    var operation = function(){
        var ajax = new $ax(Feng.ctxPath + "/leader/publish", function (data) {
           if(data.code==200){
               Feng.success("发布成功!");
               Leader.table.refresh();
           }else{
               Feng.error("发布失败!" + data.message + "!");
           }
        }, function (data) {
            Feng.error("发布失败!" + data.responseJSON.message + "!");
        });
        ajax.set("leaderId",id);
        ajax.start();
    };
    Feng.confirm("是否发布该新闻?", operation);
};

/**
 * 取消发布领导关怀管理
 */
Leader.CancelPublish = function (id) {
    var operation = function(){
        var ajax = new $ax(Feng.ctxPath + "/leader/cancelPublish", function (data) {
            if(data.code==200){
                Feng.success("取消成功!");
                Leader.table.refresh();
            }else{
                Feng.error("取消失败!" + data.message + "!");
            }
        }, function (data) {
            Feng.error("取消失败!" + data.responseJSON.message + "!");
        });
        ajax.set("leaderId",id);
        ajax.start();
    };
    Feng.confirm("是否取消发布该新闻?", operation);
};

/**
 * 删除领导关怀管理
 */
Leader.delete = function (id) {
    var operation = function(){
        var ajax = new $ax(Feng.ctxPath + "/leader/delete", function (data) {
            if(data.code==200){
                Feng.success("删除成功!");
                Leader.table.refresh();
            }else{
                Feng.error("删除失败!" + data.message + "!");
            }
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("leaderId",id);
        ajax.start();
    };
    Feng.confirm("是否删除该新闻?", operation);
};

/**
 * 查询领导关怀管理列表
 */
Leader.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    queryData['isPublish'] = $("#isPublish").val();
    Leader.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = Leader.initColumn();
    var table = new BSTable(Leader.id, "/leader/list", defaultColunms);
    table.method='GET';
    table.showToolbar=false;
    Leader.table = table.init();
});
