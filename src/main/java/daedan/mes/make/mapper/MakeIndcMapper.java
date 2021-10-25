package daedan.mes.make.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface MakeIndcMapper {
    List<Map<String,Object>> getMakeIndcList(Map<String,Object> paraMap);
    int getMakeIndcListCount(Map<String,Object> paraMap);
    Map<String, Object> getMakeIndcInfo(Map<String, Object> paraMap);

    Map<String, Object> getMakeIndcRsltInfo(Map<String, Object> paraMap);
    List<Map<String,Object>> getMakeIndcRsltList(Map<String,Object> paraMap);
    List<Map<String,Object>> indcMatr(Map<String,Object> paraMap);
    int getMakeIndcRsltListCount(Map<String,Object> paraMap);

    void dropMakeIndc(Map<String, Object> paraMap);
    List<Map<String, Object>> getComboWorkDay(Map<String, Object> paraMap);

    List<Map<String, Object>>  getMakeIndcMpList(Map<String, Object> paraMap);

    int getMakeIndcMpListCount(Map<String, Object> paraMap);

    Map<String, Object> getMakeIndcMpInfo(Map<String, Object> paraMap);

    Map<String, Object> getTimeString(Map<String, Object> paraMap);

    List<Map<String, Object>> getReqMatrList(Map<String, Object> paraMap);

    int getReqMatrListCount(Map<String, Object> paraMap);

    List<Map<String, Object>> getExportPlanList(Map<String, Object> paraMap);

    List<Map<String, Object>> getMakeDailyReportList(Map<String, Object> paraMap);

    int getMakeDailyReportListCount(Map<String, Object> paraMap);

    List<Map<String, Object>> getManPowerList(Map<String, Object> paraMap);
    int getManPowerListCount(Map<String, Object> paraMap);


    List<Map<String, Object>> getWorkerList(Map<String, Object> paraMap);


    List<Map<String, Object>> getMpUsedList(Map<String, Object> paraMap);
    int getMpUsedListCount(Map<String, Object> paraMap);

    List<Map<String, Object>> getMpUsedDetlList(Map<String, Object> paraMap);
    int getMpUsedDetlListCount(Map<String, Object> paraMap);

    void getMpDropInfo(Map<String, Object> paraMap);


    Map<String, Object> getMpInfo(Map<String, Object> paraMap);
    Map<String, Object> getMaxMakeCapacity(Map<String, Object> paraMap);

    int getMaxMakeCapacityPerDay(Map<String, Object> paraMap);

    void dropIndcRslt(Map<String, Object> paraMap);

    List<Map<String, Object>> chkReqPurs(Map<String, Object> paraMap);

    List<Map<String, Object>> getComboProdProcList(Map<String, Object> paraMap);

    List<Map<String, Object>> getProdProcList(Map<String, Object> paraMap);

    Long getMakeIndcStatus(Map<String, Object> procMap);

    List<Map<String, Object>> getComboProcList(Map<String, Object> paraMap);

    List<Map<String, Object>> getOperProdList(Map<String, Object> paraMap);

    List<Map<String, Object>> getMakeStatusList(Map<String, Object> paraMap);
    int getMakeStatusListCount(Map<String, Object> paraMap);

    void resetMakeDt(Map<String, Object> paraMap);

    void resetMakeTermByDragDrop(Map<String, Object> paraMap);

    Map<String, Object> getStartProc(Map<String, Object> paraMap);

    List<Map<String, Object>> getFaultList(Map<String, Object> paraMap);

    List<Map<String, Object>>  getMakePlanList(Map<String, Object> paraMap);

    List<Map<String, Object>> getMakePlanInfo(Map<String, Object> paraMap);

    void initPursMatr(Map<String, Object> paraMap);

    void initPursInfo(Map<String, Object> paraMap);

    void initTempMatrPos(Map<String, Object> paraMap);

    int getPosCount(Map<String, Object> paraMap);

    Map<String, Object> getMakeMainProc(Map<String, Object> paraMap);
    List<Map<String, Object>> getMakeExtraProc(Map<String, Object> paraMap);

    List<Map<String, Object>> getMakeStatList(Map<String, Object> paraMap);
    int getMakeStatListCount(Map<String, Object> paraMap);

    List<Map<String, Object>> getMakeProcList(Map<String, Object> paraMap);

    List<Map<String, Object>> getMoniterItemList(Map<String, Object> paraMap);

    List<Map<String, Object>> getManPowerSummaryList(Map<String, Object> paraMap);

    int getManPowerSummaryListCount(Map<String, Object> paraMap);

    
    List<Map<String, Object>> getMakeIndcMp(Map<String, Object> paraMap);

    List<Map<String, Object>> getWgtchkSummaryList(Map<String, Object> paraMap);

    int getWgtchkSummaryListCount(Map<String, Object> paraMap);


    List<Map<String, Object>> getWgtchkDiaryList(Map<String, Object> paraMap);

    int getWgtchkDiaryListCount(Map<String, Object> paraMap);

    List<Map<String, Object>> getForMpMakerIndcList(Map<String, Object> paraMap);

    int getIndcMpCount(Map<String, Object> paraMap);

    Float getCurrentProdStock(Map<String, Object> stkmap);

    List<Map<String, Object>> getMakeStatusReport(Map<String, Object> paraMap);
    int getMakeStatusReportCount(Map<String, Object> paraMap);

    List<Map<String, Object>> getmakeIndcProc(Map<String, Object> paraMap);

    List<Map<String, Object>> getOrdProdInfo(Map<String, Object> prodMap);


    int getMakePlanInfoCount(Map<String, Object> paraMap);

    List<Map<String, Object>> getMakePlanProdList(Map<String, Object> paraMap);
    int getMakePlanProdListCount(Map<String, Object> paraMap);

    List<Map<String, Object>> getComboOrdrCmpyList(Map<String, Object> paraMap);

    List<Map<String, Object>> getMakeIndcPrintList(Map<String, Object> paraMap);

    List<Map<String, Object>> getProcCtntList(Map<String, Object> paraMap);

    List<Map<String, Object>> getProdIndcNo(Map<String, Object> paraMap);

    List<Map<String, Object>> getStkMatrList(Map<String, Object> paraMap);

    Map<String, Object> getMatchIndcList(Map<String, Object> passMap);

    void resetIndcSts(Map<String, Object> paraMap);

    List<Map<String, Object>> getNeedProdBomList(Map<String, Object> paraMap);

    List<Map<String, Object>> getProdNo(String erpProdNm);

    void updateMatrSts(Map<String, Object> paraMap);

    void updateIndcRsltByOper(Map<String, Object> rmap);

    List<Map<String, Object>> getMakeIndcRsltListByTablet(Map<String, Object> rmap);

    List<Map<String, Object>> getIndcListByProc(Map<String, Object> paraMap);

    void dropMatrOwh(Long paraMap);

    List<Map<String, Object>> dropSumMatrOwh(Long indcNo);

    List<Map<String, Object>> chkStkByIndc(Long indcNo);

    int getFaultListCount(Map<String, Object> paraMap);

    List<Map<String, Object>> getHdfdNeedProdBomList(Map<String, Object> paraMap);

    Float getIndcRsltHstrSumQty(Map<String, Object> hmap);

    List<Map<String, Object>> getIndcSaltList(Map<String, Object> paraMap);

    int getIndcSaltListCount(Map<String, Object> paraMap);

    Map<String, Object> getIndcListBySalt(Map<String, Object> paraMap);

    void dropMakeIndcMatr(Long indcNo);

    void dropPursMatr(Long pursNo);

    List<Map<String, Object>> getDropMatrIwhList(Long pursNo);

    void dropIwhList(Long pursNo);

    List<Map<String, Object>> getMakeIndcBfPrintList(Map<String, Object> el);

    List<Map<String, Object>> getSfNeedProdBomList(Map<String, Object> paraMap);

    Map<String, Object> findByCustNoAndOrdNo(Map<String,Object> paraMap);
}

