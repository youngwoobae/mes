package daedan.mes.haccp.project.pre_rec_mgmt.p0140.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface P0140Mapper {
    List<Map<String, Object>> selectFinalCheckList(Map<String, Object> param);

    Map<String, Object> selectCountRecSeq(Map<String, Object> param);
}
