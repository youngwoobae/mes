package daedan.mes.haccp.project.admin.alert_period.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface ApMgmtMapper {
    Map<String, Object> gridPagingCnt(Map<String, Object> param);

    List<Map<String, Object>> gridPaging(Map<String, Object> param);

    Map<String, Object> getInfo(Map<String, Object> param);

    Map<String, Object> checkDuplicate(Map<String, Object> param);

    Map<String, Object> getRefCommCode(Map<String, Object> param);

    void insertData(Map<String, Object> param);

    void updateData(Map<String, Object> param);

    void deleteData(Map<String, Object> param);

    List<Map<String, Object>> selectLcodeList(Map<String, Object> param);

    List<Map<String, Object>> selectItemList(Map<String, Object> param);
}
