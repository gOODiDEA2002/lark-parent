package lark.db.jsd;

/**
 * Created by guohua.cui on 15/6/2.
 */
public enum SortType {
    ASC("ASC"), DESC("DESC");

    String value;

    SortType(String value) {
        this.value = value;
    }
}
