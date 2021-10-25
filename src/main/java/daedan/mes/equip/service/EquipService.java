package daedan.mes.equip.service;

import daedan.mes.equip.domain.EquipInfo;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

public interface EquipService {
    List<Map<String,Object>> getEquipList(Map<String, Object> paraMap);
    int getEquipListCount(Map<String, Object> paraMap);

    void saveEquip(Map<String, Object> paraMap);
    void dropEquip(Map<String, Object> map);

    @Scope(proxyMode = ScopedProxyMode.INTERFACES)
    @Transactional
    void dropEquipMast(Map<String, Object> paraMap);

    EquipInfo getEquip(Long equipNo);

    Map<String, Object> equipInfo(Map<String, Object> map);

    List<Map<String,Object>> getEquipDeviceList(Map<String, Object> paraMap);
    int getEquipDeviceListCount(Map<String, Object> paraMap);

    List<Map<String,Object>>  getEquipRunStatList(Map<String, Object> paraMap);

    int getEquipRunStatListCount(Map<String, Object> paraMap);

    List<Map<String, Object>> getEquipRunHstrList(Map<String, Object> paraMap);

    int getEquipRunHstrListCount(Map<String, Object> paraMap);



    List<Map<String,Object>> getOnlyEquipList(Map<String, Object> paraMap);
    int getOnlyEquipListCount(Map<String, Object> paraMap);

    List<Map<String,Object>> getEquipMngrItem(Map<String, Object> paraMap);

    void equipMngrItemSave(Map<String, Object> paraMap);

    List<Map<String,Object>> getEquipMngrHstr(Map<String, Object> paraMap);

    List<Map<String,Object>> getEquipMngrHstrDouble(Map<String, Object> paraMap);

    List<Map<String,Object>>  getEquipMngrHstrHdfdVer(Map<String, Object> paraMap);

    @Transactional
    void saveModbusTmpr(Map<String, Object> paraMap);

    @Transactional
    void saveModbusHumy(Map<String, Object> paraMap);


    List<Map<String,Object>>  getComboEquipList(Map<String, Object> paraMap);

//    List<Map<String,Object>>  getCheckItemList(Map<String, Object> paraMap);
    Map<Integer, Map<String, Object>> getCheckItemList(Map<String, Object> map);

    Map<Integer, Map<String, Object>> getErrorItemList(Map<String, Object> paraMap);

    @Scope(proxyMode = ScopedProxyMode.INTERFACES)
    @Transactional
    void saveEquipMast(Map<String, Object> map);

    void reportSave(Map<String, Object> paraMap);

    Map<String, Object>  getEquipMastInfo(Map<String, Object> paraMap);

    void saveCcpMetalReport(Map<String, Object> paraMap);

    List<Map<String,Object>> getEquipReportList(Map<String, Object> paraMap);

    int getEquipReportListCount(Map<String, Object> paraMap);
}
