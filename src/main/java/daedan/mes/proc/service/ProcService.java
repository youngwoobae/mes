package daedan.mes.proc.service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

public interface ProcService {
    List<Map<String, Object>> getProcList(Map<String, Object> paraMap);
    int getProcListCount(Map<String, Object> paraMap);
    Map<String, Object> getProcInfo(Map<String, Object> paraMap);
    List<Map<String, Object>> getProcBrnchList(Map<String, Object> paraMap);

    int getProcBrnchListCount(Map<String, Object> paraMap);

    @Transactional
    void saveProcInfo(Map<String, Object> paraMap);

    @Transactional
    void saveProcMpList(Map<String, Object> paraMap);

    void saveProcBrnchList(Map<String, Object> paraMap);

    List<Map<String, Object>> getProcStdList(Map<String, Object> paraMap);
    int getProcStdListCount(Map<String, Object> paraMap);

    void saveProcStd(Map<String, Object> paraMap);

    void dropProcStd(Map<String, Object> paraMap);

    List<Map<String, Object>> getPopProcList(Map<String, Object> paraMap);
    int getPopProcListCount(Map<String, Object> paraMap);

    Map<String,Object> getProcBrnchInfo(Map<String, Object> paraMap);

    void saveProcWork(Map<String, Object> paraMap);

    List<Map<String, Object>>  getProcMemberList(Map<String, Object> paraMap);

    List<Map<String, Object>> getCodeListByParAndSeqOver(Map<String, Object> paraMap);

    List<Map<String, Object>> getProcInfoList(Map<String, Object> paraMap);
    int getProcInfoListCount(Map<String, Object> paraMap);
}
