package dao;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jiaxin on 2019/5/14.
 */
@Component("MobilePhoneInfoDao")
public class MobilePhoneInfoDao extends BaseDao{
    public List<Map<String, Object>> getAllMobileInfo(){
        String sql = "select * from mobilephoneinfo";
        Map args = new HashMap<String, String>();
        List<Map<String, Object>> ls = smartDao.getListForList(sql, args);
        return ls;
    }

    public List<Map<String, Object>> getMobileInfoByDeviceIdModel(String deviceid,String model){
        String sql = "select * from mobilephoneinfo where deviceid=:deviceid and model=:model";
        Map args = new HashMap<String, String>();
        args.put("deviceid",deviceid);
        args.put("model",model);
        List<Map<String, Object>> ls = smartDao.getListForList(sql, args);
        return ls;
    }

    public List<Map<String, Object>> getMobileInfoByPhonebrand(String phonebrand){
        String sql = "select * from mobilephoneinfo where phonebrand=:phonebrand";
        Map args = new HashMap<String, String>();
        args.put("phonebrand",phonebrand);
        List<Map<String, Object>> ls = smartDao.getListForList(sql, args);
        return ls;
    }

    public List<Map<String, Object>> getMobileInfoByResolutionratio(String resolutionratio){
        String sql = "select * from mobilephoneinfo where resolutionratio=:resolutionratio";
        Map args = new HashMap<String, String>();
        args.put("resolutionratio",resolutionratio);
        List<Map<String, Object>> ls = smartDao.getListForList(sql, args);
        return ls;
    }

    public List<Map<String, Object>> getMobileInfoByAndroidVersion(String androidversion){
        String sql = "select * from mobilephoneinfo where androidversion=:androidversion";
        Map args = new HashMap<String, String>();
        args.put("androidversion",androidversion);
        List<Map<String, Object>> ls = smartDao.getListForList(sql, args);
        return ls;
    }

    public List<Map<String, Object>> getAllMobileType(){
        String sql = "select distinct phonebrand from mobilephoneinfo where owner='1'  order by phonebrand  desc";
        Map args = new HashMap<String, String>();
        List<Map<String, Object>> ls = smartDao.getListForList(sql, args);
        return ls;
    }

    public List<Map<String, Object>> getAllMobileAndroidVersion(){
        String sql = "select distinct androidversion from mobilephoneinfo where owner='1'  order by phonebrand  desc";
        Map args = new HashMap<String, String>();
        List<Map<String, Object>> ls = smartDao.getListForList(sql, args);
        return ls;
    }

    public List<Map<String, Object>> getAllMobileResolutionratio(){
        String sql = "select distinct resolutionratio from mobilephoneinfo where owner='1'  order by resolutionratio  desc";
        Map args = new HashMap<String, String>();
        List<Map<String, Object>> ls = smartDao.getListForList(sql, args);
        return ls;
    }
}
