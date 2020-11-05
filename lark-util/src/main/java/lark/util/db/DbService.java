package lark.util.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

/**
 * BaseDao
 *
 * @author smallk
 * @date 2018/7/29 21:36
 */
@Repository
public class DbService {

    @Autowired
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    /**
     * 返回map
     */
    public Map<String, Object> queryMapBySQL(String sql, SqlParameterSource params) {
        return namedParameterJdbcTemplate.queryForMap(sql, params);
    }

    /**
     * 返回指定对象，例如 User Student ...
     */
    public <T> T queryObject(String sql, Class<T> clazz, SqlParameterSource params) {
        return namedParameterJdbcTemplate.queryForObject(sql, params, new BeanPropertyRowMapper<>(clazz));
    }

    /**
     * 返回指定简单对象，例如 String Integer ...
     */
    public <T> T querySimpleObjectBySQL(String sql, Class<T> clazz, SqlParameterSource params) {
        return namedParameterJdbcTemplate.queryForObject(sql, params, clazz);

    }

    /**
     * 返回指定对象list，例如 List<User> List<Student> ...
     */
    public <T> List<T> queryObjectListBySQL(String sql, Class<T> clazz, SqlParameterSource params) {
        return namedParameterJdbcTemplate.query(sql, params, new BeanPropertyRowMapper<>(clazz));
    }

    /**
     * 返回指定简单对象list，例如 List<String> List<Integer> ...
     */
    public <T> List<T> querySimpleObjectListBySQL(String sql, Class<T> clazz, SqlParameterSource params) {
        return namedParameterJdbcTemplate.queryForList(sql, params, clazz);
    }

    /**
     * 返回list
     */
    public List<Map<String, Object>> queryListBySQL(String sql, SqlParameterSource params) {
        return namedParameterJdbcTemplate.queryForList(sql, params);
    }

    /**
     * 更新sql
     */
    public int updateSQL(String sql, SqlParameterSource params) {
        return namedParameterJdbcTemplate.update(sql, params);
    }

    /**
     * insert sql 并且返回 id
     */
    public long insertSQL(String sql, SqlParameterSource params) {
        return namedParameterJdbcTemplate.update( sql, params );
    }
}
