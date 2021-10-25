package daedan.mes.haccp.project.pre_rec_mgmt.p0151.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface P0151Mapper {
    List<Map<String, Object>> selectColList(Map<String, Object> param);

    List<Map<String, Object>> selectSavedCheckList(Map<String, Object> param);

    void insertPreVt(Map<String, Object> row);

    void insertPreImprv(Map<String, Object> row);

    void deletePreVt(Map<String, Object> row);

    void deletePreImprv(Map<String, Object> row);

    void updatePreVt(Map<String, Object> row);

    void updatePreImprv(Map<String, Object> row);

    void deletePreVtAll(Map<String, Object> param);

    void deletePreImprvAll(Map<String, Object> param);

    List<Map<String, Object>> selectFinalCheckList(Map<String, Object> param);
}
