package daedan.mes.purs.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface PursMapper {
    List<Map<String, Object>> getPursList(Map<String, Object> paraMap);

    int getPursListCount(Map<String, Object> paraMap);

    Map<String, Object> getPursInfo(Map<String, Object> paraMap);

    List<Map<String, Object>> getComboPursCmpy(Map<String, Object> paraMap);

    List<Map<String, Object>> getNeedPursList(Map<String, Object> paraMap);

    int getNeedPursListCount(Map<String, Object> paraMap);

    List<Map<String, Object>> getNeedMatrOrderList(Map<String, Object> paraMap);

    int getNeedMatrOrderListCount(Map<String, Object> paraMap);

    void dropPursInfo(Map<String, Object> paraMap);

    List<Map<String, Object>> getPursReqList(Map<String, Object> paraMap);

    int getPursReqListCount(Map<String, Object> paraMap);

    Map<String, Object> getPursReqMatrInfo(Map<String, Object> paraMap);

    void dropPursMatr(Map<String, Object> matrInfo);

    Map<String, Object> getPursMatrInfo(Map<String, Object> paraMap);

    List<Map<String, Object>> getComboMatrCmpy(Map<String, Object> paraMap);

    String isAbleCnfmPurs(Map<String, Object> paraMap);

    void resetPursCmpy(Map<String, Object> procMap);
    void resetPursCmpyQty(Map<String, Object> procMap);

    List<Map<String, Object>> getPursReqOrdList(Map<String, Object> paraMap);

    int getPursReqOrdListCount(Map<String, Object> paraMap);

    void resetDlvReqDt(Map<String, Object> paraMap);

    List<Map<String, Object>> getPursHstrList(HashMap<String, Object> paraMap);
    int getPursHstrListCount(HashMap<String, Object> paraMap);

    List<Map<String, Object>> getWhList(HashMap<String, Object> paraMap);

    List<Map<String, Object>> getPursMatrList(Map<String, Object> paraMap);

    void initTempPursMatr(Map<String, Object> paraMap);

    String isDeadPurs(HashMap<String, Object> paraMap);

    void resetPursStatusEnd(Map<String, Object> paraMap);
    void resetPursStatusIng(Map<String, Object> paraMap);
    void resetPursStatusIngByRetn(Map<String, Object> paraMap);

    List<Map<String, Object>> getpursInfoList(HashMap<String, Object> paraMap);

    List<Map<String, Object>> getpursInfoMatrList(HashMap<String, Object> paraMap);

    List<Map<String, Object>> getPursReqIndcList(Map<String, Object> paraMap);

    int getPursReqIndcListCount(Map<String, Object> paraMap);

    int getpursInfoListCount(HashMap<String, Object> paraMap);

    int getpursInfoMatrListCount(HashMap<String, Object> paraMap);

    int getPursMatrListCount(Map<String, Object> paraMap);

    List<Map<String, Object>> getPursMatrIwhList(Map<String, Object> paraMap);

    int getPursMatrIwhListCount(Map<String, Object> paraMap);
}
