package lark.util.cache.cache;

import lombok.Data;

@Data
public class CacheItem {
    //@JSONField(name = "v")
    public String version;
    //@JSONField(name = "c")
    public boolean compressed;
    //@JSONField(name = "d")
    public byte[] data;

    public CacheItem() {
        // just for serialization
    }

    public CacheItem(byte[] data) {
        this.data = data;
    }
}