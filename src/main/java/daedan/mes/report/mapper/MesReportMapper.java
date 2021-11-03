package daedan.mes.report.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface MesReportMapper {
    List<Map<String,Object>> getMetalDetectHstr(Map<String, Object> paraMap);
    int getMetalDetectHstrCount(Map<String, Object> paraMap);

    List<Map<String, Object>> getProdOwhHstr(Map<String, Object> paraMap);
    int getProdOwhHstrCount(Map<String, Object> paraMap);

    List<Map<String, Object>> getProdIwhHstr(Map<String, Object> paraMap);
    int getProdIwhHstrCount(Map<String, Object> paraMap);
}
