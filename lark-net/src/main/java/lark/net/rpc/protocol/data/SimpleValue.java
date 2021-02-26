package lark.net.rpc.protocol.data;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Created by noname on 15/12/6.
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class SimpleValue {
    private int type;

    private String data;

    public SimpleValue(int dataType, String data) {
        this.type = dataType;
        this.data = data;
    }
}
