package dao;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("ParameterDao")
public class ParameterDao extends BaseDao {

    public List<Map<String, Object>> getParamByName(String name){
        String sql = "select * from parameter where name = :name";
        Map args = new HashMap<String, String>();
        args.put("name", name);
        List<Map<String, Object>> ls = smartDao.getListForList(sql, args);
        return ls;
    }
}
