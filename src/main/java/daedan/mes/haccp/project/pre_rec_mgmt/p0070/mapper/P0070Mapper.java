package daedan.mes.haccp.project.pre_rec_mgmt.p0070.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface P0070Mapper {
    List<Map<String, Object>> selectFinalCheckList(Map<String, Object> param);
}
