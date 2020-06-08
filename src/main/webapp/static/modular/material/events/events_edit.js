function delImgEdit(obj){
    $('#images').parents('li').show();
    var index=$(obj).parent('li').index();
    $(obj).parent('.div-img').remove();
    imagesData.splice(index-1,1);
}


$(function(){
    var ajax = new $ax(Feng.ctxPath + "/events/detail", function (data) {
        if(data.code==200){
            $('#content').val(data.data.event.content);
            $('#title').val(data.data.event.title);
            $('#eventYear').val(data.data.event.eventYear);
            $('#eventMonth').val(data.data.event.eventMonth);
            setDays(eventYear,eventMonth,eventDay)
            $('#eventDay').val(data.data.event.eventDay);
            if(data.data.imageList.length>=3){
                $('#images').parents('li').hide();
            }else{
                $('#images').parents('li').show();
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
    ajax.set("eventId",$('#id').val());
    ajax.start();
    EventsInfoDlg.Statistics('#content');
    EventsInfoDlg.Statistics('#title');
})