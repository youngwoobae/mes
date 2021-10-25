package daedan.mes.haccp.project.pre_rec_mgmt.p0060.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface P0060Mapper {
    List<Map<String, Object>> selectTrap01KindList(Map<String, Object> param);

    List<Map<String, Object>> selectTrap02KindList(Map<String, Object> param);

    List<Map<String, Object>> selectTrap03KindList(Map<String, Object> param);

    List<Map<String, Object>> selectTrap01List(Map<String, Object> param);

    List<Map<String, Object>> selectTrap02List(Map<String, Object> param);

    List<Map<String, Object>> selectTrap03List(Map<String, Object> param);

    Map<String, Object> selectStdPoint(Map<String, Object> param);

    List<Map<String, Object>> selectSavedTrap01List(Map<String, Object> param);

    List<Map<String, Object>> selectSavedTrap02List(Map<String, Object> param);

    List<Map<String, Object>> selectSavedTrap03List(Map<String, Object> param);

    void insertFpGridContents(Map<String, Object> row);

    void insertImprv(Map<String, Object> row);

    void insertWpGridContents(Map<String, Object> row);

    void insertRGridContents(Map<String, Object> row);

    void updateFpInfo(Map<String, Object> row);

    Map<String, Object> selectImprvOne(Map<String, Object> row);

    void updateImprv(Map<String, Object> row);

    void deleteImprvOne(Map<String, Object> row);

    void updateWpInfo(Map<String, Object> row);

    void updateRInfo(Map<String, Object> row);

    void deleteTrapList(Map<String, Object> param);

    void deleteTrapImprv(Map<String, Object> param);

    List<Map<String, Object>> selectFinalCheckList(Map<String, Object> param);
}
