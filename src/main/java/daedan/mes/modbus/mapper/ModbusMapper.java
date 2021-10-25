package daedan.mes.modbus.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface ModbusMapper {
    Map<String,Object> getTabletInfo(Map<String,Object> paraMap);
    Map<String,Object> getTabletInfoBySysMenu(Map<String,Object> paraMap);
    List<Map<String, Object>> getTabletList(Map<String, Object> paraMap);
    int getTabletListCount(Map<String, Object> paraMap);

    List<Map<String, Object>> getComboSpot(Map<String, Object> paraMap);
    List<Map<String, Object>> getComboEquip(Map<String, Object> paraMap);

    List<Map<String, Object>> getComboTabletMenuList(Map<String, Object> paraMap);
    void initOperMoniter(Map<String, Object> paraMap);
    List<Map<String, Object>> getInitialProdList(Map<String, Object> paraMap);

    List<Map<String, Object>> getTrkList(Map<String, Object> paraMap);
    int getTrkListCount(Map<String, Object> paraMap);

    //하담푸드 포장기 카운터 이력
    List<Map<String, Object>> getSoptEquipValHist(Map<String, Object> paraMap);
    int getSoptEquipValHistCount(Map<String, Object> paraMap);

    List<Map<String, Object>> getTmprList(Map<String, Object> paraMap);

    int getTmprListCount(Map<String, Object> paraMap);

    List<Map<String, Object>> getTmprHstrList(Map<String, Object> paraMap);
    int getTmprHstrListCount(Map<String, Object> paraMap);

    void updateEquipMngrHstr(Map<String, Object> paraMap);

    void stopEquipMngrHstr(Map<String, Object> paraMap);

    void updateOperMast(Map<String, Object> paraMap);

    Map<String, Object> getSterTime(Map<String, Object> rmap);

    void updateOperMastToQty(Map<String, Object> paraMap);

    Map<String, Object> getLastEquipHistory(Map<String, Object> paraMap);
}
