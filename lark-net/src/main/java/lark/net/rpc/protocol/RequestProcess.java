package lark.net.rpc.protocol;

import lark.core.codec.JsonCodec;
import lark.core.util.Strings;
import lark.net.rpc.protocol.data.SimpleEncoder;
import lark.net.rpc.protocol.data.SimpleValue;

import java.util.ArrayList;
import java.util.List;

public class RequestProcess {

    private RequestMessage message;

    public RequestProcess( RequestMessage message ) {
        this.message = message;
    }

    public void setArgs( Object[] params ) {
        if ( params != null && params.length > 0 ) {
            List<SimpleValue> args = new ArrayList<>();
            for ( Object param: params ) {
                args.add(SimpleEncoder.encode(param));
            }
            //
            this.message.setArgs( args );
        }
    }

    public Object[] getArgs( Class[] paramTypes ) {
        if ( paramTypes != null && paramTypes.length > 0 ) {
            Object[] params = new Object[paramTypes.length];
            for (int i = 0; i < message.getArgs().size(); i++)  {
                params[i] = SimpleEncoder.decode( message.getArgs().get( i ), paramTypes[i] );
            }
            return params;
        }
        return null;
    }

    public static RequestProcess from( String requestData ) {
        if ( Strings.isEmpty( requestData ) ) {
            return null;
        }
        //
        RequestMessage requestMessage = JsonCodec.decode( requestData, RequestMessage.class );
        return new RequestProcess( requestMessage );
    }

    public static String to( String service, String method, Object... args ) {
        RequestProcess protocol = new RequestProcess( new RequestMessage() );
        protocol.setService( service );
        protocol.setMethod( method );
        protocol.setArgs( args );
        return protocol.toMessage();
    }

    public void setService( String service ) {
        this.message.setService( service );
    }

    public String getService() {
        return this.message.getService();
    }

    public void setMethod( String method ) {
        this.message.setMethod( method );
    }

    public String getMethod() {
        return this.message.getMethod();
    }

    public String toMessage() {
        return JsonCodec.encode( message );
    }
}
