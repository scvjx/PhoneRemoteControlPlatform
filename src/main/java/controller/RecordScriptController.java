package controller;

import adb.AdbDevice;
import com.alibaba.fastjson.JSONObject;
import common.Constant;
import dao.ParameterDao;
import dao.ScriptDao;
import minicap.Minicap;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;
import service.DeviceListenerService;
import service.DeviceListService;
import tools.FileTool;
import tools.LogTools;
import adb.PlayBackTool;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;


@Controller
@RequestMapping("/")
public class RecordScriptController {

    private LogTools logTools = new LogTools();
    @Resource
    Minicap minicap;
    @Resource
    private DeviceListService deviceListService;
    @Resource
    DeviceListenerService deviceListenerService;
    @Resource
    ParameterDao parameterDao;
    @Resource
    ScriptDao scriptDao;

    @RequestMapping("/recordscript")
    public ModelAndView recordscript(HttpServletRequest req, HttpServletResponse resp){
        deviceListenerService.listenUSB();
        deviceListenerService.listenADB();
        while (!deviceListenerService.getListenerres()){
            logTools.printLog("等待设备初始化","info");
            //等待设备初始化
        }
        ModelAndView mv = new ModelAndView("recordscript");
        List<AdbDevice> adbDeviceList = deviceListenerService.getAdbDeviceList();
        String deviceid = req.getParameter("deviceid");
        String model = req.getParameter("model");
        String userid =req.getParameter("userid");
        String quality = "";
        List<Map<String, Object>> paramlist = parameterDao.getParamByName(Constant.PICTURESCRIPTMINICAPRESOLUTION);
        quality = (String) paramlist.get(0).get("value");
        for(AdbDevice adbDevice :adbDeviceList){
            if(deviceid!=null && deviceid.equals(adbDevice.getSerialNumber())&& model!=null && model.equals(adbDevice.getProperty(Constant.PROP_MODEL))){
                String screensize = adbDevice.getProperty("SCREEN_SIZE");
                mv.addObject("screensize",screensize);
                mv.addObject("sn",adbDevice.getSerialNumber());
                mv.addObject("userid",userid);
                mv.addObject("deviceid",deviceid);
                mv.addObject("model",model);
                minicap.installMinicap(adbDevice);
                minicap.startMinicapThread(adbDevice,Constant.MINICAPVIRTUALSIZE,quality);
            }
        }
        return mv;
    }

    @RequestMapping("/picturerecordscript")
    public ModelAndView picturerecordscript(HttpServletRequest req, HttpServletResponse resp){
        deviceListenerService.listenUSB();
        deviceListenerService.listenADB();
        while (!deviceListenerService.getListenerres()){
            logTools.printLog("等待设备初始化","info");
            //等待设备初始化
        }
        ModelAndView mv = new ModelAndView("picturerecordscript");
        List<AdbDevice> adbDeviceList = deviceListenerService.getAdbDeviceList();
        String deviceid = req.getParameter("deviceid");
        String model = req.getParameter("model");
        String userid =req.getParameter("userid");
        String quality = "";
        List<Map<String, Object>> paramlist = parameterDao.getParamByName(Constant.PICTURESCRIPTMINICAPRESOLUTION);
        quality = (String) paramlist.get(0).get("value");
        List<Map<String, Object>> paramlist1 = parameterDao.getParamByName(Constant.PICTURESAVEPATH);
        String picpath = (String) paramlist1.get(0).get("value");
        for(AdbDevice adbDevice :adbDeviceList){
            if(deviceid!=null && deviceid.equals(adbDevice.getSerialNumber())&& model!=null && model.equals(adbDevice.getProperty(Constant.PROP_MODEL))){
                String screensize = adbDevice.getProperty("SCREEN_SIZE");
                String [] screensizearray = screensize.split("x");
                mv.addObject("screenw",screensizearray[0]);
                mv.addObject("screenh",screensizearray[1]);
                mv.addObject("screensize",screensize);
                mv.addObject("sn",adbDevice.getSerialNumber());
                mv.addObject("userid",userid);
                mv.addObject("deviceid",deviceid);
                mv.addObject("model",model);
                mv.addObject("picpath",picpath);
                minicap.installMinicap(adbDevice);
                minicap.startMinicapThread(adbDevice,Constant.MINICAPVIRTUALSIZE,quality);
            }
        }
        return mv;
    }


    @RequestMapping(value="/playback", method= RequestMethod.GET)
    @ResponseBody
    public String playBackScript(HttpServletRequest req, HttpServletResponse resp) {
        String testscript = req.getParameter("testscript");
        String sn = req.getParameter("sn");
        List<AdbDevice> adbDeviceList = deviceListenerService.getAdbDeviceList();
        for(AdbDevice adbDevice :adbDeviceList){
            if(adbDevice.getSerialNumber().equals(sn)){
                PlayBackTool playBackTool = new PlayBackTool(adbDevice);
                playBackTool.playBackScript(testscript);
            }
        }
        Map map = new HashMap();
        map.put("code", "200");
        String json = JSONObject.toJSONString(map);
        return String.valueOf(json);
    }


    @RequestMapping(value="/savescript", method= RequestMethod.GET)
    @ResponseBody
    public String saveScript(HttpServletRequest req, HttpServletResponse resp) {
        String testscript = req.getParameter("testscript");
        testscript = xssEncode(testscript);
        String scriptname = req.getParameter("scriptname");
        String userid = req.getParameter("userid");

        String deviceid = req.getParameter("deviceid");
        if(deviceid!=null) {
            deviceListService.stopDevice(userid, deviceid,"");
        }

        FileTool fileTool = new FileTool();
        List<Map<String, Object>> paramlist = parameterDao.getParamByName(Constant.SAVESCRIPTFILEPATH);
        String saveFilePath = (String) paramlist.get(0).get("value");
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
        String date = df.format(new Date());
        String filename = date+".txt";
        fileTool.saveFile(filename,testscript,saveFilePath);
        HashMap args1 = new HashMap();
        args1.put("userid",userid);
        args1.put("filename",filename);
        args1.put("scriptname",scriptname);
        args1.put("createtime",date);
        args1.put("type","1");
        args1.put("scriptid","");
        scriptDao.insertScript(args1);
        Map map = new HashMap();
        map.put("code", "200");
        String json = JSONObject.toJSONString(map);
        return String.valueOf(json);
    }

    @RequestMapping(value="/savepicturescript", method= RequestMethod.POST)
    @ResponseBody
    public String savepicturescript(@RequestBody String param) {
        String scriptstring = getPostParamValue(param,"scriptstring");
        String userid = getPostParamValue(param,"userid");
        String scriptname = getPostParamValue(param,"scriptname");
        String scriptid = getPostParamValue(param,"scriptid");
        
        String deviceid = getPostParamValue(param,"deviceid");
        if(deviceid!=null) {
            deviceListService.stopDevice(userid, deviceid,"");
        }

        try {
            scriptstring = URLDecoder.decode(scriptstring, "UTF-8");
            scriptname = URLDecoder.decode(scriptname, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        JSONObject jsonobj=JSONObject.parseObject(scriptstring);
        Iterator iter = jsonobj.entrySet().iterator();
        String scriptstr = "";
        while (iter.hasNext()){
            Map.Entry entry = (Map.Entry) iter.next();
            scriptstr += "   "+xssEncode((String) entry.getValue())+"\r\n";
        }
        FileTool fileTool = new FileTool();
        List<Map<String, Object>> paramlist = parameterDao.getParamByName(Constant.PICTURESCRIPTPATH);
        String saveFilePath = (String) paramlist.get(0).get("value")+File.separator+scriptid;
        List<Map<String, Object>> paramlist1 = parameterDao.getParamByName(Constant.PICTURESCRIPTCOMMONSTR);
        String commonstr = (String) paramlist1.get(0).get("value");
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
        String date = df.format(new Date());
        String filename = date+".txt";
        String filecontent =commonstr+"\r\n"+scriptstr;
        fileTool.saveFile(filename,filecontent,saveFilePath);
        HashMap args1 = new HashMap();
        args1.put("userid",userid);
        args1.put("filename",filename);
        args1.put("scriptname",scriptname);
        args1.put("createtime",date);
        args1.put("type","2");
        args1.put("scriptid",scriptid);
        scriptDao.insertScript(args1);
        Map map = new HashMap();
        map.put("code", "200");
        String json = JSONObject.toJSONString(map);
        return String.valueOf(json);
    }

    @RequestMapping(value="/savepicture", method= RequestMethod.POST)
    @ResponseBody
    public String savepicture( @RequestParam("file") CommonsMultipartFile file,HttpServletRequest req, HttpServletResponse resp) {

        String picname =  req.getParameter("picname");
        String scriptid = req.getParameter("scriptid");
        String pathname = picname+".jpg";
        List<Map<String, Object>> paramlist = parameterDao.getParamByName(Constant.PICTURESAVEPATH);
        String saveFilePath = (String) paramlist.get(0).get("value");
        saveFilePath = saveFilePath+File.separator+scriptid;
        File dir = new File(saveFilePath);
        if(!dir.exists()) {
            dir.mkdirs();
        }
        try {
            file.transferTo(new File(saveFilePath,pathname));
        } catch (IOException e) {
            e.printStackTrace();
            Map map = new HashMap();
            map.put("code", "0");
            String json = JSONObject.toJSONString(map);
            return String.valueOf(json);
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

    private  String xssEncode(String s) {
        //XSS静态过滤方法
        if (s == null || s.isEmpty()) {
            return s;
        }
        StringBuilder sb = new StringBuilder(s.length() + 16);
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '>':
                    sb.append('＞');// 全角大于号
                    break;
                case '<':
                    sb.append('＜');// 全角小于号
                    break;
                case '\'':
                    sb.append('‘');// 全角单引号
                    break;
                case '\"':
                    sb.append('“');// 全角双引号
                    break;
                case '&':
                    sb.append('＆');// 全角
                    break;
                case '\\':
                    sb.append('＼');// 全角斜线
                    break;
                case '#':
                    sb.append('＃');// 全角井号
                    break;
                case '(':
                    sb.append('（');//
                    break;
                case ')':
                    sb.append('）');//
                    break;
                default:
                    sb.append(c);
                    break;
            }
        }
        return sb.toString();


    }

}
