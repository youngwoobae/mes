package daedan.mes.purs.service;

import daedan.mes.purs.domain.PursInfo;
import daedan.mes.purs.domain.PursMatr;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface PursService {
    void initTempPursMatr(Map<String, Object> paraMap);

    List<Map<String, Object>> getPursList(Map<String, Object> paraMap);
    
    int getPursListCount(Map<String, Object> paraMap);
    Map<String, Object> getPursInfo(Map<String, Object> paraMap);

    /*구매자재 다중처리*/
    @Transactional
    void savePursMatrList(Map<String, Object> paraMap);

    void pursMatrSave(HashMap<String, Object> paraMap);
    void savePursMatr(Map<String, Object> paraMap);

    void resetPursCmpyQtyByList(Map<String, Object> paraMap);

    List<Map<String, Object>> getNeedPursList(Map<String, Object> paraMap);
    int getNeedPursListCount(Map<String, Object> paraMap);
    List<Map<String, Object>>  getNeedMatrOrderList(Map<String, Object> paraMap);
    int getNeedMatrOrderListCount(Map<String, Object> paraMap);

    List<Map<String, Object>> getComboPursCmpy(Map<String, Object> paraMap);
    Map<String, Object> savePursInfo(Map<String, Object> paraMap);
    Map<String, Object>  getPursMatrInfo(Map<String, Object> paraMap);
    Map<String, Object> savePursInfoByVo(PursInfo vo);
    void dropPursInfo(Map<String, Object> paraMap);

    void savePursMatrByVo(PursMatr pmvo);

    List<Map<String, Object>>  getPursReqList(Map<String, Object> paraMap);
    int  getPursReqListCount(Map<String, Object> paraMap);

    Map<String, Object> getPursReqMatrInfo(Map<String, Object> paraMap);

    List<Map<String, Object>> getPursReqOrdList(Map<String, Object> paraMap);
    int getPursReqOrdListCount(Map<String, Object> paraMap);

    void savePursReqMatr(HashMap<String, Object> paraMap);
    void dropPursReqMatr(HashMap<String, Object> paraMap);

    List<Map<String, Object>> getComboMatrCmpy(Map<String, Object> paraMap);

    void resetPursStatusEnd(Map<String, Object> paraMap);

    String isAbleCnfmPurs(Map<String, Object> paraMap);

    void resetPursCmpyByList(Map<String, Object> paraMap);

    List<Map<String,Object>> getPursHstrList(HashMap<String, Object> paraMap);

    int getPursHstrListCount(HashMap<String, Object> paraMap);

    List<String> getWhList(HashMap<String, Object> paraMap);

    List<Map<String,Object>> getPursMatrList(Map<String, Object> paraMap);

    int getPursMatrListCount(Map<String, Object> paraMap);

    void savePursMatrTemp(HashMap<String, Object> paraMap);

    void resetPursStatusIng(Map<String, Object> paraMap);

    List<Map<String,Object>> pursInfoList(HashMap<String, Object> paraMap);

    List<Map<String,Object>>  pursInfoMatrList(HashMap<String, Object> paraMap);


    List<Map<String,Object>>  getPursReqIndcList(Map<String, Object> paraMap);

    int getPursReqIndcListCount(Map<String, Object> paraMap);

    int pursInfoListCount(HashMap<String, Object> paraMap);

    int pursInfoMatrListCount(HashMap<String, Object> paraMap);

    void resetPursStatusIngByRetn(Map<String, Object> paraMap);

    List<Map<String,Object>> getPursMatrIwhList(Map<String, Object> paraMap);
    int getPursMatrIwhListCount(Map<String, Object> paraMap);

    void dropPursMatrList(Map<String, Object> paraMap);
}
