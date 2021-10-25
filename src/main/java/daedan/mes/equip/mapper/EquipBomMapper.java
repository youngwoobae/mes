package daedan.mes.equip.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


@Mapper
@Component
public interface EquipBomMapper {
    List<Map<String,Object>> getEquipBomList(Map<String, Object> paraMap);
    int getEquipBomListCount(Map<String, Object> paraMap);
    Map<String,Object> getEquipBomInfo(Map<String, Object> paraMap);
    Map<String,Object> getEquipBomCartInfo(Map<String, Object> paraMap);

}
