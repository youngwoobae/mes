package daedan.mes.kpi.service;

import daedan.mes.cmmn.service.CmmnService;
import daedan.mes.common.domain.Result;
import daedan.mes.common.service.util.DateUtils;
import daedan.mes.user.domain.UserInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service("kpiService")
public class KpiServiceImpl implements KpiService {
    private Log log = LogFactory.getLog(this.getClass());
    @Autowired
    private Environment env;

    @Autowired
    private CmmnService cmmnService;

    @Override
    public Map<String, Object> operPerformRead(Map<String, Object> paraMap) {
        String tag = "KpiService.operPerformRead => ";
        String kpiSessId = "";
        log.info(tag + "paraMap = " + paraMap.toString());

        HttpSession session = (HttpSession) paraMap.get("session");

        Map<String, Object> rmap = new HashMap<String, Object>();
        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        String kpiSess = (String) session.getAttribute("kpiSessId");

        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Map<String, Object> amap = new HashMap<String, Object>();
        JSONObject jsonStr = new JSONObject();
        jsonStr.put("sessionId", kpiSess);
        jsonStr.put("kpiDate", DateUtils.getCurrentDate("yyyy-MM-dd"));
        amap.put("apiURL", env.getProperty("kpi.operPerformReadURL"));
        JSONObject jsonData = cmmnService.getRestApiData(amap);

        //운영성과추출
        if (jsonData.get("status").toString().equals("200") && jsonData.get("error") == null) {
            rmap.put("kpiDate", jsonData.get("kipDate"));
            rmap.put("activeUserCnt", jsonData.get("activeUserCnt"));
            rmap.put("systemMenuCnt", jsonData.get("systemMenuCnt"));
            rmap.put("activeModuleCnt", jsonData.get("activeModuleCnt"));
            rmap.put("tableCnt", jsonData.get("tableCnt"));
        }
        return rmap;
    }


    @Override
    public List<Map<String, Object>> kpiRead(Map<String, Object> paraMap) {
        String tag = "BatchService.kpiRead => ";
        String kpiSessId = "";
        log.info(tag + "paraMap = " + paraMap.toString());
        Map<String, Object> rmap = new HashMap<String, Object>();
        HttpSession session = (HttpSession) paraMap.get("session");

        Result result = Result.successInstance();
        UserInfo uvo = (UserInfo) session.getAttribute("userInfo");
        String kpiSess = (String) session.getAttribute("kpiSessId");

        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        ArrayList<Map<String, Object>> kpiList = new ArrayList<>();
        Map<String, Object> amap = new HashMap<String, Object>();
        JSONObject jsonStr = new JSONObject();
        jsonStr.put("sessionId", kpiSess);
        jsonStr.put("kpiDate", DateUtils.getCurrentDate("yyyy-MM-dd"));
        amap.put("apiURL", env.getProperty("kpi.readURL"));
        JSONArray jsonDataList = cmmnService.getRestApiList(amap);
        log.info("aaaaaaaaaa = " + jsonDataList); //kill

        org.json.JSONObject jsonObj = null;
        for (int i = 0; i < jsonDataList.length(); i++) {
            try {
                jsonObj = jsonDataList.getJSONObject(i);
                rmap.put("kpiDate", jsonObj.get("kipiDate"));
                rmap.put("category", jsonObj.get("category"));
                rmap.put("kpiName", jsonObj.get("kiiName"));
                rmap.put("value", jsonObj.get("value"));
                rmap.put("unit", jsonObj.get("unit"));
                kpiList.add(rmap);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return kpiList;
    }
}

