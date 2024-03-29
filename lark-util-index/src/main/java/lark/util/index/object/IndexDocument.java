package lark.util.index.object;

/**
 * ElasticSearchDocument
 *
 * @author star
 */
public class IndexDocument<T> {

    private String id;

    private T data;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
