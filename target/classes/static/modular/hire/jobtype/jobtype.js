/**
 * 岗位分类管理管理初始化
 */
var Jobtype = {
    id: "JobtypeTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
Jobtype.initColumn = function () {
    return [
        {title: 'id', field: 'id', visible: false, align: 'center', valign: 'middle'},
        {title: '排序', field: '', align: 'center', valign: 'middle',
            formatter: function(value, row, index) {
                var total = $('#'+Jobtype.id).bootstrapTable('getData').length;
                var getArrowStyle = function(up, extra) {
                    extra = extra || '';
                    return 'margin: 0 5px;display: inline-block;width: 10px;height: 20px;background-image: url('+Feng.ctxPath+'/static/images/'
                        + (up ? extra ? 'up_disable' : 'up_able': extra ? 'down_disable' : 'down_able')
                        + '.png);background-size: 100% 100%;background-repeat: no-repeat;' + extra;
                }
                var up = index == 0
                    ? '<a style="'+getArrowStyle(true, 'cursor: not-allowed;')+'"></ a>'
                    : '<a onclick="Jobtype.arrowUp('+row.id+');" style="'+getArrowStyle(true)+'"></ a>';
                var down = index + 1 == total
                    ? '<a style="'+getArrowStyle(false, 'cursor: not-allowed;')+'"></ a>'
                    : '<a onclick="Jobtype.arrowDown('+row.id+');" style="'+getArrowStyle(false)+'"></ a>';
                return up + down;
            }
        },
        {title: '分类名称', field: 'name', align: 'center', valign: 'middle'},
        {title: '岗位总数量', field: 'jobCount', align: 'center', valign: 'middle'},
        {title: '发布中岗位数量', field: 'publishCount', align: 'center', valign: 'middle'},
        {title: '操作', field: '', align: 'center', valign: 'middle',
            formatter: function (value, row, index) {
                return ''
                    + '<a style="margin-right: 15px;" onclick="Jobtype.edit('+row.id+')">编辑</a>'
                    + '<a style="margin-right: 15px;" onclick="Jobtype.delete('+row.id+')">删除</a>'
            }
        }
    ];
};

Jobtype.arrowUp = function(id) {
    var ajax = new $ax(Feng.ctxPath + "/jobtype/up_sort", function (data) {
        if(data.code==200){
            Feng.success("上移成功!");
            Jobtype.table.refresh();
        }else{
            Feng.error(data.message);
        }

    }, function (data) {
        Feng.error("上移失败!" + data.responseJSON.message + "!");
    });
    ajax.set("id", id);
    ajax.start(true);
};
Jobtype.arrowDown = function(id) {
    var ajax = new $ax(Feng.ctxPath + "/jobtype/down_sort", function (data) {
        if(data.code==200){
            Feng.success("下移成功!");
            Jobtype.table.refresh();
        }else{
            Feng.error(data.message);
        }

    }, function (data) {
        Feng.error("下移失败!" + data.responseJSON.message + "!");
    });
    ajax.set("id", id);
    ajax.start();
};


/**
 * 检查是否选中
 */
Jobtype.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        Jobtype.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加岗位分类管理
 */
Jobtype.openAddJobtype = function () {
    var index = layer.open({
        type: 2,
        title: '新增岗位分类',
        area: ['500px', '300px'], //宽高
        fix: false, //不固定
        maxmin: false,
        content: Feng.ctxPath + '/jobtype/jobtype_add'
    });
    this.layerIndex = index;
};

/**
 * 编辑岗位分类管理
 */
Jobtype.edit = function (id) {
    var index = layer.open({
        type: 2,
        title: '编辑岗位分类',
        area: ['500px', '300px'], //宽高
        fix: false, //不固定
        maxmin: false,
        content: Feng.ctxPath + '/jobtype/jobtype_update/' + id
    });
    this.layerIndex = index;
};

/**
 * 删除岗位分类管理
 */
Jobtype.delete = function (id) {
    var operation = function(){
        var ajax = new $ax(Feng.ctxPath + "/jobtype/delete", function (data) {
            Feng.success("删除成功!");
            Jobtype.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("id",id);
        ajax.start();
    };
    Feng.confirm("是否删除该分类?", operation);
};

/**
 * 查询岗位分类管理列表
 */
Jobtype.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    Jobtype.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = Jobtype.initColumn();
    var table = new BSTable(Jobtype.id, "/jobtype/list", defaultColunms);
    table.pagination = false;
    table.showToolbar = false;
    Jobtype.table = table.init();
});
