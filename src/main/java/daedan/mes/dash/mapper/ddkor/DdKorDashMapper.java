package daedan.mes.dash.mapper.ddkor;


import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface DdKorDashMapper {
    List<Map<String, Object>> getFlowMeterList(Map<String, Object> Map);
    List<Map<String, Object>> getPowerList(Map<String, Object> Map);

    List<Map<String, Object>> getDashSpotTmpr(Map<String, Object> paraMap);

    List<Map<String, Object>> getDashMainData(Map<String, Object> paraMap);

    List<Map<String, Object>> getDashWrapRoomData(Map<String, Object> paraMap);
}
