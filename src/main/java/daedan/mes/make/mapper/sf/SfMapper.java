package daedan.mes.make.mapper.sf;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface SfMapper {

    List<Map<String, Object>> getDailyIndc(Map<String, Object> paraMap);
}