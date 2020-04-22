package controller;

import com.alibaba.fastjson.JSONObject;
import dao.ParameterDao;
import dao.ScriptDao;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import tools.LogTools;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Controller
@RequestMapping("/")
public class ScriptListController {
    private LogTools logTools = new LogTools();
    @Resource
    ScriptDao scriptDao;
    @Resource
    ParameterDao parameterDao;

    @RequestMapping("/picscriptlist")
    public ModelAndView picscriptlist(HttpServletRequest req, HttpServletResponse resp){
        String userid =req.getParameter("userid");
        ModelAndView mv = new ModelAndView("picturescriptlist");
        mv.addObject("userid",userid);
        return mv;
    }

    @RequestMapping(value="/getscriptlist" ,method = RequestMethod.POST )
    @ResponseBody
    public String getscriptlist(@RequestBody String param) {

        String start = getPostParamValue(param,"start");
        String length = getPostParamValue(param,"length");
        String userid = getPostParamValue(param,"userid");
        String draw =  getPostParamValue(param,"draw");
        List<Map<String, Object>> scriptlistcount = scriptDao.getPicScriptList(userid);
        int count = scriptlistcount.size();
        List<Map<String, Object>> scriptlist = scriptDao.getPicScriptListbyPage(userid,start,length);
        Map map = new HashMap();
        map.put("data",scriptlist);
        map.put("recordsTotal",count);
        map.put("draw",draw);
        map.put("recordsFiltered",count);
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
}
