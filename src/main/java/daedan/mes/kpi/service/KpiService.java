package daedan.mes.kpi.service;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

public interface KpiService {
    Map<String, Object> operPerformRead(Map<String, Object> paraMap);
    List<Map<String, Object>> kpiRead(Map<String, Object> paraMap);
}
