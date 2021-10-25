package daedan.mes.matr.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface MatrMapper {
    void appendBrnchInfo(Map<String, Object> paraMap);

    List<Map<String, Object>> getMatrList(Map<String, Object> paraMap);
    int getMatrListCount(Map<String, Object> paraMap);

    List<Map<String, Object>> getProdBomList(Map<String, Object> paraMap);
    int getProdBomListCount(Map<String, Object> paraMap);

    Map<String, Object> getMatrInfo(Map<String, Object> paraMap);

    List<Map<String, Object>> getMatrCmpyList(Map<String, Object> paraMap);
    int getMatrCmpyListCount(Map<String, Object> paraMap);

    List<Map<String, Object>> getCmpyMatrCombo(Map<String, Object> paraMap);
    List<Map<String, Object>> getMatrCmpyComboList(Map<String, Object> paraMap);
    void resetLineFeedToSpaceToMatrNm();

    void deleteMatr(Map<String, Object> paraMap);

    List<Map<String, Object>> getMatrProdList(Map<String, Object> paraMap);
    int getMatrProdListCount(Map<String, Object> paraMap);


    List<Map<String, Object>> getCmpyMatrList(Map<String, Object> paraMap);

    int getCmpyMatrListCount(Map<String, Object> paraMap);

    void deleteMatrCmpy(Map<String, Object> vo);

    void deleteMatrProd(Map<String, Object> vo);

    List<Map<String, Object>>  getProdMatrList(Map<String, Object> paraMap);
    int getProdMatrListCount(Map<String, Object> paraMap);

    Map<String, Object> getSafeStk(Map<String, Object> paraMap);

    Map<String, Object> getPursUnit(Map<String, Object> paraMap);
    void appendMatrInfo(Map<String, Object> map);
    void setQrImage(Map<String, Object> rmap);

    int getMadeMatrCount(Map<String, Object> paraMap);

    void embCalendarSave(Map<String, Object> paraMap);

    List<Map<String, Object>> getStockList(Map<String, Object> paraMap);

    List<Map<String, Object>> getMatrIwhList(Map<String, Object> paraMap);
    int getMatrIwhListCount(Map<String, Object> paraMap);

    Object saveMatrStkInfo(Map<String, Object> paraMap);

    List<Map<String, Object>> getProcList(Map<String, Object> paraMap);

    List<Map<String, Object>> getMatrCurrStkList(Map<String, Object> paraMap);
    int getMatrCurrStkListCount(Map<String, Object> paraMap);

    List<Map<String, Object>> getMatrWithCmpyList(Map<String, Object> paraMap);

    int getMatrWithCmpyListCount(Map<String, Object> paraMap);

    List<Map<String, Object>> getAnaMatrList(Map<String, Object> paraMap);

    List<Map<String, Object>> getMatrExcelList(Map<String, Object> paraMap);

    Map<String, Object> getCheckRetnList(Map<String, Object> paraMap);

    List<Map<String, Object>> getindcExcelList(Map<String, Object> paraMap);

    List<Map<String, Object>> getmakeIndcExcelList(Map<String, Object> paraMap);

    List<Map<String, Object>> getComeDateMatrList(Map<String, Object> paraMap);
    int getComeDateMatrListCount(Map<String, Object> paraMap);

    List<Map<String, Object>> getmatrPursUnit(Map<String, Object> paraMap);

    int getmatrPursUnitCount(Map<String, Object> paraMap);

    List<Map<String ,Object>> getSafeStkMatrList(Map<String, Object> paraMap);

    int getSafeStkMatrListCount(Map<String, Object> paraMap);
}
