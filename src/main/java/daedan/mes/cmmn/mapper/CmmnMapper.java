package daedan.mes.cmmn.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface CmmnMapper {
    List<Map<String, Object>> getCalDateList(Map<String, Object> paraMap);
    Date getIntervalDate(Map<String, Object> paraMap);
    Map<String, Object> getAuthUserMenuInfo(Map<String, Object> paraMap);

    int getWeekIdx(Map<String, Object> paraMap);
    Map<String, Object> getMakeHstrPreVal(Map<String, Object> paraMap);

    int getWeekIdxByUnixTime(long iValue);

    List<Map<String, Object>> getComboWh(Map<String, Object> paraMap);

    List<Map<String, Object>> getComboSpot(Map<String, Object> paraMap);
}
