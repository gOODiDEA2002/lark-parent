package lark.task.util;

import lark.core.lang.BusinessException;

import java.util.concurrent.ExecutionException;

/**
 * @author andy
 */
public class Retries {
    public static boolean tryTimes(int maxRetries, RetriableFunc t) {
        int count = 0;
        while (count < maxRetries) {
            try {
                t.run();
                return true;
            }
            catch (BusinessException e) {
                if (++count >= maxRetries) {
                    return false;
                }
            }
        }
        return false;
    }
}
