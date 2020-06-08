/**
 * 大事记管理管理初始化
 */
var Events = {
    id: "EventsTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
Events.initColumn = function () {
    return [
        {title: 'id', field: 'id', visible: false, align: 'center', valign: 'middle'},
        {title: '序号', field: '', align: 'center', valign: 'middle',
            formatter: function (value, row, index) {
                var pageSize=$('#EventsTable').bootstrapTable('getOptions').pageSize;//通过表的#id 可以得到每页多少条
                var pageNumber=$('#EventsTable').bootstrapTable('getOptions').pageNumber;//通过表的#id 可以得到当前第几页
                return pageSize * (pageNumber - 1) + index + 1;//返回每条的序号： 每页条数 * （当前页 - 1 ）+ 序号
            }
        },
        {title: '事记时间', field: 'eventDate', align: 'center', valign: 'middle'},
        {title: '事记标题', field: 'title', align: 'center', valign: 'middle'},
        {title: '最后编辑时间', field: 'modifiedTime', align: 'center', valign: 'middle'},
        {title: '操作', field: '', align: 'center', valign: 'middle',
            formatter: function (value, row, index) {
                    return ''
                        + '<a style="margin-right: 15px;" onclick="Events.openEventsDetail('+row.id+')">详情</a>'
                        + '<a style="margin-right: 15px;" onclick="Events.openEventsEdit('+row.id+')">编辑</a>'
                        + '<a style="margin-right: 15px;" onclick="Events.delete('+row.id+')">删除</a>'

            }
        }
    ];
};

/**
 * 检查是否选中
 */
Events.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        Events.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加大事记管理
 */
Events.openAddEvents = function () {
    var index = layer.open({
        type: 2,
        title: '新增大事记',
        area: ['800px', '550px'], //宽高
        fix: false, //不固定
        maxmin: false,
        content: Feng.ctxPath + '/events/events_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看大事记管理详情
 */
Events.openEventsDetail = function (id) {
        var index = layer.open({
            type: 2,
            title: '大事记详情',
            area: ['800px', '550px'], //宽高
            fix: false, //不固定
            maxmin: false,
            content: Feng.ctxPath + '/events/events_detail/' +id
        });
        this.layerIndex = index;
};

/**
 * 打开编辑大事记管理详情
 */
Events.openEventsEdit = function (id) {
        var index = layer.open({
            type: 2,
            title: '编辑大事记',
            area: ['800px', '550px'], //宽高
            fix: false, //不固定
            maxmin: false,
            content: Feng.ctxPath + '/events/events_update/' + id
        });
        this.layerIndex = index;
};

/**
 * 删除大事记管理
 */
Events.delete = function (id) {
    var operation = function(){
        var ajax = new $ax(Feng.ctxPath + "/events/delete", function (data) {
            if(data.code==200){
                Feng.success("删除成功!");
                Events.table.refresh();
            }else{
                Feng.error("删除失败!" + data.message + "!");
            }
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("eventId",id);
        ajax.start();
    };
    Feng.confirm("是否删除该大事件?", operation);
};

/**
 * 查询大事记管理列表
 */
Events.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    Events.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = Events.initColumn();
    var table = new BSTable(Events.id, "/events/list", defaultColunms);
    table.method='GET';
    table.showToolbar=false;
    Events.table = table.init();
});
