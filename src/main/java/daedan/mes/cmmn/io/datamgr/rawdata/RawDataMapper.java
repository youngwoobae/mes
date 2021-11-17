package daedan.mes.cmmn.io.datamgr.rawdata;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
@Mapper
@Component
public interface RawDataMapper {
    Map<String, Object> getRecDescPrcTime(Map<String, Object> stringObjectMap);

    void updateRawDataPrcTime(Map<String, Object> stringObjectMap);

    List<Map<String, Object>> ccpcleBreakYn(Map<String, Object> stringObjectMap);

    void updateGatherYn(Map<String, Object> stringObjectMap);

    Map<String, Object> selectRawDataMaxSeq(Map<String, Object> paramMap);

    void insertRawdata(Map<String, Object> stringObjectMap);
}
