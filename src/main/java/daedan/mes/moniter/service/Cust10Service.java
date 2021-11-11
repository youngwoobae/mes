package daedan.mes.moniter.service;

import java.util.List;
import java.util.Map;

public interface Cust10Service {
    List<Map<String,Object>> getMetalDetctHstr(Map<String, Object> paraMap);
    int                      getMetalDetectHstrCount(Map<String, Object> paraMap);
}
