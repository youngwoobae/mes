package daedan.mes.pms.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface PmsMapper {
    List<Map<String, Object>> getComboPmsProcStat(Map<String, Object> paraMap);

    List<Map<String, Object>> getPmsOrdrList(Map<String, Object> paraMap);
    int getPmsOrdrListCount(Map<String, Object> paraMap);

    List<Map<String, Object>> getPmsOrdInfoList(Map<String, Object> paraMap);

    List<Map<String, Object>> getPmsWhoList(Map<String, Object> paraMap);
}
