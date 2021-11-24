package daedan.mes.dash.service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

public interface Dash03Service {
    Map<String, Object> getTemp03GraphData(Map<String, Object> paraMap);
    List<Map<String, Object>> getEvtMsgList(Map<String, Object> paraMap);

    List<Map<String, Object>> getMakeRsltDash(Map<String, Object> paraMap);

    List<Map<String, Object>> getUserGroupDash(Map<String, Object> paraMap);
}
