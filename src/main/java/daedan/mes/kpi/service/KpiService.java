package daedan.mes.kpi.service;

import java.util.List;
import java.util.Map;

public interface KpiService {
    List<Map<String, Object>> operPerformRead(Map<String, Object> paraMap);
    Map<String, Object> kpiRead(Map<String, Object> paraMap);
}
