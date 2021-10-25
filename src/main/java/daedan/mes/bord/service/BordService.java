package daedan.mes.bord.service;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

public interface BordService {


    @Transactional
    void saveBord(Map<String, Object> paraMap);

    @Transactional
    void deleteBord(Map<String, Object> paraMap);

    List<Map<String, Object>> getBordList(Map<String, Object> paraMap);
    int getBordListCount(Map<String, Object> paraMap);
    Map<String, Object> getBordInfo(Map<String, Object> paraMap, HttpSession session);

    Map<String, Object> getFindBoardTerm(Map<String, Object> paraMap);
}