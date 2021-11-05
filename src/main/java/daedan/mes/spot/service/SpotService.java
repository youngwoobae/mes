package daedan.mes.spot.service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

public interface SpotService {
    List<Map<String,Object>> getSpots(Map<String, Object> paraMap); //다중선택용
    List<Map<String,Object>> getSpotList(Map<String, Object> paraMap);
    int getSpotListCount(Map<String, Object> paraMap);

    void dropSpot(Map<String, Object> paraMap);

    @Transactional
    void saveSpotInfo(Map<String, Object> paraMap);

    List<Map<String, Object>> getSpotEquipList(Map<String, Object> paraMap);

    Map<String, Object> getSpotInfo(Map<String, Object> paraMap);

    List<Map<String, Object>>  getComboSpotEquip(Map<String, Object> paraMap);

}
