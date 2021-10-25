package daedan.mes.equip.service;

import daedan.mes.equip.domain.EquipMatr;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

public interface EquipMatrService {
    List<Map<String,Object>> getEquipMatrList(Map<String, Object> paraMap);
    int getEquipMatrListCount(Map<String, Object> paraMap);
    void saveEquipMatr(Map<String, Object> emMap);

    @Transactional
    void dropEquipMatr(Map<String, Object> map);

    EquipMatr getEquipMatr(Long matrNo);

    List<Map<String,Object>> getEquipBomList(Map<String, Object> paraMap);

    Map<String, Object> equipMatrInfo(Map<String, Object> map);
}
