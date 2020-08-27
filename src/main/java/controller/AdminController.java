package controller;

import dao.DevicesDao;
import dao.MobilePhoneInfoDao;
import dao.RolesDao;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import tools.LogTools;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/")
public class AdminController {

    private LogTools logTools = new LogTools();

    @Resource
    private RolesDao rolesDao;
    @Resource
    private DevicesDao devicesDao;
    @Resource
    private MobilePhoneInfoDao mobilePhoneInfoDao;

    @RequestMapping("/admin")
    @ResponseBody
    public ModelAndView admin(HttpServletRequest req, HttpServletResponse resp){
        ModelAndView mv;
        String userid = req.getParameter("userid");
        if("1".equals(userid)){
            mv = new ModelAndView("admin");
            List<Map<String, Object>> rolesList = rolesDao.getRolesList();
            mv.addObject("rolesList",rolesList);
            List<Map<String, Object>> devicesList =devicesDao.getDevicesByStatus("1");
            int flag = 0;
            for (Map<String, Object> device : devicesList){
                String devicename = (String) mobilePhoneInfoDao.getMobileInfoByDeviceIdModel((String)device.get("deviceid"),(String)device.get("model")).get(0).get("phonetype");
                devicesList.get(flag).put("phonetype",devicename);
                String rolename = "";
                if(device.get("assignroleid").equals("0")){
                    rolename = "全部";
                }else{
                    rolename = (String) rolesDao.getRolesListById((String) device.get("assignroleid")).get(0).get("name");
                }
                devicesList.get(flag).put("rolename",rolename);
                flag++;
            }
            mv.addObject("devicesList",devicesList);
            mv.addObject("userid",userid);
        }else {
            mv = new ModelAndView("error");
        }
        return mv;

    }

    @RequestMapping("/editrole")
    @ResponseBody
    public ModelAndView editrole(HttpServletRequest req, HttpServletResponse resp){
        ModelAndView mv;
        String userid = req.getParameter("userid");
        if("1".equals(userid)){
            mv = new ModelAndView("admin");
            String deviceid = req.getParameter("deviceid");
            String model = req.getParameter("model");
            String roleid = req.getParameter("roleid");
            HashMap args = new HashMap();
            args.put("assignroleid",roleid);
            args.put("deviceid",deviceid);
            args.put("model",model);
            devicesDao.updateDevicesRoleByDeviceidModel(args);
            List<Map<String, Object>> devicesList =devicesDao.getDevicesByStatus("1");
            mv.addObject("devicesList",devicesList);
            List<Map<String, Object>> rolesList = rolesDao.getRolesList();
            mv.addObject("rolesList",rolesList);
            mv.addObject("userid",userid);
        }else {
            mv = new ModelAndView("error");
        }
        return mv;
    }
}
