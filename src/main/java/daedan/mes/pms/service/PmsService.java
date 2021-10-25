package daedan.mes.pms.service;

import java.util.List;
import java.util.Map;

public interface PmsService {

    Map<String, Object> getPmsOrdInfo(Map<String, Object> paraMap);

    List<Map<String,Object>> getComboPmsProcStat(Map<String, Object> paraMap);

    List<Map<String, Object>> getPmsOrdrList(Map<String, Object> paraMap);
    int getPmsOrdrListCount(Map<String, Object> paraMap);

    void savePmsOrdr(Map<String, Object> paraMap);

    void dropPmsOrdr(Map<String, Object> paraMap);

    List<Map<String,Object>> getPmsOrdInfoList(Map<String, Object> paraMap);

    List<Map<String,Object>> getPmsWhoList(Map<String, Object> paraMap);

    void savePmsWhom(Map<String, Object> paraMap);

    void dropPmsWhom(Map<String, Object> paraMap);
}
