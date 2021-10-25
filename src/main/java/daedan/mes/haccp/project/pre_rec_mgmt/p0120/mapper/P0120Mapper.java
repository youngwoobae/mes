package daedan.mes.haccp.project.pre_rec_mgmt.p0120.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface P0120Mapper {
    Map<String, Object> gridPagingCnt(Map<String, Object> param);

    List<Map<String, Object>> gridPaging(Map<String, Object> param);

    Map<String, Object> selectCountRecSeq(Map<String, Object> param);

    List<Map<String, Object>> selectFinalCheckList(Map<String, Object> param);
}
