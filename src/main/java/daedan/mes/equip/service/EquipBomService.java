package daedan.mes.equip.service;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

public interface EquipBomService {
    List<Map<String,Object>> getEquipBomList(Map<String, Object> paraMap);
    int getEquipBomListCount(Map<String, Object> paraMap);
//test
    @Scope(proxyMode = ScopedProxyMode.INTERFACES)
    @Transactional
    void saveEquipBomCart(Map<String, Object> map);

    @Scope(proxyMode = ScopedProxyMode.INTERFACES)
    @Transactional
    void saveEquipBom(Map<String, Object> map);

    @Scope(proxyMode = ScopedProxyMode.INTERFACES)
    @Transactional
    void dropEquipBom(Map<String, Object> map);

    Map<String, Object> getEquipBomInfo(Map<String, Object> paraMap);

    @Transactional
    void apndEquipBomCart(Map<String, Object> emMap);

    @Transactional
    void addEquipBomList(Map<String, Object> paraMap);

}
