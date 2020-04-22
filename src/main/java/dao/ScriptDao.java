package dao;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Component("ScriptDao")
public class ScriptDao extends BaseDao {

    public List<Map<String, Object>> getScriptList(String userid){
        String sql = "select * from script where userid = :userid and type=1";
        Map args = new HashMap<String, String>();
        args.put("userid", userid);
        List<Map<String, Object>> ls = smartDao.getListForList(sql, args);
        return ls;
    }

    public void insertScript(Map args){
        String sql = "insert into script values(null,:filename,:scriptname,:userid,:createtime,:type,:scriptid)";
        smartDao.insert(sql,args);
    }

    public List<Map<String, Object>> getPicScriptList(String userid){
        String sql = "select * from script where userid = :userid and type=2";
        Map args = new HashMap<String, String>();
        args.put("userid", userid);
        List<Map<String, Object>> ls = smartDao.getListForList(sql, args);
        return ls;
    }

    public List<Map<String, Object>> getPicScriptListbyPage(String userid,String start,String length){
        String sql = "select * from script where userid = :userid and type=2 limit "+start+","+length;
        Map args = new HashMap<String, String>();
        args.put("userid", userid);
//        args.put("start", start);
//        args.put("length", length);
        List<Map<String, Object>> ls = smartDao.getListForList(sql, args);
        return ls;
    }
}
