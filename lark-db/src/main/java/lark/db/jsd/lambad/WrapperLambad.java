package lark.db.jsd.lambad;

public class WrapperLambad<T,M> {

    public static <T,M> SelectFilter<T,M> selectFilter() {
        SelectFilter<T, M> select = new CompareFilter<>();
        return select;
    }

    public static <T,M> UpdateFilter<T, M> updateFilter() {
        UpdateFilter<T, M> updateFilter = new CompareFilter<>();
        return updateFilter;
    }

    public static <T,M> DeleteFilter<T, M> deleteFilter() {
        DeleteFilter<T, M> deleteFilter = new CompareFilter<>();
        return deleteFilter;
    }


}
