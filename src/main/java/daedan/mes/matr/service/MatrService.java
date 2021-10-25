package daedan.mes.matr.service;

import daedan.mes.matr.domain.MatrInfo;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface MatrService {
    List<Map<String, Object>> getMatrList(Map<String, Object> paraMap);
    int getMatrListCount(Map<String, Object> paraMap);

    List<Map<String, Object>> getMatrWithCmpyList(Map<String, Object> paraMap);
    int getMatrWithCmpyListCount(Map<String, Object> paraMap);

    //상품별 원부자재
    List<Map<String, Object>> getProdBomList(Map<String, Object> paraMap);
    int getProdBomListCount(Map<String, Object> paraMap);

    Map<String, Object> getMatrInfo(Map<String, Object> paraMap);

    void saveMatr(Map<String, Object> paraMap);

    MatrInfo getMatrInfoByJPA(Long custNo, Long matrNo);

    @Transactional  void saveMatrCmpy(Map<String, Object> paraMap);


    void saveMatrCmpyCart(Map<String, Object> paraMap);

    //원부자재구매처래처...
    List<Map<String, Object>>  getMatrCmpyList(Map<String, Object> paraMap);
    int getMatrCmpyListCount(Map<String, Object> paraMap);
    void initMatrCmpyCart(Map<String, Object> paraMap);

    List<Map<String, Object>> getMatrCmpyComboList(Map<String, Object> paraMap);

    void deleteMatr(Map<String, Object> paraMap);

    @Transactional
    void loadRawMatByExcel(Map<String, Object> paraMap)  throws Exception;

    Map<String, Object> getSafeStk(Map<String, Object> paraMap);

    List<Map<String, Object>> getCmpyMatrCombo(Map<String, Object> paraMap);

    List<Map<String, Object>> getMatrProdList(Map<String, Object> paraMap);
    int getMatrProdListCount(Map<String, Object> paraMap);

    List<Map<String, Object>>  getCmpyMatrList(Map<String, Object> paraMap);
    int getCmpyMatrListCount(Map<String, Object> paraMap);

    void dropMatrCmpy(Map<String, Object> paraMap);

    void dropMatrProd(Map<String, Object> paraMap);

    List<Map<String, Object>>  getProdMatrList(Map<String, Object> paraMap);

    int getProdMatrListCount(Map<String, Object> paraMap);

    void saveSafeStk(Map<String, Object> paraMap);

    Map<String, Object>   getPursUnit(Map<String, Object> paraMap);

    void embCalendarSave(Map<String, Object> paraMap);

    List<Map<String, Object>>  getStockList(Map<String, Object> paraMap);

    List<Map<String, Object>> getMatrIwhList(Map<String, Object> paraMap);
    int getMatrIwhListCount(Map<String, Object> paraMap);

    Object saveMatrStkInfo(Map<String, Object> paraMap);

    List<Map<String, Object>> getProcList(Map<String, Object> paraMap);

    List<Map<String, Object>> getMatrCurrStkList(Map<String, Object> paraMap);
    int getMatrCurrStkListCount(Map<String, Object> paraMap);

    List<Map<String, Object>> getAnaMatrList(Map<String, Object> paraMap);

    void matrStatExcel(Map<String, Object> paraMap, HttpServletResponse response);

    @Transactional void saveProdBom(Map<String, Object> paraMap);

    List<Map<String, Object>> getMatrExcelList(Map<String, Object> paraMap);

    List<Map<String, Object>> matrExcelIwh(HashMap<String, Object> paraMap) throws Exception;

    List<Map<String, Object>> getCheckRetnList(Map<String, Object> paraMap);

    List<Map<String, Object>> getComeDateMatrList(Map<String, Object> paraMap);

    int getComeDateMatrListCount(Map<String, Object> paraMap);

    @Transactional
    void matrSaveExcelUpLoad(Map<String, Object> paraMap)throws Exception;

    List<Map<String, Object>> getmatrPursUnit(Map<String, Object> paraMap);

    int getmatrPursUnitCount(Map<String, Object> paraMap);

    @Transactional
    void saveMatrPursUnit(HashMap<String, Object> paraMap);

    void dropPursUnit(Map<String, Object> paraMap);

    List<Map<String, Object>> getSafeStkMatrList(Map<String, Object> paraMap);

    int getSafeStkMatrListCount(Map<String, Object> paraMap);

    @Transactional
    void svMatrSaveExcelUpLoad(Map<String, Object> paraMap)throws Exception;
}
