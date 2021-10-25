package daedan.mes.proc.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component

public interface ProcMapper {

    List<Map<String, Object>> getProcList(Map<String, Object> paraMap);
    int getProcListCount(Map<String, Object> paraMap);

    List<Map<String, Object>> getProcBrnchList(Map<String, Object> paraMap);
    int getProcBrnchListCount(Map<String, Object> paraMap);

    Map<String, Object> getProcInfo(Map<String, Object> paraMap);

    List<Map<String, Object>> getProcStdList(Map<String, Object> paraMap);
    int getProcStdListCount(Map<String, Object> paraMap);

    void dropProcStd(Map<String, Object> paraMap);

    List<Map<String, Object>> getPopProcList(Map<String, Object> paraMap);
    int getPopProcListCount(Map<String, Object> paraMap);

    List<Map<String, Object>> getProcMemberList(Map<String, Object> paraMap);

    void dropProcMpByProcGrpCd(Map<String, Object> paraMap);

    List<Map<String, Object>> getCodeListByParAndSeqOver(Map<String, Object> paraMap);

    void dropProcBrnchByArrayList(Map<String, Object> paraMap);

    List<Map<String, Object>> getProcInfoList(Map<String, Object> paraMap);

    int getProcInfoListCount(Map<String, Object> paraMap);

    void getUpdateUsedyn(Long procGrpCd);
}
