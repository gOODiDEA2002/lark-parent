package lark.msg;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * @author andy
 */
public class MsgHandlerService {
    @Data
    @NoArgsConstructor
    @EqualsAndHashCode
    public static class ExecuteResult {
        /**
         * 是否成功
         */
        protected boolean success;

        /**
         * 错误信息
         */
        protected String errorInfo;
    }

    private final Map<String, Subscription> subs = new HashMap<>();

    public void addSubs( String name, Subscription sub ) {
        this.subs.put( name, sub );
    }

    public Map<String, Subscription> getSubs() {
        return subs;
    }

    public ExecuteResult execute( String name, String data ) {
        Subscription sub = this.subs.get( name.toLowerCase() );
        if ( sub != null ) {
            Handler handler = sub.getHandler();
            return handler.execute( name, data );
        }
        //
        ExecuteResult result = new ExecuteResult();
        result.setSuccess( false );
        result.setErrorInfo( String.format( ">>>Handler %s: 未找到", name ) );
        return result;
    }
}
