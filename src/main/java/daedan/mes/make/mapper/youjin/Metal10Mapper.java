package daedan.mes.make.mapper.youjin;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface Metal10Mapper {
    Map<String, Object> getCurMetalLog(Map<String, Object> paraMap);
    String getMetalScadaApi(Map<String, Object> paraMap);
    Map<String, Object> getMetalData(Map<String, Object> paraMap);
}
