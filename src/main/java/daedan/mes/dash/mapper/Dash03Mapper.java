package daedan.mes.dash.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface Dash03Mapper {
    List<Map<String, Object>> getTmpr03GraphData(Map<String, Object> paraMap);
    List<Map<String, Object>> getEvtMsgList(Map<String, Object> paraMap);

    List<Map<String, Object>> getMakeRsltDash(Map<String, Object> paraMap);

    List<Map<String, Object>> getUserGroupDash(Map<String, Object> paraMap);
    List<Map<String, Object>> getMakeRsltGroupDash(Map<String, Object> paraMap);

    List<Map<String, Object>> getWorkerList(Map<String, Object> paraMap);

    int getWorkerListCount(Map<String, Object> paraMap);
}
