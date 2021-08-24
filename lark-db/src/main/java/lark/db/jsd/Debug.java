package lark.db.jsd;

import lark.db.jsd.result.BuildResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by noname on 15/11/19.
 */
public final class Debug {
    private final static String LOGGER_NAME = "lark.db.jsd.debug";
    private static final Logger LOGGER = LoggerFactory.getLogger(LOGGER_NAME);

    static void log(BuildResult result) {
        //if (LOGGER.isDebugEnabled()) {
            LOGGER.info("sql: {}, args: {}", result.getSql(), result.getArgs());
        //}
    }

    static void log(String sql, Object[] args) {
        //if (LOGGER.isDebugEnabled()) {
            LOGGER.info("sql: {}, args: {}", sql, args);
        //}
    }
}
