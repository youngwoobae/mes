package daedan.mes.haccp.project.pre_rec_mgmt.p0050.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface P0050Mapper {
    void insertPreDegree(Map<String, Object> row);
    void insertPreImprv(Map<String, Object> row);
    void updatePreDegree(Map<String, Object> row);
    Map<String, Object> selectPreImprv(Map<String, Object> stringObjectMap);
    void updatePreImprv(Map<String, Object> stringObjectMap);
    void insertPreImprv2(Map<String, Object> stringObjectMap);

    void deletePreDegree(Map<String, Object> param);
    void deletePreImprv(Map<String, Object> param);

    List<Map<String, Object>> selectColList(Map<String, Object> param);

    Map<String, Object> gridPagingCnt(Map<String, Object> param);

    List<Map<String, Object>>  gridPaging(Map<String, Object> param);

    List<Map<String, Object>>  selectInfo(Map<String, Object> param);

    List<Map<String, Object>> electPlaceList(Map<String, Object> param);

    List<Map<String, Object>> selectFinalCheckList(Map<String, Object> param);
}
