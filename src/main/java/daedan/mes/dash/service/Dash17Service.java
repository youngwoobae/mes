package daedan.mes.dash.service;

import java.util.List;
import java.util.Map;

public interface Dash17Service {
    List<Map<String, Object>> getTmpr17List(Map<String, Object> paraMap);
    Map<String, Object> getFinalProcCnt(Map<String, Object> paraMap);
}
