package lark.db.jsd.util;

public class WrapperLark<T> {

    public static <T> QueryFilter<T> lambadFilter() {
        return new QueryFilter();
    }


//    public static <T> LambadQuery<T> lambadQueryShard(String shardDbName, Class<?> cla) {
//        DatabaseService databaseService = new DatabaseService();
//        Database database = databaseService.getShard(shardDbName);
//        return database.lambadQuery(cla);
//    }


}
