package daedan.mes.dash.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface Dash17Mapper {
    List<Map<String, Object>> getTmpr17List(Map<String, Object> paraMap);
}
