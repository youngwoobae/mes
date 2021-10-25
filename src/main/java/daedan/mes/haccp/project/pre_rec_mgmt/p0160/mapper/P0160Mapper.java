package daedan.mes.haccp.project.pre_rec_mgmt.p0160.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface P0160Mapper {
    List<Map<String, Object>> selectFinalCheckList(Map<String, Object> param);

    Map<String, Object> selectCountRecSeq(Map<String, Object> param);

    Map<String, Object> gridPagingCnt(Map<String, Object> param);

    List<Map<String, Object>> gridPaging(Map<String, Object> param);

    Map<String, Object>  selectInfo(Map<String, Object> param);

    List<Map<String, Object>> selectItemList(Map<String, Object> param);

    Map<String, Object>  selectCreateDt(Map<String, Object> param);
}
