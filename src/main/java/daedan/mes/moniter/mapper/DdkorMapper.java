package daedan.mes.moniter.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface DdkorMapper {

    List<Map<String, Object>> getMoniterHstr(Map<String, Object> paraMap);
    int getMoniterHstrCount(Map<String, Object> paraMap);

    List<Map<String, Object>> getMoniterHstrH24(Map<String, Object> paraMap);

    Map<String, Object> getEquipName(Map<String, Object> paraMap);
}
