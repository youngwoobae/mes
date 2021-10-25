package daedan.mes.make.mapper.hdfd;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface HdfdMapper {

    List<Map<String, Object>> getHdfdIndcList(Map<String, Object> paraMap);
}
