package daedan.mes.make.mapper.youjin;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.Map;

@Mapper
@Component
public interface Metal10Mapper {
    Map<String, Object> getCurMetalLog(Map<String, Object> paraMap);
    String getMetalScadaApi(Map<String, Object> paraMap);
}
