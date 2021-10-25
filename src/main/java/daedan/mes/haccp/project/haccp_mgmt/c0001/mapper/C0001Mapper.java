package daedan.mes.haccp.project.haccp_mgmt.c0001.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.Map;

@Mapper
@Component
public interface C0001Mapper {

    Map<String,Object> selectFinalCheckList(Map<String, Object> param);
}
