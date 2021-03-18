package lark.core.lang;


import lark.core.enums.BaseEnum;

import java.text.MessageFormat;

public class DataException extends BusinessException {

    protected Object data;


    public DataException(BaseEnum baseEnum) {
        super.code = baseEnum.getCode();
        super.detail = baseEnum.getMsg();
        this.data = null;
    }

    public DataException(BaseEnum baseEnum, Object data, String... arg) {
        super.code = baseEnum.getCode();
        super.detail = MessageFormat.format(baseEnum.getMsg(), arg);
        this.data = data;
    }

    public DataException(Integer code, String msg, String... arg) {
        super.code = code;
        super.detail = MessageFormat.format(msg, arg);
        this.data = data;
    }

    public Object getData() {
        return data;
    }
}
