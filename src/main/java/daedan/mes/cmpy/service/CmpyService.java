package daedan.mes.cmpy.service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface CmpyService {
    Map<String, Object> getCmpyInfo(Map<String, Object> cmpyNo);

    List<Map<String, Object>> getCmpyList(Map<String, Object> map); //거래처목록
    int getCmpyListCount(HashMap<String, Object> paraMap); //거래처목록카운터

    List<Map<String, Object>> getCmpyOrdrList(Map<String, Object> map); //매출목록
    int getCmpyOrdrListCount(HashMap<String, Object> paraMap); //매출목록카운터

    List<Map<String, Object>> getCmpyPursList(Map<String, Object> map); //구매목록
    int getCmpyPursListCount(HashMap<String, Object> paraMap); //구매목록카운터
    void saveCmpy(Map<String, Object> paraMap);

    void setUnusedCmpy(Map<String, Object> cmpyMap);

    List<Map<String, Object>>  getCmpyDlvPlcList(Map<String, Object> paraMap);

    int getCmpyDlvPlcListCount(Map<String, Object> paraMap);

    void saveDlvPlc(Map<String, Object> passMap);

    void deleteDlvPlc(Map<String, Object> passMap);

    @Transactional
    void deleteDlvPlcInfo(Map<String, Object> paraMap);

    Map<String, Object> getCmpyPursInfo(Map<String, Object> paraMap);
    List<Map<String, Object>>  getCmpyPursMatrList(Map<String, Object> paraMap);


    List<Map<String, Object>>  getCmpyIpList(Map<String, Object> paraMap);
    int getCmpyIpListCount(HashMap<String, Object> paraMap); //매출목록카운

    List<Map<String, Object>>  getEqOption(Map<String, Object> paraMap);
    List<Map<String, Object>>  getSysOption(Map<String, Object> paraMap);

    List<Map<String, Object>> getCmpyExcelList(Map<String,Object> paraMap);

    List<Map<String, Object>> getMatrCmpyList(Map<String, Object> paraMap);

    void dropMatrCmpy(Map<String, Object> paraMap);

    Map<String, Object> getDlvPlcInfo(Map<String, Object> paraMap); //AddOn By KMJ At 21.10.19
}
