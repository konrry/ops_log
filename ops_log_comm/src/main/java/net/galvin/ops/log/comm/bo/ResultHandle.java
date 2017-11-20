package net.galvin.ops.log.comm.bo;

import java.io.Serializable;

/**
 * Dubbo调用返回类型
 */
public class ResultHandle<T extends Object> implements Serializable {

    private static final long serialVersionUID = 8011630435306713588L;

    private boolean success = true;
    private String msg;
    private String errorCode;
    private String infoMsg;//提示信息

    private T t;

    public T getT() {
        return t;
    }
    public void setT(T t) {
        this.t = t;
    }

    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.success = false;
        this.msg = msg;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
    public String getErrorCode() {
        return errorCode;
    }

    public boolean isSuccess() {
        return success;
    }
    public boolean isFail() {
        return !isSuccess();
    }

    public ResultHandle() {
    }

    public ResultHandle(String msg) {
        setMsg(msg);
    }

    public String getInfoMsg() {
        return infoMsg;
    }
    public void setInfoMsg(String infoMsg) {
        this.infoMsg = infoMsg;
    }

}
