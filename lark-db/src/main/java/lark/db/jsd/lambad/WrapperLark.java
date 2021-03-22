package lark.db.jsd.lambad;

public class WrapperLark<T> {


    public static <T> SelectFilter<T> selectFilter() {
        SelectFilter<T> select = new CompareFilter<>();
        return select;
    }

    public static <T> UpdateFilter<T> updateFilter() {
        UpdateFilter<T> updateFilter = new CompareFilter<>();
        return updateFilter;
    }

    public static <T> DeleteFilter<T> deleteFilter() {
        DeleteFilter<T> deleteFilter = new CompareFilter<>();
        return deleteFilter;
    }


}
