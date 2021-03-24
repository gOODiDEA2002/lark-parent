package lark.db.jsd;

/**
 * 条件类型
 */
public enum FilterType {
    EQ("="),
    NE("<>"),
    LT("<"),
    GT(">"),
    LTE("<="),
    GTE(">="),
    IN("IN"),
    NIN("NOT IN"),
    LK("LIKE"),
    LKLEFT("LIKE LEFT"),
    LKRIGHT("LIKE RIGHT"),
    NOTLK("NOT LIKE"),
    BETWEEN("BETWEEN"),
    NOTBETWEEN("NOT BETWEEN"),
    OR("OR");

    String value;

    FilterType(String value) {
        this.value = value;
    }
}
