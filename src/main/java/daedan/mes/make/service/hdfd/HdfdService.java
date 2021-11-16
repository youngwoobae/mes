package daedan.mes.make.service.hdfd;

import java.util.List;
import java.util.Map;

public interface HdfdService {
    List<Map<String, Object>> getHdfdIndcList(Map<String, Object> paraMap);

    List<Map<String, Object>> planList(Map<String, Object> paraMap);

}
