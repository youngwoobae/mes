package daedan.mes.modbus.service;

import daedan.mes.modbus.domain.TmprHstr;

import java.util.List;
import java.util.Map;

public interface ModbusService {
    void saveTabletInfo(Map<String,Object> paraMap);

    void dropTabletInfo(Map<String, Object> paraMap);

    Map<String, Object> getTabletInfo(Map<String, Object> paraMap);
    Map<String, Object> getTabletInfoBySysMenu(Map<String, Object> paraMap);
    List<Map<String, Object>> getTabletList(Map<String, Object> paraMap);
    int getTabletListCount(Map<String, Object> paraMap);

    List<Map<String, Object>>  getComboSpot(Map<String, Object> paraMap);
    List<Map<String, Object>>  getComboEquip(Map<String, Object> paraMap);
    List<Map<String, Object>> getComboTabletMenuList(Map<String, Object> paraMap);

    Map<String, Object> startOper(Map<String, Object> paraMap);
    void stopOper(Map<String, Object> paraMap);

    Map<String, Object> startMoniter(Map<String, Object> paraMap);

    List<Map<String, Object>> getInitialProdList(Map<String, Object> paraMap);

    //하담푸드 포장기 카운터 이력
    List<Map<String, Object>> getSoptEquipValHist(Map<String, Object> paraMap);
    int getSoptEquipValHistCount(Map<String, Object> paraMap);

    void saveModbusData(Map<String, Object> paraMap);

    /*꼬치접이*/
    void saveModbusWrinkle(Map<String, Object> paraMap);

    void saveModbusFail(Map<String, Object> paraMap);

    List<Map<String, Object>> getTrkList(Map<String, Object> paraMap);
    int getTrkListCount(Map<String, Object> paraMap);

    void saveTmprAndHumy(Map<String, Object> paraMap);
    TmprHstr saveTmprHstr(Map<String, Object> paraMap);

    List<Map<String, Object>> getTmprHstrList(Map<String, Object> paraMap);
    int getTmprHstrListCount(Map<String, Object> paraMap);

    void saveOperData(Map<String, Object> paraMap);

    void matchEquipMngrHstr(Map<String, Object> paraMap);

    void stopEquipMngrHstr(Map<String, Object> paraMap);

    void setOperMastQty(Map<String, Object> paraMap);
}
