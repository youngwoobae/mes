package daedan.mes.cmpy.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface CmpyMapper {
    List<Map<String,Object>> getCmpyList(Map<String,Object> map);
    int getCmpyListCount(HashMap<String, Object> map);

    List<Map<String, Object>> getCmpyOrdrList(Map<String, Object> map);
    int getCmpyOrdrListCount(HashMap<String, Object> map);

    List<Map<String, Object>> getCmpyPursList(Map<String, Object> map);
    int getCmpyPursListCount(HashMap<String, Object> map);

    void setUnusedCmpy(Map<String, Object> cmpyMap);
    Map<String, Object> getCmpyInfo(Map<String, Object> paraMap);

    List<Map<String, Object>> getCmpyDlvPlcList(Map<String, Object> paraMap);

    int getCmpyDlvPlcListCount(Map<String, Object> paraMap);

    void deleteCmpyDlvPlc(Map<String, Object> passMap);

    void initBasePlcYn(Map<String, Object> passMap);

    void appendCmpyInfo(Map<String, Object> map);

    Map<String, Object> getCmpyPursInfo(Map<String, Object> paraMap);
    List<Map<String, Object>> getCmpyPursMatrList(Map<String, Object> paraMap);

    List<Map<String, Object>> getCmpyIpList(Map<String, Object> paraMap);

    int getCmpyIpListCount(HashMap<String, Object> map);

    List<Map<String, Object>> getEqOption(Map<String, Object> paraMap);
    List<Map<String, Object>> getSysOption(Map<String, Object> paraMap);

    List<Map<String, Object>> getCmpyExcelList(Map<String, Object> paraMap);

    List<Map<String, Object>> getMatrCmpyList(Map<String, Object> paraMap);
}
