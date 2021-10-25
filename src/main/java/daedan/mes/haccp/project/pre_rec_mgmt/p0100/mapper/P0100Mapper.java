package daedan.mes.haccp.project.pre_rec_mgmt.p0100.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface P0100Mapper {
    Map<String, Object> gridPagingCnt(Map<String, Object> param);

    List<Map<String, Object>> gridPaging(Map<String, Object> param);

    List<Map<String, Object>> selectColList(Map<String, Object> param);

    Map<String, Object> selectPreImprvInfo(Map<java.lang.String,java.lang.Object> param);

    Map<String, Object> selectCountRecSeq(Map<String, Object> param);

    List<Map<String, Object>> selectFinalCheckList(Map<String, Object> param);

    void deletePreImprv(Map<String, Object> param);

    void updatePreImprv(Map<String, Object> param);

    void insertPreImprv(Map<String, Object> param);
}
