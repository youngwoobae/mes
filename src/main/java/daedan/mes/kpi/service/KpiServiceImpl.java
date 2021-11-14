package daedan.mes.kpi.service;

import daedan.mes.cmmn.service.CmmnService;
import daedan.mes.common.service.util.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("kpiService")
public class KpiServiceImpl implements KpiService {
    private Log log = LogFactory.getLog(this.getClass());
    @Autowired
    private Environment env;

    @Autowired
    private CmmnService cmmnService;

    @Override
    public List<Map<String, Object>> operPerformRead(Map<String, Object> paraMap) {
        String tag = "KpiService.operPerformRead => ";
        log.info(tag + "paraMap = " + paraMap.toString());
        String kpiSess = paraMap.get("kpiSessId").toString();

        Map<String, Object> rmap = new HashMap<String, Object>();
        Map<String, Object> amap = new HashMap<String, Object>();
        JSONObject jsonStr = new JSONObject();
        jsonStr.put("sessionId", kpiSess);

        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //String kpiDate = sdf.format(DateUtils.getCurrentDate());
        //jsonStr.put("kpiDate", sdf.format( (Date) paraMap.get("kpiDate")));
        String kpiYmd = paraMap.get("kpiDate").toString().substring(0,10);
        log.info(tag + "KpiDate String = " + kpiYmd); //kill
        jsonStr.put("kpiDate",  kpiYmd);

        amap.put("apiURL", env.getProperty("kpi.operPerformReadURL"));
        amap.put("jsonStr",jsonStr);

        JSONObject jsonData = cmmnService.getRestApiList(amap);
        JSONArray jsonDataList = (JSONArray) jsonData.get("resultList");
        log.info(tag + "KPI LEVEL 1 운영데이터.LENGTH = " + jsonDataList.size()); //kill

        ArrayList<Map<String, Object>> kpiList = new ArrayList<>();
        //운영성과추출
        //if (jsonData.get("status").toString().equals("200") && jsonData.get("error") == null) {
        JSONObject jsonObj = null;
        for (int i = 0; i < jsonDataList.size(); i++) {
            jsonObj = (JSONObject) jsonDataList.get(i);
            rmap.put("kpiDate", jsonObj.get("kpiDate"));
            rmap.put("activeUserCnt", jsonObj.get("activeUserCnt"));
            rmap.put("systemMenuCnt", jsonObj.get("systemMenuCnt"));
            rmap.put("activeModuleCnt", jsonObj.get("activeModuleCnt"));
            rmap.put("tableCnt", jsonObj.get("tableCnt"));
            rmap.put("recordCnt", jsonObj.get("recordCnt"));
            log.info(tag + "kpiList.map(  " + i + ") = " + rmap.toString());
            kpiList.add(rmap);
        }
        log.info(tag + "KPI LiST = " + kpiList.size()); //kill
        return kpiList;
    }


    @Override
    public List<Map<String, Object>> kpiRead(Map<String, Object> paraMap) {
        String tag = "BatchService.kpiRead => ";
        String kpiSessId = "";
        log.info(tag + "paraMap = " + paraMap.toString());
        Map<String, Object> rmap = new HashMap<String, Object>();
        //UserInfo uvo = (UserInfo) session.getAttribute("userInfo");

        String kpiSess = paraMap.get("kpiSessId").toString();
        ArrayList<Map<String, Object>> kpiList = new ArrayList<>();
        Map<String, Object> amap = new HashMap<String, Object>();
        JSONObject jsonStr = new JSONObject();
        jsonStr.put("sessionId", kpiSess);

        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //String kpiDate = sdf.format(DateUtils.getCurrentDate());
        //jsonStr.put("kpiDate", sdf.format( (Date) paraMap.get("kpiDate")));

        String kpiYmd = paraMap.get("kpiDate").toString().substring(0,10);
        log.info(tag + "KpiDate String = " + kpiYmd); //kill
        jsonStr.put("kpiDate",  kpiYmd);

        amap.put("apiURL", env.getProperty("kpi.readURL"));
        amap.put("jsonStr",jsonStr);

        JSONObject jsonData = cmmnService.getRestApiList(amap);
        JSONArray jsonDataList = (JSONArray) jsonData.get("resultList");
        log.info(tag + "KPI LiST.LENGTH = " + jsonDataList.size()); //kill

        JSONObject jsonObj = null;
        for (int i = 0; i < jsonDataList.size(); i++) {
            jsonObj = (JSONObject) jsonDataList.get(i);
            rmap.put("kpiDate", jsonObj.get("kpiDate"));
            rmap.put("category", jsonObj.get("category"));
            rmap.put("kpiName", jsonObj.get("kpiName"));
            rmap.put("value", jsonObj.get("value"));
            rmap.put("unit", jsonObj.get("unit"));
            log.info(tag + "kpiList.map(  " + i + ") = " + rmap.toString());
            kpiList.add(rmap);
        }
        log.info(tag + "KPI LiST = " + kpiList.size()); //kill
        return kpiList;
    }
}

