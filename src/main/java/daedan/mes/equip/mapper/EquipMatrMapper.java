package daedan.mes.equip.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface EquipMatrMapper {
    List<Map<String,Object>> getEquipMatrList(Map<String, Object> paraMap);
    int getEquipMatrListCount(Map<String, Object> paraMap);
    Map<String, Object> getEquipMatInfo(Map<String, Object> paraMap);

    void apndEquipBomCart(Map<String, Object> paraMap);

    List<Map<String, Object>> equipMatrList(Map<String, Object> paraMap);
    List<Map<String, Object>> equipBomList(Map<String, Object> paraMap);

    Map<String, Object> equipMatrInfo(Map<String, Object> map);

    void dropEquipMatr(Map<String, Object> map);
}
