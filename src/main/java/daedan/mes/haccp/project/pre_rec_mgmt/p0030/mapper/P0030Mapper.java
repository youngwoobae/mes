package daedan.mes.haccp.project.pre_rec_mgmt.p0030.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface P0030Mapper {
    void insertPreLight(Map<String, Object> row);
    void insertPreImprv(Map<String, Object> row);
    void updateLight(Map<String, Object> row);
    Map<String, Object> selectPreImprv(Map<String, Object> stringObjectMap);
    void updatePreImprv(Map<String, Object> stringObjectMap);
    void insertPreImprv2(Map<String, Object> stringObjectMap);

    List<Map<String, Object>> selectColList(Map<String, Object> param);

    void deleteLight(Map<String, Object> param);

    void deletePreCleanImprv(Map<String, Object> param);

    List<Map<String, Object>>  selectInfo(Map<String, Object> param);

    List<Map<String, Object>> selectFinalCheckList(Map<String, Object> param);

    List<Map<String, Object>> selectPlaceList(Map<String, Object> param);

    Map<String, Object> gridPagingCnt(Map<String, Object> param);

    List<Map<String, Object>>  gridPaging(Map<String, Object> param);

    Map<String, Object> selectColInfo(Map<String, Object> param);
}
