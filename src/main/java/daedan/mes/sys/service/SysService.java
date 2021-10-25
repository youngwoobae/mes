package daedan.mes.sys.service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

public interface SysService {
    List<Map<String,Object>> getOrdProdList(Map<String, Object> paraMap);

    List<Map<String,Object>> getOrdList(Map<String, Object> paraMap);

    List<Map<String,Object>> getIndcList(Map<String, Object> paraMap);

    List<Map<String,Object>> getIndcMatrList(Map<String, Object> paraMap);

    List<Map<String,Object>> getPursList(Map<String, Object> paraMap);

    List<Map<String,Object>> getPursMatrList(Map<String, Object> paraMap);

    List<Map<String,Object>> getMatrIwhList(Map<String, Object> paraMap);

    List<Map<String,Object>> getMatrOwhList(Map<String, Object> paraMap);

    List<Map<String,Object>> getIndcRsltList(Map<String, Object> paraMap);

    List<Map<String,Object>> getProdIwhList(Map<String, Object> paraMap);

    List<Map<String,Object>> getProdOwhList(Map<String, Object> paraMap);

    void invokeChatServer();

    List<Map<String,Object>> rollBack(Map<String, Object> paraMap);

    List<Map<String,Object>> getResultProdOwhList(Map<String, Object> paraMap);

    List<Map<String,Object>> getResultProdIwhList(Map<String, Object> paraMap);

    List<Map<String,Object>> getResultIndcRslt(Map<String, Object> paraMap);
}
