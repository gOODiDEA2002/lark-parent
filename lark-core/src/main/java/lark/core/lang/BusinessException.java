package lark.core.lang;

import lark.core.enums.BaseEnum;

/**
 * @author cuigh
 */
public class BusinessException extends RuntimeException implements Error {
    protected int code;
    protected String detail;


    public BusinessException() {
        super();
    }

    public BusinessException(int code) {
        super();
        this.code = code;
    }

    public BusinessException(Error error) {
        this(error.getCode(), error.getMessage());
    }

    public BusinessException(int code, String msg) {
        super(msg);
        this.code = code;
    }

    public BusinessException(BaseEnum baseEnum) {
        this(baseEnum.getCode(), baseEnum.getMsg());
    }

    public BusinessException(int code, Throwable inner) {
        super(inner);
        this.code = code;
    }

    public BusinessException(int code, String msg, Throwable inner) {
        super(msg, inner);
        this.code = code;
    }

    public BusinessException(int code, String msg, String detail) {
        super(msg);
        this.code = code;
        this.detail = detail;
    }

    public BusinessException(int code, String msg, String detail, Throwable inner) {
        super(msg, inner);
        this.code = code;
        this.detail = detail;
    }

    @Override
    public int getCode() {
        return code;
    }

    public String getDetail() {
        return detail;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }



}
