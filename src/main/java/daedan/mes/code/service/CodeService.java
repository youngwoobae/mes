package daedan.mes.code.service;

import daedan.mes.code.domain.CodeInfo;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

public interface CodeService {

    List<Map<String, Object>> getComboCodeList(Map<String, Object> paraMap);
    List<Map<String, Object>> getComboWithoutChoiceCodeList(Map<String, Object> paraMap);

    CodeInfo findByParCodeNoAndCodeNmAndUsedYn(long l, String fileExtNm,String yn);

    List<Map<String, Object>> getCodeList(Map<String, Object> paraMap);
    int getCodeListCount(Map<String, Object> paraMap);

    @Transactional
    void codeModify(Map<String, Object> paraMap);

    Map<String,Object> getCodeInfo(Map<String, Object> paraMap);

    @Transactional
    Long saveInspCode(Map<String, Object> passMap);

    void dropCode(Map<String, Object> paraMap);

    CodeInfo saveCodeByName(Map<String, Object> paraMap);

    CodeInfo saveCode(Map<String, Object> paraMap);

    List<Map<String, Object>> getComboUserDeptList(Map<String, Object> paraMap);

    List<Object> getCodeTree(Map<String, Object> paraMap);
}
