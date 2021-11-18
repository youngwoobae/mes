package daedan.mes.dash.service;

import daedan.mes.dash.mapper.DashMapper;
import daedan.mes.dash.mapper.Dash08Mapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("dashService")
public class DashServiceImpl implements  DashService {
    private Log log = LogFactory.getLog(this.getClass());
    @Autowired
    private Environment env;

    @Autowired
    private DashMapper mapper;

    @Autowired
    private Dash08Mapper yyjgMapper;

    public List<Map<String, Object>> getGraphData(Map<String, Object> paraMap) {
        return mapper.getGraphData(paraMap);
    }

    @Override
    public List<Map<String, Object>> getDashTotalMakeIndc(Map<String, Object> paraMap) {
        return mapper.getDashTotalMakeIndc(paraMap);
    }

    @Override
    public List<Map<String, Object>> getDashTotalMakeIndcRslt(Map<String, Object> paraMap) {
        return mapper.getDashTotalMakeIndcRslt(paraMap);
    }

    public List<Map<String, Object>> getToDayQuality(Map<String, Object> paraMap) {
        return mapper.getToDayQuality(paraMap);
    }
    public List<Map<String, Object>> ToDayStoreQuality(Map<String, Object> paraMap) {
        return mapper.ToDayStoreQuality(paraMap);
    }



    @Override
    public List<Map<String, Object>> getScadaTmprList(Map<String, Object> paraMap) {
        String tag = "DashServiceImpl.getScadaTrmpList => ";
        paraMap.put("custNo",Integer.parseInt(paraMap.get("custNo").toString()));
        paraMap.put("equipNo",1L);
        List<Map<String,Object>> ds = mapper.getScadaTmprSpotList(paraMap);
        List<Map<String,Object>> rsltDs = new ArrayList<Map<String,Object>>();
        Map<String,Object> rsltMap = null;
        int idx = -1;
        StringBuffer buf = new StringBuffer();
        while (++idx < ds.size()) {
            Map<String, Object> spotmap = ds.get(idx);
            paraMap.put("spotNo", Long.parseLong(spotmap.get("spotNo").toString()));

            rsltMap = new HashMap<String, Object>();
            rsltMap.put("spot", mapper.getScadaTmprList(paraMap));
            rsltDs.add(rsltMap);
        }
        return rsltDs;
    }

    @Override
    public List<Map<String, Object>> getUtilsAndTransAndErrorList(Map<String, Object> paraMap){
        return mapper.getUtilsAndTransAndErrorList(paraMap);
    }

    @Override
    public List<Map<String, Object>> getPhoneDashPreMonthList(Map<String, Object> paraMap){
        return mapper.getPhoneDashPreMonthList(paraMap);
    }

    @Override
    public List<Map<String, Object>> getPhoneDashMonthList(Map<String, Object> paraMap){
        return mapper.getPhoneDashMonthList(paraMap);
    }

    @Override
    public List<Map<String, Object>> getPhoneDashRsltInfo(Map<String, Object> paraMap){
        return mapper.getPhoneDashRsltInfo(paraMap);
    }

    @Override
    public int getPhoneDashRsltInfoCount(Map<String, Object> paraMap){
        return mapper.getPhoneDashRsltInfoCount(paraMap);
    }

    @Override
    public List<Map<String, Object>> getYyjgProdIoList(Map<String, Object> paraMap) {
        String tag = "vsvc.DashService.getYyjgProdIoList=>";
        log.info(tag + "paraMap = " + paraMap.toString());
        return yyjgMapper.getYyjgProdIoList(paraMap);
    }
    @Override
    public int getYyjgProdIoListCount(Map<String, Object> paraMap) {
        return yyjgMapper.getYyjgProdIoListCount(paraMap);
    }

    @Override
    public List<Map<String, Object>> getYyjgMatrIoList(Map<String, Object> paraMap) {
        String tag = "vsvc.DashService.getYyjgMatrIoList=>";
        log.info(tag + "paraMap = " + paraMap.toString());
        return yyjgMapper.getYyjgMatrIoList(paraMap);
    }
    @Override
    public int getYyjgMatrIoListCount(Map<String, Object> paraMap) {
        return yyjgMapper.getYyjgMatrIoListCount(paraMap);
    }

    @Override
    public List<Map<String, Object>> getEvtMsgList(Map<String, Object> paraMap) {
        return mapper.getEvtMsgList(paraMap);
    }


    @Override
    public List<Map<String, Object>> getHumanList(Map<String, Object> paraMap){
        return mapper.getHumanList(paraMap);
    }

    @Override
    public int getToTalhumanCount(Map<String, Object> paraMap){
        return mapper.getToTalhumanCount(paraMap);
    }


    @Override
    public List<Map<String, Object>> getMetalLog(Map<String, Object> paraMap){
        return mapper.getMetalLog(paraMap);
    }

    @Override
    public int getMetalLogCount(Map<String, Object> paraMap){
        return mapper.getMetalLogCount(paraMap);
    }


}
