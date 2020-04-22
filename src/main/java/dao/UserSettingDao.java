package dao;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("UserSettingDao")
public class UserSettingDao extends BaseDao {

    public void insertUserSetting(Map args){
        String sql = "insert into usersetting values(null,:userid,:deviceid,:model,:minicapresolution)";
        smartDao.insert(sql,args);
    }

    public List<Map<String, Object>> getMinicapResolution(String deviceid, String model,String userid){
        String sql = "select * from usersetting where deviceid = :deviceid and model = :model and userid=:userid";
        Map args = new HashMap<String, String>();
        args.put("deviceid", deviceid);
        args.put("model", model);
        args.put("userid", userid);
        List<Map<String, Object>> ls = smartDao.getListForList(sql, args);
        return ls;
    }

    public void updateMinicapResolution(Map args){
        String sql = "update usersetting set minicapresolution=:minicapresolution where deviceid=:deviceid and model=:model and userid=:userid";
        smartDao.insert(sql,args);
    }
}
