package daedan.mes.dash.service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

public interface Dash18Service {
    @Transactional
    List<Map<String, Object>> getTmpr18List(Map<String, Object> paraMap);
    Map<String, Object> getFinalHeatStatus(Map<String, Object> paraMap);
}
