var UserChpwd = {
    oldPwd: {
        validators: {
            notEmpty: {
                message: '密码不能为空'
            }
            // regexp: {//正则验证
            //     regexp: /(?!^[0-9]+$)(?!^[A-z]+$)(?!^[^A-z0-9]+$)^.{6,30}$/,
            //     message: '6位-30位，可以是字母与数字的组合，也可以是纯数字或纯字母'
            // }
        }
    },
    newPwd: {
        validators: {
            notEmpty: {
                message: '密码不能为空'
            },
            // regexp: {//正则验证
            //     regexp: /^(?![\d]+$)(?![a-zA-Z]+$)(?![!#$%^&*]+$)[\da-zA-Z!#$%^&*]{6,30}$/,
            //     message: '6位-30位，必须包含数字、字母或字符中的两种，不包含空格！'
            // },
            identical: {
                field: 'rePwd',
                message: '两次输入密码不一致！'
            }
        }
    },
    rePwd: {
        validators: {
            notEmpty: {
                message: '确认新密码不能为空'
            },
            // regexp: {//正则验证
            //     regexp: /^(?![\d]+$)(?![a-zA-Z]+$)(?![!#$%^&*]+$)[\da-zA-Z!#$%^&*]{6,30}$/,
            //     message: '6位-30位，必须包含数字、字母或字符中的两种，不包含空格！'
            // },
            identical: {
                field: 'newPwd',
                message: '两次输入密码不一致！'
            }
        }
    }
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
UserChpwd.set = function (key, val) {
    this.userInfoData[key] = (typeof value == "undefined") ? $("#" + key).val() : value;
    return this;
};

/**
 * 收集数据
 */
UserChpwd.collectData = function () {
    this.set('oldPwd').set('newPwd').set('rePwd');
};

/**
 * 清除数据
 */
UserChpwd.clearData = function () {
    this.userInfoData = {};
};

$(function () {
    Feng.initValidator("changePwdForm", UserChpwd);
});
/**
 * 验证数据是否为空
 */
UserChpwd.validate = function () {
    $('#changePwdForm').data("bootstrapValidator").resetForm();
    $('#changePwdForm').bootstrapValidator('validate');
    return $("#changePwdForm").data('bootstrapValidator').isValid();
};

UserChpwd.closePage = function () {
    var index = parent.layer.getFrameIndex(window.name);
    parent.layer.close(index);
}
UserChpwd.changePwd = function () {

    this.clearData();
    this.collectData();

    if (!this.validate()) {
        return;
    }

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/mgr/changePwd", function (data) {
        if (data.code == 200) {
            Feng.success(data.message);
            UserChpwd.closePage();
        }else {
            Feng.error(data.message + "!");
        }
    }, function (data) {
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.userInfoData);
    ajax.start();
}