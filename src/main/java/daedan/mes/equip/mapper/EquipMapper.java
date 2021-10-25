package daedan.mes.equip.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface EquipMapper {
    List<Map<String,Object>> getEquipList(Map<String, Object> paraMap);
    int getEquipListCount(Map<String, Object> paraMap);
    Map<String, Object> getEquipInfo(Map<String, Object> paraMap);
    void dropEquip(Map<String, Object> map);
    List<Map<String, Object>> equipMatrList(Map<String, Object> paraMap);

    Map<String, Object> equipInfo(Map<String, Object> map);

    List<Map<String, Object>> getEquipDeviceList(Map<String, Object> paraMap);
    int getEquipDeviceListCount(Map<String, Object> paraMap);

    List<Map<String, Object>> getEquipRunStatList(Map<String, Object> paraMap);

    int getEquipRunStatListCount(Map<String, Object> paraMap);

    List<Map<String, Object>> getEquipRunHstrList(Map<String, Object> paraMap);

    int getEquipRunHstrListCount(Map<String, Object> paraMap);

    List<Map<String,Object>> getOnlyEquipList(Map<String, Object> paraMap);
    int getOnlyEquipListCount(Map<String, Object> paraMap);

    List<Map<String,Object>> getEquipMngrItem(Map<String, Object> paraMap);

    List<Map<String, Object>> getEquipMngrHstr(Map<String, Object> paraMap);

    List<Map<String, Object>> getEquipMngrHstrDouble(Map<String, Object> paraMap);

    Map<String,Object> startEquip(Map<String, Object> paraMap);
    void stopEquip(Map<String, Object> paraMap);

    List<Map<String, Object>> getComboEquipList(Map<String, Object> paraMap);

    List<Map<String, Object>> getCheckItemList(Map<String, Object> paraMap);

    List<Map<String, Object>> getOnlyOperList(Map<String, Object> paraMap);

    int getOperCount(Map<String, Object> paraMap);

    List<Map<String, Object>> getEquipMngrHstrHdfdVer(Map<String, Object> paraMap);

    List<Map<String, Object>> getEquipReportList(Map<String, Object> paraMap);
    int getEquipReportListCount(Map<String, Object> paraMap);
}
