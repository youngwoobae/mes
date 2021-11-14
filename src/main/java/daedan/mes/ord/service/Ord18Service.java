package daedan.mes.ord.service;

import java.util.List;
import java.util.Map;

public interface Ord18Service {
    Map<String, Object>  getOrd18Info(Map<String, Object> paraMap);
    void saveOrdInfo18(Map<String, Object> paraMap);

    List<Map<String, Object>> getOrd18List(Map<String, Object> paraMap);
    int getOrd18ListCount(Map<String, Object> paraMap);
    void dropOrdInfo18(Map<String, Object> paraMap);
}
