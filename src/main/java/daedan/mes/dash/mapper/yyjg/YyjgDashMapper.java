package daedan.mes.dash.mapper.yyjg;


import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface YyjgDashMapper {
    List<Map<String, Object>> getYyjgProdIoList(Map<String, Object> paraMap);
    int getYyjgProdIoListCount(Map<String, Object> paraMap);
    Map<String, Object> getYyjgPhothCounter(Map<String, Object> paraMap);

    List<Map<String, Object>> getYyjgMatrIoList(Map<String, Object> paraMap);

    int getYyjgMatrIoListCount(Map<String, Object> paraMap);

    List<Map<String, Object>> getYyjgSpotList(Map<String, Object> paraMap);
}
