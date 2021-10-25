package daedan.mes.prod.service.sf;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

public interface SfProdService {
    // 서울식품: Excel로 작업지시 등록
    String makeSeoulFoodMakeIndcByExcel(Map<String, Object> paraMap) throws Exception;

    @Transactional
    void seoulProdBomByExcel(HashMap<String, Object> paraMap) throws Exception;
}
