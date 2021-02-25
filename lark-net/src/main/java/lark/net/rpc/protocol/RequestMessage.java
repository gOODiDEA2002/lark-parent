package lark.net.rpc.protocol;

import lark.net.rpc.protocol.data.SimpleValue;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode
public class RequestMessage {
    private String service;
    private String method;
    private List<SimpleValue> args;
    private String traceId;
    private String id;
    private String client;
    private String server;
}
