package lark.db.jsd.result;

import lark.db.jsd.*;
import lark.db.jsd.util.PageEntity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by guohua.cui on 15/5/27.
 */
public final class PageResult implements AutoCloseable {
    private ConnectionManager manager;
    private String sql;
    private List<Object> args;
    private ResultSet rs;
    private Connection conn;
    private PreparedStatement statement;
    private Integer pageSize;
    private Integer pageIndex;

    public PageResult(ConnectionManager manager, String sql, List<Object> args, Integer pageIndex, Integer pageSize) {
        this.manager = manager;
        this.sql = sql;
        this.args = args;
        this.pageSize = pageSize;
        this.pageIndex = pageIndex;
    }


    /*
     * 读取所有记录转换为指定的数据实体, 并在读取所有数据后关闭数据库资源
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> List<T> all(Class<T> clazz) {
        try {
            List<T> list = new ArrayList<>();
            ResultSet rs = getResultSet();
            ResultSetMetaData metaData = rs.getMetaData();
            Mapper.EntityInfo info = Mapper.getEntityInfo(clazz);
            while (rs.next()) {
                T obj = clazz.newInstance();
                for (int i = 1; i <= metaData.getColumnCount(); i++) {
                    info.setValue(obj, metaData.getColumnLabel(i), rs.getObject(i));
                }
                list.add(obj);
            }
            return list;
        } catch (Exception e) {
            throw new JsdException(e);
        } finally {
            this.close();
        }
    }


    @Override
    public void close() {
        Releaser.release(rs);
        Releaser.release(statement);
        Releaser.release(manager, conn);
        rs = null;
        statement = null;
        conn = null;
    }

    private ResultSet getResultSet() throws SQLException {
        if (rs != null) return rs;
        conn = this.manager.getConnection();
        statement = conn.prepareStatement(sql);
        if (args != null) {
            for (int i = 0; i < args.size(); i++) {
                statement.setObject(i + 1, args.get(i));
            }
        }
        rs = statement.executeQuery();
        return rs;
    }

    private int getCount() throws SQLException {
        if (rs != null) return 0;
        conn = this.manager.getConnection();
        //去除limit
        int limit = sql.indexOf("LIMIT");
        String substring = sql.substring(0, limit);
        String sq = "  SELECT count(1) as count  FROM( ";
        sq += substring;
        sq += " ) as t ";
        statement = conn.prepareStatement(sq);
        if (args != null) {
            for (int i = 0; i < args.size(); i++) {
                statement.setObject(i + 1, args.get(i));
            }
        }
        rs = statement.executeQuery();
        int i = 0;
        if (rs.next()) {
            i = rs.getInt("count");
        }
        return i;
    }

    public int count() {
        try {
            return getCount();
        } catch (Exception e) {
            throw new JsdException(e);
        } finally {
            close();
        }
    }

    public <T> PageEntity<T> pageResult(Class<T> tClass) {
        PageEntity pageEntity = new PageEntity();
        int count = count();
        pageEntity.setList(all(tClass));
        pageEntity.setPageTotalSize(count);
        pageEntity.setPageIndex(pageIndex);
        pageEntity.setPageSize(pageSize);
        //计算总页数
        int pageCount = totalPage(count, pageSize);
        pageEntity.setPageCount(pageCount);
        return pageEntity;
    }

    private int totalPage(int totalCount, int pageSize) {
        if (pageSize == 0) {
            return 0;
        }
        return totalCount % pageSize == 0 ? (totalCount / pageSize) : (totalCount / pageSize + 1);
    }


}
