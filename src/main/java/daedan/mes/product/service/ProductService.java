package daedan.mes.product.service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

public interface ProductService {
    /*간략 생산지시용*/
    void saveProductPlan(Map<String, Object> paraMap);

    /*간략 생산계획용*/
    void makeProductPlan(Map<String, Object> paraMap);

    List<Map<String,Object>> getProductPlanList(Map<String,java.lang.Object> paraMap);
    Map<String,Object> getProductPlanInfo(Map<String,java.lang.Object> paraMap);

    List<Map<String, Object>> getProductIndcList(Map<String, Object> paraMap);

    void saveProductIndc(Map<String, Object> paraMap);

    List<Map<String, Object>> getPursMatrListByIndc(Map<String, Object> paraMap);

    @Transactional
    Long expectMakeIndc(Map<String, Object> paraMap);
}
