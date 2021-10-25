package daedan.mes.haccp.project.haccp_mgmt.c0020.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface C0020Mapper {
    List<Map<String,Object>> getCcpCleRead(Map<String, Object> param);

    List<Map<String,Object>> getCcpCleList(Map<String, Object> param);

    List<Map<String,Object>> recImprvChk(Map<String, Object> param);

    List<Map<String,Object>> equipImprvMessage(Map<String, Object> param);

    List<Map<String, Object>> selectFinalCheckList(Map<String, Object> param);
}
