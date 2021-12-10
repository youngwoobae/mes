package daedan.mes.make.service.metal10;

import java.util.Map;

public interface Metal10Service {
    Map<String, Object> getCurMetalLog(Map<String, Object> paraMap);
    Map<String, Object> metalDetectOper(Map<String, Object> paraMap);
    Map<String, Object>  getMetalData(Map<String, Object> paraMap);
}
