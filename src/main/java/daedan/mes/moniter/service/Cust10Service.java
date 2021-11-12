package daedan.mes.moniter.service;

import java.util.List;
import java.util.Map;

public interface Cust10Service {
    List<Map<String,Object>> getMetalLogHstr(Map<String, Object> paraMap);
    int getMetalLogHstrCount(Map<String, Object> paraMap);
}
