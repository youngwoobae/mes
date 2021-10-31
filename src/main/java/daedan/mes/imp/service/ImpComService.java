package daedan.mes.imp.service;

import javax.transaction.Transactional;
import java.util.HashMap;

public interface ImpComService {
    @Transactional void makeCmpyByExcel(HashMap<String, Object> paraMap);

    //@Transactional
    void makeMatrByExcel(HashMap<String, Object> paraMap);

    void makeProdByExcel(HashMap<String, Object> paraMap);

    void makeBomByExcel(HashMap<String, Object> paraMap);

    @Transactional void makeMatrStkClosExcel(HashMap<String, Object> paraMap);
    @Transactional void makeProdStkClosExcel(HashMap<String, Object> paraMap);
}
