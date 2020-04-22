package dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据库操作公共类
 */
@Component("SmartDao")
public class SmartDao {

    @Resource(name = "namedParameterJdbcTemplate")
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public void batchInsert(String sql, Map[] args) {
        int[] res = namedParameterJdbcTemplate.batchUpdate(sql, args);
        for (int i = 0; i < res.length; i++) {
            if (res[i] == 0) {
                System.out.println(" insert data  failed ");
            }
        }
    }


    public List<String> getAllRowData(String sql) {
        List<String> namelist = namedParameterJdbcTemplate.queryForList(sql, (SqlParameterSource) new HashMap<>(), String.class);
        return namelist;
    }

    public Map<String, String> getListForMap(String sql, Map args) {
        Map resultList = new HashMap();
        try {
            resultList = namedParameterJdbcTemplate.queryForMap(sql, args);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
        return resultList;
    }

    public List<Map<String, Object>> getListForList(String sql, Map args) {
        List<Map<String, Object>> resultList;
        try {
            resultList = namedParameterJdbcTemplate.queryForList(sql, args);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
        return resultList;
    }

    public void insert(String sql, Map args) {
        int res = namedParameterJdbcTemplate.update(sql, args);
        if (res == 0) {
        }
    }


    public void setNamedParameterJdbcTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
        return namedParameterJdbcTemplate;
    }

    public void deleteTable(String sql, Map args) {
        namedParameterJdbcTemplate.update(sql, args);
    }

}
