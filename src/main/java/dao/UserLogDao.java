package dao;

import org.springframework.stereotype.Component;

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
}
