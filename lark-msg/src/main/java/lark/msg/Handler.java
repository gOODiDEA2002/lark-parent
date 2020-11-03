package lark.msg;

import lark.util.msg.Message;

/**
 * @author cuigh
 */
@FunctionalInterface
public interface Handler {
    void handle(Message message);
}
