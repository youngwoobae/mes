package daedan.mes.qc.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface QcMapper {
    List<Map<String,Object>> getMatrIwhChkList(Map<String, Object> paraMap);
    int getMatrIwhChkListCount(Map<String, Object> paraMap);

    List<Map<String, Object>> getMatrIwhChkDocList(Map<String, Object> paraMap);
    int getMatrIwhDocChkListCount(Map<String, Object> paraMap);
    void initMatrIwhDoc();

    List<Map<String, Object>> getProdOwhChkList(Map<String, Object> paraMap);
    int getProdOwhChkListCount(Map<String, Object> paraMap);
    List<Map<String, Object>> getProdOwhChkDocList(Map<String, Object> paraMap);
    int getProdOwhChkDocListCount(Map<String, Object> paraMap);


    void initProdOwhDoc();
}
