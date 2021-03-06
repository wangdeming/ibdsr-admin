package cn.ibdsr.web.common.exception;

/**
 * @Description 所有业务异常的枚举
 * @author fengshuonan
 * @date 2016年11月12日 下午5:04:51
 */
public enum BizExceptionEnum {

	/**
	 * 字典
	 */
	DICT_EXISTED(400,"字典已经存在"),
	ERROR_CREATE_DICT(500,"创建字典失败"),
	ERROR_WRAPPER_FIELD(500,"包装字典属性失败"),


	/**
	 * 文件上传
	 */
	FILE_READING_ERROR(400,"FILE_READING_ERROR!"),
	FILE_NOT_FOUND(400,"FILE_NOT_FOUND!"),
	FILE_CANT_BE_NULL(400,"上传文件不能为空!"),
	FILE_SIZE_TOO_LONG(400,"文件大小超出限制!"),
	UPLOAD_ERROR(500,"上传文件失败"),
	DOWNLOAD_ERROR(500,"下载文件失败"),
	EXPORT_ERROR(500,"导出EXCEL文件失败"),

	/**
	 * 权限和数据问题
	 */
	DB_RESOURCE_NULL(400,"数据库中没有该资源"),
	NO_PERMITION(405, "权限异常"),
	REQUEST_INVALIDATE(400,"请求数据格式不正确"),
	INVALID_KAPTCHA(400,"验证码不正确"),
	CANT_DELETE_ADMIN(600,"不能删除超级管理员"),
	CANT_FREEZE_ADMIN(600,"不能冻结超级管理员"),
	CANT_FREEZE_ME(600,"不能冻结自己"),
	CANT_CHANGE_ADMIN(600,"不能修改超级管理员角色"),
	CANT_UNFREEZE_ME(600,"不能解冻自己"),

	/**
	 * 账户问题
	 */
	USER_ALREADY_REG(401,"该用户已经注册"),
	NO_THIS_USER(400,"没有此用户"),
	USER_NOT_EXISTED(400, "没有此用户"),
	ACCOUNT_FREEZED(401, "账号被冻结"),
	OLD_PWD_NOT_RIGHT(402, "原密码不正确"),
	TWO_PWD_NOT_MATCH(405, "两次输入密码不一致"),
	ERROR_PASSWORD_FORMAT(405, "密码长度为6-30字符，允许包含字母、数字、符号或任意组合，不包含空格"),
	NULL_USER_ID(410, "用户ID不能为空"),
	ACCOUNT_NOT_EDIT(411, "账号不能修改"),

	/**
	 * 错误的请求
	 */
	MENU_PCODE_COINCIDENCE(400,"菜单编号和副编号不能一致"),
	EXISTED_THE_MENU(400,"菜单编号重复，不能添加"),
	DICT_MUST_BE_NUMBER(400,"字典的值必须为数字"),
	REQUEST_NULL(400, "请求有错误"),
	SESSION_TIMEOUT(400, "会话超时"),
	SERVER_ERROR(500, "服务器异常"),

	/**
	 * 角色管理
	 */
	ROLE_NAME_EXIST(420, "角色名称不可重复"),
	ROLE_NOT_EXIST(421, "没有此角色"),
	ADMIN_CANT_FREEZE(422, "管理员角色不能禁用"),
	ROLE_BING_USER(423, "角色已关联用户，不能禁用"),
	ADMIN_CANT_ALLOT(424, "管理员角色不能授权"),

	/**菜单管理*/
	MENU_CODE_IS_EXIT(450, "菜单编号已存在"),
	MENU_NOT_EXIT(451, "菜单编号已存在"),

	/**
	 * 岗位类别
	 */
	JOB_TYPE_NOT_EXIST(601, "没有此岗位类别信息"),
	JOB_TYPE_CONTAIN_JOB(602, "此岗位类别包含岗位,不可删除"),
	JOB_TYPE_SORT_IS_MAX(603, "此岗位类别已置底"),
	JOB_TYPE_SORT_IS_MIN(604, "此岗位类别已置顶"),

	/**
	 * 岗位
	 */
	JOB_NOT_EXIST(605, "没有此岗位类别信息"),
	JOB_PUBLISH_CANT_EDIT(606, "发布中的岗位不可编辑"),
	JOB_PUBLISH_CANT_DELETE(607, "发布中的岗位不可删除"),
	JOB_REL_RESUME_CANT_DELETE(608, "此岗位投递过简历,不可删除"),
	RESUME_TYPE_ERROR(609, "简历文件格式错误"),
	RESUME_FILE_LARGER(610, "简历文件不可超过3M"),

	/**
	 * 留言信息
	 */
	MESSAGE_NOT_EXIST(700, "没有此留言信息"),

	/**
	 * 入孵记录信息
	 */
	COMPANY_NOT_EXIST(700, "没有此入孵记录信息"),

	/**
	 * 新闻资讯
	 */
	NEWS_ID_IS_NULL(901, "新闻资讯ID不能为空"),
	NEWS_INFO_NOT_EXIST(902, "没有此新闻资讯信息"),
	NEWS_INFO_SHOW_TIME_ERROR(903, "展示时间不能设置在当前时间之后"),
	NEWS_INFO_EXTRA_FILE_TOO_BIG(904, "附件文件不可超过10M"),

	/**
	 * 领导关怀
	 */
	LEADERS_IMAGE_CANT_NULL(901, "领导关怀图片列表不可为空"),
	LEADERS_IMAGE_CANT_LARGER(902, "领导关怀图片不可超过九张"),
	LEADERS_INFO_NOT_EXIST(903, "没有此领导关怀信息"),
	LEADERS_INFO_PUBLISH_CANT_OPERATE(904, "此领导关怀已发布不可执行此操作"),
	LEADERS_IMAGE_NOT_EXIST(905, "没有此图片信息"),
	LEADERS_INFO_SHOW_TIME_ERROR(906, "展示时间不能设置在当前时间之后"),

	/**
	 * 员工风采
	 */
	EMPLOYEE_INFO_NOT_EXIST(901, "没有此员工风采信息"),
	EMPLOYEE_INFO_PUBLISH_CANT_OPERATE(902, "此员工风采已发布不可执行此操作"),
	EMPLOYEE_INFO_SHOW_TIME_ERROR(903, "展示时间不能设置在当前时间之后"),
	EMPLOYEE_INFO_ID_IS_NULL(904, "员工风采ID不能为空"),

	/**
	 * 大事记
	 */
	EVENTS_DATE_ERROR(901, "事记时间格式错误"),
	EVENTS_INFO_NOT_EXIST(902, "没有此大事记信息"),
	EVENTS_IMAGE_CANT_NULL(903, "领导关怀图片列表不可为空"),
	EVENTS_IMAGE_CANT_LARGER(904, "领导关怀图片不可超过三张"),

	/**
	 * 轮播图
	 */
	BANNER_IMAGE_CANT_LARGER(901, "轮播图不可超过五张"),
	;



	BizExceptionEnum(int code, String message) {
		this.friendlyCode = code;
		this.friendlyMsg = message;
	}
	
	BizExceptionEnum(int code, String message,String urlPath) {
		this.friendlyCode = code;
		this.friendlyMsg = message;
		this.urlPath = urlPath;
	}

	private int friendlyCode;

	private String friendlyMsg;
	
	private String urlPath;

	public int getCode() {
		return friendlyCode;
	}

	public void setCode(int code) {
		this.friendlyCode = code;
	}

	public String getMessage() {
		return friendlyMsg;
	}

	public void setMessage(String message) {
		this.friendlyMsg = message;
	}

	public String getUrlPath() {
		return urlPath;
	}

	public void setUrlPath(String urlPath) {
		this.urlPath = urlPath;
	}

}
