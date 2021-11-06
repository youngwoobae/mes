package daedan.mes.dash.service;

import java.util.List;
import java.util.Map;

public interface Dash08Service {
    Map<String,Object> getYyjgPhothCounter(Map<String, Object> paraMap);

    List<Map<String, Object>> getYyjgSpotList(Map<String, Object> paraMap);
}
