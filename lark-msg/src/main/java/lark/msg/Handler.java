package lark.msg;

/**
 * @author cuigh
 */
public interface Handler {
    void handle(Message message);

    MsgHandlerService.ExecuteResult execute(String name, String body );
}
