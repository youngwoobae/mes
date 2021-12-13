package daedan.mes.dash.service;

import java.util.List;
import java.util.Map;

public interface Dash14Service {
    Map<String, Object> getTemp03GraphData(Map<String, Object> paraMap);
    List<Map<String, Object>> getEvtMsgList(Map<String, Object> paraMap);

    List<Map<String, Object>> getMakeRsltDash(Map<String, Object> paraMap);

    List<Map<String, Object>> getUserGroupDash(Map<String, Object> paraMap);

    List<Map<String, Object>>  getMakeRsltGroupDash(Map<String, Object> paraMap);

    List<Map<String, Object>>  getDashTotalMakeIndcRslt(Map<String, Object> paraMap);
}
