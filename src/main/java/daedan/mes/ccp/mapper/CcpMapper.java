package daedan.mes.ccp.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface CcpMapper {
    List<Map<String, Object>> getGuideList(HashMap<String, Object> paraMap);
    int getGuideListCount(HashMap<String, Object> paraMap);
    void dropGuide(HashMap<String, Object> paraMap);

    List<Map<String, Object>> getHeatLmtList(HashMap<String, Object> paraMap);
    int getHeatLmtListCount(HashMap<String, Object> paraMap);

    Map<String, Object> getHeatLmtInfo(Map<String, Object> paraMap);
}
