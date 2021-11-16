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


        Map<String, Object> amap = new HashMap<String, Object>();
        JSONObject jsonStr = new JSONObject();
        jsonStr.put("sessionId", kpiSess);

        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String kpiDateFr = paraMap.get("kpiDateFr").toString().substring(0,10);
        String kpiDateTo = paraMap.get("kpiDateTo").toString().substring(0,10);
        ArrayList<Map<String, Object>> kpiList = new ArrayList<>();
        try {
            Date kpiYmdFr = sdf.parse(kpiDateFr);
            Date kpiYmdTo = sdf.parse(kpiDateTo);
            List<Date> dateList = DateUtils. getBetweenDates(kpiYmdFr,kpiYmdTo);
            for (Date el : dateList) {
                String kpiYmd = sdf.format(el).substring(0,10);
                log.info(tag + "KpiDate String = " + kpiYmd); //kill
                jsonStr.put("kpiDate",  kpiYmd);

                amap.put("apiURL", env.getProperty("kpi.operPerformReadURL"));
                amap.put("jsonStr",jsonStr);

                JSONObject jsonData = cmmnService.getRestApiList(amap);
                JSONArray jsonDataList = (JSONArray) jsonData.get("resultList");
                log.info(tag + "KPI LEVEL 1 운영데이터.LENGTH = " + jsonDataList.size()); //kill

                //운영성과추출
                //if (jsonData.get("status").toString().equals("200") && jsonData.get("error") == null) {
                JSONObject jsonObj = null;
                for (int i = 0; i < jsonDataList.size(); i++) {
                    jsonObj = (JSONObject) jsonDataList.get(i);
                    Map<String, Object> rmap = new HashMap<String, Object>();
                    rmap.put("kpiDate", jsonObj.get("kpiDate"));
                    rmap.put("activeUserCnt", jsonObj.get("activeUserCnt"));
                    rmap.put("systemMenuCnt", jsonObj.get("systemMenuCnt"));
                    rmap.put("activeModuleCnt", jsonObj.get("activeModuleCnt"));
                    rmap.put("tableCnt", jsonObj.get("tableCnt"));
                    rmap.put("recordCnt", jsonObj.get("recordCnt"));
                    log.info(tag + "kpiList.map(  " + i + ") = " + rmap.toString());
                    kpiList.add(rmap);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        log.info(tag + "KPI LiST = " + kpiList.size()); //kill
        return kpiList;
    }


    @Override
    public Map<String, Object> kpiRead(Map<String, Object> paraMap) {
        String tag = "BatchService.kpiRead => ";
        log.info(tag + "paraMap = " + paraMap.toString());

        String kpiSess = paraMap.get("kpiSessId").toString();

        Map<String, Object> amap = new HashMap<String, Object>();
        JSONObject jsonStr = new JSONObject();
        jsonStr.put("sessionId", kpiSess);

        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String kpiDateFr = paraMap.get("kpiDateFr").toString().substring(0,10);
        String kpiDateTo = paraMap.get("kpiDateTo").toString().substring(0,10);

        ArrayList<Map<String, Object>> kpiList = new ArrayList<Map<String, Object>>();
        ArrayList<Float> kpiGraphList = new ArrayList<Float>(); //그래프데이터
        ArrayList<String> kpiLabelList = new ArrayList<String>(); //그래플라벨
        Map<String,Object> rmap = new HashMap<String,Object>();
        try {
            Date kpiYmdFr = sdf.parse(kpiDateFr);
            Date kpiYmdTo = sdf.parse(kpiDateTo);

            List<Date> dateList = DateUtils. getBetweenDates(kpiYmdFr,kpiYmdTo);
            for (Date el : dateList) {
                String kpiYmd = sdf.format(el).substring(0,10);
                log.info(tag + "KpiDate String = " + kpiYmd); //kill
                jsonStr.put("kpiDate",  kpiYmd);

                amap.put("apiURL", env.getProperty("kpi.readURL"));
                amap.put("jsonStr",jsonStr);

                JSONObject jsonData = cmmnService.getRestApiList(amap);
                JSONArray jsonDataList = (JSONArray) jsonData.get("resultList");
                log.info(tag + "KPI LEVEL 2 KPI.LENGTH = " + jsonDataList.size()); //kill
                JSONObject jsonObj = null;
                String svSendDt = "";
                String sendDt = "";

                for (int i = 0; i < jsonDataList.size(); i++) {
                    jsonObj = (JSONObject) jsonDataList.get(i);
                    Map<String, Object> smap = new HashMap<String, Object>();
                    if (svSendDt.equals("")) {
                        svSendDt = jsonObj.get("kpiDate").toString().substring(2);
                        sendDt = svSendDt;
                    }
                    else {
                        sendDt = jsonObj.get("kpiDate").toString().substring(2);
                        if (svSendDt.equals(sendDt)) {
                            continue;
                        }
                        else {
                            svSendDt = sendDt;
                        }
                    }
                    smap.put("kpiDate", sendDt);
                    smap.put("category", jsonObj.get("category"));
                    smap.put("kpiName", jsonObj.get("kpiName"));
                    smap.put("value", jsonObj.get("value"));
                    smap.put("unit", jsonObj.get("unit"));
                    log.info(tag + "kpiList.map(  " + i + ") = " + smap.toString());
                    kpiList.add(smap);
                    kpiGraphList.add(Float.parseFloat(smap.get("value").toString())); //그래프처리용 데이터리스트
                    kpiLabelList.add(sendDt); //그래프처리용 라벨리스트
                }
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        rmap.put("kpiList",kpiList);
        rmap.put("kpiLabelList",kpiLabelList);
        rmap.put("kpiGraphList",kpiGraphList);
        return rmap;
    }
}

