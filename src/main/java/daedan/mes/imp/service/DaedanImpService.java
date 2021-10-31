package daedan.mes.imp.service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Map;

public interface DaedanImpService {
    @Transactional
    ArrayList<Map<String, Object>> makeStkByExcel(Map<String, Object> paraMap)throws Exception;

    ArrayList<Map<String, Object>> makeMatrStkByExcel(Map<String, Object> paraMap)throws Exception;
}
