package service;

import dao.DevicesDao;
import dao.MobilePhoneInfoDao;
import org.springframework.stereotype.Component;
import tools.LogTools;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jiaxin on 2019/5/14.
 * 设备列表显示的处理实现
 */
@Component("DeviceListService")
public class DeviceListService {
    private LogTools logTools = new LogTools();
    @Resource
    DevicesDao devicesDao;
    @Resource
    MobilePhoneInfoDao mobilePhoneInfoDao;

    public List<Map<String, Object>> getPhoneInfo(String userid){

        List<Map<String, Object>> phoneList = new ArrayList<>();
        List<Map<String, Object>> mobilephoneList = null;
        mobilephoneList = mobilePhoneInfoDao.getAllMobileInfo();//查询所有设备库信息
        List<Map<String, Object>> deviceList = devicesDao.getDevicesByStatus("1");//查询所有在线设备信息
        for (Map mobilephone:mobilephoneList) {
            Map phoneitem = new HashMap();
            phoneitem.put("phonetype",mobilephone.get("phonetype"));
            phoneitem.put("resolutionratio",mobilephone.get("resolutionratio"));
            phoneitem.put("androidversion",mobilephone.get("androidversion"));
            phoneitem.put("iconpath",mobilephone.get("iconpath"));
            phoneitem.put("deviceid",mobilephone.get("deviceid"));
            phoneitem.put("model",mobilephone.get("model"));
            phoneitem.put("status","占用");
            for(Map device:deviceList){
                if(device.get("model").equals(mobilephone.get("model")) && device.get("deviceid").equals(mobilephone.get("deviceid"))){
                    //在线设备判断
                    if(device.get("userid").equals(userid)){
                        //当前用户占用
                        phoneitem.put("status","停止使用");
                    }
                    if("".equals(device.get("userid"))){
                        //没有用户占用
                        phoneitem.put("status","开始使用");
                    }
                }
            }
            phoneList.add(phoneitem);
        }
        return phoneList;
    }

    public void stopDevice(String userid,String deviceid,String model){
        HashMap args = new HashMap();
        args.put("userid",userid);
        args.put("deviceid",deviceid);
        args.put("model",model);
        args.put("newid","");
        args.put("status","1");
        devicesDao.updateDevicesUseridByDeviceidModelUserid(args);

    }

    public List<Map<String, Object>> getPhoneType(){
        List<Map<String, Object>> phoneTypeList = new ArrayList<>();
        phoneTypeList = mobilePhoneInfoDao.getAllMobileType();
        return phoneTypeList;
    }

    public List<Map<String, Object>> getPhoneAndroidVersion(){
        List<Map<String, Object>> phoneAndroidVersionList = new ArrayList<>();
        phoneAndroidVersionList = mobilePhoneInfoDao.getAllMobileAndroidVersion();
        return phoneAndroidVersionList;
    }

    public List<Map<String, Object>> getPhoneResolutionratio(){
        List<Map<String, Object>> phoneResolutionratioList = new ArrayList<>();
        phoneResolutionratioList = mobilePhoneInfoDao.getAllMobileResolutionratio();
        return phoneResolutionratioList;
    }

    public List<Map<String, Object>> getUseablePhoneInfo(String userid){

        List<Map<String, Object>> phoneList = new ArrayList<>();
        List<Map<String, Object>> deviceList = devicesDao.getDevicesByStatus("1");//查询所有在线设备信息
        for(Map device:deviceList){
            if("".equals(device.get("userid")) || userid.equals(device.get("userid"))){//本人占用或者没有人用才可以查到
                String deviceid = (String) device.get("deviceid");
                String model = (String) device.get("model");
                List<Map<String, Object>> mobilephone = mobilePhoneInfoDao.getMobileInfoByDeviceIdModel(deviceid,model);//查询所有设备库信息
                Map phoneitem = new HashMap();
                if(mobilephone.size()==0){
                    //数据库没有配置手机信息
                    logTools.printLog("没有配置手机信息无法使用","error");
                }else{
                    phoneitem.put("phonetype",mobilephone.get(0).get("phonetype"));
                    phoneitem.put("resolutionratio",mobilephone.get(0).get("resolutionratio"));
                    phoneitem.put("androidversion",mobilephone.get(0).get("androidversion"));
                    phoneitem.put("iconpath",mobilephone.get(0).get("iconpath"));
                    phoneitem.put("deviceid",mobilephone.get(0).get("deviceid"));
                    phoneitem.put("model",mobilephone.get(0).get("model"));
                    phoneitem.put("status","占用");
                    if(device.get("userid").equals(userid)){
                        //当前用户占用
                        phoneitem.put("status","停止使用");
                    }
                    if("".equals(device.get("userid"))){
                        //没有用户占用
                        phoneitem.put("status","开始使用");
                    }
                    phoneList.add(phoneitem);
                }

            }
        }
        return phoneList;
    }


    public List<Map<String, Object>> getPhoneInfoByPhonebrand(String userid,String phonebrand){
        List<Map<String, Object>> phoneList = new ArrayList<>();
        List<Map<String, Object>> mobilephoneList = null;
        mobilephoneList = mobilePhoneInfoDao.getMobileInfoByPhonebrand(phonebrand);//查询所有设备库信息
        List<Map<String, Object>> deviceList = devicesDao.getDevicesByStatus("1");//查询所有在线设备信息
        for (Map mobilephone:mobilephoneList) {
            Map phoneitem = new HashMap();
            phoneitem.put("phonetype",mobilephone.get("phonetype"));
            phoneitem.put("resolutionratio",mobilephone.get("resolutionratio"));
            phoneitem.put("androidversion",mobilephone.get("androidversion"));
            phoneitem.put("iconpath",mobilephone.get("iconpath"));
            phoneitem.put("deviceid",mobilephone.get("deviceid"));
            phoneitem.put("model",mobilephone.get("model"));
            phoneitem.put("status","占用");
            for(Map device:deviceList){
                if(device.get("model").equals(mobilephone.get("model")) && device.get("deviceid").equals(mobilephone.get("deviceid"))){
                    //在线设备判断
                    if(device.get("userid").equals(userid)){
                        //当前用户占用
                        phoneitem.put("status","停止使用");
                    }
                    if("".equals(device.get("userid"))){
                        //没有用户占用
                        phoneitem.put("status","开始使用");
                    }
                }
            }
            phoneList.add(phoneitem);
        }
        return phoneList;
    }


    public List<Map<String, Object>> getPhoneInfoByResolutionratio(String userid,String searchresolutionratio){
        List<Map<String, Object>> phoneList = new ArrayList<>();
        List<Map<String, Object>> mobilephoneList = null;
        mobilephoneList = mobilePhoneInfoDao.getMobileInfoByResolutionratio(searchresolutionratio);//查询所有设备库信息
        List<Map<String, Object>> deviceList = devicesDao.getDevicesByStatus("1");//查询所有在线设备信息
        for (Map mobilephone:mobilephoneList) {
            Map phoneitem = new HashMap();
            phoneitem.put("phonetype",mobilephone.get("phonetype"));
            phoneitem.put("resolutionratio",mobilephone.get("resolutionratio"));
            phoneitem.put("androidversion",mobilephone.get("androidversion"));
            phoneitem.put("iconpath",mobilephone.get("iconpath"));
            phoneitem.put("deviceid",mobilephone.get("deviceid"));
            phoneitem.put("model",mobilephone.get("model"));
            phoneitem.put("status","占用");
            for(Map device:deviceList){
                if(device.get("model").equals(mobilephone.get("model")) && device.get("deviceid").equals(mobilephone.get("deviceid"))){
                    //在线设备判断
                    if(device.get("userid").equals(userid)){
                        //当前用户占用
                        phoneitem.put("status","停止使用");
                    }
                    if("".equals(device.get("userid"))){
                        //没有用户占用
                        phoneitem.put("status","开始使用");
                    }
                }
            }
            phoneList.add(phoneitem);
        }
        return phoneList;
    }

    public List<Map<String, Object>> getPhoneInfoByAndroidVersion(String userid,String searchandroidversion){
        List<Map<String, Object>> phoneList = new ArrayList<>();
        List<Map<String, Object>> mobilephoneList = null;
        mobilephoneList = mobilePhoneInfoDao.getMobileInfoByAndroidVersion(searchandroidversion);//查询所有设备库信息
        List<Map<String, Object>> deviceList = devicesDao.getDevicesByStatus("1");//查询所有在线设备信息
        for (Map mobilephone:mobilephoneList) {
            Map phoneitem = new HashMap();
            phoneitem.put("phonetype",mobilephone.get("phonetype"));
            phoneitem.put("resolutionratio",mobilephone.get("resolutionratio"));
            phoneitem.put("androidversion",mobilephone.get("androidversion"));
            phoneitem.put("iconpath",mobilephone.get("iconpath"));
            phoneitem.put("deviceid",mobilephone.get("deviceid"));
            phoneitem.put("model",mobilephone.get("model"));
            phoneitem.put("status","占用");
            for(Map device:deviceList){
                if(device.get("model").equals(mobilephone.get("model")) && device.get("deviceid").equals(mobilephone.get("deviceid"))){
                    //在线设备判断
                    if(device.get("userid").equals(userid)){
                        //当前用户占用
                        phoneitem.put("status","停止使用");
                    }
                    if("".equals(device.get("userid"))){
                        //没有用户占用
                        phoneitem.put("status","开始使用");
                    }
                }
            }
            phoneList.add(phoneitem);
        }
        return phoneList;
    }
}
