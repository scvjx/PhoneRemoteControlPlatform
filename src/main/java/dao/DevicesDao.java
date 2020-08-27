package dao;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jiaxin on 2019/5/13.
 */
@Component("DevicesDao")
public class DevicesDao extends BaseDao{

    public void insertDevices(Map args){
        String sql = "insert into devices values(:deviceid,:resolution,:androidversion,:cpu,:manufacturer,:model,:status,:userid,:lastusetime,:minicapport,:adbkitport,:assignroleid)";
        smartDao.insert(sql,args);
    }

    public List<Map<String, Object>> getDevicesByDeviceidModel(String deviceid,String model){
        String sql = "select * from devices where deviceid = :deviceid and model = :model";
        Map args = new HashMap<String, String>();
        args.put("deviceid", deviceid);
        args.put("model", model);
        List<Map<String, Object>> ls = smartDao.getListForList(sql, args);
        return ls;

    }

    public List<Map<String, Object>> getDevicesByStatus(String status){
        String sql = "select * from devices where status=:status";
        Map args = new HashMap<String, String>();
        args.put("status", status);
        List<Map<String, Object>> ls = smartDao.getListForList(sql, args);
        return ls;
    }

    public void updateDevicesStatusPortByDeviceidModel(Map args) {
        String sql = "update devices set status =:status,minicapport=:minicapport  where deviceid = :deviceid and model = :model";
        smartDao.insert(sql,args);
    }

    public void updateDevicesStatus(Map args) {
        String sql = "update devices set status =:status";
        smartDao.insert(sql,args);
    }

    public void updateDevicesUseridByDeviceidModelUserid(Map args){
        String sql = "update devices set userid =:newid,status=:status where deviceid=:deviceid and userid=:userid";
        smartDao.insert(sql,args);
    }

    public void updateDevicesStatusPort(Map args){
        String sql = "update devices set status =:status,minicapport=:minicapport";
        smartDao.insert(sql,args);
    }

    public void updateDevicesUseridStatusByDeviceidModel(Map args){
        String sql = "update devices set userid =:userid,status=:status where deviceid=:deviceid and model=:model";
        smartDao.insert(sql,args);
    }

    public void updateDevicesRoleByDeviceidModel(Map args){
        String sql = "update devices set assignroleid =:assignroleid where deviceid=:deviceid and model = :model";
        smartDao.insert(sql,args);
    }

}
