package daedan.mes.pumapi.service;

import daedan.mes.user.domain.UserInfo;

import javax.transaction.Transactional;
import java.util.Map;

public interface PumApiService {
    @Transactional
    Map<String, Object>  syncUser(Map<String, Object> paraMap);

    @Transactional
    Map<String, Object> syncProd(Map<String, Object> paraMap);

    @Transactional
    Map<String, Object>  syncMatr(Map<String, Object> paraMap);

    @Transactional
    Map<String, Object> syncOrdPlan(Map<String, Object> paraMap);

    UserInfo getUserInfoBySaupNo(String saupNo);


    Map<String, Object>  syncBom(Map<String, Object> paraMap);
}
