package lark.api.response;

import lark.core.lang.BusinessException;

import java.text.MessageFormat;

/**
 * Created by Andy Yuan on 2020/7/31.
 */
public class ApiFaultException extends BusinessException {
    public ApiFaultException(int errorCode, String messageFormat, Object... messageArgs) {
        super(errorCode, MessageFormat.format(messageFormat, messageArgs));
    }
}
