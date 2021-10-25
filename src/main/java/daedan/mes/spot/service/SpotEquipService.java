package daedan.mes.spot.service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

public interface SpotEquipService {
    List<Map<String, Object>> getSpotEquipList(Map<String, Object> paraMap);

    int getSpotEquipListCount(Map<String, Object> paraMap);

    @Transactional
    void addSpotEquip(Map<String, Object> paraMap);

    Map<String, Object> getSpotEquipInfo(Map<String, Object> paraMap);

//    @Scope(proxyMode = ScopedProxyMode.INTERFACES)

    @Transactional
    void saveSpot(Map<String, Object> paraMap);

    @Transactional
    void saveSpotEquip(Map<String, Object> paraMap);

//    @Scope(proxyMode = ScopedProxyMode.INTERFACES)
    @Transactional
    void dropSpotEquip(Map<String, Object> map);
    List<Map<String, Object>> getComboSpotEquip(Map<String, Object> paraMap);


}
