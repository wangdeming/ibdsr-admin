/**
 * 简历管理管理初始化
 */
var Resume = {
    id: "ResumeTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
Resume.initColumn = function () {
    return [
        {field: 'selectItem', checkbox: true,},
        {title: '简历id', field: 'id', visible: false, align: 'center', valign: 'middle'},
        {title: '阅读状态', field: 'isRead', visible: false, align: 'center', valign: 'middle'},
        {title: '投递岗位', field: 'jobName', align: 'center', valign: 'middle'},
        {title: '姓名', field: 'name',align: 'center', valign: 'middle'},
        {title: '性别', field: 'sexName',align: 'center', valign: 'middle'},
        {title: '出生年月', field: 'birthday',align: 'center', valign: 'middle'},
        {title: '联系电话', field: 'phone',align: 'center', valign: 'middle'},
        {title: '电子邮箱', field: 'email',align: 'center', valign: 'middle'},
        {title: '附件', field: '',align: 'center', valign: 'middle',
            formatter: function (value, row, index) {
                return ''
                    + '<a style="margin-right: 15px;" href="javascript:void(0)" onclick="downloadFile('+row.id+')">'+row.resumeName+'</a>'
            }
        },
        {title: '备注', field: 'remark',align: 'center', valign: 'middle',width:'400px'},
        {title: '投递时间', field: 'createdTime',align: 'center', valign: 'middle',sortable: true},
        {title: '状态', field: 'readStatus',align: 'center', valign: 'middle'},
        {title: '操作', field: '',align: 'center', valign: 'middle',
            formatter: function (value, row, index) {
                return ''
                    + '<a style="margin-right: 15px;" onclick="Resume.checkDetail(this)" data-url="'+row.resumePath+'" data-id="'+row.id+'">查看</a>'
                    + '<a style="margin-right: 15px;" onclick="Resume.export('+row.id+')">导出</a>'
            }
         },
    ];
};

/**
 * 检查是否选中
 */
Resume.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        Resume.seItem = selected;
        return true;
    }
};


/**
 * 打开查看附件详情
 */
Resume.checkDetail = function (obj) {
    var resumeIds=$(obj).attr('data-id');
    var url=$(obj).attr('data-url');
    var ajax = new $ax(Feng.ctxPath + "/resume/read", function (data) {
        if(data.code==200){
            var onlineViewType = ['doc', 'docx', 'xls', 'xlsx', 'xlsm', 'ppt', 'pptx']
            var fileTypeName = url.substring(url.lastIndexOf('.') + 1, url.length).split('?')[0]
            var isWord = onlineViewType.find((type) => type === fileTypeName)
            if (isWord) {
                url = 'http://view.officeapps.live.com/op/view.aspx?src=' + url;
                window.open(url,'about:blank');
            }else{
                window.open(url, '_blank')
            }
            Resume.search();
        }
    }, function (data) {
        Feng.error("查看失败!" + data.responseJSON.message + "!");
    });
    ajax.set('resumeIds',resumeIds);
    ajax.start();
};


/**
 * 查询简历管理列表
 */
Resume.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    queryData['jobId'] = $("#jobId").val();
    queryData['isRead']=$('.radio input[type="radio"]:checked').val();
    Resume.table.refresh({query: queryData});
};


/**
 * 标记为已读
 */
Resume.hasRead=function(){
    if (this.check()) {
        var resumeIdsData=[];
        for(var i=0;i<Resume.seItem.length;i++){
            resumeIdsData.push(Resume.seItem[i].id);
        }
        var resumeIds = resumeIdsData.join(",");
        var ajax = new $ax(Feng.ctxPath + "/resume/read", function (data) {
            if(data.code==200){
                Feng.success("标记成功!");
                Resume.table.refresh();
            }
        }, function (data) {
            Feng.error("标记失败!" + data.responseJSON.message + "!");
        });
        ajax.set('resumeIds',resumeIds);
        ajax.start();
    }
};


/**
 * 导出
 */
Resume.export = function (id) {
    window.location = Feng.ctxPath + "/resume/export?resumeIds="+id;
};


/**
 * 批量导出
 */
Resume.batchExport=function(){
    if (this.check()) {
        var resumeIdsData=[];
        for(var i=0;i<Resume.seItem.length;i++){
            resumeIdsData.push(Resume.seItem[i].id);
        }
        var resumeId = resumeIdsData.join(",");
        window.location = Feng.ctxPath + "/resume/export?resumeIds="+resumeId;
    }
};

function downloadFile(id){
    window.location = Feng.ctxPath + "/resume/download?resumeId="+id;

}

$(function () {
    var defaultColunms = Resume.initColumn();
    var table = new BSTable(Resume.id, "/resume/list", defaultColunms);
    table.showToolbar=false;
    table.queryParams={'isRead': 0};
    Resume.table = table.init();

    var ajax = new $ax(Feng.ctxPath + "/resume/job/list", function (data) {
       if(data.code==200){
           for(var i=0;i<data.data.length;i++){
               var option=$('<option value="'+data.data[i].id+'">'+data.data[i].name+'</option>');
               $("#jobId").append(option)
           }
       }
    }, function (data) {
        Feng.error("岗位列表加载失败!" + data.responseJSON.message + "!");
    });
    ajax.start();

    $('.radio input[type="radio"]').change(function(e){
        if($(this).is(':checked')){
            var state=$(this).val();
            Resume.table.refresh({query: {'isRead': state}});
        }
    });
});
