package daedan.mes.moniter.service;

import java.util.List;
import java.util.Map;

public interface MonCust18Service {
    List<Map<String,Object>> getHeatLogHstr(Map<String, Object> paraMap);
    void heaterOn(Map<String, Object> paraMap);
    void heaterOff(Map<String, Object> paraMap);
}
