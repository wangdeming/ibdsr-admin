/**
 * 员工数风采管理管理初始化
 */
var Employee = {
    id: "EmployeeTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
Employee.initColumn = function () {
    return [
        {title: 'id', field: 'id', visible: false, align: 'center', valign: 'middle'},
        {title: 'isPublish', field: 'isPublish', visible: false, align: 'center', valign: 'middle'},
        {title: '序号', field: '', align: 'center', valign: 'middle',
            formatter: function (value, row, index) {
                var pageSize=$('#EmployeeTable').bootstrapTable('getOptions').pageSize;//通过表的#id 可以得到每页多少条
                var pageNumber=$('#EmployeeTable').bootstrapTable('getOptions').pageNumber;//通过表的#id 可以得到当前第几页
                return pageSize * (pageNumber - 1) + index + 1;//返回每条的序号： 每页条数 * （当前页 - 1 ）+ 序号
            }
        },
        {title: '标题', field: 'title', align: 'center', valign: 'middle'},
        {title: '状态', field: 'publishStatus', align: 'center', valign: 'middle'},
        {title: '展示时间', field: 'showTime', align: 'center', valign: 'middle',sortable: true},
        {title: '最后编辑时间', field: 'modifiedTime', align: 'center', valign: 'middle',sortable: true},
        {title: '操作', field: '', align: 'center', valign: 'middle',
            formatter: function (value, row, index) {
                if(row.isPublish){
                    return ''
                        + '<a style="margin-right: 15px;" onclick="Employee.openDetail('+row.id+')">详情</a>'
                        + '<a style="margin-right: 15px;" onclick="Employee.CancelPublish('+row.id+')">取消发布</a>'
                }else{
                    return ''
                        + '<a style="margin-right: 15px;" onclick="Employee.openEdit('+row.id+')">编辑</a>'
                        + '<a style="margin-right: 15px;" onclick="Employee.Publish('+row.id+')">发布</a>'
                        + '<a style="margin-right: 15px;" onclick="Employee.delete('+row.id+')">删除</a>'
                }

            }
        }
    ];
};

/**
 * 检查是否选中
 */
Employee.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        Employee.seItem = selected[0];
        return true;
    }
};

/**
 * 编辑员工数风采管理
 */
Employee.openEdit = function (id) {
    window.location=Feng.ctxPath + '/employee/employee_update/'+id;
};

/**
 * 详情
 */
Employee.openDetail = function (id) {
    window.location=Feng.ctxPath + '/employee/employee_detail/'+id;
};

/**
 * 发布
 */
Employee.Publish = function (id) {
    var operation = function(){
        var ajax = new $ax(Feng.ctxPath + "/employee/publish", function (data) {
            if(data.code==200){
                Feng.success("发布成功!");
                Employee.table.refresh();
            }else{
                Feng.error("发布失败!" + data.message + "!");
            }
        }, function (data) {
            Feng.error("发布失败!" + data.responseJSON.message + "!");
        });
        ajax.set("employeeId",id);
        ajax.start();
    };
    Feng.confirm("是否发布该新闻?", operation);
};

/**
 * 取消发布
 */
Employee.CancelPublish = function (id) {
    var operation = function(){
        var ajax = new $ax(Feng.ctxPath + "/employee/cancelPublish", function (data) {
            if(data.code==200){
                Feng.success("取消成功!");
                Employee.table.refresh();
            }else{
                Feng.error("取消失败!" + data.message + "!");
            }
        }, function (data) {
            Feng.error("取消失败!" + data.responseJSON.message + "!");
        });
        ajax.set("employeeId",id);
        ajax.start();
    };
    Feng.confirm("是否取消发布该新闻?", operation);
};

/**
 * 删除员工数风采管理
 */
Employee.delete = function (id) {
    var operation = function(){
        var ajax = new $ax(Feng.ctxPath + "/employee/delete", function (data) {
            if(data.code==200){
                Feng.success("删除成功!");
                Employee.table.refresh();
            }else{
                Feng.error("删除失败!" + data.message + "!");
            }
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("employeeId",id);
        ajax.start();
    };
    Feng.confirm("是否删除该新闻?", operation);
};

/**
 * 查询员工数风采管理列表
 */
Employee.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    queryData['isPublish'] = $("#isPublish").val();
    Employee.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = Employee.initColumn();
    var table = new BSTable(Employee.id, "/employee/list", defaultColunms);
    table.method='GET';
    table.showToolbar=false;
    Employee.table = table.init();
});
