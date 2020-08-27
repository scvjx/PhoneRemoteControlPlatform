package dao;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("UsersDao")
public class UsersDao extends BaseDao{
    public List<Map<String, Object>> getUsersListById(String userid){
        String sql = "select * from users where id=:userid";
        Map args = new HashMap<String, String>();
        args.put("userid",userid);
        List<Map<String, Object>> ls = smartDao.getListForList(sql, args);
        return ls;
    }
}
