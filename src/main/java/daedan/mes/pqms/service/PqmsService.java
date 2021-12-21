package daedan.mes.pqms.service;

import java.util.List;
import java.util.Map;

public interface PqmsService {
    List<Map<String,Object>> getOrdRecvList(Map<String, Object> paraMap);
    int getOrdRecvListCount(Map<String, Object> paraMap);

    List<Map<String,Object>>  getComboOrdSender(Map<String, Object> paraMap);
}
