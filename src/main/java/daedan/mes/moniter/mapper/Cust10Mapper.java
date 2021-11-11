package daedan.mes.moniter.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface Cust10Mapper {
    List<Map<String,Object>> getMetalDetctHstr(Map<String, Object> paraMap);
    int getMetalDetctHstrCount(Map<String, Object> paraMap);
}
