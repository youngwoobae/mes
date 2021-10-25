package daedan.mes.moniter.service;

import java.util.List;
import java.util.Map;

public interface MoniterService {
    List<Map<String, Object>> getComboEquip(Map<String, Object> paraMap);
    List<Map<String, Object>> getEquipUsedHstr(Map<String, Object> paraMap);
    int getEquipUsedHstrCount(Map<String, Object> paraMap);

    List<Map<String, Object>> getTotalStatusList(Map<String, Object> paraMap);

    int getTotalStatusListCount(Map<String, Object> paraMap);

    List<Map<String, Object>>  getComboOrderCmpy(Map<String, Object> paraMap);

    List<Map<String, Object>> getCcpHeatList(Map<String, Object> paraMap);

    int getCcpHeatListCount(Map<String, Object> paraMap);

    List<Map<String, Object>> getEquipProdMove(Map<String, Object> paraMap);

    List<Map<String, Object>> getCcpTexts(Map<String, Object> paraMap);

    List<Map<String,Object>> getProdIoFileList(Map<String, Object> paraMap);
    int getProdIoFileListCount(Map<String, Object> paraMap);

    List<Map<String,Object>> getOrdHstList(Map<String, Object> paraMap);
    int getOrdHstListCount(Map<String, Object> paraMap);

    List<Map<String,Object>> getMatrIwhHstList(Map<String, Object> paraMap);
    int getMatrIwhHstListCount(Map<String, Object> paraMap);

    List<Map<String,Object>> getMatrOwhHstList(Map<String, Object> paraMap);
    int getMatrOwhHstListCount(Map<String, Object> paraMap);

    List<Map<String,Object>> getProdIwhHstList(Map<String, Object> paraMap);
    int getProdIwhHstListCount(Map<String, Object> paraMap);

    List<Map<String,Object>> getProdOwhHstList(Map<String, Object> paraMap);
    int getProdOwhHstListCount(Map<String, Object> paraMap);

    List<Map<String,Object>> getMatrPursHstList(Map<String, Object> paraMap);
    int getMatrPursHstListCount(Map<String, Object> paraMap);
}
