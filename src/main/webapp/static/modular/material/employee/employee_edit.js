
function delImgEdit(obj){
    $(obj).parent('.div-img').remove();
    $(".div-input").show();
    $('#coverImage').val('');
    $('#coverImage').attr('data-src','');
    var tip=$('<small class="help-block" data-bv-validator="notEmpty" data-bv-for="title" data-bv-result="INVALID" style="">' +
        '封面图片不能为空' +
        '</small>')
    $('#coverImage').parent('.div-input').after(tip);
    $('#coverImage').parents('.form-group').addClass('has-error').removeClass('has-success');
}


$(function(){
    var ajax = new $ax(Feng.ctxPath + "/employee/detail", function (data) {
        if(data.code==200){
            $('#title').val(data.data.title);
            $('#showTime').text(data.data.showTime);

            //封面图片
            $('#coverImage').attr('data-src',data.data.coverImage);
            $('#coverImage').attr('data-oldSrc',data.data.coverImage);
            $(".div-input").hide();
            var divImg=$(' <div class="div-img form-control">' +
                ' <img src="'+data.data.coverImage+'" alt="图片缺失">' +
                ' <button onclick="delImgEdit(this)"><i class="fa fa-close"></i></button>' +
                '</div>')
            $('.upImg-div').append(divImg);
            editor.txt.html(data.data.mainContent)
        }else{
            Feng.error("获取详情失败!" + data.message + "!");
        }
    }, function (data) {
        Feng.error("获取详情失败!" + data.responseJSON.message + "!");
    });
    ajax.set("employeeId",$('#id').val());
    ajax.start();
})