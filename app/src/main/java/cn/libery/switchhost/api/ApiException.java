package cn.libery.switchhost.api;

/**
 * @author shizhiqiang on 2018/1/18.
 */

public class ApiException extends RuntimeException {

    public int getCode() {
        return code;
    }

    private int code;

    public ApiException(int code, String msg) {
        this(getApiExceptionMessage(code, msg));
        this.code = code;
    }

    public ApiException(String detailMessage) {
        super(detailMessage);
    }

    /**
     * 由于服务器传递过来的错误信息直接给用户看的话，用户未必能够理解
     * 需要根据错误码对错误信息进行一个转换，在显示给用户
     *
     * @param code 服务器错误码
     * @param msg  服务器错误信息
     * @return 错误信息
     */
    private static String getApiExceptionMessage(int code, String msg) {
        return msg;
    }

}