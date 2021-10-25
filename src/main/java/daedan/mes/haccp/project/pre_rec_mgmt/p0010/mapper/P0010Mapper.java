package daedan.mes.haccp.project.pre_rec_mgmt.p0010.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface P0010Mapper {
    Map<String, Object> gridPagingCnt(Map<String, Object> param);
    List<Map<String,Object>> gridPaging(Map<String, Object> param);
    List<Map<String,Object>>  selectSavedCheckList(Map<String, Object> param);
    List<Map<String, Object>> selectPreCleanImprv(Map<String, Object> param);
    void insertPreClean(Map<String, Object> row);
    void insertPreCleanImprv(Map<String, Object> row);
    Map<String, Object> selectPreCleanImprvOne(Map<String, Object> row);

    void updatePreClean(Map<String, Object> row);

    void deletePreCleanImprvOne(Map<String, Object> row);

    void updatePreCleanImprv(Map<String, Object> row);

    void deletePreClean(Map<String, Object> param);

    void deletePreCleanImprv(Map<String, Object> param);

    List<Map<String, Object>> selectFinalCheckList(Map<String, Object> param);

    List<Map<String, Object>> selectCheckList(Map<String, Object> param);
}
