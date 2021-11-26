package daedan.mes.moniter.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


@Mapper
@Component
public interface Cust18Mapper {
    List<Map<String, Object>> getHeatLogHstr(Map<String, Object> paraMap);
}
