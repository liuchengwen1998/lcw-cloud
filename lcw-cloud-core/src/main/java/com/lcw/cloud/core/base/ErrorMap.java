package com.lcw.cloud.core.base;




import com.lcw.cloud.core.exception.BizException;

import java.util.HashMap;
import java.util.Map;

/**
 * 错误信息map类
 *
 * @author ：yzhang
 * @since ：2022/7/27 09:55
 */
public class ErrorMap {

    protected static final Map<String, String> ERROR_MAP = new HashMap<>(50);

    public static final String getMsg(String code) {
        return ERROR_MAP.get(code);
    }

    public static final void setErr(String code, String msg) {
        ERROR_MAP.put(code, msg);
    }

    public static final BizException ofException(String code) {
        return new BizException(code, ErrorMap.getMsg(code));
    }

    /**
     * 参数类错误
     */
    public static final String ERROR_COMMON_PARAMS_ERROR = "400";

    /**
     * 暂不支持该文件类型
     */
    public static final String ERROR_NOT_SUPPORT_FILE_TYPE = "1000";

    /**
     * 上传文件有误，请重新上传
     */
    public static final String ERROR_UPLOAD_FILE_PARSE_FAILED = "1001";

    /**
     * 文件后缀不存在
     */
    public static final String ERROR_FILE_SUFFIX_NOT_EXISTS = "1002";

    /**
     * 上传文件失败
     */
    public static final String ERROR_UPLOAD_FILE_FAILED = "1003";

    /**
     * 上传文件失败
     */
    public static final String ERROR_DELETE_FILE_FAILED = "1004";


    /**
     * 记录不允许修改
     */
    public static final String ERROR_RECORDED_NOT_ALLOWED_MODIFY = "1005";

    /**
     * 记录不允许删除
     */
    public static final String ERROR_RECORDED_NOT_ALLOWED_REMOVE = "1006";

    /**
     * 记录不允许删除
     */
    public static final String ERROR_JSON_PARSER_ERROR = "1007";

    /**
     * 下载文件失败
     */
    public static final String ERROR_DOWNLOAD_FILE_FAILED = "1008";

    /**
     * 文件不存在
     */
    public static final String ERROR_FILE_NOT_EXIST = "1009";

    /**
     * 获取秘钥失败
     */
    public static final String ERROR_SECRET_NOT_SUPPORT = "1010";
    /**
     * 比较数据不能为空
     */
    public static final String ERROR_DATA_NOT_NULL = "1011";
    /**
     * 文件名称不能为空
     */
    public static final String ERROR_FILE_NAME_NOT_BLANK = "1012";

    /**
     * 认证失败
     */
    public static final String ERROR_AUTHENTICATION_FAILURE = "1013";
    /**
     * 会话过期
     */
    public static final String ERROR_SESSION_OVERDUE = "1014";
    /**
     * 验签失败
     */
    public static final String ERROR_SIGN_FAILED = "1015";

    /**
     * 账号异地登录
     */
    public static final String ERROR_ACCOUNT_ANOTHER_PLACE_LOGIN = "账号已在异地登录，请注意账号安全";

    static {
        ERROR_MAP.put(ERROR_COMMON_PARAMS_ERROR, "参数错误");
        ERROR_MAP.put(ERROR_NOT_SUPPORT_FILE_TYPE, "暂不支持该文件类型");
        ERROR_MAP.put(ERROR_UPLOAD_FILE_PARSE_FAILED, "上传文件有误，请重新上传");
        ERROR_MAP.put(ERROR_FILE_SUFFIX_NOT_EXISTS, "文件后缀不存在");
        ERROR_MAP.put(ERROR_UPLOAD_FILE_FAILED, "上传文件失败");
        ERROR_MAP.put(ERROR_DELETE_FILE_FAILED, "文件删除失败");
        ERROR_MAP.put(ERROR_RECORDED_NOT_ALLOWED_MODIFY, "不允许修改");
        ERROR_MAP.put(ERROR_RECORDED_NOT_ALLOWED_REMOVE, "不允许删除");
        ERROR_MAP.put(ERROR_JSON_PARSER_ERROR, "解析JSON错误，请检查格式");
        ERROR_MAP.put(ERROR_DOWNLOAD_FILE_FAILED, "下载文件失败");
        ERROR_MAP.put(ERROR_FILE_NOT_EXIST, "文件不存在");
        ERROR_MAP.put(ERROR_SECRET_NOT_SUPPORT, "获取秘钥失败：无此算法。");
        ERROR_MAP.put(ERROR_FILE_NAME_NOT_BLANK, "文件名称不能为空");
        ERROR_MAP.put(ERROR_AUTHENTICATION_FAILURE, "认证失败");
        ERROR_MAP.put(ERROR_SESSION_OVERDUE, "会话已过期，请重新登录");
        ERROR_MAP.put(ERROR_ACCOUNT_ANOTHER_PLACE_LOGIN, "账号已在异地登录，请注意账号安全");
        ERROR_MAP.put(ERROR_SIGN_FAILED, "当前操作异常，请联系管理员");
    }
}
