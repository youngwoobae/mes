package daedan.mes.ord.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface Ord18Mapper {
    List<Map<String, Object>> getOrd18List(Map<String, Object> paraMap);
    int getOrd18ListCount(Map<String, Object> paraMap);
}
