package daedan.mes.dash.service.Yyjg;

import java.util.List;
import java.util.Map;

public interface YyjgDashService {
    Map<String,Object> getYyjgPhothCounter(Map<String, Object> paraMap);

    List<Map<String, Object>> getYyjgSpotList(Map<String, Object> paraMap);
}
