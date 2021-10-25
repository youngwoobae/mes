package daedan.mes.dash.service.DdKor;

import java.util.List;
import java.util.Map;

public interface DdKorDashService {
    List<Map<String, Object>> getFlowMeterList(Map<String, Object> Map);
    List<Map<String, Object>> getPowerList(Map<String, Object> Map);

    List<Map<String, Object>>  getDashSpotTmpr(Map<String, Object> paraMap);

    List<Map<String, Object>>  getDashMainData(Map<String, Object> paraMap);

    List<Map<String, Object>> getDashWrapRoomData(Map<String, Object> paraMap);
}
