package daedan.mes.make.service.sf;

import java.util.List;
import java.util.Map;

public interface SfService {
    Map<String, Object> getXsl2Hmtl(Map<String, Object> paraMap);

    void makeDailyIndc(Map<String, Object> paraMap);

    List<Map<String, Object>> getDailyIndc(Map<String, Object> paraMap);
}

