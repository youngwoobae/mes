package daedan.mes.haccp.project.haccp_mgmt.c0030.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface C0030Mapper {
    List<Map<String,Object>> getCcpDteList(Map<String, Object> param);

    List<Map<String,Object>> mdTestGrid(Map<String, Object> param);

    List<Map<String,Object>> prodGrid(Map<String, Object> param);

    Map<String,Object> getProdBreakCnt(Map<String, Object> param);

    Map<String,Object> getMdTestDataImprv(Map<String, Object> param);

    void insertMdProdImprv(Map<String, Object> param);

    Map<String, Object> getMdProdTotCnt(Map<String, Object> param);

    void updateRecDataCnt(Map<String, Object> param);

    List<Map<String,Object>> recImprvChk(Map<String, Object> param);

    List<Map<String,Object>> equipImprvMessage(Map<String, Object> param);

    List<Map<String, Object>> selectFinalCheckList(Map<String, Object> param);

    List<Map<String, Object>> selectFinalCheckListImprv(Map<String, Object> param);
}
