package daedan.mes.dash.service;

import java.util.List;
import java.util.Map;

public interface DashService {

    List<Map<String, Object>> getToDayQuality(Map<String, Object> paraMap);

    List<Map<String, Object>> ToDayStoreQuality(Map<String, Object> paraMap);

    List<Map<String, Object>> getGraphData(Map<String, Object> paraMap);

    List<Map<String, Object>> getScadaTmprList(Map<String, Object> paraMap);

    List<Map<String, Object>> getDashTotalMakeIndc(Map<String, Object> paraMap);

    List<Map<String, Object>> getDashTotalMakeIndcRslt(Map<String, Object> paraMap);

    List<Map<String, Object>> getUtilsAndTransAndErrorList(Map<String, Object> paraMap);

    List<Map<String, Object>> getPhoneDashPreMonthList(Map<String, Object> paraMap);

    List<Map<String, Object>> getPhoneDashMonthList(Map<String, Object> passMap);

    List<Map<String, Object>> getPhoneDashRsltInfo(Map<String, Object> paraMap);

    int getPhoneDashRsltInfoCount(Map<String, Object> paraMap);

    List<Map<String, Object>>  getYyjgProdIoList(Map<String, Object> paraMap);
    int  getYyjgProdIoListCount(Map<String, Object> paraMap);

    List<Map<String, Object>> getEvtMsgList(Map<String, Object> paraMap);

    List<Map<String, Object>> gethumanList(Map<String, Object> paraMap);
    int getToTalhumanCount(Map<String, Object> paraMap);

    List<Map<String, Object>> getYyjgMatrIoList(Map<String, Object> paraMap);
    int getYyjgMatrIoListCount(Map<String, Object> paraMap);
}
