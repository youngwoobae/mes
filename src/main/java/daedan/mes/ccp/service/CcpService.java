package daedan.mes.ccp.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface CcpService {
    List<Map<String, Object>> getGuideList(HashMap<String, Object> paraMap);
    int getGuideListCount(HashMap<String, Object> paraMap);

    Map<String, Object> getGuideInfo(HashMap<String, Object> paraMap);

    void saveGuide(HashMap<String, Object> paraMap);
    void dropGuide(HashMap<String, Object> paraMap);

    void ccpHstrSave(HashMap<String, Object> paraMap);

    List<Map<String, Object>> getHeatLmtList(HashMap<String, Object> paraMap);
    int getHeatLmtListCount(HashMap<String, Object> paraMap);

    Map<String, Object>  getHeatLmtInfo(Map<String,Object> paraMap);

    void dropHeatLmtInfo(HashMap<String, Object> paraMap);

    void saveHeatLmtInfo(HashMap<String, Object> paraMap);
}
