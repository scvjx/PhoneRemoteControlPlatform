package dao;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("RolesDao")
public class RolesDao extends BaseDao{

    public List<Map<String, Object>> getRolesList(){
        String sql = "select * from roles";
        Map args = new HashMap<String, String>();
        List<Map<String, Object>> ls = smartDao.getListForList(sql, args);
        return ls;
    }

    public List<Map<String, Object>> getRolesListById(String roleid){
        String sql = "select * from roles where id=:roleid";
        Map args = new HashMap<String, String>();
        args.put("roleid",roleid);
        List<Map<String, Object>> ls = smartDao.getListForList(sql, args);
        return ls;
    }
}
