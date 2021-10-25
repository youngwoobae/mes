package daedan.mes.imp.service;

import javax.transaction.Transactional;
import java.io.IOException;

import java.util.List;
import java.util.Map;


public interface ImpService {
    @Transactional
    void makeCodeInfoByExcel(Map<String, Object> paraMap);

    @Transactional
    void makeCmpyByExcel(Map<String, Object> paraMap);

    @Transactional
    void makeMatrByNormalExcel(Map<String, Object> paraMap);

    /*쟈재명 , 쟈재구분 두개 필드만 가지고 자재생성시 사용
     * 자재발주처는 사용자의 소속사로 자동설정함.
     * 생성자재의 보관온도는 냉장으로 자동 서함.*/
    @Transactional
    void makeMatrByBriefExcel(Map<String, Object> paraMap)throws Exception;

    @Transactional
    void makePursByExcel(Map<String, Object> paraMap);

    @Transactional
    void makeProdByExcel(Map<String, Object> paraMap);

    @Transactional
    void makeProdBomByExcel(Map<String, Object> paraMap);

    @Transactional
    void makeOrdByExcel(Map<String, Object> paraMap);

    int getProcRate(Map<String, Object> paraMap);

    void makeOrdProdByExcel(Map<String, Object> paraMap);

    // 대동 고려삼: Excel로 상품, 원자재 등록
//    @Transactional
    void prodIndcExcel(Map<String, Object> paraMap) throws Exception;

    @Transactional
    void prodBomExcel(Map<String, Object> paraMap) throws Exception;
    @Transactional
    void prodExcel(Map<String, Object> paraMap) throws Exception;

    @Transactional
    void makeUserByExcel(Map<String, Object> paraMap) throws IOException;

    void makeDefaultMatrCmpy(Map<String, Object> paraMap);
    // 일별생산일보
    List<Map<String, Object>>indcExcelList(Map<String, Object> paraMap);
    // 일별생산실적
    List<Map<String, Object>> makeIndcExcelList(Map<String, Object> paraMap);

    void prodCodeBomExcel(Map<String, Object> paraMap)throws Exception;

    void prodInfoExcel(Map<String, Object> paraMap)throws Exception;
}
