package controller;

import adb.AdbDevice;
import com.alibaba.fastjson.JSONObject;
import common.Constant;
import dao.*;
import minicap.Minicap;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;
import service.DeviceListService;
import service.DeviceListenerService;
import tools.CmdTool;
import tools.GetLogReceiver;
import tools.LogTools;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by jiaxin on 2019/5/16.
 */
@Controller
@RequestMapping("/")
public class DeviceController {

    @Resource
    Minicap minicap;
    @Resource
    DeviceListenerService deviceListenerService;
    @Resource
    private DeviceListService deviceListService;
    @Resource
    private UserLogDao userLogDao;
    @Resource
    private UserSettingDao userSetting;
    @Resource
    DevicesDao devicesDao;
    @Resource
    ParameterDao parameterDao;
    @Resource
    User2roleDao user2roleDao;

    private  LogTools logTools = new LogTools();
    private boolean isKilled = false;
    private int logcatCurrentRow = 0;

    @RequestMapping("/devicecontrol")
    public ModelAndView devicecontrol(HttpServletRequest req, HttpServletResponse resp){
        String userid =req.getParameter("userid");
        List<Map<String, Object>> roleList  = user2roleDao.getRoleList(userid);
        String roleid = (String) roleList.get(0).get("roleid");
        String deviceid = req.getParameter("deviceid");
        String model = req.getParameter("model");
        List<Map<String, Object>> deviceList = devicesDao.getDevicesByDeviceidModel(deviceid,model);
        String assignroleid = (String) deviceList.get(0).get("assignroleid");
        if((assignroleid!=null&&roleid!=null&&roleid.equals(assignroleid))||"0".equals(assignroleid)){
            ModelAndView mv = new ModelAndView("devicecontrol");
            List<AdbDevice> adbDeviceList = deviceListenerService.getAdbDeviceList();


            String phonetype = req.getParameter("phonetype");
            List<Map<String, Object>> userSettingList = userSetting.getMinicapResolution(deviceid,model,userid);
            String quality = "";
            if(userSettingList.size()==0){
                //用户没有设置过自己的清晰度用默认
                List<Map<String, Object>> paramlist = parameterDao.getParamByName(Constant.MINICAPINITRESOLUTION);
                quality = (String) paramlist.get(0).get("value");
            }else{
                quality = (String) userSettingList.get(0).get("minicapresolution");
            }
            //更新数据库使用状态
            HashMap args = new HashMap();
            args.put("userid",userid);
            args.put("deviceid",deviceid);
            args.put("model",model);
            args.put("status","1");
            devicesDao.updateDevicesUseridStatusByDeviceidModel(args);

            String adbkitPort = (String) deviceList.get(0).get("adbkitport");
            List<Map<String, Object>> paramlist1 = parameterDao.getParamByName(Constant.ADBKITPATH);
            String adbkitpath = (String) paramlist1.get(0).get("value");
            List<Map<String, Object>> paramlist2 = parameterDao.getParamByName(Constant.LOGCATROWS);
            String logcatRows = (String) paramlist2.get(0).get("value");
            List<Map<String, Object>> paramlist3 = parameterDao.getParamByName(Constant.SERVERIP);
            String serverIp = (String) paramlist3.get(0).get("value");
            for(AdbDevice adbDevice :adbDeviceList){
                if(deviceid!=null && deviceid.equals(adbDevice.getSerialNumber())&& model!=null && model.equals(adbDevice.getProperty(Constant.PROP_MODEL))){
                    CmdTool cmdTool = new CmdTool();
                    String startAdbKit = adbkitpath+" usb-device-to-tcp -p "+adbkitPort+" "+adbDevice.getSerialNumber();
                    logTools.printLog(startAdbKit,"debug");
                    cmdTool.execCommand(startAdbKit);
                    String screensize = adbDevice.getProperty("SCREEN_SIZE");
                    mv.addObject("screensize",screensize);
                    mv.addObject("manufacture",adbDevice.getProperty("MANUFACTURE"));
                    mv.addObject("sn",adbDevice.getSerialNumber());
                    mv.addObject("phonetype",phonetype);
                    mv.addObject("userid",userid);
                    mv.addObject("deviceid",deviceid);
                    mv.addObject("model",model);
                    mv.addObject("resolution",quality);
                    mv.addObject("adbkitport",adbkitPort);
                    mv.addObject("logcatRows",logcatRows);
                    mv.addObject("serverip",serverIp);
                    minicap.installMinicap(adbDevice);
                    minicap.startMinicapThread(adbDevice,Constant.MINICAPVIRTUALSIZE,quality);
                }
            }
            //记录用户使用日志
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            String date = df.format(new Date());
            List<Map<String, Object>> paramlist = parameterDao.getParamByName(Constant.LOGCATROWS);
            int count = Integer.parseInt((String) paramlist.get(0).get("value"));
            HashMap args1 = new HashMap();
            args1.put("userid",userid);
            args1.put("deviceid",deviceid);
            args1.put("model",model);
            args1.put("starttime",date);
            args1.put("endtime","");
            args1.put("logcatcols",count);
            userLogDao.insertUserLog(args1);
            return mv;
        }else {
            ModelAndView mv = new ModelAndView("error");
            return mv;
        }
    }

    @RequestMapping(value="/devicesetting", method= RequestMethod.POST)
    @ResponseBody
    public String devicesetting( @RequestBody String param){
        String sn = getPostParamValue(param,"sn");
        String userid = getPostParamValue(param,"userid");
        String model = getPostParamValue(param,"model");
        String quality = getPostParamValue(param,"quality");
        //查看之前用户是否已经设置过
        List<Map<String, Object>> userSettingList = userSetting.getMinicapResolution(sn,model,userid);
        if(userSettingList.size()==0){
            //之前没有设置过新插入
            HashMap args1 = new HashMap();
            args1.put("userid",userid);
            args1.put("deviceid",sn);
            args1.put("model",model);
            args1.put("minicapresolution",quality);
            userSetting.insertUserSetting(args1);
        }else{
            //之前设置过进行更新
            HashMap args1 = new HashMap();
            args1.put("userid",userid);
            args1.put("deviceid",sn);
            args1.put("model",model);
            args1.put("minicapresolution",quality);
            userSetting.updateMinicapResolution(args1);
        }
        List<AdbDevice> adbDeviceList = deviceListenerService.getAdbDeviceList();
        for(AdbDevice adbDevice :adbDeviceList){
            if(adbDevice.getSerialNumber().equals(sn)){
                List<Map<String, Object>> mobilephoneList = devicesDao.getDevicesByDeviceidModel(sn, model);
                Map map = new HashMap();
                map = mobilephoneList.get(0);
                minicap.onClose(adbDevice, Integer.parseInt((String) map.get("minicapport")));
                minicap.startMinicapThread(adbDevice,new Constant().MINICAPVIRTUALSIZE,quality);
            }
        }
        Map map = new HashMap();
        map.put("code", "200");
        map.put("msg", "修改成功");
        map.put("resolution",quality);
        String json = JSONObject.toJSONString(map);
        return String.valueOf(json);
    }


    @RequestMapping(value="/installapp", method= RequestMethod.POST)
    @ResponseBody
    public String installapp(@RequestParam("appfile") CommonsMultipartFile file, HttpServletRequest req, HttpServletResponse resp){
        String deviceid =  req.getParameter("sn");
        String localFileName = "";
        List<Map<String, Object>> paramlist = parameterDao.getParamByName(Constant.UPLOADAPPFILEPATH);
        localFileName = new Date().getTime() + deviceid + ".apk";
        String localFilePath = (String) paramlist.get(0).get("value");
        try {
            file.transferTo(new File(localFilePath,localFileName));
        } catch (IOException e) {
            e.printStackTrace();
            logTools.printLog("异常：APP上传失败", "debug");
            Map map = new HashMap();
            map.put("code", "-1");
            map.put("msg", "APP上传失败");
            String json = JSONObject.toJSONString(map);
            return String.valueOf(json);
        }
        List<AdbDevice> adbDeviceList = deviceListenerService.getAdbDeviceList();
            for (AdbDevice adbDevice : adbDeviceList) {
                if (adbDevice.getSerialNumber().equals(deviceid)) {
                    boolean res = adbDevice.adbinstall(paramlist.get(0).get("value") + localFileName);
                    if (!res) {
                        logTools.printLog("异常：安装APP失败", "debug");
                        Map map = new HashMap();
                        map.put("code", "-1");
                        map.put("msg", "安装APP失败");
                        String json = JSONObject.toJSONString(map);
                        return String.valueOf(json);
                    }
                }
            }

        Map map = new HashMap();
        map.put("code", "200");
        map.put("msg", "成功");
        String json = JSONObject.toJSONString(map);
        return String.valueOf(json);
    }


    @RequestMapping(value="/getlogcat", method= RequestMethod.GET)
    @ResponseBody
    public String getlogcat(HttpServletRequest req, HttpServletResponse resp) {
        String deviceid = req.getParameter("sn");
        String logcatkey = req.getParameter("logcatkey");
        List<AdbDevice> adbDeviceList = deviceListenerService.getAdbDeviceList();
        Map map = new HashMap();
        map.put("code", "-1");
        GetLogReceiver receiver = new GetLogReceiver();
        for (AdbDevice adbDevice : adbDeviceList) {
            if (adbDevice.getSerialNumber().equals(deviceid)) {
                int start = logcatCurrentRow;
                List<Map<String, Object>> paramlist = parameterDao.getParamByName(Constant.LOGCATROWS);
                int count = Integer.parseInt((String) paramlist.get(0).get("value"));
                logcatCurrentRow += count;
                adbDevice.adblogcat();
                receiver = adbDevice.getGetLogReceiver();
                List<String> logcat = receiver.getNextBatchLogs(start, count);
                logTools.printLog("start="+start,"debug");
                map.put("code", "200");
                map.put("msg", listToString(logcat));
                String json = JSONObject.toJSONString(map);
                return String.valueOf(json);
            }
        }
        String json = JSONObject.toJSONString(map);
        return String.valueOf(json);
    }

    @RequestMapping(value="/inputevent", method= RequestMethod.GET)
    @ResponseBody
    public String inputEvent(HttpServletRequest req, HttpServletResponse resp) {
        String deviceid = req.getParameter("sn");
        String eventNumber = req.getParameter("eventnumber");
        List<AdbDevice> adbDeviceList = deviceListenerService.getAdbDeviceList();
        for (AdbDevice adbDevice : adbDeviceList) {
            if (adbDevice.getSerialNumber().equals(deviceid)) {
                adbDevice.adbTapkeyevent(eventNumber);
            }
        }
        Map map = new HashMap();
        map.put("code", "200");
        String json = JSONObject.toJSONString(map);
        return String.valueOf(json);
    }




    private String getPostParamValue(String paramStr,String key){
        //自己写解析前台传过来 name=value&name1=value1&..形式的参数
        String value = "";
        String[] paramlist = paramStr.split("&");
        for(String param:paramlist){
            int nameflag = param.indexOf("=");
            String name = param.substring(0,nameflag);
            if(name.equals(key)){
                value = param.substring(nameflag+1,param.length());
            }
        }
        return value;
    }

    private String listToString(List<String> list){

        String res = "";
        for(String item : list){
            res +=item+"\n";
        }
        return res;

    }
}
