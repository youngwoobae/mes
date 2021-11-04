package daedan.mes.io.service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface IoService {
    List<Map<String, Object>> getComboWhList(Map<String, Object> paraMap);

    /*설정된 자재가 존재하는 창고추출*/
    List<Map<String, Object>> getComboMatrWhList(Map<String, Object> paraMap);

    List<Map<String,Object>> getWhList(Map<String, Object> paraMap);
    int getWhListCount(Map<String, Object> paraMap);

    Map<String, Object> getWhInfo(Map<String, Object> paraMap);

    @Transactional
    void saveWhInfo(Map<String, Object> paraMap);

    @Transactional
    void dropWhInfo(Map<String, Object> paraMap);

    List<Map<String, Object>>  getWaitMatrIwhList(Map<String, Object> paraMap);
    int getWaitMatrIwhListCount(Map<String, Object> paraMap);

    List<Map<String, Object>> getMatrIwhList(Map<String, Object> paraMap);
    int getMatrIwhListCount(Map<String, Object> paraMap);

    Map<String, Object> getMatrIwhInfo(Map<String, Object> paraMap);

    /*입고반품처리*/
//    @Transactional
//    void retnMatrStk(Map<String, Object> paraMap);

    @Transactional
    void dropMatrIwh(Map<String, Object> paraMap);

    String getCnfmAbleIwh(Map<String, Object> paraMap);

    @Transactional
    void cnfmIwh(Map<String, Object> paraMap);

    List<Map<String, Object>>  getMatrStkList(Map<String, Object> paraMap);
    int getMatrStkListCount(Map<String, Object> paraMap);

    List<Map<String, Object>>  getMatrStkIwhList(Map<String, Object> paraMap);
    int getMatrStkIwhListCount(Map<String, Object> paraMap);

    List<Map<String, Object>>  getMatrStkOwhList(Map<String, Object> paraMap);
    int getMatrStkOwhListCount(Map<String, Object> paraMap);

    List<Map<String, Object>> getReqOwhList(Map<String, Object> paraMap);
    int getReqOwhListCount(Map<String, Object> paraMap);

    Map<String, Object> matrOwhInfo(Map<String, Object> paraMap);

    void saveMatrOwh(Map<String, Object> paraMap);

    List<Map<String, Object>> getComboOwhReqDtList(Map<String, Object> paraMap);

    @Transactional
    void dropMatrOwh(Map<String, Object> paraMap);

    List<Map<String, Object>> getReqMatrIwhList(Map<String, Object> paraMap);
    int getReqMatrIwhListCount(Map<String, Object> paraMap);

    List<Map<String, Object>> getReqMatrIwhSumList(Map<String, Object> paraMap);
    int getReqMatrIwhSumListCount(Map<String, Object> paraMap);

    List<Map<String, Object>> getReqMatrOwhList(Map<String, Object> paraMap);
    int getReqMatrOwhListCount(Map<String, Object> paraMap);

    List<Map<String, Object>> getReqMatrOwhSumList(Map<String, Object> paraMap);
    int getReqMatrOwhSumListCount(Map<String, Object> paraMap);
    Map<String, Object> getReqMatrIwhInfo(Map<String, Object> paraMap);

    List<Map<String, Object>> getComboPursCmpy(Map<String, Object> paraMap);

    @Transactional
    void saveMatrStk(Map<String, Object> passMap);

    /*출고대상 자재 적재위치 목록*/
    List<Map<String,Object>> getMatrPosList(Map<String, Object> paraMap);
    int getMatrPosListCount(Map<String, Object> paraMap);

    void extrProdStk(Map<String, Object> paraMap);

    void setProdOwh(Map<String, Object> paraMap);

    List<Map<String, Object>> getProdIwhList(Map<String, Object> paraMap);
    int getProdIwhListCount(Map<String, Object> paraMap);

    List<Map<String, Object>> getReqProdIwhSumList(Map<String, Object> paraMap);
    int getReqProdIwhSumListCount(Map<String, Object> paraMap);

    void saveProdIwh(Map<String, Object> paraMap);

    Map<String, Object> getProdIwhInfo(Map<String, Object> paraMap);

    List<Map<String, Object>> getReqProdOwhSumList(Map<String, Object> paraMap);
    int getReqProdOwhSumListCount(Map<String, Object> paraMap);

    List<Map<String, Object>> getReqProdOwhList(Map<String, Object> paraMap);
    int getReqProdOwhListCount(Map<String, Object> paraMap);

    List<Map<String, Object>> getMakeIndcList(Map<String, Object> paraMap);
    int getMakeIndcListCount(Map<String, Object> paraMap);

    void saveProdStkQty(Map<String, Object> paraMap);

    void saveKioMatrIwh(Map<String, Object> paraMap);

    void saveKioMatrStk(Map<String, Object> paraMap);

    List<Map<String, Object>> getKioMatrIwhList(Map<String, Object> paraMap);
    int getKioMatrIwhListCount(Map<String, Object> paraMap);


    //출하계획
    List<Map<String, Object>> getProdOutPlanList(Map<String, Object> paraMap);

    Map<String, Object> getMatrPosInfo(Map<String, Object> paraMap);

    void initTempMatrPos(Map<String, Object> paraMap);

    void saveMatrStkPosList(Map<String, Object> paraMap);
    
    List<Map<String, Object>> getMatrStkPosList(Map<String, Object> paraMap);

    int getMatrStkPosListCount(Map<String, Object> paraMap);

    List<Map<String, Object>> getMatchPosList(Map<String, Object> paraMap);

    int getMatchPosListCount(Map<String, Object> paraMap);

    void renewalMatrPos(Map<String, Object> paraMap);

    void renewalMatrIwh(Map<String, Object> paraMap);

    void renewalPursMatr(Map<String, Object> paraMap);

    void outMatrPos(Map<String, Object> paraMap);

    void updateMatrOwhStk(Map<String, Object> paraMap);

    void updateMatrOwh(Map<String, Object> paraMap);

    void updateMakeIndcMatr(Map<String, Object> paraMap);

    List<Map<String, Object>> getMatrStkImpoList(Map<String, Object> paraMap);
    int getMatrStkImpoListCount(Map<String, Object> paraMap);

    Map<String, Object> getExportPlanInfo(Map<String, Object> paraMap);
    List<Map<String, Object>>  getExportProdList(Map<String, Object> paraMap);

    Map<String, Object> getExportExecInfo(Map<String, Object> paraMap);

    void saveExportProdInfo(Map<String, Object> paraMap);

    List<Map<String, Object>> getForImportProdList(Map<String, Object> paraMap);
    int getForImportProdListCount(Map<String, Object> paraMap);

    void tabletMatrStk(Map<String, Object> paraMap);

    List<Map<String, Object>> getTabletReqMatrIwhList(Map<String, Object> paraMap);
    int getTabletReqMatrIwhListCount(Map<String, Object> paraMap);

    void tabletMatrIwh(Map<String, Object> paraMap);

    List<Map<String, Object>> getTabletWhNoAndMatrUnit(Map<String, Object> paraMap);

    int getMatrOwhHistListCount(Map<String, Object> paraMap);

    void saveProdIwhFromIndc(Map<String, Object> passMap);

    List<Map<String, Object>> OwhWhStkList(Map<String, Object> paraMap);

    void owhMatrList(Map<String, Object> paraMap);

     //구매 과정없이 자재 입고처리하는 경우 : 간단 MES용
    List<Map<String,Object>> getMatrForIwhList(Map<String,Object> paraMap);
    int getMatrForIwhListCount(Map<String,Object> paraMap);

    // 테블릿 자재 입고
    void saveMatrIwhList(Map<String, Object> paraMap);

    //과정없이 자재 출고처리하는 경우 : 간단 MES용
    List<Map<String,Object>> getMatrForOwhList(Map<String, Object> paraMap);
    int getMatrForOwhListCount(Map<String, Object> paraMap);

    //테블릿 자재 출고
    void saveMatrOwhList(Map<String, Object> paraMap);


    List<Map<String,Object>> getProdForIwhList(Map<String, Object> paraMap);
    int getProdForIwhListCount(Map<String, Object> paraMap);

    void saveProdIwhList(Map<String, Object> paraMap);

    List<Map<String,Object>> getProdForOwhList(Map<String, Object> paraMap);
    int getProdForOwhListCount(Map<String, Object> paraMap);

    void saveProdOwhList(Map<String, Object> paraMap);
    //자재 입고 현황
    List<Map<String,Object>> stsMatrIwhList(HashMap<String, Object> paraMap);
    int stsMatrIwhListCount(HashMap<String, Object> paraMap);

    //자재 출고 현황
    List<Map<String,Object>>stsMatrOwhList(HashMap<String, Object> paraMap);
    int stsMatrOwhListCount(HashMap<String, Object> paraMap);

    //제품 입고 현황
    List<Map<String,Object>> stsProdIwhList(HashMap<String, Object> paraMap);
    int stsProdIwhListCount(HashMap<String, Object> paraMap);

    //제품 출고 현황
    List<Map<String,Object>> stsProdOwhList(HashMap<String, Object> paraMap);
    int stsProdOwhListCount(HashMap<String, Object> paraMap);

    List<Map<String,Object>> getMatrOwhWhNm(HashMap<String, Object> paraMap);


    List<Map<String, Object>> getMatrOwhList(HashMap<String, Object> paraMap);

    int getMatrOwhListCount(HashMap<String, Object> paraMap);

    List<Map<String,Object>>  getProdOwHstrList(Map<String, Object> paraMap);
    int getProdOwHstrListCount(Map<String, Object> paraMap);

    Map<String,Object> getOwhInfo(Map<String, Object> paraMap);

    void dropOwhInfo(Map<String, Object> paraMap);

    List<Map<String,Object>> matrWhNm(HashMap<String, Object> paraMap);

    int matrWhNmCount(HashMap<String, Object> paraMap);

    List<Map<String,Object>> getMatrWhStkList(Map<String, Object> paraMap);

    int getMatrWhStkListCount(Map<String, Object> paraMap);

    List<Map<String,Object>> getIwhMatrList(HashMap<String, Object> paraMap);

    int getIwhMatrListCount(HashMap<String, Object> paraMap);

    List<Map<String,Object>> prodWhNm(HashMap<String, Object> paraMap);

    int prodWhNmCount(HashMap<String, Object> paraMap);

    List<Map<String,Object>> getProdWhStkList(Map<String, Object> paraMap);

    int getProdWhStkListCount(Map<String, Object> paraMap);

    List<Map<String,Object>> getIwhProdList(HashMap<String, Object> paraMap);

    int getIwhProdListCount(HashMap<String, Object> paraMap);

    List<Map<String,Object>> getOwhProdList(HashMap<String, Object> paraMap);

    int getOwhProdListCount(HashMap<String, Object> paraMap);

    List<Map<String,Object>> getProdIwHstrList(Map<String, Object> paraMap);

    int getProdIwHstrListCount(Map<String, Object> paraMap);

    List<Map<String,Object>> getMatrIwHstrList(Map<String, Object> paraMap);

    int getMatrIwHstrListCount(Map<String, Object> paraMap);

    List<Map<String,Object>> getMatrOwHstrList(Map<String, Object> paraMap);

    int getMatrOwHstrListCount(Map<String, Object> paraMap);

    List<Map<String,Object>>  getDaedongReqProdIwhSumList(Map<String, Object> paraMap);

    int getDaedongReqProdIwhSumListCount(Map<String, Object> paraMap);

    void getOwhHstr(Map<String, Object> paraMap);

    void dropProdIwh(Map<String, Object> paraMap);

    Map<String,Object> getPursMatrInfo(Map<String, Object> paraMap);

    List<Map<String,Object>> getIwhHsrt(Map<String, Object> paraMap);
    int getIwhHsrtCount(Map<String, Object> paraMap);

    List<Map<String, Object>> comboEmbIwhWhInfo(Map<String, Object> paraMap);

    List<Map<String, Object>> getMatrOwhHistList(Map<String, Object> paraMap);

    List<Map<String, Object>> getOwhMatrList(Map<String, Object> paraMap);

    int getOwhMatrListCount(Map<String, Object> paraMap);

    void changeStkData(Map<String, Object> paraMap);

    void changeTotalStkData(Map<String, Object> paraMap);

    void dropStkData(Map<String, Object> paraMap);

    void dropTotalStkData(Map<String, Object> paraMap);
}
