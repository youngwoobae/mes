package daedan.mes.haccp.project.pre_rec_mgmt.p0021.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
@Mapper
@Component
public interface P0021Mapper {
    Map<String, Object> gridPagingCnt(Map<String, Object> param);

    List<Map<String, Object>> gridPaging(Map<String, Object> param);

    List<Map<String, Object>>  selectUserList(Map<String, Object> param);

    Map<String, Object> selectUserListCnt(Map<String, Object> param);

    Map<String, Object> selectHealthCardInfo(Map<String, Object> param);

    Map<String, Object> selectHealthCardNoCnt(Map<String, Object> param);

    Map<String, Object> selectLcnsInfoAll(Map<String, Object> param);

    void insertHealthCard(Map<String, Object> param);

    Map<String, Object> selectInfoFromHlthCard(Map<String, Object> param);

    void insertFileInfo(Map<String, Object> param);

    Map<String, Object> getLatestFileKey(Map<String, Object> param);

    void updateFileInfoIntoRec(Map<String, Object> param);

    void updateFileInfo(Map<String, Object> param);

    List<Map<String, Object>> selectFinalCheckList(Map<String, Object> param);

    void updateHealthCard(Map<String, Object> param);

    Map<String, Object> selectFileInfo(Map<String, Object> recFileInfo);

    void deleteHealthCard(Map<String, Object> param);
}
