package dao;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("UserLogDao")
public class UserLogDao extends BaseDao{
    public void insertUserLog(Map args){
        String sql = "insert into userlog values(null,:userid,:starttime,:endtime,:deviceid,:model)";
        smartDao.insert(sql,args);
    }

    public void updateEndtime(Map args){
        String sql = "update userlog set endtime=:endtime where deviceid=:deviceid and model=:model and userid=:userid";
        smartDao.insert(sql,args);
    }

    public List<Map<String, Object>> getUserlogByUserid(String userid){
        String sql = "select * from userlog where userid=:userid";
        Map args = new HashMap<String, String>();
        args.put("userid", userid);
        List<Map<String, Object>> ls = smartDao.getListForList(sql, args);
        return ls;
    }

    public List<Map<String, Object>> getUserlog(){
        String sql = "select * from userlog";
        Map args = new HashMap<String, String>();
        List<Map<String, Object>> ls = smartDao.getListForList(sql, args);
        return ls;
    }
}
