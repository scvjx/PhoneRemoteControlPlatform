package controller;


import dao.MobilePhoneInfoDao;
import dao.UserLogDao;
import dao.UsersDao;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import tools.LogTools;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/")
public class UseLogController {

    private LogTools logTools = new LogTools();
    @Resource
    UserLogDao userLogDao;
    @Resource
    MobilePhoneInfoDao mobilePhoneInfoDao;
    @Resource
    UsersDao usersDao;
    @RequestMapping("/uselog")
    @ResponseBody
    public ModelAndView uselog(HttpServletRequest req, HttpServletResponse resp) {
        ModelAndView mv;
        String userid = req.getParameter("userid");
        mv = new ModelAndView("uselog");
        List<Map<String, Object>> userloglist;
        if("1".equals(userid)){
            userloglist =userLogDao.getUserlog();

        }else{
            userloglist =userLogDao.getUserlogByUserid(userid);
        }
        int flag=0;
        double usetimesum = 0;
        for(Map<String, Object> userlog:userloglist){
            String deviceid = (String) userlog.get("deviceid");
            String model = (String) userlog.get("model");
            String useid = (String) userlog.get("userid");
            String phonetype = (String) mobilePhoneInfoDao.getMobileInfoByDeviceIdModel(deviceid,model).get(0).get("phonetype");
            userloglist.get(flag).put("phonetype",phonetype);
            String username = (String) usersDao.getUsersListById(useid).get(0).get("username");
            userloglist.get(flag).put("username",username);
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                Date d2 = df.parse((String) userlog.get("endtime"));
                Date d1 = df.parse((String) userlog.get("starttime"));
                double diff = d2.getTime() - d1.getTime();
                double minutes = diff / (1000 * 60);
                minutes = (double)(Math.round(minutes*100)/100.0);
                usetimesum += minutes;
                userloglist.get(flag).put("usetime",Double.toString(minutes));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            flag++;
        }
        usetimesum = (double)(Math.round(usetimesum*100)/100.0);
        mv.addObject("usetimesum",usetimesum);
        mv.addObject("userloglist",userloglist);

        return mv;
    }
}
