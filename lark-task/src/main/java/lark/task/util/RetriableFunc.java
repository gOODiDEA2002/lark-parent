package lark.task.util;

import lark.core.lang.BusinessException;

public interface RetriableFunc {
    void run() throws BusinessException;
}
