/**
 * 岗位管理管理初始化
 */
var Job = {
    id: "JobTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
Job.initColumn = function () {
    return [
        {title: 'id', field: 'id', visible: false, align: 'center', valign: 'middle'},
        {title: 'isPublish', field: 'isPublish', visible: false, align: 'center', valign: 'middle'},
        {title: '岗位分类', field: 'typeName', align: 'center', valign: 'middle'},
        {title: '岗位名称', field: 'name', align: 'center', valign: 'middle'},
        {title: '招聘数量（名）', field: 'num', align: 'center', valign: 'middle',
            formatter: function (value, row, index) {
                if(value==0){
                    return '若干'
                }else{
                    return value
                }
            }},
        {title: '排序权重', field: 'sort', align: 'center', valign: 'middle',sortable: true},
        {title: '简历数量（份)', field: 'resumeCount', align: 'center', valign: 'middle'},
        {title: '状态', field: 'publishStatus', align: 'center', valign: 'middle'},
        {title: '最后编辑时间', field: 'modifiedTime', align: 'center', valign: 'middle',sortable: true},
        {title: '操作', field: '', align: 'center', valign: 'middle',
            formatter: function (value, row, index) {
                if(row.isPublish){
                    return ''
                        + '<a style="margin-right: 15px;" onclick="Job.openJobDetail('+row.id+')">详情</a>'
                        + '<a style="margin-right: 15px;" onclick="Job.CancelJobPublish('+row.id+')">取消发布</a>'
                }else{
                   if(row.resumeCount<=0){
                       return ''
                           + '<a style="margin-right: 15px;" onclick="Job.JobPublish('+row.id+')">发布</a>'
                           + '<a style="margin-right: 15px;" onclick="Job.openEditJob('+row.id+')">编辑</a>'
                           + '<a style="margin-right: 15px;" onclick="Job.delete('+row.id+')">删除</a>'
                   }else{
                       return ''
                           + '<a style="margin-right: 15px;" onclick="Job.JobPublish('+row.id+')">发布</a>'
                           + '<a style="margin-right: 15px;" onclick="Job.openEditJob('+row.id+')">编辑</a>'
                   }
                }

            }
        }
    ];
};

/**
 * 检查是否选中
 */
Job.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        Job.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加岗位管理
 */
Job.openAddJob = function () {
    var index = layer.open({
        type: 2,
        title: '新增岗位',
        area: ['800px', '700px'], //宽高
        fix: false, //不固定
        maxmin: false,
        content: Feng.ctxPath + '/job/job_add'
    });
    this.layerIndex = index;
};


/**
 * 点击编辑岗位管理
 */
    Job.openEditJob = function (id) {
    var index = layer.open({
        type: 2,
        title: '编辑岗位',
        area: ['800px', '700px'], //宽高
        fix: false, //不固定
        maxmin: false,
        content: Feng.ctxPath + '/job/job_update/'+id
    });
    this.layerIndex = index;
};

/**
 * 打开查看岗位管理详情
 */
Job.openJobDetail = function (id) {
    var index = layer.open({
        type: 2,
        title: '岗位详情',
        area: ['800px', '700px'], //宽高
        fix: false, //不固定
        maxmin: false,
        content: Feng.ctxPath + '/job/job_detail/' + id
    });
    this.layerIndex = index;
};

/**
 * 发布岗位管理
 */
Job.JobPublish = function (id) {
    var operation = function(){
        var ajax = new $ax(Feng.ctxPath + "/job/publish", function (data) {
            Feng.success("发布成功!");
            Job.table.refresh();
        }, function (data) {
            Feng.error("发布失败!" + data.responseJSON.message + "!");
        });
        ajax.set("id",id);
        ajax.start();
    };
    Feng.confirm("是否发布该岗位?", operation);
};

/**
 * 取消发布岗位管理
 */
Job.CancelJobPublish = function (id) {
    var operation = function(){
        var ajax = new $ax(Feng.ctxPath + "/job/cancelPublish", function (data) {
            Feng.success("取消成功!");
            Job.table.refresh();
        }, function (data) {
            Feng.error("取消成功!" + data.responseJSON.message + "!");
        });
        ajax.set("id",id);
        ajax.start();
    };
    Feng.confirm("是否取消发布该岗位?", operation);
};


/**
 * 删除岗位管理
 */
Job.delete = function (id) {
    var operation = function(){
        var ajax = new $ax(Feng.ctxPath + "/job/delete", function (data) {
            if(data.code==200){
                Feng.success("删除成功!");
                Job.table.refresh();
            }
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("id",id);
        ajax.start();
    };
    Feng.confirm("是否删除该岗位?", operation);
};

/**
 * 查询岗位管理列表
 */
Job.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    queryData['jobType'] = $("#jobTypeId").val();
    queryData['isPublish'] = $("#isPublish").val();
    Job.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = Job.initColumn();
    var table = new BSTable(Job.id, "/job/list", defaultColunms);
    table.showToolbar = false;
    Job.table = table.init();

    //获取岗位分类列表
    var ajax = new $ax(Feng.ctxPath + "/jobtype/list", function(data){
        // if(data.code==200){
            for(var i=0;i<data.rows.length;i++){
                var option=$('<option value="'+data.rows[i].id+'">'+data.rows[i].name+'</option>');
                $("#jobTypeId").append(option)
            }
        // }
    },function(data){
        Feng.error("获取岗位分类列表失败!" + data.responseJSON.message + "!");
    });
    ajax.start();
});
