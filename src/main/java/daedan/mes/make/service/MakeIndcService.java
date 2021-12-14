package daedan.mes.make.service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface MakeIndcService {
    List<Map<String,Object>> getMakeIndcList(Map<String, Object> paraMap);
    int getMakeIndcListCount(Map<String, Object> paraMap);
    Map<String, Object> getMakeIndcInfo(Map<String, Object> paraMap);

    Map<String, Object> getMakeIndcRsltInfo(Map<String, Object> paraMap);

    List<Map<String, Object>> getMakePlanInfo(Map<String, Object> paraMap);

    List<Map<String, Object>> getMakeIndcRsltList(Map<String, Object> paraMap);
    int getMakeIndcRsltListCount(Map<String, Object> paraMap);

    /*작업현황 캘린더에서 드래그&드롭으로 변경된 작업지시 일자 변경*/
    @Transactional
    void resetMakeTermByDragDrop(Map<String, Object> paraMap);

    @Transactional
    Long saveMakeIndcFull(Map<String, Object> procMap);

    @Transactional
    Long saveIndcInfo(Map<String, Object> paraMap);

    Long saveMakeIndc(Map<String, Object> paraMap);
    Float getCurrentProdStock(Map<String, Object> paraMap);

    //MakeIndc saveMakeIndcInfo(Map<String, Object> paraMap);

    void dropMakeIndc(Map<String, Object> paraMap);

    List<Map<String, Object>>  getComboWorkDay(Map<String, Object> paraMap);
    void saveMakeIndcMp(Map<String, Object> paraMap);

    List<Map<String, Object>>  getMakeIndcMpList(Map<String, Object> paraMap);

    int getMakeIndcMpListCount(Map<String, Object> paraMap);

    Map<String, Object> getMakeIndcMpInfo(Map<String, Object> paraMap);

    Map<String,Object> getTimeString(Map<String, Object> paraMap);

    List<Map<String, Object>>  getReqMatrList(Map<String, Object> paraMap);

    int getReqMatrListCount(Map<String, Object> paraMap);

       List<Map<String, Object>> getMakeProcList(Map<String, Object> paraMap);


    /*상품별 제조공정 이외 별도 공정을 추가하는 경우 처리됨*/
    List<Map<String, Object>> getMakeExtraProc(Map<String, Object> paraMap);

    List<Map<String, Object>>  getExportPlanList(Map<String, Object> paraMap);

    int getMakeStatusListCount(Map<String, Object> paraMap);

    List<Map<String, Object>>  getWorkerList(Map<String, Object> paraMap);

    void saveIndcRslt(Map<String, Object> paraMap); //작업결과저장

    List<Map<String, Object>> getMpUsedList(Map<String, Object> paraMap);
    int getMpUsedListCount(Map<String, Object> paraMap);


    List<Map<String, Object>>  getMpUsedDetlList(Map<String, Object> paraMap);
    int getMpUsedDetlListCount(Map<String, Object> paraMap);


    int getMaxMakeCapacityPerDay(Map<String, Object> paraMap);

    @Transactional
    void getMpDropInfo(Map<String, Object> paraMap);

    @Transactional
    void saveMpInfo(Map<String, Object> paraMap);

    @Transactional
    void loadMpByExcel(HashMap<String, Object> paraMap) throws Exception;

    Map<String, Object> getMaxMakeCapacity(Map<String, Object> paraMap);

    @Transactional
    void dropIndcRslt(Map<String, Object> paraMap);

    List<Map<String, Object>> getMakeDailyReportList(Map<String, Object> paraMap);
    int getMakeDailyReportListCount(Map<String, Object> paraMap);
    List<Map<String, Object>> getComboProcList(Map<String, Object> paraMap);
    List<Map<String, Object>> getMakeStatusList(Map<String, Object> paraMap);



    List<Map<String, Object>> getOperProdList(Map<String, Object> paraMap);

    Map<String, Object> getStartProc(Map<String, Object> paraMap);

    Map<String, Object> getIndcInfo(Map<String, Object> paraMap);

    List<Map<String, Object>> getFaultList(Map<String, Object> paraMap);


    List<Map<String, Object>>  getComboProdProcList(Map<String, Object> paraMap);



    void saveReqMatr(Map<String, Object> paraMap);

    Map<String, Object>  getMakeMainProc(Map<String, Object> paraMap);

    List<Map<String, Object>> getMakeStatList(Map<String, Object> paraMap);
    int getMakeStatListCount(Map<String, Object> paraMap);

    List<Map<String, Object>> getMakeStatMoreList(Map<String, Object> paraMap);
    int getMakeStatMoreListCount(Map<String, Object> paraMap);


    List<Map<String, Object>> getMoniterItemList(Map<String, Object> paraMap);

    List<Map<String, Object>> getManPowerSummaryList(Map<String, Object> paraMap);
    int getManPowerSummaryListCount(Map<String, Object> paraMap);
    List<Map<String, Object>> getManPowerList(Map<String, Object> paraMap);
    int getManPowerListCount(Map<String, Object> paraMap);

    List<Map<String, Object>> getWgtchkSummaryList(Map<String, Object> paraMap);
    int getWgtchkSummaryListCount(Map<String, Object> paraMap);

    List<Map<String, Object>> getWgtchkDiaryList(Map<String, Object> paraMap);
    int getWgtchkDiaryListCount(Map<String, Object> paraMap);

    void autoSaveIndcMp(Map<String, Object> paraMap);

    List<Map<String, Object>> getMakeStatusReport(Map<String, Object> paraMap);
    int getMakeStatusReportCount(Map<String, Object> paraMap);

    List<Map<String, Object>>  makeIndcProc(Map<String, Object> paraMap);

    void saveMakeIndcFullByPlan(Map<String, Object> paraMap);

    List<Map<String, Object>> getMakePlanList(Map<String, Object> paraMap);

    List<Map<String, Object>> getMakePlanProdList(Map<String, Object> paraMap);
    int getMakePlanProdListCount(Map<String, Object> paraMap);
    List<Map<String, Object>> getComboOrdrCmpyList(Map<String, Object> paraMap);

    List<Map<String, Object>> getMakeIndcPrintList(Map<String, Object> paraMap);

    List<Map<String, Object>> getProcCtntList(Map<String, Object> paraMap);

    List<Map<String, Object>> getProdIndcNo(Map<String, Object> paraMap);

    List<Map<String, Object>> getMatchIndcList(Map<String, Object> paraMap);

    void resetIndcSts(Map<String, Object> paraMap);

    List<Map<String,Object>> getNeedProdBomList(Map<String, Object> paraMap);

    List<Map<String, Object>> getIndcListByProc(Map<String, Object> paraMap);

    int getFaultListCount(Map<String, Object> paraMap);

    List<Map<String, Object>> getIndcSaltList(Map<String, Object> paraMap);

    int getIndcSaltListCount(Map<String, Object> paraMap);

    void saveIndcSaltList(Map<String, Object> paraMap);

    List<Map<String, Object>> getMakeIndcBfPrintList(Map<String, Object> paraMap);


    void saveIndcPrintText(Map<String, Object> paraMap);

    void planSave(Map<String, Object> paraMap);

    @Transactional
    void saveMakeIndcProc(Map<String, Object> paraMap);

    void saveMakePlan(Map<String, Object> paraMap);

    List<Map<String, Object>> getProductionPlan(Map<String, Object> paraMap);

    Map<String, Object> getIndcPlanInfo(Map<String, Object> paraMap);

    /*간단 생산지시용*/
    void saveIndcInfoBrief(Map<String, Object> paraMap);
    
    /*간단 생산지시 목록*/
    List<Map<String, Object>> getIndcList(Map<String, Object> paraMap);

    int getIndcListCount(Map<String, Object> paraMap);
}
