package daedan.mes.report.service;

import java.util.List;
import java.util.Map;

public interface ReportService {
    List<Map<String,Object>> getMetalDetectHstr(Map<String, Object> paraMap);
    int getMetalDetectHstrCount(Map<String, Object> paraMap);

}
