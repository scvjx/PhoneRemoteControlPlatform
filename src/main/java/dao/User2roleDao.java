package dao;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Component("user2role")
public class User2roleDao extends BaseDao {

    public List<Map<String, Object>> getRoleList(String userid){
        String sql = "select * from user2role where userid = :userid";
        Map args = new HashMap<String, String>();
        args.put("userid", userid);
        List<Map<String, Object>> ls = smartDao.getListForList(sql, args);
        return ls;
    }
}
