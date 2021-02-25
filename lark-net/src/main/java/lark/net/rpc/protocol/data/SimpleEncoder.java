package lark.net.rpc.protocol.data;

import lark.core.codec.JsonCodec;
import lark.core.util.Strings;

/**
 * simple 序列化
 *
 * @author cuigh
 */
@SuppressWarnings("unchecked")
public class SimpleEncoder {
    public static final int DT_NULL = 0;
    public static final int DT_JSON_OBJECT = 150;

    private SimpleEncoder() {
        // 防止实例化
    }

    public static int getDataType(Class<?> clazz) {
        if (clazz == null) {
            return DT_NULL;
        } else {
            return DT_JSON_OBJECT;
        }
    }

    public static SimpleValue encode(Object obj) {
        if (obj == null) {
            return new SimpleValue(DT_NULL, Strings.EMPTY);
        }
        String data = JsonCodec.encode( obj );
        return new SimpleValue(DT_JSON_OBJECT, data);
    }

    public static Object decode(SimpleValue value, Class<?> clazz) {
        return decode(value.getType(), value.getData(), clazz);
    }

    public static Object decode(int type, String data, Class<?> clazz) {
        switch (type) {
            case DT_NULL:
                return null;
            case DT_JSON_OBJECT:
                return decodeJson(data, clazz);
            default:
                throw new RuntimeException("unknown object type: " + type);
        }
    }

    private static Object decodeJson(String data, Class<?> clazz) {
        return JsonCodec.decode( data, clazz );
    }
}
