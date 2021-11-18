package daedan.mes.dash.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface Dash03Mapper {
    List<Map<String, Object>> getTmpr03List(Map<String, Object> paraMap);
}
