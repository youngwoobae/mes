package daedan.mes.moniter.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface Cust10Mapper {
    List<Map<String,Object>> getMetalLogHstr(Map<String, Object> paraMap);
    int getMetalLogHstrCount(Map<String, Object> paraMap);
}
