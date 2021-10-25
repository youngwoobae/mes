package daedan.mes.sys.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface SysMapper {

    List<Map<String, Object>> getOrdProdList(Map<String, Object> paraMap);

    List<Map<String, Object>> getOrdList(Map<String, Object> paraMap);

    List<Map<String, Object>> getIndcList(Map<String, Object> paraMap);

    List<Map<String, Object>> getIndcMatrList(Map<String, Object> paraMap);

    List<Map<String, Object>> getPursList(Map<String, Object> paraMap);

    List<Map<String, Object>> getPursMatrList(Map<String, Object> paraMap);

    List<Map<String, Object>> getMatrIwhList(Map<String, Object> paraMap);

    List<Map<String, Object>> getMatrOwhList(Map<String, Object> paraMap);

    List<Map<String, Object>> getIndcRsltList(Map<String, Object> paraMap);

    List<Map<String, Object>> getProdIwhList(Map<String, Object> paraMap);

    List<Map<String, Object>> getProdOwhList(Map<String, Object> paraMap);

    List<Map<String, Object>> getResultProdOwhList(Map<String, Object> paraMap);

    List<Map<String, Object>> getResultProdIwhList(Map<String, Object> paraMap);

    List<Map<String, Object>> getResultIndcRslt(Map<String, Object> paraMap);
}
