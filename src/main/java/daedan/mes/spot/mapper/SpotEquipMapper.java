package daedan.mes.spot.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface SpotEquipMapper {
    List<Map<String, Object>> getSpotEquipList(Map<String, Object> paraMap);
    int getSpotEquipListCount(Map<String, Object> paraMap);

    Map<String, Object> getSpotEquipInfo(Map<String, Object> paraMap);

    List<Map<String, Object>> getComboSpotEquip(Map<String, Object> paraMap);
}
