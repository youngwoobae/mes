package daedan.mes.moniter.service;

import daedan.mes.moniter.mapper.DdkorMapper;
import net.sf.json.JSONArray;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Timestamp;
import java.util.*;

@Service("ddkorService")
public class DdKorServiceImpl implements  DdkorService {
    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private Environment env;

    @Autowired
    private DdkorMapper mapper;

    @Override
    public List<Map<String, Object>> getMoniterHstr(Map<String, Object> paraMap) {
        String tag = "DdkorService.getMoniterHstr => ";
        log.info(tag + "paraMap = " + paraMap.toString() );
        return mapper.getMoniterHstr(paraMap);
    }

    @Override
    public int getMoniterHstrCount(Map<String, Object> paraMap) {
        return mapper.getMoniterHstrCount(paraMap);
    }

    @Override
    public List<Map<String, Object>> getMoniterHstrH24(Map<String, Object> paraMap) {
        String tag = "DdkorService.moniterHstrH24 => ";
        log.info(tag + "paraMap = " + paraMap.toString() );
        return mapper.getMoniterHstrH24(paraMap);
    }

    @Override
    public List<Map<String, Object>> getOpeerStat(Map<String, Object> paraMap) {
        String tag = "DdKorService.getOperStat => ";
        JSONParser parser = new JSONParser();
        Map<String, Object> equipMap = new HashMap<>();
        URL url = null;
        String uri = "http://ddk.daedan.com/getData.php";
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            url = new URL(uri);
            URLConnection conn = url.openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            org.json.simple.JSONArray jsonArr =  (org.json.simple.JSONArray) parser.parse(br.readLine());

            for (Object el : jsonArr) {
                Map<String, Object> rmap = new HashMap<String, Object>();
                Object obj = parser.parse(el.toString());
                JSONObject jsonObj = (JSONObject) obj;

                rmap.put("eqno", jsonObj.get("eqno"));
                rmap.put("oper", jsonObj.get("oper"));
                rmap.put("temp", jsonObj.get("temp"));
                rmap.put("humy", jsonObj.get("hum"));
                rmap.put("co2", jsonObj.get("co2"));
                rmap.put("hz", jsonObj.get("hz"));
                rmap.put("alarm", jsonObj.get("alarm"));
                rmap.put("txt", jsonObj.get("txt"));
                rmap.put("val", jsonObj.get("val"));
                rmap.put("prss", jsonObj.get("prss"));
                rmap.put("kw", jsonObj.get("kw"));
                rmap.put("add_ea", jsonObj.get("add_ea"));
                rmap.put("err_ea", jsonObj.get("err_ea"));
                rmap.put("ok_ea", jsonObj.get("ok_ea"));
                rmap.put("ea", jsonObj.get("ea"));

                /*spotInfo에 있는 spot_nm 가져오기*/
               equipMap = mapper.getEquipName(rmap);

               try{
                   rmap.put("equipNm", equipMap.get("spot_nm"));
               }catch(NullPointerException ne){
                   rmap.put("equipNm", null);
               }

                list.add(rmap);

            }
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("JSON list : "+list);
        return list;
    }
}
