package daedan.mes.dash.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface Dash14Mapper {
    List<Map<String, Object>> getUserGroupDashs(Map<String, Object> paraMap);
    List<Map<String, Object>> getDashTotalMakeIndcRslt(Map<String, Object> paraMap);
}
