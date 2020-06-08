function delImgEdit(obj){
    $('#images').parents('li').show();
    var index=$(obj).parent('li').index();
    $(obj).parent('.div-img').remove();
    imagesData.splice(index-1,1);
    if(imagesData.length==0){
        $('#images').parents('.form-group').find('.help-block').remove();
        $('#images').parents('.form-group').addClass('has-error');
        var tip=$('<small class="help-block" data-bv-validator="notEmpty" data-bv-for="title" data-bv-result="INVALID" style="">' +
            '封面图片不能为空' +
            '</small>')
        $('#images').parents('.upImg-div').append(tip);
    }
}


$(function(){
    var ajax = new $ax(Feng.ctxPath + "/leader/detail", function (data) {
        if(data.code==200){
            $('#leaderName').val(data.data.leaderWords.leaderName);
            $('#content').val(data.data.leaderWords.content);
            $('#isTop').val(data.data.leaderWords.isTop);
            $('#showTime').text(data.data.leaderWords.showTime);
            if(data.data.imageList.length>=9){
                $('#images').parents('li').hide();
            }
            for(var i=0;i<data.data.imageList.length;i++){
                var divImg=$(' <li class="div-img"  data-src="'+data.data.imageList[i].path+'" data-id="'+data.data.imageList[i].id+'">' +
                    ' <img src="'+data.data.imageList[i].path+'" alt="图片缺失">' +
                    ' <button onclick="delImgEdit(this)"><i class="fa fa-close"></i></button>' +
                    '</li>')
                $("#images").parents('ul').append(divImg);
                imagesData.push(data.data.imageList[i].path);
            }
        }else{
            Feng.error("获取详情失败!" + data.message + "!");
        }
    }, function (data) {
        Feng.error("获取详情失败!" + data.responseJSON.message + "!");
    });
    ajax.set("leaderId",$('#id').val());
    ajax.start();
    LeaderInfoDlg.Statistics('#content');
});