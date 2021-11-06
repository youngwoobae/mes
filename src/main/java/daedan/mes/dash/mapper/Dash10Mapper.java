package daedan.mes.dash.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface Dash10Mapper {
    List<Map<String, Object>> getTmpr10List(Map<String, Object> paraMap);
    Map<String, Object> getFinalMetalDetect(Map<String, Object> paraMap);
}
