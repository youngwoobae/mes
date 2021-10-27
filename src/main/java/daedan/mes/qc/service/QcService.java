package daedan.mes.qc.service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

public interface QcService {
    List<Map<String,Object>> getMatrIwhChkList(Map<String, Object> paraMap);
    int getMatrIwhChkListCount(Map<String, Object> paraMap);
    List<Map<String, Object>> getMatrIwhChkDocList(Map<String, Object> paraMap);
    int getMatrIwhChkDocListCount(Map<String, Object> paraMap);

    Map<String,Object> matrIwhChkInfo(Map<String, Object> paraMap);

    void saveMatrIwhChk(Map<String, Object> paraMap);

    @Transactional
    void dropMatrIwhChk(Map<String, Object> paraMap);

    List<Map<String, Object>> getProdOwhChkList(Map<String, Object> paraMap);
    int getProdOwhChkListCount(Map<String, Object> paraMap);
    List<Map<String, Object>> getProdOwhChkDocList(Map<String, Object> paraMap);
    int getProdOwhChkDocListCount(Map<String, Object> paraMap);

    void saveProdOwhChk(Map<String, Object> paraMap);

    void dropProdOwhChk(Map<String, Object> paraMap);

    Map<String, Object> prodOwhChkInfo(Map<String, Object> paraMap);
}
