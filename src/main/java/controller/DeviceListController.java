package controller;

import dao.UserLogDao;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import service.DeviceListService;
import service.DeviceListenerService;
import tools.LogTools;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/")
public class DeviceListController {

    private  LogTools logTools = new LogTools();
    @Resource
    private DeviceListenerService deviceListenerService;
    @Resource
    private DeviceListService deviceListService;
    @Resource
    private UserLogDao userLogDao;
    @RequestMapping("/")
    @ResponseBody
    public String index() {
        return "index";
    }


    @RequestMapping("/devicelist")
    @ResponseBody
    public ModelAndView deviceList(HttpServletRequest req, HttpServletResponse resp){
        while(deviceListenerService==null){
            logTools.printLog("等待初始化中","info");
        }
        if(deviceListenerService!=null){
            deviceListenerService.listenUSB();
            deviceListenerService.listenADB();
            while (!deviceListenerService.getListenerres()){
                logTools.printLog("等待设备初始化","info");
                //等待设备初始化
            }
        }
        String userid = req.getParameter("userid");
        String useablesearchstatus = req.getParameter("useablesearchstatus");
        String searchphonetype = req.getParameter("searchphonetype");
        String searchresolutionratio = req.getParameter("searchresolutionratio");
        String searchandroidversion = req.getParameter("searchandroidversion");
        String deviceid = req.getParameter("deviceid");
        String model = req.getParameter("model");
        String action = req.getParameter("action");
        if(deviceid!=null && model!=null&&action!=null&&"stop".equals(action)){
            deviceListService.stopDevice(userid,deviceid,model);
            //更新用户使用日志停止时间
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            String date = df.format(new Date());
            HashMap args = new HashMap();
            args.put("userid",userid);
            args.put("deviceid",deviceid);
            args.put("model",model);
            args.put("endtime",date);
            userLogDao.updateEndtime(args);
        }
        List<Map<String, Object>> phoneList;
        if("可使用".equals(useablesearchstatus)){
            phoneList = deviceListService.getUseablePhoneInfo(userid);
        }else if(searchphonetype!=null) {
            phoneList = deviceListService.getPhoneInfoByPhonebrand(userid,searchphonetype);
        }else if(searchresolutionratio!=null){
            phoneList = deviceListService.getPhoneInfoByResolutionratio(userid,searchresolutionratio);
        }else if(searchandroidversion!=null){
            phoneList = deviceListService.getPhoneInfoByAndroidVersion(userid,searchandroidversion);
        }else{
            phoneList = deviceListService.getPhoneInfo(userid);
        }
        List<Map<String, Object>> phoneTypeList = deviceListService.getPhoneType();
        List<Map<String, Object>> phoneAndroidVersionList = deviceListService.getPhoneAndroidVersion();
        List<Map<String, Object>> phoneResolutionratioList = deviceListService.getPhoneResolutionratio();
        ModelAndView mv = new ModelAndView("devicelist");
        mv.addObject("userid",userid);
        mv.addObject("phoneList",phoneList);
        mv.addObject("phoneTypeList",phoneTypeList);
        mv.addObject("phoneAndroidVersionList",phoneAndroidVersionList);
        mv.addObject("phoneResolutionratioList",phoneResolutionratioList);

        logTools.printLog("准备返回前台显示页面","info");
        return mv;
    }

}
