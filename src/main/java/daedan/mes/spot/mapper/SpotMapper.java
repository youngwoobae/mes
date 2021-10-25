package daedan.mes.spot.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface SpotMapper {
    List<Map<String,Object>> getSpotList(Map<String, Object> paraMap);
    int getSpotListCount(Map<String, Object> paraMap);

    List<Map<String,Object>> dropSpot(Map<String, Object> paraMap);

    Map<String, Object> getSpotInfo(Map<String, Object> paraMap);

    List<Map<String, Object>> getSpotEquipList(Map<String, Object> paraMap);

    List<Map<String, Object>> getComboSpotEquip(Map<String, Object> paraMap);

    List<Map<String, Object>> getSpots(Map<String, Object> paraMap); //다중선택용-페이징기능없음.
}
