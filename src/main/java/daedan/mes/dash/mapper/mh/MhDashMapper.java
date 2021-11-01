package daedan.mes.dash.mapper.mh;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface MhDashMapper {
    List<Map<String, Object>> getMhTmprList(Map<String, Object> paraMap);
}
