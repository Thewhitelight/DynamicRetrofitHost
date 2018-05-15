package cn.libery.switchhost.model;

/**
 * @author shizhiqiang on 2018/4/10.
 */
public class Response<T> {
    public int code;
    public String msg;
    public T data;

    @Override
    public String toString() {
        return "Response{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
