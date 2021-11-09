package daedan.mes.report.service;

import java.util.List;
import java.util.Map;

public interface MesReportService {
    List<Map<String,Object>> getMetalDetectHstr(Map<String, Object> paraMap);
    int getMetalDetectHstrCount(Map<String, Object> paraMap);

    List<Map<String,Object>> getProdOwhHstr(Map<String, Object> paraMap);
    int getProdOwhHstrCount(Map<String, Object> paraMap);

    List<Map<String,Object>> getProdIwhHstr(Map<String, Object> paraMap);
    int getProdIwhHstrCount(Map<String, Object> paraMap);

    List<Map<String,Object>> getTmprLogHstr(Map<String, Object> paraMap);


}
