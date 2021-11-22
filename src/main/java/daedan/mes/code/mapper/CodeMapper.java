package daedan.mes.code.mapper;

import daedan.mes.code.domain.CodeInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface CodeMapper {
    List<Map<String, Object>> getComboCodeList(Map<String, Object> Map);
    List<Map<String, Object>> getComboWithoutChoiceCodeList(Map<String, Object> paraMap);
    List<Map<String, Object>> getCodeList(Map<String, Object> paraMap);
    int getCodeListCount(Map<String, Object> paraMap);

    int Modify(Map<String, Object> passMap);
    void appendCodeInfo(Map<String, Object> passMap);

    void dropCode(Map<String, Object> paraMap);

    List<Map<String, Object>> getComboUserDeptList(Map<String, Object> paraMap);

    List<Map<String, Object>> getCodeTree(Map<String, Object> paraMap);

    void updateCcpTpByProcWork(Map<String, Object> paraMap);

    void updateCodeNm(CodeInfo coin);


    Integer getMaxCodeSeq(Map<String, Object> paraMap);

    List<Map<String, Object>> getComboMatrWhList(Map<String, Object> paraMap);
}
