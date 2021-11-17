package daedan.mes.cmmn.io.datamgr.mondata;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
@Mapper
@Component
public interface MonDataMapper {

    List<Map<String, Object>> selectLastMonData(Map<String, Object> paramMap);
    List<Map<String, Object>> selectMondataEquipList();
    Map<String, Object> selectStdCycle(String mCode);
    Map<String, Object> selectLastCCbreakYn(String mCode);
}
