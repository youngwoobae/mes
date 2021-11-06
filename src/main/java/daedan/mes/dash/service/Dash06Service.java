package daedan.mes.dash.service;

import java.util.List;
import java.util.Map;

public interface Dash06Service {
    List<Map<String, Object>> getFlowMeterList(Map<String, Object> Map);
    List<Map<String, Object>> getPowerList(Map<String, Object> Map);

    List<Map<String, Object>>  getDashSpotTmpr(Map<String, Object> paraMap);

    List<Map<String, Object>>  getDashMainData(Map<String, Object> paraMap);

    List<Map<String, Object>> getDashWrapRoomData(Map<String, Object> paraMap);
}
